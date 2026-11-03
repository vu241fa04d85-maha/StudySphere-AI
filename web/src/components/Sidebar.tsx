import React from 'react';
import {
  LayoutDashboard,
  Bot,
  FileText,
  Code2,
  HelpCircle,
  FileCheck2,
  Trophy,
  CalendarCheck,
  ShieldCheck,
  Settings,
  ChevronLeft,
  ChevronRight,
  BookOpen
} from 'lucide-react';
import type { UserSession } from '../lib/store';

interface SidebarProps {
  activeTab: string;
  setActiveTab: (tab: string) => void;
  collapsed: boolean;
  setCollapsed: (collapsed: boolean) => void;
  user: UserSession | null;
  onOpenPrivacy: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({
  activeTab,
  setActiveTab,
  collapsed,
  setCollapsed,
  user,
  onOpenPrivacy,
}) => {
  const navItems = [
    { id: 'home', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'chat', label: 'AI Study Tutor', icon: Bot, highlight: true },
    { id: 'files', label: 'Document Studio', icon: FileText },
    { id: 'codelab', label: '14-Lang Code Lab', icon: Code2, badge: '14' },
    { id: 'quizzes', label: 'Adaptive Quizzes', icon: HelpCircle },
    { id: 'tests', label: 'Timed Exam Portal', icon: FileCheck2 },
    { id: 'hackathons', label: 'Hackathons', icon: Trophy, badge: '$40k' },
    { id: 'planner', label: 'Study Planner', icon: CalendarCheck },
    ...(user?.role === 'admin'
      ? [{ id: 'admin', label: 'Admin Center', icon: ShieldCheck, adminOnly: true }]
      : [{ id: 'admin', label: 'Admin Portal', icon: ShieldCheck, badge: '50' }]),
  ];

  return (
    <aside
      className={`fixed lg:sticky top-[61px] left-0 h-[calc(100vh-61px)] z-30 transition-all duration-300 glass-sidebar flex flex-col justify-between py-4 ${
        collapsed ? 'w-20 px-2' : 'w-64 px-4'
      }`}
    >
      {/* Top Nav Items */}
      <div className="space-y-1.5 overflow-y-auto pr-1">
        <div className="flex items-center justify-between mb-3 px-2">
          {!collapsed && (
            <span className="text-[11px] font-bold uppercase tracking-wider text-[#746589]">
              Main Navigation
            </span>
          )}
          <button
            onClick={() => setCollapsed(!collapsed)}
            className="p-1.5 rounded-lg text-[#645B74] hover:bg-white/80 hover:text-[#6C42C4] transition-colors ml-auto"
            title={collapsed ? 'Expand Sidebar' : 'Collapse Sidebar'}
          >
            {collapsed ? <ChevronRight className="w-4 h-4" /> : <ChevronLeft className="w-4 h-4" />}
          </button>
        </div>

        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;

          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl font-medium text-sm transition-all duration-200 group relative ${
                isActive
                  ? 'bg-gradient-to-r from-[#6C42C4] to-[#B8A4F4] text-white shadow-md font-semibold'
                  : 'text-[#251C38] hover:bg-white/90 hover:text-[#6C42C4]'
              }`}
              title={collapsed ? item.label : undefined}
            >
              <Icon
                className={`w-5 h-5 flex-shrink-0 transition-transform group-hover:scale-110 ${
                  isActive ? 'text-white' : item.adminOnly ? 'text-[#6C42C4]' : 'text-[#645B74]'
                }`}
              />
              {!collapsed && (
                <div className="flex-1 flex items-center justify-between text-left truncate">
                  <span className="truncate">{item.label}</span>
                  {item.badge && (
                    <span
                      className={`text-[10px] px-1.5 py-0.5 rounded-md font-bold uppercase ${
                        isActive
                          ? 'bg-white/20 text-white'
                          : item.adminOnly
                          ? 'bg-[#B8A4F4]/30 text-[#6C42C4]'
                          : 'bg-[#D6F36A]/50 text-[#556B16]'
                      }`}
                    >
                      {item.badge}
                    </span>
                  )}
                </div>
              )}
            </button>
          );
        })}
      </div>

      {/* Footer / Secondary Actions */}
      <div className="pt-3 border-t border-[#B8A4F4]/30 space-y-1">
        <button
          onClick={() => setActiveTab('settings')}
          className={`w-full flex items-center gap-3 px-3 py-2 rounded-xl text-sm transition-colors ${
            activeTab === 'settings'
              ? 'bg-[#B8A4F4]/30 text-[#6C42C4] font-semibold'
              : 'text-[#645B74] hover:bg-white/80 hover:text-[#251C38]'
          }`}
          title="Settings"
        >
          <Settings className="w-5 h-5 flex-shrink-0 text-[#746589]" />
          {!collapsed && <span className="truncate">Settings & Themes</span>}
        </button>

        <button
          onClick={onOpenPrivacy}
          className="w-full flex items-center gap-3 px-3 py-2 rounded-xl text-sm text-[#746589] hover:bg-white/80 hover:text-[#251C38] transition-colors"
          title="Privacy & Terms"
        >
          <BookOpen className="w-5 h-5 flex-shrink-0" />
          {!collapsed && <span className="truncate text-xs">Privacy & Guidelines</span>}
        </button>
      </div>
    </aside>
  );
};
