import React from 'react';
import { Sparkles, Bot, Code2, HelpCircle, FileCheck2, Trophy, ArrowRight, ShieldCheck, CheckCircle2 } from 'lucide-react';

interface LandingPageProps {
  onGetStarted: () => void;
  onOpenAdminAuth: () => void;
}

export const LandingPage: React.FC<LandingPageProps> = ({ onGetStarted, onOpenAdminAuth }) => {
  return (
    <div className="space-y-12 py-6 animate-fade-in">
      {/* Hero Section */}
      <div className="glass p-8 sm:p-14 relative overflow-hidden border border-white/95 text-center max-w-5xl mx-auto shadow-glassFloating">
        <div className="absolute -top-24 -left-24 w-96 h-96 bg-[#B8A4F4]/30 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute -bottom-24 -right-24 w-96 h-96 bg-[#D6F36A]/25 rounded-full blur-3xl pointer-events-none" />

        <div className="relative z-10 space-y-6 max-w-3xl mx-auto">
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-[#B8A4F4]/30 text-[#6C42C4] text-xs font-bold border border-[#B8A4F4]/50 shadow-2xs">
            <Sparkles className="w-3.5 h-3.5" /> Premium Light Glassmorphism Architecture
          </div>

          <h1 className="font-heading font-extrabold text-3xl sm:text-5xl text-[#251C38] leading-tight tracking-tight">
            Accelerate Academic Mastery with <span className="text-[#6C42C4]">AI-SA</span>
          </h1>

          <p className="text-sm sm:text-base text-[#645B74] leading-relaxed max-w-2xl mx-auto">
            The next-generation study platform engineered for university academics. Includes an intelligent AI Tutor,
            a 14-language Code Lab, adaptive active recall quizzes, timed official midterms, and $40,000 hackathon challenges.
          </p>

          <div className="flex flex-wrap items-center justify-center gap-3 pt-2">
            <button
              onClick={onGetStarted}
              className="px-6 py-3 btn-lavender text-sm font-bold flex items-center gap-2"
            >
              <span>Explore Dashboard</span>
              <ArrowRight className="w-4 h-4" />
            </button>
            <button
              onClick={onOpenAdminAuth}
              className="px-6 py-3 btn-chartreuse text-sm font-bold flex items-center gap-2"
            >
              <ShieldCheck className="w-4 h-4" />
              <span>Admin Code Login (50 Accounts)</span>
            </button>
          </div>
        </div>
      </div>

      {/* Feature Highlights Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
        <div className="glass p-6 border border-white/95 glass-card-hover space-y-3">
          <div className="w-12 h-12 rounded-2xl bg-[#B8A4F4]/30 text-[#6C42C4] flex items-center justify-center">
            <Bot className="w-6 h-6" />
          </div>
          <h3 className="font-heading font-bold text-base text-[#251C38]">
            Multimodal AI Study Tutor
          </h3>
          <p className="text-xs text-[#645B74] leading-relaxed">
            Explains complex CS algorithms, simplifies calculus formulas, and builds personalized study flashcards with heuristic instant fallback.
          </p>
        </div>

        <div className="glass p-6 border border-white/95 glass-card-hover space-y-3">
          <div className="w-12 h-12 rounded-2xl bg-[#D6F36A]/40 text-[#556B16] flex items-center justify-center">
            <Code2 className="w-6 h-6" />
          </div>
          <h3 className="font-heading font-bold text-base text-[#251C38]">
            14-Language Code Lab
          </h3>
          <p className="text-xs text-[#645B74] leading-relaxed">
            Write, compile, and debug Python, TypeScript, C++, Rust, Go, Kotlin, and 8 more languages with automated Big-O complexity profiling.
          </p>
        </div>

        <div className="glass p-6 border border-white/95 glass-card-hover space-y-3">
          <div className="w-12 h-12 rounded-2xl bg-[#F4FAD9] text-[#556B16] border border-[#D6F36A]/50 flex items-center justify-center">
            <Trophy className="w-6 h-6" />
          </div>
          <h3 className="font-heading font-bold text-base text-[#251C38]">
            Timed Exams & Hackathons
          </h3>
          <p className="text-xs text-[#645B74] leading-relaxed">
            Realistic examination portal with countdown timers, question palettes, and global hackathon competitions with $40,000 in prizes.
          </p>
        </div>
      </div>

      {/* Trust & Architecture Badges */}
      <div className="glass p-6 border border-white/95 max-w-4xl mx-auto flex flex-wrap items-center justify-around gap-4 text-xs text-[#645B74]">
        <div className="flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 text-[#556B16]" />
          <span>PostgreSQL + 22 RLS Security Tables</span>
        </div>
        <div className="flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 text-[#556B16]" />
          <span>Full Offline Mode & Local Fallbacks</span>
        </div>
        <div className="flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 text-[#556B16]" />
          <span>Responsive Mobile & Desktop Glass PWA</span>
        </div>
      </div>
    </div>
  );
};
