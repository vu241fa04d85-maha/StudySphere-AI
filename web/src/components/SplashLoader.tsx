import React, { useState, useEffect } from 'react';
import { Sparkles, Brain, Cpu, ShieldCheck } from 'lucide-react';

interface SplashLoaderProps {
  onFinish: () => void;
  durationMs?: number;
}

const STAGES = [
  { text: 'Booting AI Study Engine & Neural Accelerators...', icon: Brain },
  { text: 'Connecting Knowledge Graph & Syllabi Database...', icon: Cpu },
  { text: 'Configuring Interactive Code Lab & Assessment Matrix...', icon: Sparkles },
  { text: 'Calibrating Zero-Trust Security Tokens & Readying Login...', icon: ShieldCheck },
];

export const SplashLoader: React.FC<SplashLoaderProps> = ({ onFinish, durationMs = 5000 }) => {
  const [progress, setProgress] = useState(0);
  const [stageIndex, setStageIndex] = useState(0);

  useEffect(() => {
    const startTime = Date.now();
    const interval = setInterval(() => {
      const elapsed = Date.now() - startTime;
      const nextProgress = Math.min(100, Math.round((elapsed / durationMs) * 100));
      setProgress(nextProgress);

      const nextStage = Math.min(
        STAGES.length - 1,
        Math.floor((elapsed / durationMs) * STAGES.length)
      );
      setStageIndex(nextStage);

      if (elapsed >= durationMs) {
        clearInterval(interval);
        onFinish();
      }
    }, 50);

    return () => clearInterval(interval);
  }, [durationMs, onFinish]);

  const CurrentIcon = STAGES[stageIndex].icon;
  const secondsLeft = Math.max(0, Math.ceil((durationMs - (progress / 100) * durationMs) / 1000));

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-gradient-to-br from-[#F4EFFF] via-[#FFFEF8] to-[#F4FAD9] overflow-hidden select-none">
      {/* Ambient background blur orbs */}
      <div className="absolute top-1/4 -left-20 w-96 h-96 bg-[#B8A4F4]/35 rounded-full blur-3xl pointer-events-none animate-pulse" />
      <div className="absolute bottom-1/4 -right-20 w-96 h-96 bg-[#D6F36A]/35 rounded-full blur-3xl pointer-events-none animate-pulse" style={{ animationDelay: '1s' }} />
      <div className="absolute top-2/3 left-1/3 w-72 h-72 bg-[#FF6347]/15 rounded-full blur-2xl pointer-events-none" />

      {/* Glass card container */}
      <div className="relative glass p-8 sm:p-10 rounded-3xl max-w-md w-full border border-white/90 shadow-glassFloating text-center flex flex-col items-center">
        {/* Pulsing Outer Ring & Brand Logo */}
        <div className="relative mb-6">
          <div className="absolute -inset-4 rounded-3xl bg-gradient-to-r from-[#B8A4F4] via-[#D6F36A] to-[#FF6347] opacity-40 blur-md animate-spin" style={{ animationDuration: '6s' }} />
          <div className="relative w-24 h-24 rounded-2xl bg-gradient-to-br from-[#6C42C4] to-[#B8A4F4] flex flex-col items-center justify-center text-white shadow-lg border-2 border-white">
            <span className="font-heading font-black text-2xl tracking-tight">AI-SA</span>
            <span className="text-[9px] uppercase tracking-widest font-semibold text-[#D6F36A]">Copilot</span>
          </div>
          {/* Animated floating mini badge */}
          <div className="absolute -bottom-2 -right-2 w-8 h-8 rounded-xl bg-[#D6F36A] text-[#556B16] flex items-center justify-center shadow-md border-2 border-white animate-bounce">
            <CurrentIcon className="w-4 h-4" />
          </div>
        </div>

        {/* Application Title */}
        <h1 className="font-heading font-black text-2xl text-[#251C38] tracking-tight">
          AI Study Assistant
        </h1>
        <p className="text-xs text-[#645B74] mt-1 font-medium">
          Full-Stack Academic Platform & Study Copilot
        </p>

        {/* 5-Second Countdown pill */}
        <div className="mt-5 inline-flex items-center gap-2 px-3 py-1 rounded-full bg-[#6C42C4]/10 border border-[#B8A4F4]/30 text-xs text-[#6C42C4] font-semibold">
          <span className="w-2 h-2 rounded-full bg-[#6C42C4] animate-ping" />
          <span>Starting up in {secondsLeft}s ({progress}%)</span>
        </div>

        {/* Progress Bar */}
        <div className="w-full mt-6">
          <div className="w-full h-3 rounded-full bg-[#B8A4F4]/20 p-0.5 overflow-hidden">
            <div
              className="h-full rounded-full bg-gradient-to-r from-[#6C42C4] via-[#B8A4F4] to-[#556B16] transition-all duration-75 ease-out"
              style={{ width: `${progress}%` }}
            />
          </div>
          <div className="flex justify-between items-center text-[11px] text-[#746589] mt-2 font-mono">
            <span>Core v3.2.0</span>
            <span className="font-bold text-[#6C42C4]">{progress}%</span>
            <span>5.0s System Check</span>
          </div>
        </div>

        {/* Rotating Status Message */}
        <div className="mt-6 min-h-[44px] flex items-center justify-center px-4 py-2 rounded-xl bg-white/60 border border-white w-full">
          <p className="text-xs text-[#251C38] font-medium transition-all duration-300">
            {STAGES[stageIndex].text}
          </p>
        </div>

        {/* Bottom micro-info */}
        <p className="text-[10px] text-[#746589]/80 mt-4 font-mono">
          Strict Light Glassmorphism UI • Offline Local & Supabase Sync
        </p>
      </div>
    </div>
  );
};
