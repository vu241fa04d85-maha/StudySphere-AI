import React from 'react';
import { X, ShieldCheck, Lock, FileText, CheckCircle2 } from 'lucide-react';

interface PrivacyTermsModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const PrivacyTermsModal: React.FC<PrivacyTermsModalProps> = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[#251C38]/40 backdrop-blur-sm animate-fade-in">
      <div className="glass max-w-2xl w-full max-h-[85vh] overflow-hidden flex flex-col shadow-glassFloating border border-white">
        {/* Modal Header */}
        <div className="px-6 py-4 border-b border-[#B8A4F4]/20 flex items-center justify-between bg-white/40">
          <div className="flex items-center gap-2.5">
            <div className="p-2 rounded-xl bg-[#B8A4F4]/30 text-[#6C42C4]">
              <ShieldCheck className="w-5 h-5" />
            </div>
            <div>
              <h3 className="font-heading font-bold text-lg text-[#251C38]">
                Terms of Service & Privacy Policy
              </h3>
              <p className="text-xs text-[#746589]">AI Study Assistant (AI-SA) Platform Governance</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 rounded-lg text-[#746589] hover:bg-white/80 hover:text-[#251C38]"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Modal Content */}
        <div className="p-6 overflow-y-auto space-y-5 text-sm text-[#645B74] leading-relaxed">
          <div className="p-4 rounded-xl bg-[#D6F36A]/20 border border-[#D6F36A]/40 flex items-start gap-3">
            <Lock className="w-5 h-5 text-[#556B16] flex-shrink-0 mt-0.5" />
            <p className="text-xs text-[#251C38]">
              <strong>Zero-Data Selling Guarantee:</strong> Your study notes, uploaded PDFs, quiz scores, and
              code submissions remain strictly private to your account.
            </p>
          </div>

          <section>
            <h4 className="font-heading font-bold text-[#251C38] mb-2 flex items-center gap-2">
              <FileText className="w-4 h-4 text-[#6C42C4]" />
              1. Platform Purpose & Academic Integrity
            </h4>
            <p>
              AI Study Assistant provides intelligent tools including AI conceptual breakdowns, 14-language code
              execution assistance, timed self-assessments, and collaborative hackathons. The platform is designed
              exclusively for educational enhancement and academic preparation.
            </p>
          </section>

          <section>
            <h4 className="font-heading font-bold text-[#251C38] mb-2 flex items-center gap-2">
              <ShieldCheck className="w-4 h-4 text-[#6C42C4]" />
              2. Administrative Role & RBAC Controls
            </h4>
            <p>
              Administrative privileges are secured via 50 pre-seeded cryptographic admin codes. System administrators
              possess authority over official exam publication, hackathon verification, and disaster-recovery backup
              snapshots. All administrative actions are logged in immutable audit tables.
            </p>
          </section>

          <section>
            <h4 className="font-heading font-bold text-[#251C38] mb-2 flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4 text-[#556B16]" />
              3. Data Retention & Offline Sync
            </h4>
            <p>
              When working offline, changes are persisted securely to client-side storage. Upon network restoration,
              state synchronizes seamlessly with the PostgreSQL cloud backend.
            </p>
          </section>
        </div>

        {/* Modal Footer */}
        <div className="px-6 py-3.5 border-t border-[#B8A4F4]/20 flex justify-end bg-white/40">
          <button onClick={onClose} className="px-5 py-2 btn-lavender text-xs">
            I Understand & Agree
          </button>
        </div>
      </div>
    </div>
  );
};
