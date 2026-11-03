import React from 'react';
import { ShieldCheck, Cookie, X } from 'lucide-react';

interface CookieConsentProps {
  onAccept: () => void;
  onOpenPrivacy: () => void;
}

export const CookieConsent: React.FC<CookieConsentProps> = ({ onAccept, onOpenPrivacy }) => {
  return (
    <div className="fixed bottom-20 lg:bottom-6 right-4 left-4 sm:left-auto sm:max-w-md z-50 animate-slide-up">
      <div className="glass p-4 sm:p-5 shadow-glassFloating border border-white/95">
        <div className="flex items-start gap-3">
          <div className="p-2.5 rounded-xl bg-[#D6F36A]/40 text-[#556B16] flex-shrink-0">
            <Cookie className="w-5 h-5" />
          </div>
          <div className="flex-1">
            <div className="flex items-center justify-between">
              <h4 className="font-heading font-bold text-sm text-[#251C38] flex items-center gap-1.5">
                Privacy & Data Preferences
                <ShieldCheck className="w-4 h-4 text-[#6C42C4]" />
              </h4>
              <button
                onClick={onAccept}
                className="text-[#746589] hover:text-[#251C38] p-1"
                aria-label="Dismiss banner"
              >
                <X className="w-4 h-4" />
              </button>
            </div>
            <p className="text-xs text-[#645B74] mt-1.5 leading-relaxed">
              We store your local study progress, offline flashcards, and preferences securely.
              No third-party trackers are used.
            </p>
            <div className="flex items-center gap-2.5 mt-3.5">
              <button
                onClick={onAccept}
                className="px-3.5 py-1.5 btn-lavender text-xs font-semibold"
              >
                Accept All
              </button>
              <button
                onClick={onOpenPrivacy}
                className="text-xs text-[#6C42C4] hover:underline font-medium"
              >
                Read Guidelines
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
