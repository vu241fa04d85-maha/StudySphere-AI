import React from 'react';
import { Sparkles, Bell, Shield, User, Flame, Search, Menu, LogOut } from 'lucide-react';
import type { UserSession } from '../lib/store';

interface NavbarProps {
  user: UserSession | null;
  activeTab: string;
  setActiveTab: (tab: string) => void;
  unreadCount: number;
  onOpenAuth: () => void;
  onLogout: () => void;
  onToggleSidebar: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({
  user,
  activeTab,
  setActiveTab,
  unreadCount,
  onOpenAuth,
  onLogout,
  onToggleSidebar,
}) => {
  return (
    <header className="sticky top-0 z-40 w-full glass-nav px-4 lg:px-8 py-3 transition-all duration-200">
      <div className="max-w-7xl mx-auto flex items-center justify-between gap-4">
        {/* Brand & Mobile Hamburger */}
        <div className="flex items-center gap-3">
          <button
            onClick={onToggleSidebar}
            className="lg:hidden p-2 rounded-xl text-[#251C38] hover:bg-[#B8A4F4]/20 transition-colors"
            aria-label="Toggle Navigation Sidebar"
          >
            <Menu className="w-5 h-5 text-[#6C42C4]" />
          </button>

          <div
            onClick={() => setActiveTab('home')}
            className="flex items-center gap-2.5 cursor-pointer group"
          >
            <div className="w-10 h-10 rounded-2xl bg-gradient-to-tr from-[#6C42C4] via-[#B8A4F4] to-[#D6F36A] p-[2px] shadow-sm group-hover:scale-105 transition-transform">
              <div className="w-full h-full bg-white rounded-[14px] flex items-center justify-center font-extrabold text-[#6C42C4] tracking-tight">
                AI<span className="text-[#556B16]">-SA</span>
              </div>
            </div>
            <div>
              <div className="font-heading font-extrabold text-lg text-[#251C38] leading-tight flex items-center gap-1.5">
                AI Study Assistant
                <span className="hidden sm:inline-flex items-center gap-0.5 px-2 py-0.5 text-[10px] font-bold rounded-full bg-[#B8A4F4]/20 text-[#6C42C4] border border-[#B8A4F4]/40">
                  <Sparkles className="w-2.5 h-2.5" /> Light Glass
                </span>
              </div>
              <p className="text-[11px] font-medium text-[#645B74] hidden md:block">
                Smart Academics, 14-Lang Code Lab & Test Portal
              </p>
            </div>
          </div>
        </div>

        {/* Global Search Bar (Desktop) */}
        <div className="hidden md:flex flex-1 max-w-md mx-4">
          <div className="relative w-full">
            <Search className="w-4 h-4 absolute left-3.5 top-1/2 -translate-y-1/2 text-[#746589]" />
            <input
              type="text"
              placeholder="Search lectures, quizzes, code templates, tasks..."
              className="w-full pl-10 pr-4 py-2 text-sm rounded-xl glass-input placeholder:text-[#746589]"
            />
          </div>
        </div>

        {/* Right Section: Streak, Alerts, Role Pill & Profile */}
        <div className="flex items-center gap-2.5 sm:gap-3">
          {/* Study Streak Pill */}
          <div className="flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-[#D6F36A]/30 border border-[#D6F36A]/60 shadow-xs">
            <Flame className="w-4 h-4 text-[#556B16]" />
            <span className="text-xs font-bold text-[#556B16]">
              {user ? `${user.studyStreak}d streak` : '5d streak'}
            </span>
          </div>

          {/* Notifications with Tomato Red Badge */}
          <button
            onClick={() => setActiveTab('notifications')}
            className={`relative p-2 rounded-xl transition-all ${
              activeTab === 'notifications'
                ? 'bg-[#B8A4F4]/30 text-[#6C42C4]'
                : 'text-[#251C38] hover:bg-white/80'
            }`}
            title="Notifications"
          >
            <Bell className="w-5 h-5" />
            {unreadCount > 0 && (
              <span className="absolute top-1.5 right-1.5 w-2.5 h-2.5 bg-[#FF6347] rounded-full ring-2 ring-white animate-pulse" />
            )}
          </button>

          {/* User Profile or Login CTA */}
          {user ? (
            <div className="flex items-center gap-2 pl-1 border-l border-[#B8A4F4]/30">
              <button
                onClick={() => setActiveTab('profile')}
                className="flex items-center gap-2 p-1.5 sm:px-2.5 rounded-xl hover:bg-white/80 transition-colors"
              >
                <div className="w-8 h-8 rounded-full bg-[#B8A4F4]/40 flex items-center justify-center text-[#6C42C4] font-bold text-xs border border-[#B8A4F4]/60">
                  {user.role === 'admin' ? <Shield className="w-4 h-4" /> : <User className="w-4 h-4" />}
                </div>
                <div className="hidden lg:block text-left">
                  <div className="text-xs font-bold text-[#251C38] leading-tight flex items-center gap-1">
                    {user.name.split(' ')[0]}
                    {user.role === 'admin' && (
                      <span className="px-1.5 py-0.2 text-[9px] font-extrabold uppercase rounded-sm bg-[#6C42C4] text-white">
                        ADMIN
                      </span>
                    )}
                  </div>
                  <div className="text-[10px] text-[#746589]">
                    {user.role === 'admin' ? user.adminCode : user.course || 'CS Student'}
                  </div>
                </div>
              </button>

              <button
                onClick={onLogout}
                className="p-1.5 rounded-lg text-[#746589] hover:text-[#C34630] hover:bg-[#FF6347]/10 transition-colors"
                title="Sign out"
              >
                <LogOut className="w-4 h-4" />
              </button>
            </div>
          ) : (
            <button
              onClick={onOpenAuth}
              className="px-4 py-2 btn-lavender text-xs flex items-center gap-1.5"
            >
              <User className="w-3.5 h-3.5" />
              <span>Sign In</span>
            </button>
          )}
        </div>
      </div>
    </header>
  );
};
