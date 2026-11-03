import React from 'react';
import { Bell, AlertCircle, CheckCircle2, Calendar, Sparkles } from 'lucide-react';
import type { Announcement } from '../lib/store';

interface NotificationsCenterProps {
  announcements: Announcement[];
}

export const NotificationsCenter: React.FC<NotificationsCenterProps> = ({ announcements }) => {
  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
          Notifications & Academic Alerts
          <span className="px-2 py-0.5 rounded-full text-xs font-bold bg-[#B8A4F4]/30 text-[#6C42C4] border border-[#B8A4F4]/50">
            {announcements.length} Updates
          </span>
        </h1>
        <p className="text-xs text-[#645B74] mt-1">
          Official communications regarding exam schedules, hackathons, and platform announcements.
        </p>
      </div>

      <div className="space-y-3.5">
        {announcements.map((item) => (
          <div
            key={item.id}
            className={`glass p-5 border transition-all duration-200 flex items-start gap-4 ${
              item.isUrgent
                ? 'border-[#FF6347]/50 bg-white/90 shadow-glass'
                : 'border-white/95 glass-card-hover'
            }`}
          >
            <div
              className={`p-3 rounded-2xl flex-shrink-0 ${
                item.isUrgent
                  ? 'bg-[#FF6347]/20 text-[#C34630]'
                  : 'bg-[#B8A4F4]/30 text-[#6C42C4]'
              }`}
            >
              {item.isUrgent ? <AlertCircle className="w-5 h-5" /> : <Bell className="w-5 h-5" />}
            </div>

            <div className="flex-1">
              <div className="flex flex-wrap items-center justify-between gap-2 mb-1.5">
                <span
                  className={`text-[10px] px-2 py-0.5 rounded-md font-bold uppercase ${
                    item.isUrgent
                      ? 'bg-[#FF6347] text-white'
                      : 'bg-[#D6F36A]/40 text-[#556B16]'
                  }`}
                >
                  {item.category}
                </span>
                <span className="text-[11px] text-[#746589] flex items-center gap-1">
                  <Calendar className="w-3 h-3" />
                  {item.createdAt}
                </span>
              </div>

              <h3 className="font-heading font-bold text-base text-[#251C38]">{item.title}</h3>
              <p className="text-xs text-[#645B74] mt-1.5 leading-relaxed">{item.content}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
