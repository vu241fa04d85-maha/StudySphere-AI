import React, { useState, useEffect } from 'react';
import { Navbar } from './components/Navbar';
import { Sidebar } from './components/Sidebar';
import { MobileBottomNav } from './components/MobileBottomNav';
import { CookieConsent } from './components/CookieConsent';
import { PrivacyTermsModal } from './components/PrivacyTermsModal';
import { SplashLoader } from './components/SplashLoader';

import { AuthPage } from './pages/AuthPage';
import { LandingPage } from './pages/LandingPage';
import { StudentDashboard } from './pages/StudentDashboard';
import { AIChatbot } from './pages/AIChatbot';
import { DocumentLibrary } from './pages/DocumentLibrary';
import { CodeLab } from './pages/CodeLab';
import { QuizPortal } from './pages/QuizPortal';
import { TestPortal } from './pages/TestPortal';
import { HackathonPortal } from './pages/HackathonPortal';
import { StudyPlanner } from './pages/StudyPlanner';
import { AdminDashboard } from './pages/AdminDashboard';
import { NotificationsCenter } from './pages/NotificationsCenter';
import { ProfileSettings } from './pages/ProfileSettings';
import { AuthModal } from './pages/AuthModal';
import { NotFound } from './pages/NotFound';

import {
  INITIAL_TASKS,
  INITIAL_ANNOUNCEMENTS,
  INITIAL_HACKATHONS,
  type UserSession,
  type StudyTask,
  type Announcement,
  type Hackathon
} from './lib/store';

export function App() {
  // App Phase Flow: 1) splash (5s) -> 2) auth (login/signup) -> 3) main (dashboard)
  const [appPhase, setAppPhase] = useState<'splash' | 'auth' | 'main'>('splash');

  const [user, setUser] = useState<UserSession | null>(() => {
    const saved = localStorage.getItem('ai_sa_user');
    if (saved) {
      try { return JSON.parse(saved); } catch (e) { return null; }
    }
    return null;
  });

  const [activeTab, setActiveTab] = useState<string>('home');
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [mobileSidebarOpen, setMobileSidebarOpen] = useState(false);

  const [tasks, setTasks] = useState<StudyTask[]>(INITIAL_TASKS);
  const [announcements, setAnnouncements] = useState<Announcement[]>(INITIAL_ANNOUNCEMENTS);
  const [hackathons] = useState<Hackathon[]>(INITIAL_HACKATHONS);

  const [isAuthOpen, setIsAuthOpen] = useState(false);
  const [isPrivacyOpen, setIsPrivacyOpen] = useState(false);
  const [cookieConsentDismissed, setCookieConsentDismissed] = useState(() => {
    return localStorage.getItem('ai_sa_cookie_consent') === 'true';
  });

  useEffect(() => {
    if (user) {
      localStorage.setItem('ai_sa_user', JSON.stringify(user));
    } else {
      localStorage.removeItem('ai_sa_user');
    }
  }, [user]);

  const handleLoginSuccess = (session: UserSession) => {
    setUser(session);
    setAppPhase('main');
    if (session.role === 'admin') {
      setActiveTab('admin');
    } else {
      setActiveTab('home');
    }
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('ai_sa_user');
    setAppPhase('auth');
    setActiveTab('home');
  };

  const handleDismissCookie = () => {
    localStorage.setItem('ai_sa_cookie_consent', 'true');
    setCookieConsentDismissed(true);
  };

  const handleAddAnnouncement = (item: Announcement) => {
    setAnnouncements((prev) => [item, ...prev]);
  };

  // Phase 1: Dedicated 5-Second Loading Animation
  if (appPhase === 'splash') {
    return <SplashLoader onFinish={() => setAppPhase('auth')} durationMs={5000} />;
  }

  // Phase 2: Dedicated Authentication Screen (Email ID & Password checking, Sign In / Sign Up, Admin / User Switching)
  if (appPhase === 'auth') {
    return (
      <>
        <AuthPage
          onLoginSuccess={handleLoginSuccess}
          onOpenPrivacy={() => setIsPrivacyOpen(true)}
        />
        <PrivacyTermsModal
          isOpen={isPrivacyOpen}
          onClose={() => setIsPrivacyOpen(false)}
        />
      </>
    );
  }

  // Phase 3: Authenticated Dashboard Experience
  return (
    <div className="min-h-screen flex flex-col">
      {/* Top Navigation */}
      <Navbar
        user={user}
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        unreadCount={announcements.filter((a) => a.isUrgent).length}
        onOpenAuth={() => setAppPhase('auth')}
        onLogout={handleLogout}
        onToggleSidebar={() => setMobileSidebarOpen(!mobileSidebarOpen)}
      />

      <div className="flex-1 flex max-w-7xl w-full mx-auto">
        {/* Desktop Sidebar */}
        <Sidebar
          activeTab={activeTab}
          setActiveTab={(tab) => {
            setActiveTab(tab);
            setMobileSidebarOpen(false);
          }}
          collapsed={sidebarCollapsed}
          setCollapsed={setSidebarCollapsed}
          user={user}
          onOpenPrivacy={() => setIsPrivacyOpen(true)}
        />

        {/* Mobile Drawer Overlay */}
        {mobileSidebarOpen && (
          <div
            className="fixed inset-0 z-40 bg-black/30 lg:hidden backdrop-blur-xs"
            onClick={() => setMobileSidebarOpen(false)}
          />
        )}

        {/* Main Content Pane */}
        <main className="flex-1 px-4 sm:px-6 lg:px-8 py-6 pb-24 lg:pb-12 min-w-0">
          {activeTab === 'home' && (
            <StudentDashboard
              user={user}
              tasks={tasks}
              announcements={announcements}
              hackathons={hackathons}
              setActiveTab={setActiveTab}
              onOpenAuth={() => setAppPhase('auth')}
            />
          )}

          {activeTab === 'chat' && <AIChatbot />}
          {activeTab === 'files' && <DocumentLibrary />}
          {activeTab === 'codelab' && <CodeLab />}
          {activeTab === 'quizzes' && <QuizPortal />}
          {activeTab === 'tests' && <TestPortal />}
          {activeTab === 'hackathons' && <HackathonPortal />}
          {activeTab === 'planner' && <StudyPlanner />}
          {activeTab === 'admin' && (
            <AdminDashboard onAddAnnouncement={handleAddAnnouncement} />
          )}
          {activeTab === 'notifications' && (
            <NotificationsCenter announcements={announcements} />
          )}
          {activeTab === 'settings' && (
            <ProfileSettings user={user} onUpdateUser={setUser} />
          )}
          {activeTab === 'landing' && (
            <LandingPage
              onGetStarted={() => setActiveTab('home')}
              onOpenAdminAuth={() => setAppPhase('auth')}
            />
          )}
          {!['home', 'chat', 'files', 'codelab', 'quizzes', 'tests', 'hackathons', 'planner', 'admin', 'notifications', 'settings', 'landing'].includes(activeTab) && (
            <NotFound onGoHome={() => setActiveTab('home')} />
          )}
        </main>
      </div>

      {/* Floating Bottom Nav for Mobile Screens */}
      <MobileBottomNav activeTab={activeTab} setActiveTab={setActiveTab} />

      {/* Floating Cookie Consent */}
      {!cookieConsentDismissed && (
        <CookieConsent
          onAccept={handleDismissCookie}
          onOpenPrivacy={() => setIsPrivacyOpen(true)}
        />
      )}

      {/* Auth Modal for Quick Switching inside Dashboard */}
      <AuthModal
        isOpen={isAuthOpen}
        onClose={() => setIsAuthOpen(false)}
        onLoginSuccess={handleLoginSuccess}
      />

      {/* Privacy and Terms Modal */}
      <PrivacyTermsModal
        isOpen={isPrivacyOpen}
        onClose={() => setIsPrivacyOpen(false)}
      />
    </div>
  );
}

export default App;
