import React, { useState } from 'react';
import { HelpCircle, CheckCircle2, XCircle, Award, RotateCcw, ArrowRight, Sparkles, Clock } from 'lucide-react';
import { INITIAL_QUIZZES, type QuizItem } from '../lib/store';

export const QuizPortal: React.FC = () => {
  const [quizzes] = useState<QuizItem[]>(INITIAL_QUIZZES);
  const [activeQuiz, setActiveQuiz] = useState<QuizItem | null>(null);
  const [currentQIndex, setCurrentQIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState<Record<number, number>>({});
  const [isSubmitted, setIsSubmitted] = useState(false);

  const startQuiz = (quiz: QuizItem) => {
    setActiveQuiz(quiz);
    setCurrentQIndex(0);
    setSelectedAnswers({});
    setIsSubmitted(false);
  };

  const handleSelectOption = (optionIndex: number) => {
    if (isSubmitted) return;
    setSelectedAnswers((prev) => ({ ...prev, [currentQIndex]: optionIndex }));
  };

  const calculateScore = () => {
    if (!activeQuiz) return { score: 0, total: 0, percentage: 0 };
    let correct = 0;
    activeQuiz.questions.forEach((q, idx) => {
      if (selectedAnswers[idx] === q.correctIndex) {
        correct++;
      }
    });
    const total = activeQuiz.questions.length;
    return { score: correct, total, percentage: Math.round((correct / total) * 100) };
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
            Adaptive Quiz Generator
            <span className="px-2 py-0.5 rounded-full text-xs font-bold bg-[#B8A4F4]/30 text-[#6C42C4] border border-[#B8A4F4]/50">
              Active Recall
            </span>
          </h1>
          <p className="text-xs text-[#645B74] mt-1">
            Test and calibrate your knowledge with dynamic multiple-choice assessments.
          </p>
        </div>
      </div>

      {!activeQuiz ? (
        /* Quiz Catalog */
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {quizzes.map((quiz) => (
            <div key={quiz.id} className="glass p-6 border border-white/95 glass-card-hover space-y-4">
              <div className="flex items-start justify-between gap-3">
                <div className="flex items-center gap-3">
                  <div className="p-3 rounded-2xl bg-[#B8A4F4]/30 text-[#6C42C4]">
                    <HelpCircle className="w-6 h-6" />
                  </div>
                  <div>
                    <h3 className="font-heading font-bold text-base text-[#251C38]">{quiz.title}</h3>
                    <p className="text-xs text-[#746589]">{quiz.subject}</p>
                  </div>
                </div>
                <span
                  className={`text-[10px] px-2 py-0.5 rounded-md font-bold uppercase ${
                    quiz.difficulty === 'hard'
                      ? 'bg-[#FF6347]/20 text-[#C34630]'
                      : quiz.difficulty === 'medium'
                      ? 'bg-[#D6F36A]/40 text-[#556B16]'
                      : 'bg-[#B8A4F4]/30 text-[#6C42C4]'
                  }`}
                >
                  {quiz.difficulty}
                </span>
              </div>

              <div className="flex items-center justify-between pt-2 border-t border-[#B8A4F4]/20 text-xs text-[#746589]">
                <span>{quiz.questions.length} Questions</span>
                <button
                  onClick={() => startQuiz(quiz)}
                  className="px-4 py-2 btn-chartreuse text-xs font-bold flex items-center gap-1.5"
                >
                  <span>Start Quiz</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        /* Active Quiz Session */
        <div className="glass p-6 sm:p-8 border border-white/95 shadow-glass space-y-6">
          {/* Quiz Top bar */}
          <div className="flex items-center justify-between pb-4 border-b border-[#B8A4F4]/20">
            <div>
              <h2 className="font-heading font-bold text-lg text-[#251C38]">{activeQuiz.title}</h2>
              <p className="text-xs text-[#746589]">
                Question {currentQIndex + 1} of {activeQuiz.questions.length}
              </p>
            </div>
            <button
              onClick={() => setActiveQuiz(null)}
              className="text-xs text-[#746589] hover:text-[#251C38] underline"
            >
              Exit Quiz
            </button>
          </div>

          {!isSubmitted ? (
            /* Question Box */
            <div className="space-y-6">
              <h3 className="font-heading font-bold text-base text-[#251C38] leading-relaxed">
                {activeQuiz.questions[currentQIndex].text}
              </h3>

              {/* Options */}
              <div className="space-y-2.5">
                {activeQuiz.questions[currentQIndex].options.map((opt, oIdx) => {
                  const isSelected = selectedAnswers[currentQIndex] === oIdx;
                  return (
                    <div
                      key={oIdx}
                      onClick={() => handleSelectOption(oIdx)}
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

              {/* Navigation buttons */}
              <div className="flex items-center justify-between pt-4 border-t border-[#B8A4F4]/20">
                <button
                  disabled={currentQIndex === 0}
                  onClick={() => setCurrentQIndex((prev) => prev - 1)}
                  className="px-4 py-2 rounded-xl text-xs font-bold bg-white text-[#251C38] border border-[#B8A4F4]/40 disabled:opacity-40"
                >
                  Previous
                </button>

                {currentQIndex < activeQuiz.questions.length - 1 ? (
                  <button
                    onClick={() => setCurrentQIndex((prev) => prev + 1)}
                    className="px-4 py-2 btn-lavender text-xs font-bold flex items-center gap-1.5"
                  >
                    <span>Next Question</span>
                    <ArrowRight className="w-3.5 h-3.5" />
                  </button>
                ) : (
                  <button
                    onClick={() => setIsSubmitted(true)}
                    className="px-5 py-2 btn-chartreuse text-xs font-bold"
                  >
                    Submit & Grade
                  </button>
                )}
              </div>
            </div>
          ) : (
            /* Results & Rationales */
            <div className="space-y-6">
              {(() => {
                const { score, total, percentage } = calculateScore();
                return (
                  <div className="p-6 rounded-2xl bg-[#D6F36A]/20 border border-[#D6F36A]/50 text-center space-y-2">
                    <div className="w-12 h-12 mx-auto rounded-full bg-[#D6F36A] text-[#251C38] flex items-center justify-center">
                      <Award className="w-6 h-6" />
                    </div>
                    <h3 className="font-heading font-extrabold text-2xl text-[#251C38]">
                      {percentage}% Score ({score}/{total})
                    </h3>
                    <p className="text-xs text-[#556B16]">
                      {percentage >= 80 ? 'Mastery demonstrated!' : 'Good attempt. Review rationales below.'}
                    </p>
                  </div>
                );
              })()}

              {/* Breakdown of questions */}
              <div className="space-y-4">
                {activeQuiz.questions.map((q, idx) => {
                  const userChoice = selectedAnswers[idx];
                  const isCorrect = userChoice === q.correctIndex;
                  return (
                    <div key={q.id} className="p-4 rounded-xl bg-white/70 border border-white space-y-2">
                      <div className="flex items-start gap-2">
                        {isCorrect ? (
                          <CheckCircle2 className="w-5 h-5 text-[#556B16] flex-shrink-0 mt-0.5" />
                        ) : (
                          <XCircle className="w-5 h-5 text-[#FF6347] flex-shrink-0 mt-0.5" />
                        )}
                        <div>
                          <div className="text-sm font-bold text-[#251C38]">{q.text}</div>
                          <div className="text-xs text-[#645B74] mt-1">
                            Your answer:{' '}
                            <span className={isCorrect ? 'text-[#556B16] font-bold' : 'text-[#FF6347] font-bold'}>
                              {userChoice !== undefined ? q.options[userChoice] : 'Not answered'}
                            </span>
                          </div>
                          {!isCorrect && (
                            <div className="text-xs text-[#556B16] font-semibold mt-0.5">
                              Correct answer: {q.options[q.correctIndex]}
                            </div>
                          )}
                          <div className="text-xs text-[#746589] mt-2 p-2 rounded-lg bg-white/80 border border-[#B8A4F4]/30">
                            <strong>Rationale:</strong> {q.explanation}
                          </div>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>

              <div className="flex justify-center pt-4">
                <button
                  onClick={() => startQuiz(activeQuiz)}
                  className="px-5 py-2.5 btn-lavender text-xs font-bold flex items-center gap-2"
                >
                  <RotateCcw className="w-4 h-4" />
                  Retake Quiz
                </button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
