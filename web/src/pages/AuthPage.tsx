import React, { useState } from 'react';
import {
  ShieldCheck,
  UserCheck,
  KeyRound,
  Mail,
  Lock,
  User,
  GraduationCap,
  BookOpen,
  ArrowRight,
  Sparkles,
  AlertCircle,
  CheckCircle2,
  HelpCircle
} from 'lucide-react';
import { verifyAdminCode, SEEDED_ADMINS } from '../lib/adminSeeds';
import type { UserSession } from '../lib/store';

interface AuthPageProps {
  onLoginSuccess: (user: UserSession) => void;
  onOpenPrivacy?: () => void;
}

export const AuthPage: React.FC<AuthPageProps> = ({ onLoginSuccess, onOpenPrivacy }) => {
  // Role switching: User (Student) vs Admin
  const [roleMode, setRoleMode] = useState<'student' | 'admin'>('student');
  
  // Auth mode: Sign In vs Sign Up
  const [authMode, setAuthMode] = useState<'signin' | 'signup'>('signin');

  // Form Fields - Student
  const [email, setEmail] = useState('chinkaramu2006@gmail.com');
  const [password, setPassword] = useState('password123');
  const [fullName, setFullName] = useState('Ramu Chinka');
  const [college, setCollege] = useState('Institute of Technology & Sciences');
  const [course, setCourse] = useState('Computer Science & AI');
  const [confirmPassword, setConfirmPassword] = useState('password123');

  // Form Fields - Admin
  const [adminCode, setAdminCode] = useState('ADM-7001');
  const [adminEmail, setAdminEmail] = useState('admin.jenkins@ai-sa.edu');
  const [adminPassword, setAdminPassword] = useState('AdminSecret2026!');
  const [adminFullName, setAdminFullName] = useState('Dr. Sarah Jenkins');
  const [adminConfirmPassword, setAdminConfirmPassword] = useState('AdminSecret2026!');

  // Validation & UI State
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const validateEmail = (input: string) => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(input.trim());
  };

  const handleStudentSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage(null);
    setSuccessMessage(null);

    // Validation checks
    if (!validateEmail(email)) {
      setErrorMessage('Please enter a valid email address (e.g., student@university.edu)');
      return;
    }

    if (password.length < 6) {
      setErrorMessage('Password must be at least 6 characters long');
      return;
    }

    if (authMode === 'signup') {
      if (!fullName.trim()) {
        setErrorMessage('Please enter your full name');
        return;
      }
      if (password !== confirmPassword) {
        setErrorMessage('Passwords do not match');
        return;
      }
    }

    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      setSuccessMessage(authMode === 'signup' ? 'Account created successfully! Redirecting...' : 'Authenticated successfully! Redirecting to dashboard...');

      const session: UserSession = {
        id: 'student_' + Date.now(),
        name: authMode === 'signup' ? fullName.trim() : (fullName.trim() || 'Alex Chen'),
        email: email.trim().toLowerCase(),
        role: 'student',
        college: college.trim() || 'Global University of Technology',
        course: course.trim() || 'Computer Science & AI',
        year: 'Junior (Year 3)',
        studyStreak: 12,
        totalStudyMinutes: 3840,
      };

      setTimeout(() => {
        onLoginSuccess(session);
      }, 600);
    }, 400);
  };

  const handleAdminSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage(null);
    setSuccessMessage(null);

    const verified = verifyAdminCode(adminCode);
    if (!verified) {
      setErrorMessage('Invalid Admin Code. Valid seeded admin codes are ADM-7001 to ADM-7050.');
      return;
    }

    if (adminPassword.length < 6) {
      setErrorMessage('Admin Security Password must be at least 6 characters');
      return;
    }

    if (authMode === 'signup') {
      if (!adminFullName.trim()) {
        setErrorMessage('Please enter the Admin full name');
        return;
      }
      if (!validateEmail(adminEmail)) {
        setErrorMessage('Please enter a valid institution email');
        return;
      }
      if (adminPassword !== adminConfirmPassword) {
        setErrorMessage('Admin passwords do not match');
        return;
      }
    }

    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      setSuccessMessage(`Welcome ${verified.name}! Verified as ${verified.roleLabel}. Redirecting...`);

      const session: UserSession = {
        id: 'admin_' + verified.code,
        name: authMode === 'signup' ? adminFullName.trim() : verified.name,
        email: authMode === 'signup' ? adminEmail.trim() : verified.email,
        role: 'admin',
        adminCode: verified.code,
        college: verified.department,
        course: verified.roleLabel,
        studyStreak: 25,
        totalStudyMinutes: 9120,
      };

      setTimeout(() => {
        onLoginSuccess(session);
      }, 600);
    }, 400);
  };

  const fillQuickStudent = () => {
    setRoleMode('student');
    setEmail('chinkaramu2006@gmail.com');
    setPassword('password123');
    setFullName('Ramu Chinka');
    setCollege('Institute of Technology');
    setCourse('Computer Science & AI');
    setConfirmPassword('password123');
    setErrorMessage(null);
  };

  const fillQuickAdmin = (code: string = 'ADM-7001') => {
    setRoleMode('admin');
    setAdminCode(code);
    setAdminPassword('AdminSecret2026!');
    const found = verifyAdminCode(code);
    if (found) {
      setAdminEmail(found.email);
      setAdminFullName(found.name);
    }
    setAdminConfirmPassword('AdminSecret2026!');
    setErrorMessage(null);
  };

  return (
    <div className="min-h-screen flex flex-col justify-center items-center px-4 py-12 relative overflow-hidden bg-gradient-to-br from-[#F4EFFF] via-[#FFFEF8] to-[#F4FAD9]">
      {/* Ambient background blur elements */}
      <div className="absolute top-10 left-10 w-96 h-96 bg-[#B8A4F4]/30 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-10 right-10 w-96 h-96 bg-[#D6F36A]/30 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute top-1/2 right-1/4 w-64 h-64 bg-[#FF6347]/10 rounded-full blur-2xl pointer-events-none" />

      {/* Main Glass Authentication Card */}
      <div className="relative glass max-w-xl w-full rounded-3xl p-6 sm:p-8 border border-white/90 shadow-glassFloating z-10">
        {/* Brand Header */}
        <div className="text-center mb-6">
          <div className="inline-flex items-center justify-center gap-2 mb-3">
            <div className="w-12 h-12 rounded-2xl bg-gradient-to-br from-[#6C42C4] to-[#B8A4F4] flex items-center justify-center text-white font-black text-lg shadow-md border-2 border-white">
              AI
            </div>
            <div className="text-left">
              <span className="font-heading font-black text-xl text-[#251C38] tracking-tight block">
                AI Study Assistant
              </span>
              <span className="text-[11px] font-semibold text-[#6C42C4] uppercase tracking-wider block">
                Single Sign-On & Access Gateway
              </span>
            </div>
          </div>
          <p className="text-xs text-[#645B74]">
            Sign in or create an account to access the dashboard, AI tutor, code lab, and quizzes
          </p>
        </div>

        {/* 1. ADMIN & USER SWITCHING OPTION (Requested Feature) */}
        <div className="mb-6">
          <label className="block text-[11px] font-bold uppercase tracking-wider text-[#746589] mb-2 text-center">
            Select Your Workspace Role
          </label>
          <div className="grid grid-cols-2 gap-2 p-1.5 rounded-2xl bg-white/60 border border-white">
            <button
              type="button"
              onClick={() => {
                setRoleMode('student');
                setErrorMessage(null);
              }}
              className={`flex items-center justify-center gap-2 py-3 px-4 rounded-xl font-bold text-xs transition-all duration-200 ${
                roleMode === 'student'
                  ? 'bg-gradient-to-r from-[#6C42C4] to-[#B8A4F4] text-white shadow-md'
                  : 'text-[#645B74] hover:bg-white/80'
              }`}
            >
              <UserCheck className="w-4 h-4" />
              <span>Student / Learner</span>
            </button>
            <button
              type="button"
              onClick={() => {
                setRoleMode('admin');
                setErrorMessage(null);
              }}
              className={`flex items-center justify-center gap-2 py-3 px-4 rounded-xl font-bold text-xs transition-all duration-200 ${
                roleMode === 'admin'
                  ? 'bg-gradient-to-r from-[#556B16] to-[#D6F36A] text-[#251C38] shadow-md'
                  : 'text-[#645B74] hover:bg-white/80'
              }`}
            >
              <ShieldCheck className="w-4 h-4" />
              <span>Admin / Faculty</span>
            </button>
          </div>
        </div>

        {/* 2. SIGN IN vs SIGN UP SWITCHER (Requested Feature) */}
        <div className="flex border-b border-[#B8A4F4]/20 mb-6">
          <button
            type="button"
            onClick={() => {
              setAuthMode('signin');
              setErrorMessage(null);
            }}
            className={`flex-1 pb-3 text-center text-xs font-bold transition-all border-b-2 ${
              authMode === 'signin'
                ? 'border-[#6C42C4] text-[#6C42C4]'
                : 'border-transparent text-[#746589] hover:text-[#251C38]'
            }`}
          >
            Sign In
          </button>
          <button
            type="button"
            onClick={() => {
              setAuthMode('signup');
              setErrorMessage(null);
            }}
            className={`flex-1 pb-3 text-center text-xs font-bold transition-all border-b-2 ${
              authMode === 'signup'
                ? 'border-[#6C42C4] text-[#6C42C4]'
                : 'border-transparent text-[#746589] hover:text-[#251C38]'
            }`}
          >
            Create New Account (Sign Up)
          </button>
        </div>

        {/* Error Banner */}
        {errorMessage && (
          <div className="mb-5 p-3 rounded-xl bg-[#FF6347]/10 border border-[#FF6347]/30 flex items-start gap-2.5 text-[#C34630] text-xs animate-shake">
            <AlertCircle className="w-4 h-4 shrink-0 mt-0.5" />
            <div className="flex-1 font-medium">{errorMessage}</div>
          </div>
        )}

        {/* Success Banner */}
        {successMessage && (
          <div className="mb-5 p-3 rounded-xl bg-[#D6F36A]/40 border border-[#556B16]/30 flex items-center gap-2.5 text-[#556B16] text-xs font-bold animate-fade-in">
            <CheckCircle2 className="w-4 h-4 shrink-0" />
            <div className="flex-1">{successMessage}</div>
          </div>
        )}

        {/* 3. AUTH FORM FOR STUDENT */}
        {roleMode === 'student' && (
          <form onSubmit={handleStudentSubmit} className="space-y-4">
            {authMode === 'signup' && (
              <>
                <div>
                  <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                    Full Name <span className="text-[#FF6347]">*</span>
                  </label>
                  <div className="relative">
                    <User className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                    <input
                      type="text"
                      required
                      value={fullName}
                      onChange={(e) => setFullName(e.target.value)}
                      placeholder="e.g. Ramu Chinka"
                      className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#B8A4F4] shadow-xs"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                  <div>
                    <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                      College / University
                    </label>
                    <div className="relative">
                      <GraduationCap className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                      <input
                        type="text"
                        value={college}
                        onChange={(e) => setCollege(e.target.value)}
                        placeholder="Institute Name"
                        className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#B8A4F4] shadow-xs"
                      />
                    </div>
                  </div>
                  <div>
                    <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                      Course / Major
                    </label>
                    <div className="relative">
                      <BookOpen className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                      <input
                        type="text"
                        value={course}
                        onChange={(e) => setCourse(e.target.value)}
                        placeholder="e.g. Computer Science"
                        className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#B8A4F4] shadow-xs"
                      />
                    </div>
                  </div>
                </div>
              </>
            )}

            <div>
              <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                Email Address <span className="text-[#FF6347]">*</span>
              </label>
              <div className="relative">
                <Mail className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="student@university.edu"
                  className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#B8A4F4] shadow-xs"
                />
              </div>
            </div>

            <div>
              <div className="flex justify-between items-center mb-1">
                <label className="block text-[11px] font-bold text-[#251C38]">
                  Password <span className="text-[#FF6347]">*</span>
                </label>
                {authMode === 'signin' && (
                  <span className="text-[10px] text-[#6C42C4] font-medium cursor-pointer hover:underline">
                    Forgot password?
                  </span>
                )}
              </div>
              <div className="relative">
                <Lock className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="At least 6 characters"
                  className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#B8A4F4] shadow-xs"
                />
              </div>
            </div>

            {authMode === 'signup' && (
              <div>
                <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                  Confirm Password <span className="text-[#FF6347]">*</span>
                </label>
                <div className="relative">
                  <Lock className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                  <input
                    type="password"
                    required
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="Repeat password"
                    className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#B8A4F4] shadow-xs"
                  />
                </div>
              </div>
            )}

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full py-3 px-4 rounded-xl bg-gradient-to-r from-[#6C42C4] to-[#B8A4F4] hover:opacity-95 text-white font-bold text-xs flex items-center justify-center gap-2 shadow-md transition-all mt-2 cursor-pointer"
            >
              <span>{isSubmitting ? 'Checking Credentials...' : authMode === 'signup' ? 'Create Student Account' : 'Sign In to Student Dashboard'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </form>
        )}

        {/* 4. AUTH FORM FOR ADMIN */}
        {roleMode === 'admin' && (
          <form onSubmit={handleAdminSubmit} className="space-y-4">
            <div className="p-3 rounded-xl bg-[#556B16]/10 border border-[#D6F36A] flex items-center gap-2.5 text-[#556B16] text-[11px]">
              <ShieldCheck className="w-4 h-4 shrink-0" />
              <span>Admin access requires a pre-authorized Admin Code (ADM-7001 to ADM-7050) with role enforcement.</span>
            </div>

            {authMode === 'signup' && (
              <>
                <div>
                  <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                    Admin Full Name <span className="text-[#FF6347]">*</span>
                  </label>
                  <div className="relative">
                    <User className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                    <input
                      type="text"
                      required
                      value={adminFullName}
                      onChange={(e) => setAdminFullName(e.target.value)}
                      placeholder="e.g. Dr. Sarah Jenkins"
                      className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#556B16] shadow-xs"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                    Institution Email <span className="text-[#FF6347]">*</span>
                  </label>
                  <div className="relative">
                    <Mail className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                    <input
                      type="email"
                      required
                      value={adminEmail}
                      onChange={(e) => setAdminEmail(e.target.value)}
                      placeholder="admin@ai-sa.edu"
                      className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#556B16] shadow-xs"
                    />
                  </div>
                </div>
              </>
            )}

            <div>
              <div className="flex justify-between items-center mb-1">
                <label className="block text-[11px] font-bold text-[#251C38]">
                  Assigned Admin Code <span className="text-[#FF6347]">*</span>
                </label>
                <span className="text-[10px] text-[#556B16] font-semibold">
                  Valid: ADM-7001 to ADM-7050
                </span>
              </div>
              <div className="relative">
                <KeyRound className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                <input
                  type="text"
                  required
                  value={adminCode}
                  onChange={(e) => setAdminCode(e.target.value.toUpperCase())}
                  placeholder="ADM-7001"
                  className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs font-mono font-bold text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#556B16] shadow-xs"
                />
              </div>
            </div>

            <div>
              <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                Security Password <span className="text-[#FF6347]">*</span>
              </label>
              <div className="relative">
                <Lock className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                <input
                  type="password"
                  required
                  value={adminPassword}
                  onChange={(e) => setAdminPassword(e.target.value)}
                  placeholder="Admin Security Password"
                  className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#556B16] shadow-xs"
                />
              </div>
            </div>

            {authMode === 'signup' && (
              <div>
                <label className="block text-[11px] font-bold text-[#251C38] mb-1">
                  Confirm Admin Password <span className="text-[#FF6347]">*</span>
                </label>
                <div className="relative">
                  <Lock className="w-4 h-4 absolute left-3 top-3 text-[#746589]" />
                  <input
                    type="password"
                    required
                    value={adminConfirmPassword}
                    onChange={(e) => setAdminConfirmPassword(e.target.value)}
                    placeholder="Repeat password"
                    className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-white/70 border border-white text-xs text-[#251C38] placeholder-[#746589]/60 focus:outline-none focus:ring-2 focus:ring-[#556B16] shadow-xs"
                  />
                </div>
              </div>
            )}

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full py-3 px-4 rounded-xl bg-gradient-to-r from-[#556B16] to-[#D6F36A] hover:opacity-95 text-[#251C38] font-bold text-xs flex items-center justify-center gap-2 shadow-md transition-all mt-2 cursor-pointer"
            >
              <span>{isSubmitting ? 'Verifying Admin Credentials...' : authMode === 'signup' ? 'Enroll New Admin Account' : 'Authenticate & Open Admin Portal'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </form>
        )}

        {/* 5. QUICK 1-CLICK DEMO CREDENTIAL FILLERS */}
        <div className="mt-6 pt-5 border-t border-[#B8A4F4]/20">
          <div className="flex items-center justify-between mb-2">
            <span className="text-[11px] font-bold text-[#746589] uppercase tracking-wider flex items-center gap-1.5">
              <Sparkles className="w-3.5 h-3.5 text-[#6C42C4]" />
              Quick Demo Logins:
            </span>
          </div>
          <div className="grid grid-cols-2 gap-2">
            <button
              type="button"
              onClick={fillQuickStudent}
              className="px-3 py-2 rounded-xl bg-white/60 hover:bg-white text-left border border-white text-[11px] transition-all group"
            >
              <div className="font-bold text-[#251C38] group-hover:text-[#6C42C4]">Student Demo</div>
              <div className="text-[10px] text-[#746589] truncate">chinkaramu2006@gmail.com</div>
            </button>
            <button
              type="button"
              onClick={() => fillQuickAdmin('ADM-7001')}
              className="px-3 py-2 rounded-xl bg-white/60 hover:bg-white text-left border border-white text-[11px] transition-all group"
            >
              <div className="font-bold text-[#251C38] group-hover:text-[#556B16]">Admin Demo</div>
              <div className="text-[10px] text-[#746589] truncate">ADM-7001 (Super Admin)</div>
            </button>
          </div>
        </div>

        {/* Footer info */}
        <div className="mt-5 text-center">
          <p className="text-[10px] text-[#746589]">
            Protected by End-to-End Encryption & Role-Based Access Control (RBAC).
            {onOpenPrivacy && (
              <button
                type="button"
                onClick={onOpenPrivacy}
                className="ml-1 text-[#6C42C4] underline font-medium hover:text-[#251C38]"
              >
                Terms & Privacy
              </button>
            )}
          </p>
        </div>
      </div>
    </div>
  );
};
