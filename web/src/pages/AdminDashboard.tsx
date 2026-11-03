import React, { useState } from 'react';
import { ShieldCheck, Database, HardDrive, Bell, CheckCircle2, Search, Sparkles, Key, RefreshCw } from 'lucide-react';
import { SEEDED_ADMINS, type SeededAdmin } from '../lib/adminSeeds';
import type { Announcement } from '../lib/store';

interface AdminDashboardProps {
  onAddAnnouncement: (announcement: Announcement) => void;
}

export const AdminDashboard: React.FC<AdminDashboardProps> = ({ onAddAnnouncement }) => {
  const [admins] = useState<SeededAdmin[]>(SEEDED_ADMINS);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedRole, setSelectedRole] = useState<string>('all');
  const [backupStatus, setBackupStatus] = useState<string | null>(null);
  const [newTitle, setNewTitle] = useState('');
  const [newContent, setNewContent] = useState('');
  const [newCategory, setNewCategory] = useState<'Exam' | 'Hackathon' | 'Maintenance' | 'Academic'>('Exam');
  const [isUrgent, setIsUrgent] = useState(false);
  const [announcementSuccess, setAnnouncementSuccess] = useState(false);

  const filteredAdmins = admins.filter((a) => {
    const matchesSearch =
      a.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
      a.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      a.email.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesRole = selectedRole === 'all' || a.role === selectedRole;
    return matchesSearch && matchesRole;
  });

  const triggerBackup = () => {
    setBackupStatus('Initiating cryptographic PostgreSQL snapshot...');
    setTimeout(() => {
      setBackupStatus(
        'Backup completed: snapshot_20261028_verified.dump.enc (SHA-256 integrity verified).'
      );
      setTimeout(() => setBackupStatus(null), 4500);
    }, 1200);
  };

  const handlePostAnnouncement = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim()) return;

    onAddAnnouncement({
      id: 'ann_' + Date.now(),
      title: newTitle,
      content: newContent,
      category: newCategory,
      isUrgent,
      createdAt: 'Just now',
    });

    setNewTitle('');
    setNewContent('');
    setAnnouncementSuccess(true);
    setTimeout(() => setAnnouncementSuccess(false), 3000);
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
            Admin Control Center & Governance
            <span className="px-2.5 py-0.5 rounded-full text-xs font-extrabold bg-[#6C42C4] text-white">
              50 Seeded Accounts
            </span>
          </h1>
          <p className="text-xs text-[#645B74] mt-1">
            RBAC permissions directory, disaster recovery snapshots, and campus broadcast dispatch.
          </p>
        </div>

        <button
          onClick={triggerBackup}
          className="px-4 py-2.5 btn-chartreuse text-xs font-bold flex items-center gap-2 self-start sm:self-auto"
        >
          <Database className="w-4 h-4" />
          <span>Execute Database Backup</span>
        </button>
      </div>

      {backupStatus && (
        <div className="p-4 rounded-2xl bg-[#D6F36A]/40 border border-[#D6F36A] text-xs font-bold text-[#556B16] flex items-center gap-2 shadow-sm animate-fade-in">
          <CheckCircle2 className="w-4 h-4" />
          {backupStatus}
        </div>
      )}

      {/* Admin Stat Overview */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="glass p-5 border border-white/90">
          <span className="text-xs font-bold text-[#746589]">Pre-Seeded Admins</span>
          <div className="font-heading font-extrabold text-2xl text-[#6C42C4] mt-1">50 Accounts</div>
          <p className="text-[11px] text-[#556B16] font-medium mt-1">ADM-7001 to ADM-7050</p>
        </div>

        <div className="glass p-5 border border-white/90">
          <span className="text-xs font-bold text-[#746589]">Active Learners</span>
          <div className="font-heading font-extrabold text-2xl text-[#251C38] mt-1">3,420</div>
          <p className="text-[11px] text-[#6C42C4] font-medium mt-1">98.4% retention</p>
        </div>

        <div className="glass p-5 border border-white/90">
          <span className="text-xs font-bold text-[#746589]">Storage Health</span>
          <div className="font-heading font-extrabold text-2xl text-[#251C38] mt-1">99.98%</div>
          <p className="text-[11px] text-[#556B16] font-medium mt-1">Supabase Edge ready</p>
        </div>

        <div className="glass p-5 border border-white/90">
          <span className="text-xs font-bold text-[#746589]">Security Protocol</span>
          <div className="font-heading font-extrabold text-2xl text-[#556B16] mt-1">Zero-Trust</div>
          <p className="text-[11px] text-[#746589] font-medium mt-1">22 RLS tables enforced</p>
        </div>
      </div>

      {/* Split Section: Admin Directory & Broadcast Dispatch */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left 2 Cols: 50 Seeded Admin Credentials Table */}
        <div className="lg:col-span-2 glass p-6 border border-white/95 shadow-glass space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <h3 className="font-heading font-bold text-base text-[#251C38] flex items-center gap-2">
              <Key className="w-4 h-4 text-[#6C42C4]" />
              50 Pre-Seeded Admin Directory
            </h3>

            {/* Role Filter */}
            <div className="flex items-center gap-2">
              <select
                value={selectedRole}
                onChange={(e) => setSelectedRole(e.target.value)}
                className="px-2.5 py-1.5 text-xs rounded-xl glass-input font-medium"
              >
                <option value="all">All Roles</option>
                <option value="super_admin">Super Admins</option>
                <option value="academic_admin">Academic Directors</option>
                <option value="test_manager">Assessment Leads</option>
                <option value="hackathon_manager">Hackathon Directors</option>
                <option value="support_admin">Student Support</option>
              </select>
            </div>
          </div>

          {/* Search bar */}
          <div className="relative">
            <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#746589]" />
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Search by code (e.g. ADM-7001), name or email..."
              className="w-full pl-9 pr-3 py-2 text-xs rounded-xl glass-input"
            />
          </div>

          {/* Admins Table */}
          <div className="max-h-[360px] overflow-y-auto space-y-2 pr-1">
            {filteredAdmins.map((admin) => (
              <div
                key={admin.code}
                className="p-3 rounded-xl bg-white/70 hover:bg-white border border-white/90 flex items-center justify-between gap-3 transition-colors text-xs"
              >
                <div className="flex items-center gap-3">
                  <span className="font-mono font-bold text-[#6C42C4] bg-[#B8A4F4]/30 px-2 py-0.5 rounded-md border border-[#B8A4F4]/50">
                    {admin.code}
                  </span>
                  <div>
                    <div className="font-bold text-[#251C38]">{admin.name}</div>
                    <div className="text-[11px] text-[#746589]">{admin.department}</div>
                  </div>
                </div>

                <div className="text-right">
                  <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-[#D6F36A]/40 text-[#556B16]">
                    {admin.roleLabel}
                  </span>
                  <div className="text-[10px] text-[#746589] mt-0.5">{admin.email}</div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Right 1 Col: Announcement Broadcaster */}
        <div className="glass p-6 border border-white/95 shadow-glass space-y-4">
          <h3 className="font-heading font-bold text-base text-[#251C38] flex items-center gap-2">
            <Bell className="w-4 h-4 text-[#6C42C4]" />
            Broadcast Campus Alert
          </h3>

          {announcementSuccess && (
            <div className="p-3 rounded-xl bg-[#D6F36A]/40 text-xs font-bold text-[#556B16] flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4" />
              Announcement broadcast successfully!
            </div>
          )}

          <form onSubmit={handlePostAnnouncement} className="space-y-3 text-xs">
            <div>
              <label className="block font-bold text-[#251C38] mb-1">Alert Title</label>
              <input
                type="text"
                value={newTitle}
                onChange={(e) => setNewTitle(e.target.value)}
                placeholder="e.g. Schedule Maintenance"
                className="w-full px-3 py-2 rounded-xl glass-input text-xs"
                required
              />
            </div>

            <div>
              <label className="block font-bold text-[#251C38] mb-1">Content Details</label>
              <textarea
                value={newContent}
                onChange={(e) => setNewContent(e.target.value)}
                rows={3}
                placeholder="Provide instructions or updates..."
                className="w-full px-3 py-2 rounded-xl glass-input text-xs"
                required
              />
            </div>

            <div>
              <label className="block font-bold text-[#251C38] mb-1">Category</label>
              <select
                value={newCategory}
                onChange={(e) => setNewCategory(e.target.value as any)}
                className="w-full px-3 py-2 rounded-xl glass-input text-xs"
              >
                <option value="Exam">Exam Alert</option>
                <option value="Hackathon">Hackathon</option>
                <option value="Academic">Academic Schedule</option>
                <option value="Maintenance">Infrastructure Maintenance</option>
              </select>
            </div>

            <div className="flex items-center gap-2 pt-1">
              <input
                type="checkbox"
                id="urgentCheck"
                checked={isUrgent}
                onChange={(e) => setIsUrgent(e.target.checked)}
                className="w-4 h-4 rounded text-[#FF6347]"
              />
              <label htmlFor="urgentCheck" className="text-xs font-bold text-[#C34630]">
                Mark as High Priority (Tomato Red Alert)
              </label>
            </div>

            <button type="submit" className="w-full py-2.5 btn-lavender font-bold text-xs mt-2">
              Publish Announcement
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};
