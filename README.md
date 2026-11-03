# AI Study Assistant (AI-SA) — Full-Stack & Mobile Architecture

An all-in-one, enterprise-grade AI learning platform delivering an intelligent study chatbot, document intelligence (summaries, flashcards, Q&A), multi-language Code Lab, dynamic quizzes, admin-managed proctored tests, competitive hackathons, study planning, and role-based access control with 50 pre-seeded admin accounts.

---

## 1. System Architecture & Components

```
┌────────────────────────────────────────────────────────────────────────┐
│                        AI Study Assistant (AI-SA)                      │
└────────────────────────────────────────────────────────────────────────┘
                    │                                   │
        ┌───────────┴───────────┐           ┌───────────┴───────────┐
        ▼                       ▼           ▼                       ▼
┌──────────────────┐    ┌────────────────┐ ┌────────────────┐ ┌────────────────┐
│  Web Portal &    │    │ Native Android │ │ Supabase Auth  │ │ Supabase Edge  │
│  Installable PWA │    │ (Compose/Room) │ │ & PostgreSQL   │ │ Functions (AI) │
└──────────────────┘    └────────────────┘ └────────────────┘ └────────────────┘
```

- **Frontend Web & PWA**: React 18, TypeScript, Vite, Tailwind CSS, TanStack Query, and Service Worker.
- **Native Android**: Kotlin, Jetpack Compose (M3 Dark Theme), Room SQLite reactive flow cache, and offline heuristics.
- **Backend & Database**: Supabase PostgreSQL with 22 normalized tables, Row Level Security (RLS), and automated triggers.
- **AI Engine**: Google Gemini API integrated via secure Supabase Edge Functions with prompt grounding and citation tracking.
- **Role-Based Access Control**: Strict separation between Student profiles and 50 Admin Accounts (`ADM001` through `ADM050`).

---

## 2. Separate Portals & Initial Admin Accounts

### Student Portal
- Routes: `/login`, `/register`, `/forgot-password`, `/dashboard`
- Role: `student` (isolated by `auth.uid() = user_id`)

### Administration Portal
- Routes: `/admin/login`, `/admin/dashboard`
- Role: Verified against `public.admin_profiles` via database-side RLS and `public.is_admin()` function.
- Initial 5 Pre-Seeded Admin Accounts:
  1. `ADM001` — `super_admin` — Chief Systems Officer (`adm001.superadmin@studyportal.edu`)
  2. `ADM002` — `academic_admin` — Dean of Academic Affairs (`adm002.academicadmin@studyportal.edu`)
  3. `ADM003` — `test_manager` — Head of Examination Board (`adm003.testadmin@studyportal.edu`)
  4. `ADM004` — `hackathon_manager` — Director of Innovation (`adm004.hackathonadmin@studyportal.edu`)
  5. `ADM005` — `support_admin` — Technical Operations Lead (`adm005.supportadmin@studyportal.edu`)
  *(Accounts `ADM006` through `ADM050` can be provisioned via the `admin-provision` Edge Function by `ADM001`).*

---

## 3. Database Setup & Supabase Migrations

1. Create a new Supabase Project at [supabase.com](https://supabase.com).
2. Navigate to **SQL Editor** in your Supabase Dashboard.
3. Run the complete migration script located at:
   ```bash
   supabase/migrations/20261028000000_ai_study_assistant_schema.sql
   ```
4. Verify that Row Level Security (RLS) is enabled on all tables and the `public.is_admin()` security definer function is created.
5. In **Storage**, create a private bucket named `study-materials` with authenticated read/write policies.

---

## 4. Environment Variables (`.env`)

Copy `.env.example` to `.env` and supply your project credentials:

```ini
# Supabase Public Keys
VITE_SUPABASE_URL=https://your-project.supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-public-key

# Supabase Server-Side Secret Key (Edge Functions & Provisioning ONLY)
SUPABASE_SERVICE_ROLE_KEY=your-service-role-secret-key

# Gemini AI API Key
GEMINI_API_KEY=your-gemini-api-key

# Backup Configuration
BACKUP_STORAGE_BUCKET=ai-study-assistant-backups
BACKUP_ENCRYPTION_PASSPHRASE=your-secure-passphrase
```

---

## 5. Web & PWA Deployment Instructions

### Development Server
```bash
cd web
npm install
npm run dev
```

### Production Build & Hosting (Vercel / Netlify)
```bash
cd web
npm run build
```
For single-page application (SPA) routing:
- **Vercel**: Pre-configured `vercel.json` rewrites all requests to `/index.html`.
- **Netlify**: Ensure `_redirects` contains `/* /index.html 200`.

### PWA Installation
- Open the deployed HTTPS URL in Google Chrome, Microsoft Edge, or Safari Mobile.
- Tap the **Install App** icon in the address bar or select **Add to Home Screen**.
- Offline capabilities are managed by `public/sw.js` with `offline.html` serving as a graceful network fallback.

---

## 6. Android Application Compilation & Packaging

### Running Gradle Verification & Robolectric Tests
```bash
gradle :app:testDebugUnitTest
```

### Compiling APK / AAB
```bash
gradle :app:assembleDebug
```
Output APK location: `app/build/outputs/apk/debug/app-debug.apk`.

### Capacitor Hybrid Mobile Packaging (Optional for iOS / Android Webview)
```bash
cd web
npm install @capacitor/core @capacitor/cli @capacitor/android @capacitor/ios
npx cap init "AI Study Assistant" "com.aistudio.aistudyassistant.wxvj"
npx cap add android
npx cap copy
npx cap open android
```

---

## 7. Backup & Disaster Recovery Guide

### Running Scheduled Database Backups
```bash
export PGHOST="db.your-project.supabase.co"
export PGDATABASE="postgres"
export PGUSER="postgres"
export BACKUP_ENCRYPTION_PASSPHRASE="your-passphrase"

./scripts/backup_database.sh
```
The script performs:
1. `pg_dump` of PostgreSQL schema, roles, and records.
2. OpenSSL AES-256-CBC encryption using PBKDF2 key derivation.
3. SHA-256 tamper verification checksum computation.
4. Secure erasure of the unencrypted intermediate dump.

### Safe Restoration Verification
```bash
./scripts/restore_database.sh /path/to/ai_sa_db_20261028.sql.enc
```
*Restoration requires interactive confirmation (`RESTORE_CONFIRM`) and should always be tested first in a staging environment.*

---

## 8. Test Execution Commands

| Target | Command | Purpose |
| :--- | :--- | :--- |
| **Android JVM & Robolectric** | `gradle :app:testDebugUnitTest` | Validates resource identifiers, models, and Room DAO queries |
| **Web Frontend Tests** | `cd web && npm run test` | Validates Zod schemas, UI components, and state stores |
| **Supabase Edge Functions** | `supabase functions serve` | Local testing of AI prompts, rate limiting, and RBAC gates |
