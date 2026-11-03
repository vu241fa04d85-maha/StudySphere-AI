import React from 'react';
import {
  Flame,
  Clock,
  Award,
  Sparkles,
  Bot,
  Code2,
  HelpCircle,
  FileUp,
  ArrowRight,
  Calendar,
  CheckCircle2,
  AlertCircle,
  Trophy
} from 'lucide-react';
import type { UserSession, StudyTask, Announcement, Hackathon } from '../lib/store';

interface StudentDashboardProps {
  user: UserSession | null;
  tasks: StudyTask[];
  announcements: Announcement[];
  hackathons: Hackathon[];
  setActiveTab: (tab: string) => void;
  onOpenAuth: () => void;
}

export const StudentDashboard: React.FC<StudentDashboardProps> = ({
  user,
  tasks,
  announcements,
  hackathons,
  setActiveTab,
  onOpenAuth,
}) => {
  return (
    <div className="space-y-6 animate-fade-in">
      {/* 1. Hero Greeting Banner */}
      <div className="glass p-6 sm:p-8 relative overflow-hidden border border-white/95">
        <div className="absolute top-0 right-0 w-80 h-80 bg-gradient-to-br from-[#B8A4F4]/30 via-[#D6F36A]/20 to-transparent rounded-full blur-3xl pointer-events-none -mr-20 -mt-20" />

        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div>
            <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-[#B8A4F4]/30 text-[#6C42C4] text-xs font-bold mb-3 border border-[#B8A4F4]/50">
              <Sparkles className="w-3.5 h-3.5" /> Light Glassmorphism Architecture
            </div>
            <h1 className="font-heading font-extrabold text-2xl sm:text-3xl text-[#251C38] tracking-tight">
              Welcome back, {user ? user.name : 'Curious Learner'}!
            </h1>
            <p className="text-sm text-[#645B74] mt-1.5 max-w-xl">
              You are on a <span className="font-bold text-[#556B16]">12-day streak</span>. Your next scheduled
              milestone is <span className="font-semibold text-[#6C42C4]">Algorithms Midterm</span> in 3 days.
            </p>
          </div>

          <div className="flex flex-wrap items-center gap-3">
            <button
              onClick={() => setActiveTab('chat')}
              className="px-4 py-2.5 btn-lavender text-xs sm:text-sm flex items-center gap-2"
            >
              <Bot className="w-4 h-4" />
              Ask AI Tutor
            </button>
            <button
              onClick={() => setActiveTab('codelab')}
              className="px-4 py-2.5 btn-chartreuse text-xs sm:text-sm flex items-center gap-2"
            >
              <Code2 className="w-4 h-4" />
              14-Lang Code Lab
            </button>
          </div>
        </div>
      </div>

      {/* 2. Glass Metric Stat Cards */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Streak */}
        <div className="glass p-5 glass-card-hover border border-white/90">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-bold text-[#746589]">Study Streak</span>
            <div className="p-2 rounded-xl bg-[#D6F36A]/40 text-[#556B16]">
              <Flame className="w-4 h-4" />
            </div>
          </div>
          <div className="font-heading font-extrabold text-2xl text-[#251C38]">
            {user ? `${user.studyStreak} Days` : '12 Days'}
          </div>
          <p className="text-[11px] text-[#556B16] font-medium mt-1">Top 5% of active students</p>
        </div>

        {/* Study Time */}
        <div className="glass p-5 glass-card-hover border border-white/90">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-bold text-[#746589]">Study Hours</span>
            <div className="p-2 rounded-xl bg-[#B8A4F4]/30 text-[#6C42C4]">
              <Clock className="w-4 h-4" />
            </div>
          </div>
          <div className="font-heading font-extrabold text-2xl text-[#251C38]">64.5 hrs</div>
          <p className="text-[11px] text-[#6C42C4] font-medium mt-1">+8.2 hrs this week</p>
        </div>

        {/* Quiz Accuracy */}
        <div className="glass p-5 glass-card-hover border border-white/90">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-bold text-[#746589]">Quiz Accuracy</span>
            <div className="p-2 rounded-xl bg-[#F4FAD9] text-[#556B16] border border-[#D6F36A]/50">
              <Award className="w-4 h-4" />
            </div>
          </div>
          <div className="font-heading font-extrabold text-2xl text-[#251C38]">94.2%</div>
          <p className="text-[11px] text-[#556B16] font-medium mt-1">28 quizzes evaluated</p>
        </div>

        {/* Hackathon Readiness */}
        <div className="glass p-5 glass-card-hover border border-white/90">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-bold text-[#746589]">Hackathon Rank</span>
            <div className="p-2 rounded-xl bg-[#B8A4F4]/30 text-[#6C42C4]">
              <Trophy className="w-4 h-4" />
            </div>
          </div>
          <div className="font-heading font-extrabold text-2xl text-[#251C38]">#14 Tier A</div>
          <p className="text-[11px] text-[#6C42C4] font-medium mt-1">$15,000 challenge registered</p>
        </div>
      </div>

      {/* 3. Main Split Section: Study Hub & Upcoming Schedule */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left 2 Cols: Action Grid & Recent Tasks */}
        <div className="lg:col-span-2 space-y-6">
          {/* Quick Feature Launchpad */}
          <div className="glass p-6 border border-white/90">
            <h2 className="font-heading font-bold text-base text-[#251C38] mb-4 flex items-center justify-between">
              <span>Interactive Learning Modules</span>
              <span className="text-xs text-[#746589] font-normal">All offline capable</span>
            </h2>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3.5">
              <div
                onClick={() => setActiveTab('chat')}
                className="p-4 rounded-2xl bg-white/70 hover:bg-white border border-[#B8A4F4]/30 hover:border-[#B8A4F4] transition-all cursor-pointer group shadow-xs"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2.5 rounded-xl bg-[#B8A4F4]/30 text-[#6C42C4] group-hover:scale-110 transition-transform">
                    <Bot className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-heading font-bold text-sm text-[#251C38] group-hover:text-[#6C42C4]">
                      AI Concept Breakdown
                    </h3>
                    <p className="text-xs text-[#645B74]">Formulas, algorithms & flashcard generator</p>
                  </div>
                </div>
              </div>

              <div
                onClick={() => setActiveTab('codelab')}
                className="p-4 rounded-2xl bg-white/70 hover:bg-white border border-[#D6F36A]/50 hover:border-[#D6F36A] transition-all cursor-pointer group shadow-xs"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2.5 rounded-xl bg-[#D6F36A]/40 text-[#556B16] group-hover:scale-110 transition-transform">
                    <Code2 className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-heading font-bold text-sm text-[#251C38] group-hover:text-[#556B16]">
                      14-Language Code Lab
                    </h3>
                    <p className="text-xs text-[#645B74]">Python, C++, Java, Rust, Kotlin & more</p>
                  </div>
                </div>
              </div>

              <div
                onClick={() => setActiveTab('quizzes')}
                className="p-4 rounded-2xl bg-white/70 hover:bg-white border border-[#B8A4F4]/30 hover:border-[#B8A4F4] transition-all cursor-pointer group shadow-xs"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2.5 rounded-xl bg-[#B8A4F4]/30 text-[#6C42C4] group-hover:scale-110 transition-transform">
                    <HelpCircle className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-heading font-bold text-sm text-[#251C38] group-hover:text-[#6C42C4]">
                      Adaptive Quizzes
                    </h3>
                    <p className="text-xs text-[#645B74]">Dynamic difficulty with instant rationales</p>
                  </div>
                </div>
              </div>

              <div
                onClick={() => setActiveTab('files')}
                className="p-4 rounded-2xl bg-white/70 hover:bg-white border border-white hover:border-[#B8A4F4] transition-all cursor-pointer group shadow-xs"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2.5 rounded-xl bg-[#F4EFFF] text-[#6C42C4] group-hover:scale-110 transition-transform">
                    <FileUp className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-heading font-bold text-sm text-[#251C38] group-hover:text-[#6C42C4]">
                      Document Studio
                    </h3>
                    <p className="text-xs text-[#645B74]">Extract key definitions & PDF summaries</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Active Study Tasks & Kanban preview */}
          <div className="glass p-6 border border-white/90">
            <div className="flex items-center justify-between mb-4">
              <h2 className="font-heading font-bold text-base text-[#251C38] flex items-center gap-2">
                <Calendar className="w-4 h-4 text-[#6C42C4]" />
                Priority Study Tasks
              </h2>
              <button
                onClick={() => setActiveTab('planner')}
                className="text-xs font-bold text-[#6C42C4] hover:underline flex items-center gap-1"
              >
                Open Planner <ArrowRight className="w-3.5 h-3.5" />
              </button>
            </div>

            <div className="space-y-2.5">
              {tasks.slice(0, 3).map((task) => (
                <div
                  key={task.id}
                  className="p-3.5 rounded-xl bg-white/80 hover:bg-white border border-white/90 flex items-center justify-between gap-3 transition-colors shadow-2xs"
                >
                  <div className="flex items-center gap-3">
                    <div className="w-5 h-5 rounded-md border-2 border-[#B8A4F4] flex items-center justify-center cursor-pointer">
                      {task.completed && <CheckCircle2 className="w-4 h-4 text-[#556B16]" />}
                    </div>
                    <div>
                      <h4 className="text-xs font-bold text-[#251C38]">{task.title}</h4>
                      <p className="text-[11px] text-[#746589]">
                        {task.subject} • Due {task.dueDate}
                      </p>
                    </div>
                  </div>
                  <span
                    className={`text-[10px] px-2 py-0.5 rounded-md font-bold uppercase ${
                      task.priority === 'High'
                        ? 'bg-[#FF6347]/20 text-[#C34630] border border-[#FF6347]/30'
                        : 'bg-[#D6F36A]/40 text-[#556B16]'
                    }`}
                  >
                    {task.priority}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Right 1 Col: Urgent Announcements & Hackathon Spotlight */}
        <div className="space-y-6">
          {/* Announcements with Tomato Red tags */}
          <div className="glass p-6 border border-white/90">
            <div className="flex items-center justify-between mb-4">
              <h3 className="font-heading font-bold text-sm text-[#251C38] flex items-center gap-1.5">
                <AlertCircle className="w-4 h-4 text-[#FF6347]" />
                Campus & Exam Alerts
              </h3>
              <button
                onClick={() => setActiveTab('notifications')}
                className="text-xs font-bold text-[#6C42C4] hover:underline"
              >
                View all
              </button>
            </div>

            <div className="space-y-3">
              {announcements.slice(0, 3).map((item) => (
                <div
                  key={item.id}
                  className={`p-3.5 rounded-xl border transition-colors ${
                    item.isUrgent
                      ? 'bg-[#FF6347]/10 border-[#FF6347]/30'
                      : 'bg-white/70 border-white/80'
                  }`}
                >
                  <div className="flex items-center justify-between mb-1">
                    <span
                      className={`text-[9px] px-1.5 py-0.2 rounded font-bold uppercase ${
                        item.isUrgent
                          ? 'bg-[#FF6347] text-white'
                          : 'bg-[#B8A4F4]/30 text-[#6C42C4]'
                      }`}
                    >
                      {item.category}
                    </span>
                    <span className="text-[10px] text-[#746589]">{item.createdAt}</span>
                  </div>
                  <h4 className="text-xs font-bold text-[#251C38] leading-snug">{item.title}</h4>
                  <p className="text-[11px] text-[#645B74] mt-1 line-clamp-2">{item.content}</p>
                </div>
              ))}
            </div>
          </div>

          {/* Hackathon Card */}
          <div className="glass p-6 border border-white/90 relative overflow-hidden">
            <div className="flex items-center justify-between mb-2">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#556B16] bg-[#D6F36A]/40 px-2 py-0.5 rounded-md">
                Live Challenge
              </span>
              <span className="text-xs font-bold text-[#251C38]">{hackathons[0]?.prizePool} Pool</span>
            </div>
            <h3 className="font-heading font-bold text-base text-[#251C38] mt-2">
              {hackathons[0]?.title}
            </h3>
            <p className="text-xs text-[#645B74] mt-1.5 leading-relaxed">
              {hackathons[0]?.description}
            </p>
            <button
              onClick={() => setActiveTab('hackathons')}
              className="mt-4 w-full py-2 btn-chartreuse text-xs flex items-center justify-center gap-1.5"
            >
              <Trophy className="w-3.5 h-3.5" />
              View Hackathon Arena
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
