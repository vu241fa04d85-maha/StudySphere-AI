package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Normal Student / Learner Profile
 */
@Entity(tableName = "profiles")
data class UserProfile(
    @PrimaryKey val id: String,
    val fullName: String,
    val email: String,
    val college: String,
    val course: String,
    val year: String,
    val profileImage: String = "",
    val studyStreak: Int = 5,
    val totalStudyHours: Double = 14.5,
    val completedQuizzes: Int = 12,
    val testsTaken: Int = 4,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Admin Profile with dedicated 8-character Admin Code (ADMIN001 - ADMIN050)
 */
@Entity(tableName = "admin_profiles")
data class AdminProfile(
    @PrimaryKey val id: String,
    val adminCode: String, // e.g. ADMIN001 to ADMIN050
    val fullName: String,
    val email: String,
    val role: String = "admin", // "super_admin" or "admin"
    val status: String = "active", // "active" or "suspended"
    val mustChangePassword: Boolean = false,
    val lastLogin: Long = System.currentTimeMillis()
)

/**
 * Uploaded / Stored Study Material
 */
@Entity(tableName = "study_files")
data class StudyFile(
    @PrimaryKey val id: String,
    val userId: String,
    val fileName: String,
    val fileType: String, // "PDF", "PPT", "DOC", "TXT"
    val fileSizeKb: Int,
    val subject: String,
    val description: String,
    val uploadDate: Long = System.currentTimeMillis(),
    val processingStatus: String = "completed", // "pending", "processing", "completed", "error"
    val textContent: String = ""
)

/**
 * Generated AI Summary & Study Pack for a Document
 */
@Entity(tableName = "summaries")
data class FileSummary(
    @PrimaryKey val id: String,
    val fileId: String,
    val userId: String,
    val summaryText: String,
    val keyPoints: List<String> = emptyList(),
    val flashcardsJson: String = "",
    val sampleQuestionsJson: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class FlashcardItem(
    val question: String,
    val answer: String
)

/**
 * AI Study Chat Conversation
 */
@Entity(tableName = "conversations")
data class ChatConversation(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val studyMode: String = "Standard", // "Standard", "Simple Explanation", "Exam Prep", "Code Debug"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * AI Study Chat Message
 */
@Entity(tableName = "messages")
data class ChatMessage(
    @PrimaryKey val id: String,
    val conversationId: String,
    val sender: String, // "user" or "ai"
    val message: String,
    val codeSnippet: String = "",
    val codeLanguage: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Interactive Quiz
 */
@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val subject: String,
    val difficulty: String = "Medium", // "Easy", "Medium", "Hard"
    val questionCount: Int = 5,
    val createdBy: String = "AI Assistant",
    val status: String = "published",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey val id: String,
    val quizId: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String, // "A", "B", "C", or "D"
    val explanation: String,
    val marks: Int = 1
)

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey val id: String,
    val quizId: String,
    val quizTitle: String,
    val userId: String,
    val score: Int,
    val totalMarks: Int,
    val timeTakenSeconds: Int,
    val completedAt: Long = System.currentTimeMillis()
)

/**
 * Official Admin Tests
 */
@Entity(tableName = "official_tests")
data class OfficialTest(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val subject: String,
    val durationMinutes: Int,
    val totalMarks: Int,
    val passingScore: Int,
    val createdByAdminCode: String,
    val status: String = "published", // "published", "draft", "archived"
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis() + 86400000L * 7,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_questions")
data class TestQuestion(
    @PrimaryKey val id: String,
    val testId: String,
    val questionNumber: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val marks: Int = 2
)

@Entity(tableName = "test_attempts")
data class TestAttempt(
    @PrimaryKey val id: String,
    val testId: String,
    val testTitle: String,
    val userId: String,
    val userName: String,
    val score: Int,
    val totalMarks: Int,
    val passed: Boolean,
    val submittedAt: Long = System.currentTimeMillis()
)

/**
 * Hackathons created by Admins
 */
@Entity(tableName = "hackathons")
data class Hackathon(
    @PrimaryKey val id: String,
    val title: String,
    val theme: String,
    val description: String,
    val rules: String,
    val eligibility: String = "Open to all verified students",
    val prizePool: String = "$5,000 in Grants & Prizes",
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long = System.currentTimeMillis() + 86400000L * 14,
    val registrationDeadline: Long = System.currentTimeMillis() + 86400000L * 5,
    val createdByAdmin: String = "ADMIN001",
    val status: String = "active", // "active", "upcoming", "ended"
    val participantCount: Int = 42
)

@Entity(tableName = "hackathon_participants")
data class HackathonParticipant(
    @PrimaryKey val id: String,
    val hackathonId: String,
    val userId: String,
    val userName: String,
    val projectName: String = "",
    val projectUrl: String = "",
    val submissionStatus: String = "registered", // "registered", "submitted", "reviewed"
    val score: Int = 0,
    val registeredAt: Long = System.currentTimeMillis()
)

/**
 * Multi-Language Code Lab Session
 */
@Entity(tableName = "code_sessions")
data class CodeSession(
    @PrimaryKey val id: String,
    val userId: String,
    val language: String, // "Python", "Java", "C++", "C", "JavaScript", "TypeScript", "Go", "Rust", "Kotlin", etc.
    val title: String,
    val problemStatement: String,
    val inputFormat: String,
    val outputFormat: String,
    val constraints: String,
    val code: String,
    val explanation: String = "",
    val timeComplexity: String = "O(n)",
    val spaceComplexity: String = "O(1)",
    val suggestions: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Study Planner & Tasks
 */
@Entity(tableName = "study_plans")
data class StudyPlan(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val subject: String,
    val targetExamDate: Long,
    val dailyHours: Double,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey val id: String,
    val planId: String,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: String = "Medium", // "High", "Medium", "Low"
    val isCompleted: Boolean = false,
    val estimatedMinutes: Int = 45
)

/**
 * Announcements & Notifications
 */
@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val category: String = "General", // "Exam", "Hackathon", "Platform"
    val createdBy: String = "Admin Board",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class AppNotification(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String = "info", // "test", "hackathon", "quiz", "planner"
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
