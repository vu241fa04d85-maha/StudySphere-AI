import React from 'react';
import { HelpCircle, ArrowLeft } from 'lucide-react';

interface NotFoundProps {
  onGoHome: () => void;
}

export const NotFound: React.FC<NotFoundProps> = ({ onGoHome }) => {
  return (
    <div className="min-h-[60vh] flex items-center justify-center p-4">
      <div className="glass max-w-md w-full p-8 border border-white/95 shadow-glass text-center space-y-4">
        <div className="w-16 h-16 mx-auto rounded-3xl bg-[#B8A4F4]/30 text-[#6C42C4] flex items-center justify-center font-heading font-extrabold text-2xl">
          404
        </div>
        <h2 className="font-heading font-bold text-xl text-[#251C38]">
          Curriculum Route Not Found
        </h2>
        <p className="text-xs text-[#645B74] leading-relaxed">
          The study module or exam portal route you requested does not exist or has been relocated.
        </p>
        <button
          onClick={onGoHome}
          className="px-5 py-2.5 btn-lavender text-xs font-bold inline-flex items-center gap-2"
        >
          <ArrowLeft className="w-4 h-4" />
          Return to Dashboard
        </button>
      </div>
    </div>
  );
};
