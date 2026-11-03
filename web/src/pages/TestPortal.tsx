import React, { useState, useEffect } from 'react';
import { FileCheck2, Clock, Flag, CheckCircle, AlertTriangle, ShieldCheck, ArrowRight } from 'lucide-react';
import { INITIAL_TESTS, type TimedTest } from '../lib/store';

export const TestPortal: React.FC = () => {
  const [tests] = useState<TimedTest[]>(INITIAL_TESTS);
  const [activeTest, setActiveTest] = useState<TimedTest | null>(null);
  const [currentIdx, setCurrentIdx] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState<Record<number, number>>({});
  const [flagged, setFlagged] = useState<Record<number, boolean>>({});
  const [secondsRemaining, setSecondsRemaining] = useState(45 * 60);
  const [submitted, setSubmitted] = useState(false);
  const [showSubmitConfirm, setShowSubmitConfirm] = useState(false);

  useEffect(() => {
    if (!activeTest || submitted) return;
    const interval = setInterval(() => {
      setSecondsRemaining((prev) => {
        if (prev <= 1) {
          clearInterval(interval);
          setSubmitted(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(interval);
  }, [activeTest, submitted]);

  const startTest = (test: TimedTest) => {
    setActiveTest(test);
    setCurrentIdx(0);
    setSelectedAnswers({});
    setFlagged({});
    setSecondsRemaining(test.durationMinutes * 60);
    setSubmitted(false);
    setShowSubmitConfirm(false);
  };

  const formatTimer = (secs: number) => {
    const mins = Math.floor(secs / 60);
    const s = secs % 60;
    return `${mins.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  const calculateResults = () => {
    if (!activeTest) return { earned: 0, total: 0, percentage: 0 };
    let earned = 0;
    let total = 0;
    activeTest.questions.forEach((q, idx) => {
      total += q.marks;
      if (selectedAnswers[idx] === q.correctIndex) {
        earned += q.marks;
      }
    });
    return { earned, total, percentage: Math.round((earned / total) * 100) };
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Portal Header */}
      <div>
        <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
          Timed Official Examination Portal
          <span className="px-2 py-0.5 rounded-full text-xs font-bold bg-[#D6F36A]/40 text-[#556B16] border border-[#D6F36A]/60">
            Proctored Environment
          </span>
        </h1>
        <p className="text-xs text-[#645B74] mt-1">
          High-stakes mock assessments designed to mirror university midterms and accreditation exams.
        </p>
      </div>

      {!activeTest ? (
        /* Test List */
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {tests.map((test) => (
            <div key={test.id} className="glass p-6 border border-white/95 glass-card-hover space-y-4">
              <div className="flex items-start justify-between gap-3">
                <div className="flex items-center gap-3">
                  <div className="p-3 rounded-2xl bg-[#D6F36A]/40 text-[#556B16]">
                    <FileCheck2 className="w-6 h-6" />
                  </div>
                  <div>
                    <h3 className="font-heading font-bold text-base text-[#251C38]">{test.title}</h3>
                    <p className="text-xs text-[#746589]">{test.subject}</p>
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-2 text-xs text-[#645B74]">
                <div className="p-2.5 rounded-xl bg-white/70 border border-white flex items-center gap-2">
                  <Clock className="w-4 h-4 text-[#6C42C4]" />
                  <span>{test.durationMinutes} Minutes</span>
                </div>
                <div className="p-2.5 rounded-xl bg-white/70 border border-white flex items-center gap-2">
                  <ShieldCheck className="w-4 h-4 text-[#556B16]" />
                  <span>{test.totalMarks} Total Marks</span>
                </div>
              </div>

              <div className="flex items-center justify-between pt-2 border-t border-[#B8A4F4]/20">
                <span className="text-xs text-[#746589]">{test.questions.length} Exam Questions</span>
                <button
                  onClick={() => startTest(test)}
                  className="px-4 py-2 btn-lavender text-xs font-bold flex items-center gap-1.5"
                >
                  <span>Launch Exam</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        /* Active Examination Screen */
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Main Question Area (3 Cols) */}
          <div className="lg:col-span-3 glass p-6 sm:p-8 border border-white/95 shadow-glass space-y-6">
            {/* Exam Header */}
            <div className="flex items-center justify-between pb-4 border-b border-[#B8A4F4]/20">
              <div>
                <h2 className="font-heading font-bold text-lg text-[#251C38]">{activeTest.title}</h2>
                <div className="text-xs text-[#746589] mt-0.5">
                  Question {currentIdx + 1} of {activeTest.questions.length} •{' '}
                  <span className="font-bold text-[#6C42C4]">
                    {activeTest.questions[currentIdx].marks} Marks
                  </span>
                </div>
              </div>

              {/* Countdown Timer with Tomato warning */}
              <div
                className={`px-3.5 py-1.5 rounded-xl font-mono font-bold text-sm flex items-center gap-1.5 border ${
                  secondsRemaining < 300
                    ? 'bg-[#FF6347]/20 text-[#C34630] border-[#FF6347] animate-pulse'
                    : 'bg-[#D6F36A]/30 text-[#556B16] border-[#D6F36A]/60'
                }`}
              >
                <Clock className="w-4 h-4" />
                <span>{formatTimer(secondsRemaining)}</span>
              </div>
            </div>

            {!submitted ? (
              <div className="space-y-6">
                <div className="flex items-start justify-between gap-4">
                  <h3 className="font-heading font-bold text-base text-[#251C38] leading-relaxed flex-1">
                    {activeTest.questions[currentIdx].text}
                  </h3>
                  <button
                    onClick={() =>
                      setFlagged((prev) => ({ ...prev, [currentIdx]: !prev[currentIdx] }))
                    }
                    className={`p-2 rounded-xl text-xs font-bold flex items-center gap-1 transition-colors ${
                      flagged[currentIdx]
                        ? 'bg-[#FF6347]/20 text-[#C34630] border border-[#FF6347]/40'
                        : 'bg-white/80 text-[#746589] hover:bg-white'
                    }`}
                  >
                    <Flag className="w-3.5 h-3.5" />
                    <span>{flagged[currentIdx] ? 'Flagged' : 'Flag'}</span>
                  </button>
                </div>

                {/* Options List */}
                <div className="space-y-3">
                  {activeTest.questions[currentIdx].options.map((opt, oIdx) => {
                    const isSelected = selectedAnswers[currentIdx] === oIdx;
                    return (
                      <div
                        key={oIdx}
                        onClick={() => setSelectedAnswers((prev) => ({ ...prev, [currentIdx]: oIdx }))}
                        className={`p-4 rounded-xl border transition-all cursor-pointer flex items-center gap-3 ${
                          isSelected
                            ? 'bg-[#6C42C4] text-white border-[#6C42C4] shadow-xs'
                            : 'bg-white/80 hover:bg-white text-[#251C38] border-white/90'
                        }`}
                      >
                        <div
                          className={`w-6 h-6 rounded-full flex items-center justify-center font-bold text-xs ${
                            isSelected ? 'bg-white text-[#6C42C4]' : 'bg-[#B8A4F4]/30 text-[#6C42C4]'
                          }`}
                        >
                          {String.fromCharCode(65 + oIdx)}
                        </div>
                        <span className="text-sm font-medium flex-1">{opt}</span>
                      </div>
                    );
                  })}
                </div>

                {/* Bottom Navigation */}
                <div className="flex items-center justify-between pt-4 border-t border-[#B8A4F4]/20">
                  <button
                    disabled={currentIdx === 0}
                    onClick={() => setCurrentIdx((prev) => prev - 1)}
                    className="px-4 py-2 rounded-xl text-xs font-bold bg-white text-[#251C38] border border-[#B8A4F4]/40 disabled:opacity-40"
                  >
                    Previous
                  </button>

                  <div className="flex items-center gap-3">
                    {currentIdx < activeTest.questions.length - 1 ? (
                      <button
                        onClick={() => setCurrentIdx((prev) => prev + 1)}
                        className="px-4 py-2 btn-lavender text-xs font-bold flex items-center gap-1.5"
                      >
                        <span>Next Question</span>
                        <ArrowRight className="w-3.5 h-3.5" />
                      </button>
                    ) : (
                      <button
                        onClick={() => setShowSubmitConfirm(true)}
                        className="px-5 py-2 btn-chartreuse text-xs font-bold"
                      >
                        Finish & Submit
                      </button>
                    )}
                  </div>
                </div>
              </div>
            ) : (
              /* Examination Results */
              <div className="space-y-6 text-center py-6">
                {(() => {
                  const { earned, total, percentage } = calculateResults();
                  return (
                    <div className="space-y-4">
                      <div className="w-16 h-16 mx-auto rounded-3xl bg-[#D6F36A] text-[#251C38] flex items-center justify-center shadow-sm">
                        <CheckCircle className="w-8 h-8" />
                      </div>
                      <h3 className="font-heading font-extrabold text-3xl text-[#251C38]">
                        Exam Finalized: {earned} / {total} Marks ({percentage}%)
                      </h3>
                      <p className="text-sm text-[#645B74] max-w-md mx-auto">
                        Official answers recorded in local database. Performance metrics have been factored into
                        your campus leaderboard standing.
                      </p>
                      <button
                        onClick={() => setActiveTest(null)}
                        className="px-6 py-2.5 btn-lavender text-xs font-bold mt-4"
                      >
                        Return to Exam Catalog
                      </button>
                    </div>
                  );
                })()}
              </div>
            )}
          </div>

          {/* Question Palette Sidebar (1 Col) */}
          <div className="glass p-5 border border-white/95 shadow-glass space-y-4 h-fit">
            <h3 className="font-heading font-bold text-sm text-[#251C38]">Question Palette</h3>

            <div className="grid grid-cols-4 gap-2">
              {activeTest.questions.map((_, idx) => {
                const isCurrent = currentIdx === idx;
                const isAnswered = selectedAnswers[idx] !== undefined;
                const isFlagged = flagged[idx];

                return (
                  <button
                    key={idx}
                    onClick={() => setCurrentIdx(idx)}
                    className={`h-9 rounded-xl font-bold text-xs transition-all flex items-center justify-center relative ${
                      isCurrent
                        ? 'ring-2 ring-[#6C42C4] bg-white text-[#6C42C4]'
                        : isAnswered
                        ? 'bg-[#D6F36A] text-[#251C38]'
                        : 'bg-white/70 text-[#746589] hover:bg-white'
                    }`}
                  >
                    {idx + 1}
                    {isFlagged && (
                      <span className="w-2 h-2 rounded-full bg-[#FF6347] absolute top-1 right-1" />
                    )}
                  </button>
                );
              })}
            </div>

            <div className="space-y-1.5 pt-3 border-t border-[#B8A4F4]/20 text-[11px] text-[#645B74]">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 rounded-md bg-[#D6F36A]" />
                <span>Answered</span>
              </div>
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 rounded-md bg-white border border-[#B8A4F4]" />
                <span>Unanswered</span>
              </div>
              <div className="flex items-center gap-2">
                <span className="w-2.5 h-2.5 rounded-full bg-[#FF6347]" />
                <span>Flagged for Review</span>
              </div>
            </div>

            {!submitted && (
              <button
                onClick={() => setShowSubmitConfirm(true)}
                className="w-full mt-4 py-2 btn-chartreuse text-xs font-bold"
              >
                Submit Exam Now
              </button>
            )}
          </div>
        </div>
      )}

      {/* Submit Confirmation Modal */}
      {showSubmitConfirm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[#251C38]/40 backdrop-blur-sm animate-fade-in">
          <div className="glass max-w-sm w-full p-6 shadow-glassFloating border border-white text-center space-y-4">
            <AlertTriangle className="w-10 h-10 text-[#FF6347] mx-auto" />
            <h3 className="font-heading font-bold text-base text-[#251C38]">
              Ready to submit examination?
            </h3>
            <p className="text-xs text-[#645B74]">
              You have answered {Object.keys(selectedAnswers).length} of {activeTest?.questions.length} questions.
              Once submitted, marks are calculated immediately.
            </p>
            <div className="flex items-center gap-2 justify-center pt-2">
              <button
                onClick={() => setShowSubmitConfirm(false)}
                className="px-4 py-2 rounded-xl text-xs font-bold bg-white text-[#251C38] border border-[#B8A4F4]/40"
              >
                Keep Reviewing
              </button>
              <button
                onClick={() => {
                  setShowSubmitConfirm(false);
                  setSubmitted(true);
                }}
                className="px-4 py-2 btn-tomato text-xs font-bold"
              >
                Confirm & Submit
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
