import React, { useState } from 'react';
import { User, Shield, School, BookOpen, Flame, Clock, Award, Save, Check } from 'lucide-react';
import type { UserSession } from '../lib/store';

interface ProfileSettingsProps {
  user: UserSession | null;
  onUpdateUser: (user: UserSession) => void;
}

export const ProfileSettings: React.FC<ProfileSettingsProps> = ({ user, onUpdateUser }) => {
  const [name, setName] = useState(user?.name || 'Alex Chen');
  const [email] = useState(user?.email || 'alex.chen@university.edu');
  const [college, setCollege] = useState(user?.college || 'Stanford School of Engineering');
  const [course, setCourse] = useState(user?.course || 'Computer Science');
  const [year, setYear] = useState(user?.year || 'Senior (Year 4)');
  const [saved, setSaved] = useState(false);

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;

    const updated: UserSession = {
      ...user,
      name,
      college,
      course,
      year,
    };

    onUpdateUser(updated);
    setSaved(true);
    setTimeout(() => setSaved(false), 2500);
  };

  return (
    <div className="space-y-6 animate-fade-in max-w-4xl">
      <div>
        <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
          Profile & Academic Preferences
          <span className="px-2.5 py-0.5 rounded-full text-xs font-bold bg-[#D6F36A]/40 text-[#556B16]">
            {user?.role === 'admin' ? `Admin ID: ${user.adminCode}` : 'Enrolled Student'}
          </span>
        </h1>
        <p className="text-xs text-[#645B74] mt-1">
          Manage your student credentials, academic curriculum, and study analytics.
        </p>
      </div>

      {saved && (
        <div className="p-4 rounded-2xl bg-[#D6F36A]/40 border border-[#D6F36A] text-xs font-bold text-[#556B16] flex items-center gap-2 animate-fade-in">
          <Check className="w-4 h-4" />
          Profile settings saved successfully!
        </div>
      )}

      {/* Main Form */}
      <form onSubmit={handleSave} className="glass p-6 sm:p-8 border border-white/95 shadow-glass space-y-6">
        {/* User Avatar & Streak */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-6 border-b border-[#B8A4F4]/20">
          <div className="flex items-center gap-4">
            <div className="w-16 h-16 rounded-3xl bg-gradient-to-tr from-[#6C42C4] via-[#B8A4F4] to-[#D6F36A] p-[2px] shadow-sm">
              <div className="w-full h-full bg-white rounded-[22px] flex items-center justify-center font-extrabold text-xl text-[#6C42C4]">
                {name.charAt(0)}
              </div>
            </div>
            <div>
              <h3 className="font-heading font-bold text-lg text-[#251C38]">{name}</h3>
              <p className="text-xs text-[#746589]">{email}</p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <div className="px-3 py-2 rounded-xl bg-[#D6F36A]/30 border border-[#D6F36A]/50 text-center">
              <div className="text-[10px] font-bold text-[#556B16]">STREAK</div>
              <div className="font-heading font-extrabold text-sm text-[#251C38]">
                {user ? `${user.studyStreak} Days` : '12 Days'}
              </div>
            </div>
            <div className="px-3 py-2 rounded-xl bg-[#B8A4F4]/30 border border-[#B8A4F4]/50 text-center">
              <div className="text-[10px] font-bold text-[#6C42C4]">STUDIED</div>
              <div className="font-heading font-extrabold text-sm text-[#251C38]">64.5 hrs</div>
            </div>
          </div>
        </div>

        {/* Inputs */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs">
          <div>
            <label className="block font-bold text-[#251C38] mb-1">Full Legal Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full px-3 py-2.5 rounded-xl glass-input text-xs"
              required
            />
          </div>

          <div>
            <label className="block font-bold text-[#251C38] mb-1">University / College</label>
            <input
              type="text"
              value={college}
              onChange={(e) => setCollege(e.target.value)}
              className="w-full px-3 py-2.5 rounded-xl glass-input text-xs"
              required
            />
          </div>

          <div>
            <label className="block font-bold text-[#251C38] mb-1">Major / Degree Course</label>
            <input
              type="text"
              value={course}
              onChange={(e) => setCourse(e.target.value)}
              className="w-full px-3 py-2.5 rounded-xl glass-input text-xs"
              required
            />
          </div>

          <div>
            <label className="block font-bold text-[#251C38] mb-1">Academic Year</label>
            <input
              type="text"
              value={year}
              onChange={(e) => setYear(e.target.value)}
              className="w-full px-3 py-2.5 rounded-xl glass-input text-xs"
              required
            />
          </div>
        </div>

        <div className="flex justify-end pt-4 border-t border-[#B8A4F4]/20">
          <button type="submit" className="px-5 py-2.5 btn-lavender text-xs font-bold flex items-center gap-2">
            <Save className="w-4 h-4" />
            Save Profile Changes
          </button>
        </div>
      </form>
    </div>
  );
};
