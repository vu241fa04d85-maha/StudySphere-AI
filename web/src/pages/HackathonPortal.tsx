import React, { useState } from 'react';
import { Trophy, Calendar, Users, Sparkles, CheckCircle2, ArrowRight, ExternalLink } from 'lucide-react';
import { INITIAL_HACKATHONS, type Hackathon } from '../lib/store';

export const HackathonPortal: React.FC = () => {
  const [hackathons, setHackathons] = useState<Hackathon[]>(INITIAL_HACKATHONS);
  const [successToast, setSuccessToast] = useState<string | null>(null);

  const toggleRegister = (id: string) => {
    setHackathons((prev) =>
      prev.map((h) => {
        if (h.id === id) {
          const nextState = !h.registered;
          setSuccessToast(
            nextState
              ? `Successfully registered team for "${h.title}"! Competition discord invitation sent.`
              : `Registration cancelled for "${h.title}".`
          );
          setTimeout(() => setSuccessToast(null), 3500);
          return {
            ...h,
            registered: nextState,
            participants: nextState ? h.participants + 1 : h.participants - 1
          };
        }
        return h;
      })
    );
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Header */}
      <div>
        <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
          Hackathon Arena & Innovation Sprints
          <span className="px-2 py-0.5 rounded-full text-xs font-bold bg-[#D6F36A]/40 text-[#556B16] border border-[#D6F36A]/60">
            $40,000 Total Prizes
          </span>
        </h1>
        <p className="text-xs text-[#645B74] mt-1">
          Compete in university & industry hackathons, prototype AI solutions, and submit repo links.
        </p>
      </div>

      {successToast && (
        <div className="p-4 rounded-2xl bg-[#D6F36A]/40 border border-[#D6F36A] text-xs font-bold text-[#556B16] flex items-center gap-2 shadow-sm animate-fade-in">
          <CheckCircle2 className="w-4 h-4" />
          {successToast}
        </div>
      )}

      {/* Hackathons Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {hackathons.map((hackathon) => (
          <div key={hackathon.id} className="glass p-6 sm:p-7 border border-white/95 glass-card-hover space-y-5 flex flex-col justify-between">
            <div className="space-y-4">
              {/* Card Top */}
              <div className="flex items-start justify-between gap-3">
                <div className="flex items-center gap-3">
                  <div className="p-3.5 rounded-2xl bg-gradient-to-tr from-[#6C42C4] to-[#B8A4F4] text-white shadow-xs">
                    <Trophy className="w-6 h-6" />
                  </div>
                  <div>
                    <h3 className="font-heading font-bold text-lg text-[#251C38] leading-tight">
                      {hackathon.title}
                    </h3>
                    <p className="text-xs text-[#6C42C4] font-semibold mt-0.5">{hackathon.theme}</p>
                  </div>
                </div>

                <span className="px-2.5 py-1 rounded-xl text-xs font-extrabold bg-[#D6F36A] text-[#251C38] border border-[#556B16]/30 shadow-2xs">
                  {hackathon.prizePool}
                </span>
              </div>

              <p className="text-xs text-[#645B74] leading-relaxed">
                {hackathon.description}
              </p>

              {/* Tags */}
              <div className="flex flex-wrap gap-1.5">
                {hackathon.tags.map((tag, tIdx) => (
                  <span
                    key={tIdx}
                    className="px-2 py-0.5 rounded-md text-[10px] font-bold bg-white/70 text-[#251C38] border border-[#B8A4F4]/30"
                  >
                    {tag}
                  </span>
                ))}
              </div>

              {/* Meta stats */}
              <div className="grid grid-cols-2 gap-2 text-xs text-[#645B74] pt-2">
                <div className="p-2.5 rounded-xl bg-white/70 border border-white flex items-center gap-2">
                  <Calendar className="w-4 h-4 text-[#6C42C4]" />
                  <span>Deadline: {hackathon.deadline}</span>
                </div>
                <div className="p-2.5 rounded-xl bg-white/70 border border-white flex items-center gap-2">
                  <Users className="w-4 h-4 text-[#556B16]" />
                  <span>{hackathon.participants} Builders Registered</span>
                </div>
              </div>
            </div>

            {/* Registration CTA */}
            <div className="pt-4 border-t border-[#B8A4F4]/20 flex items-center justify-between">
              <span className="text-xs font-medium text-[#746589]">
                {hackathon.registered ? (
                  <span className="text-[#556B16] font-bold flex items-center gap-1">
                    <CheckCircle2 className="w-3.5 h-3.5" /> Registered
                  </span>
                ) : (
                  'Open for Team Registration'
                )}
              </span>

              <button
                onClick={() => toggleRegister(hackathon.id)}
                className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                  hackathon.registered
                    ? 'bg-white text-[#C34630] border border-[#FF6347]/40 hover:bg-[#FF6347]/10'
                    : 'btn-chartreuse'
                }`}
              >
                {hackathon.registered ? 'Withdraw Registration' : 'Register Team Now'}
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
