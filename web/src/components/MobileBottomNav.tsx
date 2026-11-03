import React from 'react';
import { LayoutDashboard, Bot, FileText, Code2, HelpCircle } from 'lucide-react';

interface MobileBottomNavProps {
  activeTab: string;
  setActiveTab: (tab: string) => void;
}

export const MobileBottomNav: React.FC<MobileBottomNavProps> = ({ activeTab, setActiveTab }) => {
  const items = [
    { id: 'home', label: 'Home', icon: LayoutDashboard },
    { id: 'chat', label: 'AI Tutor', icon: Bot },
    { id: 'files', label: 'Docs', icon: FileText },
    { id: 'codelab', label: 'Code Lab', icon: Code2 },
    { id: 'quizzes', label: 'Quizzes', icon: HelpCircle },
  ];

  return (
    <nav className="lg:hidden fixed bottom-3 left-3 right-3 z-40">
      <div className="glass px-3 py-2 flex items-center justify-around shadow-glassFloating border border-white/90">
        {items.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;
          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`flex flex-col items-center justify-center p-1.5 rounded-xl transition-all duration-200 min-w-[54px] ${
                isActive
                  ? 'text-[#6C42C4] font-bold scale-105'
                  : 'text-[#746589] hover:text-[#251C38]'
              }`}
            >
              <div
                className={`p-1 rounded-lg transition-colors ${
                  isActive ? 'bg-[#B8A4F4]/30' : 'bg-transparent'
                }`}
              >
                <Icon className="w-5 h-5" />
              </div>
              <span className="text-[10px] mt-0.5 leading-none">{item.label}</span>
            </button>
          );
        })}
      </div>
    </nav>
  );
};
