export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[];

export interface Database {
  public: {
    Tables: {
      profiles: {
        Row: {
          id: string;
          full_name: string;
          email: string;
          college: string;
          course: string;
          year: string;
          profile_image: string | null;
          study_streak: number;
          total_study_minutes: number;
          created_at: string;
          updated_at: string;
        };
        Insert: Omit<Database["public"]["Tables"]["profiles"]["Row"], "created_at" | "updated_at">;
        Update: Partial<Database["public"]["Tables"]["profiles"]["Insert"]>;
      };
      admin_profiles: {
        Row: {
          id: string;
          auth_user_id: string | null;
          admin_code: string;
          full_name: string;
          email: string;
          role: "super_admin" | "academic_admin" | "test_manager" | "hackathon_manager" | "support_admin";
          status: "active" | "suspended" | "pending_verification";
          mfa_enabled: boolean;
          must_change_password: boolean;
          last_login: string | null;
          created_at: string;
          updated_at: string;
        };
        Insert: Omit<Database["public"]["Tables"]["admin_profiles"]["Row"], "id" | "created_at" | "updated_at">;
        Update: Partial<Database["public"]["Tables"]["admin_profiles"]["Insert"]>;
      };
      study_files: {
        Row: {
          id: string;
          user_id: string;
          file_name: string;
          file_type: "PDF" | "PPT" | "PPTX" | "DOC" | "DOCX" | "TXT" | "IMAGE";
          file_size_bytes: number;
          storage_path: string;
          subject: string;
          description: string | null;
          extracted_text: string | null;
          processing_status: string;
          created_at: string;
          updated_at: string;
        };
        Insert: Omit<Database["public"]["Tables"]["study_files"]["Row"], "id" | "created_at" | "updated_at">;
        Update: Partial<Database["public"]["Tables"]["study_files"]["Insert"]>;
      };
      quizzes: {
        Row: {
          id: string;
          title: string;
          description: string | null;
          subject: string;
          difficulty: "easy" | "medium" | "hard";
          question_count: number;
          created_by: string | null;
          is_official: boolean;
          created_at: string;
        };
      };
      tests: {
        Row: {
          id: string;
          title: string;
          description: string | null;
          subject: string;
          duration_minutes: number;
          total_marks: number;
          passing_score: number;
          created_by_admin: string;
          status: string;
          start_time: string;
          end_time: string;
          created_at: string;
        };
      };
      hackathons: {
        Row: {
          id: string;
          title: string;
          theme: string;
          description: string;
          rules: string;
          eligibility: string;
          prize_pool: string;
          start_date: string;
          end_date: string;
          registration_deadline: string;
          created_by_admin: string;
          status: string;
          participant_count: number;
          created_at: string;
        };
      };
      study_tasks: {
        Row: {
          id: string;
          plan_id: string;
          user_id: string;
          title: string;
          description: string | null;
          due_date: string;
          priority: "High" | "Medium" | "Low";
          is_completed: boolean;
          estimated_minutes: number;
          created_at: string;
        };
        Insert: Omit<Database["public"]["Tables"]["study_tasks"]["Row"], "id" | "created_at">;
        Update: Partial<Database["public"]["Tables"]["study_tasks"]["Insert"]>;
      };
      announcements: {
        Row: {
          id: string;
          created_by: string;
          title: string;
          content: string;
          category: string;
          created_at: string;
        };
      };
      backup_jobs: {
        Row: {
          id: string;
          job_type: string;
          destination_uri: string;
          size_bytes: number | null;
          checksum_sha256: string | null;
          status: "started" | "in_progress" | "completed" | "failed" | "verified";
          error_message: string | null;
          started_at: string;
          completed_at: string | null;
        };
      };
    };
  };
}
