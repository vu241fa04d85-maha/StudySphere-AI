# AI Study Assistant (AI-SA) — Implementation Plan

A comprehensive, production-grade AI Study Assistant native Android application built with Kotlin and Jetpack Compose. AI-SA delivers an all-in-one learning environment featuring an AI study chatbot, document intelligence (summaries, flashcards, Q&A), a multi-language Code Lab, dynamic quizzes, admin-managed tests & hackathons, an automated study scheduler, and role-based access control with 50 pre-seeded admin accounts.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and UX decisions have been established based on your inputs and environment constraints:

- **Confirmed Runtime**: Native Android application powered by Jetpack Compose (Material Design 3) and Kotlin Coroutines/Flow.
- **Confirmed Data Layer**: Local-first Room Database persistence providing complete offline capability, instant reactivity, and seamless demo data seeding across all tables.
- **Confirmed Authentication & RBAC**: Dual-mode authentication offering Student profiles (institution, course, year) and a dedicated Admin Code gateway (ADMIN001–ADMIN050) requiring secure initial setup and password resets.
- **Confirmed Dashboard Priority**: Balanced learning hub emphasizing AI Chat, Code Lab, and Interactive Quizzes alongside quick-access links to Document Summaries, Tests, Hackathons, and Study Plans.
- **AI Capability Architecture**: Hybrid AI Engine featuring built-in offline educational intelligence (heuristics, document parsing, complexity analysis, test-case generation) augmented with Google Gemini API when configured via AI Studio Secrets.

---

### 1. Overview & Core Concept

**AI Study Assistant (AI-SA)** solves the fragmented student workflow by unifying lecture materials, code practice, test preparation, competitive hackathons, and personal scheduling into one cohesive mobile workstation.

- **Target Persona**: High school and university students tackling technical, science, and humanities coursework, alongside educators and campus administrators managing evaluations and hackathons.
- **Key Value**:
  - Zero-latency study assistance and practice quizzes anytime, anywhere.
  - Interactive multi-language coding sandbox with automated complexity audits and debugging insights.
  - Institutional evaluation portal allowing administrators to conduct structured timed tests and track hackathon leaderboards.

---

### 2. User Experience & Visual Design

#### Visual Identity & Theme
- **Theme**: Premium Dark Modern SaaS aesthetic (`#0A0E17` deep canvas, `#141B2D` elevated cards, `#1E293B` borders).
- **Brand Identity**: Signature **AI-SA** hexagonal tech badge with vibrant gradient accents:
  - *Primary Accent*: Electric Indigo (`#6366F1`) & Cyber Cyan (`#06B6D4`) for AI and intelligence flows.
  - *Secondary Accent*: Emerald Green (`#10B981`) for quiz successes, scores, and active hackathons.
  - *Alert Accent*: Amber (`#F59E0B`) & Rose (`#EF4444`) for deadlines and pending submissions.
- **Typography**: Clean, high-legibility sans hierarchy (`HeadlineLarge` for metrics, `TitleMedium` for course cards, `JetBrains Mono` style monospace for the Code Lab).
- **Motion & Polish**: Smooth animated tab transitions, staggered list entrances, pulse indicators for active timers, and interactive ripples on all components.

#### Adaptive Layouts
- **Mobile (Compact)**: Sticky top app bar with AI-SA branding, search, and notification bell; content organized in scrollable cards; ergonomic 5-tab Bottom Navigation (Home, AI Chat, Files, Code Lab, More) with a bottom sheet drawer for Admin and secondary tools.
- **Tablet / Foldable (Expanded)**: Canonical layout utilizing a persistent Navigation Rail on the left and a two-pane list-detail view for Code Lab, Quizzes, and Document Analysis.

---

### 3. Key Product Decisions & Trade-Offs

1. **Room SQLite over Cloud-Only Backend**:
   - *Chosen Approach*: Comprehensive local Room database with 15+ relational entities and pre-populated demo records.
   - *Why*: Guarantees instant 100% offline reliability for studying in classrooms or transit without network delays or quota lockouts, while satisfying the user preference.
2. **50-Account Admin Code System (ADMIN001–ADMIN050)**:
   - *Chosen Approach*: Dedicated admin gateway requiring a valid 8-character admin key, default credential handshake, and forced password change on first login.
   - *Why*: Matches institutional campus proctoring requirements while keeping administrative endpoints strictly segregated from student profiles.
3. **Dedicated Code Lab Sandbox**:
   - *Chosen Approach*: Language-specific problem solver supporting 14 languages (C, C++, Java, Python, JS, TS, HTML, CSS, SQL, PHP, Go, Rust, Kotlin, Swift) with real-time tokenized display, complexity breakdown, edge-case generation, and algorithmic hints.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        AI Study Assistant (AI-SA)                      │
└────────────────────────────────────────────────────────────────────────┘
                                    │
           ┌────────────────────────┴────────────────────────┐
           ▼                                                 ▼
┌───────────────────────┐                         ┌──────────────────────┐
│   Student Workspace   │                         │   Admin Dashboard    │
│  - Dashboard & Streak │                         │  - Stats & Metrics   │
│  - AI Study Chat      │                         │  - Test Creator      │
│  - Document Studio    │                         │  - Quiz Manager      │
│  - Multi-Lang CodeLab │                         │  - Hackathons Portal │
│  - Quizzes & Tests    │                         │  - 50 Admin Accounts │
│  - Study Planner      │                         │  - User Management   │
└───────────────────────┘                         └──────────────────────┘
           │                                                 │
           └────────────────────────┬────────────────────────┘
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                          State & ViewModel                             │
│     StudyViewModel: AuthState, ChatState, CodeState, QuizState,        │
│              FileState, AdminState, PlannerState, StatsState           │
└────────────────────────────────────────────────────────────────────────┘
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                        Data Layer & Repositories                       │
│    AppDatabase (Room SQLite) ◄──► StudyRepository ◄──► AI Engine       │
│  - Profiles & Admin Profiles      - Document Text Extraction Mock/Real │
│  - Files, Summaries & Q&A         - Offline Heuristics + Gemini API    │
│  - Quizzes, Tests & Hackathons    - Algorithmic Complexity Estimator   │
│  - Code Sessions & Study Plans    - Schedule Generation Algorithms     │
└────────────────────────────────────────────────────────────────────────┘
```

#### Room Entities & Relational Schema
1. `UserProfile`: id, name, email, college, course, year, avatar, studyStreak, totalStudyTimeMinutes.
2. `AdminProfile`: id, adminCode (ADMIN001–ADMIN050), role (SuperAdmin/Admin), status, mustChangePassword, lastLogin.
3. `StudyFile`: id, fileName, fileType (PDF, PPTX, DOCX, TXT), fileSize, subject, description, uploadDate, textContent, processingStatus.
4. `FileSummary`: id, fileId, summaryText, keyTopicsJson, flashcardsJson, qnaJson.
5. `ChatConversation` & `ChatMessage`: id, title, sender (User/AI), message, codeSnippet, timestamp.
6. `Quiz`, `QuizQuestion`, `QuizAttempt`: Quizzes categorized by difficulty (Easy, Medium, Hard) and subject, question options A-D, explanations, scoring.
7. `OfficialTest`, `TestQuestion`, `TestAttempt`: Timed evaluations with durations, passing scores, question sets, and student scores.
8. `Hackathon` & `HackathonParticipant`: Title, theme, rules, timeline, registration status, project submission link, and leaderboard ranking.
9. `CodeSession`: Language, code, problemTitle, problemStatement, explanation, timeComplexity, spaceComplexity.
10. `StudyPlan` & `StudyTask`: Exam schedules, daily target slots, priority (High/Med/Low), completion states.
11. `AppAnnouncement` & `AppNotification`: System broadcasts and user alert records.

#### Key Flows & Working Handlers
- **Document Analysis**: Pick or select bundled sample documents (CS, Physics, Biology, Math), run AI summarization, generate 5-question flashcard decks, and ask contextual questions.
- **Code Lab**: Switch between 14 languages, toggle sample problems, generate AI optimization proposals, analyze asymptotic complexity, and convert snippets.
- **Admin Management**: View live statistics (registered students, active hackathons, test submission metrics), publish/edit tests and hackathons, review participants, and administer admin codes.
- **Study Scheduler**: Generate automated week/day study schedules with dynamic completion tracking and streak analytics.
