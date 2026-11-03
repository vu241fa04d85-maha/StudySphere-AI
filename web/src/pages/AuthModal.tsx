import React, { useState } from 'react';
import { X, ShieldCheck, UserCheck, KeyRound, Mail, Sparkles, Check } from 'lucide-react';
import { verifyAdminCode, SEEDED_ADMINS } from '../lib/adminSeeds';
import type { UserSession } from '../lib/store';

interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
  onLoginSuccess: (user: UserSession) => void;
}

export const AuthModal: React.FC<AuthModalProps> = ({ isOpen, onClose, onLoginSuccess }) => {
  const [tab, setTab] = useState<'student' | 'admin' | 'register'>('student');
  const [email, setEmail] = useState('alex.chen@university.edu');
  const [password, setPassword] = useState('password123');
  const [fullName, setFullName] = useState('Alex Chen');
  const [adminCode, setAdminCode] = useState('ADM-7001');
  const [adminPassword, setAdminPassword] = useState('AdminSecret2026!');
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleStudentSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMsg(null);
    if (!email) {
      setErrorMsg('Please enter a valid email address');
      return;
    }

    const session: UserSession = {
      id: 'student_' + Date.now(),
      name: tab === 'register' ? fullName : 'Alex Chen',
      email: email,
      role: 'student',
      college: 'Stanford School of Engineering',
      course: 'Computer Science',
      year: 'Senior (Year 4)',
      studyStreak: 12,
      totalStudyMinutes: 3840,
    };

    onLoginSuccess(session);
    onClose();
  };

  const handleAdminSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMsg(null);
    const verified = verifyAdminCode(adminCode);

    if (!verified) {
      setErrorMsg('Invalid Admin Code. Valid format is ADM-7001 to ADM-7050.');
      return;
    }

    const session: UserSession = {
      id: 'admin_' + verified.code,
      name: verified.name,
      email: verified.email,
      role: 'admin',
      adminCode: verified.code,
      studyStreak: 24,
      totalStudyMinutes: 8900,
    };

    onLoginSuccess(session);
    onClose();
  };

  const fillQuickAdmin = (code: string) => {
    setAdminCode(code);
    setErrorMsg(null);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[#251C38]/40 backdrop-blur-sm animate-fade-in">
      <div className="glass max-w-md w-full shadow-glassFloating border border-white overflow-hidden">
        {/* Modal Header */}
        <div className="px-6 py-4 border-b border-[#B8A4F4]/20 flex items-center justify-between bg-white/40">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-xl bg-gradient-to-tr from-[#6C42C4] to-[#B8A4F4] flex items-center justify-center text-white font-bold text-xs">
              AI
            </div>
            <div>
              <h3 className="font-heading font-bold text-base text-[#251C38]">
                {tab === 'admin' ? 'Admin Portal Access' : tab === 'register' ? 'Join AI Study Assistant' : 'Welcome Back'}
              </h3>
              <p className="text-[11px] text-[#746589]">Secure Single Sign-On & RBAC Gateway</p>
            </div>
          </div>
          <button onClick={onClose} className="p-1 rounded-lg text-[#746589] hover:bg-white/80">
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Tab Selection */}
        <div className="px-6 pt-4 flex gap-2">
          <button
            type="button"
            onClick={() => { setTab('student'); setErrorMsg(null); }}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              tab === 'student'
                ? 'bg-[#6C42C4] text-white shadow-sm'
                : 'bg-white/60 text-[#645B74] hover:bg-white'
            }`}
          >
            Student Login
          </button>
          <button
            type="button"
            onClick={() => { setTab('register'); setErrorMsg(null); }}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              tab === 'register'
                ? 'bg-[#6C42C4] text-white shadow-sm'
                : 'bg-white/60 text-[#645B74] hover:bg-white'
            }`}
          >
            Register
          </button>
          <button
            type="button"
            onClick={() => { setTab('admin'); setErrorMsg(null); }}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all flex items-center justify-center gap-1 ${
              tab === 'admin'
                ? 'bg-[#D6F36A] text-[#251C38] shadow-sm'
                : 'bg-white/60 text-[#645B74] hover:bg-white'
            }`}
          >
            <ShieldCheck className="w-3.5 h-3.5 text-[#556B16]" />
            Admin Code
          </button>
        </div>

        {/* Error notification */}
        {errorMsg && (
          <div className="mx-6 mt-3 p-3 rounded-xl bg-[#FF6347]/15 border border-[#FF6347]/40 text-xs text-[#C34630] font-medium">
            {errorMsg}
          </div>
        )}

        {/* Forms */}
        {tab === 'admin' ? (
          <form onSubmit={handleAdminSubmit} className="p-6 space-y-4">
            <div className="p-3 rounded-xl bg-[#D6F36A]/20 border border-[#D6F36A]/50 text-xs text-[#251C38]">
              <div className="font-bold flex items-center gap-1.5 text-[#556B16] mb-1">
                <Sparkles className="w-3.5 h-3.5" />
                50 Seeded Admin Credentials Available
              </div>
              <p className="text-[11px] text-[#645B74]">
                Codes range from <code className="font-mono font-bold text-[#6C42C4]">ADM-7001</code> to{' '}
                <code className="font-mono font-bold text-[#6C42C4]">ADM-7050</code>.
              </p>
              {/* Quick pills */}
              <div className="flex flex-wrap gap-1.5 mt-2">
                {['ADM-7001', 'ADM-7002', 'ADM-7005', 'ADM-7010'].map((code) => (
                  <button
                    key={code}
                    type="button"
                    onClick={() => fillQuickAdmin(code)}
                    className="px-2 py-0.5 rounded-md bg-white text-[10px] font-mono font-bold text-[#6C42C4] border border-[#B8A4F4]/50 hover:bg-[#B8A4F4]/20"
                  >
                    {code}
                  </button>
                ))}
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-[#251C38] mb-1">Admin Code</label>
              <div className="relative">
                <KeyRound className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#746589]" />
                <input
                  type="text"
                  value={adminCode}
                  onChange={(e) => setAdminCode(e.target.value)}
                  placeholder="ADM-7001"
                  className="w-full pl-9 pr-3 py-2 text-sm rounded-xl glass-input uppercase font-mono font-bold text-[#6C42C4]"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-[#251C38] mb-1">Master Password</label>
              <input
                type="password"
                value={adminPassword}
                onChange={(e) => setAdminPassword(e.target.value)}
                className="w-full px-3 py-2 text-sm rounded-xl glass-input"
                required
              />
            </div>

            <button type="submit" className="w-full py-2.5 btn-chartreuse text-sm flex items-center justify-center gap-1.5">
              <ShieldCheck className="w-4 h-4" />
              Verify & Enter Admin Console
            </button>
          </form>
        ) : (
          <form onSubmit={handleStudentSubmit} className="p-6 space-y-4">
            {tab === 'register' && (
              <div>
                <label className="block text-xs font-bold text-[#251C38] mb-1">Full Name</label>
                <input
                  type="text"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  placeholder="Sarah Connor"
                  className="w-full px-3 py-2 text-sm rounded-xl glass-input"
                  required
                />
              </div>
            )}

            <div>
              <label className="block text-xs font-bold text-[#251C38] mb-1">University Email</label>
              <div className="relative">
                <Mail className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#746589]" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="student@university.edu"
                  className="w-full pl-9 pr-3 py-2 text-sm rounded-xl glass-input"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-[#251C38] mb-1">Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-3 py-2 text-sm rounded-xl glass-input"
                required
              />
            </div>

            <button type="submit" className="w-full py-2.5 btn-lavender text-sm flex items-center justify-center gap-1.5">
              <UserCheck className="w-4 h-4" />
              {tab === 'register' ? 'Create Student Account' : 'Sign In as Student'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
};
