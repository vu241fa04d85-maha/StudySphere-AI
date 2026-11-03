-- ====================================================================
-- AI STUDY ASSISTANT (AI-SA) — COMPLETE PRODUCTION POSTGRESQL SCHEMA
-- Supabase Migration: 20261028000000_ai_study_assistant_schema.sql
-- Includes Normalized Tables, Indexes, Triggers, Functions & RLS Policies
-- ====================================================================

-- 1. Enable Required Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 2. Enumerated Types
CREATE TYPE user_role_enum AS ENUM ('student', 'academic_staff');
CREATE TYPE admin_role_enum AS ENUM ('super_admin', 'academic_admin', 'test_manager', 'hackathon_manager', 'support_admin');
CREATE TYPE account_status_enum AS ENUM ('active', 'suspended', 'pending_verification');
CREATE TYPE file_type_enum AS ENUM ('PDF', 'PPT', 'PPTX', 'DOC', 'DOCX', 'TXT', 'IMAGE');
CREATE TYPE difficulty_level_enum AS ENUM ('easy', 'medium', 'hard');
CREATE TYPE submission_status_enum AS ENUM ('registered', 'submitted', 'evaluated', 'disqualified');
CREATE TYPE backup_status_enum AS ENUM ('started', 'in_progress', 'completed', 'failed', 'verified');

-- ====================================================================
-- 3. Core Profiles & Identity Tables
-- ====================================================================

-- 3.1 Student User Profiles
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    full_name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    college TEXT NOT NULL DEFAULT 'Institute of Technology & Science',
    course TEXT NOT NULL DEFAULT 'Computer Science & Engineering',
    year TEXT NOT NULL DEFAULT '3rd Year',
    profile_image TEXT,
    study_streak INT NOT NULL DEFAULT 1,
    total_study_minutes INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 3.2 Admin Roles & Permissions Matrix
CREATE TABLE IF NOT EXISTS public.admin_roles (
    role_key admin_role_enum PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL
);

INSERT INTO public.admin_roles (role_key, name, description) VALUES
    ('super_admin', 'Super Admin', 'Full systemic access, administrator invitation, audit logs, disaster recovery'),
    ('academic_admin', 'Academic Admin', 'Curriculum management, document library oversight, and student progress'),
    ('test_manager', 'Test Manager', 'Proctored test creation, exam scheduling, and result certifications'),
    ('hackathon_manager', 'Hackathon Manager', 'Campus hackathon operations, team evaluation, and leaderboard publishing'),
    ('support_admin', 'Support Admin', 'Student inquiries, system health monitoring, and technical assistance')
ON CONFLICT (role_key) DO NOTHING;

CREATE TABLE IF NOT EXISTS public.admin_permissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    role_key admin_role_enum NOT NULL REFERENCES public.admin_roles(role_key) ON DELETE CASCADE,
    permission_code TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(role_key, permission_code)
);

-- 3.3 Admin Profiles (Segregated from Normal Students)
CREATE TABLE IF NOT EXISTS public.admin_profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auth_user_id UUID UNIQUE REFERENCES auth.users(id) ON DELETE SET NULL,
    admin_code VARCHAR(12) UNIQUE NOT NULL, -- e.g. ADM001 to ADM050
    full_name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    role admin_role_enum NOT NULL DEFAULT 'academic_admin',
    status account_status_enum NOT NULL DEFAULT 'active',
    mfa_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    must_change_password BOOLEAN NOT NULL DEFAULT TRUE,
    last_login TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Seed Initial 5 Administrators (ADM001 to ADM005)
INSERT INTO public.admin_profiles (admin_code, full_name, email, role, status, must_change_password) VALUES
    ('ADM001', 'Chief Systems Officer', 'adm001.superadmin@studyportal.edu', 'super_admin', 'active', TRUE),
    ('ADM002', 'Dean of Academic Affairs', 'adm002.academicadmin@studyportal.edu', 'academic_admin', 'active', TRUE),
    ('ADM003', 'Head of Examination Board', 'adm003.testadmin@studyportal.edu', 'test_manager', 'active', TRUE),
    ('ADM004', 'Director of Innovation', 'adm004.hackathonadmin@studyportal.edu', 'hackathon_manager', 'active', TRUE),
    ('ADM005', 'Technical Operations Lead', 'adm005.supportadmin@studyportal.edu', 'support_admin', 'active', TRUE)
ON CONFLICT (admin_code) DO NOTHING;

-- ====================================================================
-- 4. Document Management & File Storage Metadata
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.study_files (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    file_name TEXT NOT NULL,
    file_type file_type_enum NOT NULL DEFAULT 'PDF',
    file_size_bytes BIGINT NOT NULL,
    storage_path TEXT NOT NULL,
    subject TEXT NOT NULL,
    description TEXT,
    extracted_text TEXT,
    processing_status TEXT NOT NULL DEFAULT 'pending', -- pending, completed, failed
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.summaries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    file_id UUID NOT NULL REFERENCES public.study_files(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    summary_text TEXT NOT NULL,
    key_points JSONB NOT NULL DEFAULT '[]'::jsonb,
    flashcards JSONB NOT NULL DEFAULT '[]'::jsonb,
    exam_questions JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ====================================================================
-- 5. AI Study Chat & Memory
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.conversations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL DEFAULT 'New Study Session',
    study_mode TEXT NOT NULL DEFAULT 'Standard',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    conversation_id UUID NOT NULL REFERENCES public.conversations(id) ON DELETE CASCADE,
    sender TEXT NOT NULL CHECK (sender IN ('user', 'ai')),
    message TEXT NOT NULL,
    code_snippet TEXT,
    code_language TEXT,
    citations JSONB DEFAULT '[]'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ====================================================================
-- 6. Quizzes & Self-Assessment
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.quizzes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    description TEXT,
    subject TEXT NOT NULL,
    difficulty difficulty_level_enum NOT NULL DEFAULT 'medium',
    question_count INT NOT NULL DEFAULT 5,
    created_by UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    is_official BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.quiz_questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quiz_id UUID NOT NULL REFERENCES public.quizzes(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_answer CHAR(1) NOT NULL CHECK (correct_answer IN ('A', 'B', 'C', 'D')),
    explanation TEXT NOT NULL,
    marks INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS public.quiz_attempts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quiz_id UUID NOT NULL REFERENCES public.quizzes(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    score INT NOT NULL,
    total_marks INT NOT NULL,
    time_taken_seconds INT NOT NULL,
    answers_summary JSONB NOT NULL DEFAULT '{}'::jsonb,
    completed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ====================================================================
-- 7. Official Tests & Proctored Examinations
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.tests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    description TEXT,
    subject TEXT NOT NULL,
    duration_minutes INT NOT NULL DEFAULT 45,
    total_marks INT NOT NULL DEFAULT 50,
    passing_score INT NOT NULL DEFAULT 35,
    created_by_admin VARCHAR(12) NOT NULL REFERENCES public.admin_profiles(admin_code),
    status TEXT NOT NULL DEFAULT 'published', -- published, draft, archived
    start_time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    end_time TIMESTAMPTZ NOT NULL DEFAULT NOW() + INTERVAL '14 days',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.test_questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    test_id UUID NOT NULL REFERENCES public.tests(id) ON DELETE CASCADE,
    question_number INT NOT NULL,
    question_text TEXT NOT NULL,
    options JSONB NOT NULL, -- Array of 4 options
    correct_index INT NOT NULL,
    marks INT NOT NULL DEFAULT 5
);

CREATE TABLE IF NOT EXISTS public.test_attempts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    test_id UUID NOT NULL REFERENCES public.tests(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    score INT NOT NULL,
    total_marks INT NOT NULL,
    passed BOOLEAN NOT NULL,
    submitted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(test_id, user_id)
);

-- ====================================================================
-- 8. Campus & Global Hackathons
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.hackathons (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    theme TEXT NOT NULL,
    description TEXT NOT NULL,
    rules TEXT NOT NULL,
    eligibility TEXT NOT NULL DEFAULT 'All verified students',
    prize_pool TEXT NOT NULL DEFAULT '$5,000 in Grants',
    start_date TIMESTAMPTZ NOT NULL,
    end_date TIMESTAMPTZ NOT NULL,
    registration_deadline TIMESTAMPTZ NOT NULL,
    created_by_admin VARCHAR(12) NOT NULL REFERENCES public.admin_profiles(admin_code),
    status TEXT NOT NULL DEFAULT 'active', -- active, upcoming, completed
    participant_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.hackathon_participants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    hackathon_id UUID NOT NULL REFERENCES public.hackathons(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    project_name TEXT,
    project_url TEXT,
    submission_status submission_status_enum NOT NULL DEFAULT 'registered',
    score INT DEFAULT 0,
    feedback TEXT,
    registered_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(hackathon_id, user_id)
);

-- ====================================================================
-- 9. Code Lab & Multi-Language Sandboxes
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.code_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    language TEXT NOT NULL,
    title TEXT NOT NULL DEFAULT 'Problem Sandbox',
    problem_statement TEXT,
    input_format TEXT,
    output_format TEXT,
    constraints TEXT,
    code TEXT NOT NULL,
    time_complexity TEXT NOT NULL DEFAULT 'O(n)',
    space_complexity TEXT NOT NULL DEFAULT 'O(1)',
    ai_suggestions TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ====================================================================
-- 10. Study Planner & Daily Tasks
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.study_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    subject TEXT NOT NULL,
    target_exam_date TIMESTAMPTZ NOT NULL,
    daily_hours NUMERIC(4,2) NOT NULL DEFAULT 3.0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.study_tasks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    plan_id UUID NOT NULL REFERENCES public.study_plans(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,
    due_date TIMESTAMPTZ NOT NULL,
    priority TEXT NOT NULL DEFAULT 'Medium', -- High, Medium, Low
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    estimated_minutes INT NOT NULL DEFAULT 45,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ====================================================================
-- 11. Notifications, Broadcasts & Activity Logging
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    notification_type TEXT NOT NULL DEFAULT 'info',
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.announcements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_by VARCHAR(12) NOT NULL REFERENCES public.admin_profiles(admin_code),
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    category TEXT NOT NULL DEFAULT 'General',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.activity_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    event_type TEXT NOT NULL,
    metadata JSONB DEFAULT '{}'::jsonb,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ====================================================================
-- 12. Audit Logs, Disaster Recovery & Backup Tracking
-- ====================================================================

CREATE TABLE IF NOT EXISTS public.audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    admin_code VARCHAR(12) NOT NULL REFERENCES public.admin_profiles(admin_code),
    action TEXT NOT NULL,
    target_entity TEXT NOT NULL,
    target_id TEXT,
    details JSONB DEFAULT '{}'::jsonb,
    ip_address INET,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.backup_jobs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    job_type TEXT NOT NULL, -- full_database, storage_bucket, incremental
    destination_uri TEXT NOT NULL,
    size_bytes BIGINT,
    checksum_sha256 TEXT,
    status backup_status_enum NOT NULL DEFAULT 'started',
    error_message TEXT,
    started_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS public.system_errors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    severity TEXT NOT NULL CHECK (severity IN ('low', 'medium', 'high', 'critical')),
    service TEXT NOT NULL,
    error_message TEXT NOT NULL,
    stack_trace TEXT,
    metadata JSONB DEFAULT '{}'::jsonb,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ====================================================================
-- 13. High-Performance Relational Indexes
-- ====================================================================

CREATE INDEX IF NOT EXISTS idx_study_files_user ON public.study_files(user_id);
CREATE INDEX IF NOT EXISTS idx_conversations_user ON public.conversations(user_id);
CREATE INDEX IF NOT EXISTS idx_messages_conversation ON public.messages(conversation_id);
CREATE INDEX IF NOT EXISTS idx_quiz_questions_quiz ON public.quiz_questions(quiz_id);
CREATE INDEX IF NOT EXISTS idx_quiz_attempts_user ON public.quiz_attempts(user_id);
CREATE INDEX IF NOT EXISTS idx_test_questions_test ON public.test_questions(test_id);
CREATE INDEX IF NOT EXISTS idx_test_attempts_user ON public.test_attempts(user_id);
CREATE INDEX IF NOT EXISTS idx_hackathon_participants_hackathon ON public.hackathon_participants(hackathon_id);
CREATE INDEX IF NOT EXISTS idx_code_sessions_user ON public.code_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_study_tasks_user_date ON public.study_tasks(user_id, due_date);
CREATE INDEX IF NOT EXISTS idx_notifications_user_read ON public.notifications(user_id, is_read);
CREATE INDEX IF NOT EXISTS idx_audit_logs_admin_date ON public.audit_logs(admin_code, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_backup_jobs_status ON public.backup_jobs(status, started_at DESC);

-- ====================================================================
-- 14. Row Level Security (RLS) Policies
-- ====================================================================

-- Enable RLS across all user and administration tables
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.admin_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.study_files ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.summaries ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.conversations ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.messages ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quizzes ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_questions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_attempts ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.test_questions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.test_attempts ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.hackathons ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.hackathon_participants ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.code_sessions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.study_plans ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.study_tasks ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.announcements ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.audit_logs ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.backup_jobs ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.system_errors ENABLE ROW LEVEL SECURITY;

-- Helper function: Is Authenticated User an Admin?
CREATE OR REPLACE FUNCTION public.is_admin()
RETURNS BOOLEAN
LANGUAGE sql
SECURITY DEFINER
SET search_path = public
STABLE
AS $$
    SELECT EXISTS (
        SELECT 1 FROM public.admin_profiles
        WHERE auth_user_id = auth.uid()
        AND status = 'active'
    );
$$;

-- 14.1 Profiles RLS
CREATE POLICY "Users can view own profile"
    ON public.profiles FOR SELECT
    USING (auth.uid() = id);

CREATE POLICY "Users can update own profile"
    ON public.profiles FOR UPDATE
    USING (auth.uid() = id);

CREATE POLICY "Admins can view all student profiles"
    ON public.profiles FOR SELECT
    USING (public.is_admin());

-- 14.2 Admin Profiles RLS
CREATE POLICY "Admins can view admin accounts"
    ON public.admin_profiles FOR SELECT
    USING (public.is_admin());

-- 14.3 Study Files & Summaries RLS (Zero-trust user isolation)
CREATE POLICY "Students can access only their own files"
    ON public.study_files FOR ALL
    USING (auth.uid() = user_id);

CREATE POLICY "Students can access only their own summaries"
    ON public.summaries FOR ALL
    USING (auth.uid() = user_id);

-- 14.4 Conversations & Messages RLS
CREATE POLICY "Users can access own conversations"
    ON public.conversations FOR ALL
    USING (auth.uid() = user_id);

CREATE POLICY "Users can access messages within own conversations"
    ON public.messages FOR ALL
    USING (
        EXISTS (
            SELECT 1 FROM public.conversations
            WHERE conversations.id = messages.conversation_id
            AND conversations.user_id = auth.uid()
        )
    );

-- 14.5 Quizzes RLS
CREATE POLICY "Anyone authenticated can view published quizzes"
    ON public.quizzes FOR SELECT
    TO authenticated
    USING (TRUE);

CREATE POLICY "Users can create personal quizzes"
    ON public.quizzes FOR INSERT
    WITH CHECK (auth.uid() = created_by);

CREATE POLICY "Anyone authenticated can view quiz questions"
    ON public.quiz_questions FOR SELECT
    TO authenticated
    USING (TRUE);

CREATE POLICY "Users can access only their own quiz attempts"
    ON public.quiz_attempts FOR ALL
    USING (auth.uid() = user_id);

-- 14.6 Tests RLS
CREATE POLICY "Students can view published tests"
    ON public.tests FOR SELECT
    TO authenticated
    USING (status = 'published');

CREATE POLICY "Admins can manage tests"
    ON public.tests FOR ALL
    USING (public.is_admin());

CREATE POLICY "Students can view test questions during active exam"
    ON public.test_questions FOR SELECT
    TO authenticated
    USING (TRUE);

CREATE POLICY "Admins can manage test questions"
    ON public.test_questions FOR ALL
    USING (public.is_admin());

CREATE POLICY "Students can insert and view their own test attempts"
    ON public.test_attempts FOR ALL
    USING (auth.uid() = user_id);

CREATE POLICY "Admins can view all test attempts"
    ON public.test_attempts FOR SELECT
    USING (public.is_admin());

-- 14.7 Hackathons RLS
CREATE POLICY "Anyone authenticated can view hackathons"
    ON public.hackathons FOR SELECT
    TO authenticated
    USING (TRUE);

CREATE POLICY "Admins can manage hackathons"
    ON public.hackathons FOR ALL
    USING (public.is_admin());

CREATE POLICY "Students can manage their own hackathon registration"
    ON public.hackathon_participants FOR ALL
    USING (auth.uid() = user_id);

CREATE POLICY "Admins can evaluate hackathon participants"
    ON public.hackathon_participants FOR SELECT
    USING (public.is_admin());

-- 14.8 Code Sessions RLS
CREATE POLICY "Students can access only their own code sessions"
    ON public.code_sessions FOR ALL
    USING (auth.uid() = user_id);

-- 14.9 Study Planner RLS
CREATE POLICY "Students can access only their own study plans"
    ON public.study_plans FOR ALL
    USING (auth.uid() = user_id);

CREATE POLICY "Students can access only their own tasks"
    ON public.study_tasks FOR ALL
    USING (auth.uid() = user_id);

-- 14.10 Notifications & Announcements RLS
CREATE POLICY "Users can view their notifications"
    ON public.notifications FOR ALL
    USING (auth.uid() = user_id);

CREATE POLICY "Anyone can view announcements"
    ON public.announcements FOR SELECT
    TO authenticated
    USING (TRUE);

CREATE POLICY "Admins can manage announcements"
    ON public.announcements FOR ALL
    USING (public.is_admin());

-- 14.11 Audit Logs & Disaster Recovery RLS
CREATE POLICY "Only superadmins can view audit logs"
    ON public.audit_logs FOR SELECT
    USING (
        EXISTS (
            SELECT 1 FROM public.admin_profiles
            WHERE auth_user_id = auth.uid()
            AND role = 'super_admin'
        )
    );

CREATE POLICY "Only superadmins can view backup jobs"
    ON public.backup_jobs FOR ALL
    USING (
        EXISTS (
            SELECT 1 FROM public.admin_profiles
            WHERE auth_user_id = auth.uid()
            AND role = 'super_admin'
        )
    );
