package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {

    // --- User Profiles ---
    @Query("SELECT * FROM profiles LIMIT 1")
    fun getCurrentUser(): Flow<UserProfile?>

    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getUserById(id: String): UserProfile?

    @Query("SELECT * FROM profiles")
    fun getAllUsers(): Flow<List<UserProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Update
    suspend fun updateUser(user: UserProfile)

    // --- Admin Profiles ---
    @Query("SELECT * FROM admin_profiles ORDER BY adminCode ASC")
    fun getAllAdmins(): Flow<List<AdminProfile>>

    @Query("SELECT * FROM admin_profiles WHERE adminCode = :code LIMIT 1")
    suspend fun getAdminByCode(code: String): AdminProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmins(admins: List<AdminProfile>)

    @Update
    suspend fun updateAdmin(admin: AdminProfile)

    // --- Study Files ---
    @Query("SELECT * FROM study_files ORDER BY uploadDate DESC")
    fun getAllFiles(): Flow<List<StudyFile>>

    @Query("SELECT * FROM study_files WHERE id = :id")
    suspend fun getFileById(id: String): StudyFile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: StudyFile)

    @Update
    suspend fun updateFile(file: StudyFile)

    @Query("DELETE FROM study_files WHERE id = :id")
    suspend fun deleteFileById(id: String)

    // --- Summaries ---
    @Query("SELECT * FROM summaries WHERE fileId = :fileId LIMIT 1")
    fun getSummaryForFile(fileId: String): Flow<FileSummary?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: FileSummary)

    // --- AI Chat Conversations ---
    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC")
    fun getAllConversations(): Flow<List<ChatConversation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conv: ChatConversation)

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversation(id: String)

    // --- AI Chat Messages ---
    @Query("SELECT * FROM messages WHERE conversationId = :convId ORDER BY createdAt ASC")
    fun getMessagesForConversation(convId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: ChatMessage)

    @Query("DELETE FROM messages WHERE conversationId = :convId")
    suspend fun clearMessagesForConversation(convId: String)

    // --- Quizzes ---
    @Query("SELECT * FROM quizzes ORDER BY createdAt DESC")
    fun getAllQuizzes(): Flow<List<Quiz>>

    @Query("SELECT * FROM quizzes WHERE id = :id")
    suspend fun getQuizById(id: String): Quiz?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM quiz_questions WHERE quizId = :quizId")
    suspend fun getQuestionsForQuiz(quizId: String): List<QuizQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestion>)

    @Query("SELECT * FROM quiz_attempts ORDER BY completedAt DESC")
    fun getAllQuizAttempts(): Flow<List<QuizAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizAttempt(attempt: QuizAttempt)

    // --- Official Tests ---
    @Query("SELECT * FROM official_tests ORDER BY createdAt DESC")
    fun getAllTests(): Flow<List<OfficialTest>>

    @Query("SELECT * FROM official_tests WHERE id = :id")
    suspend fun getTestById(id: String): OfficialTest?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: OfficialTest)

    @Update
    suspend fun updateTest(test: OfficialTest)

    @Query("DELETE FROM official_tests WHERE id = :id")
    suspend fun deleteTest(id: String)

    @Query("SELECT * FROM test_questions WHERE testId = :testId ORDER BY questionNumber ASC")
    suspend fun getQuestionsForTest(testId: String): List<TestQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestQuestions(questions: List<TestQuestion>)

    @Query("SELECT * FROM test_attempts ORDER BY submittedAt DESC")
    fun getAllTestAttempts(): Flow<List<TestAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestAttempt(attempt: TestAttempt)

    // --- Hackathons ---
    @Query("SELECT * FROM hackathons ORDER BY startDate DESC")
    fun getAllHackathons(): Flow<List<Hackathon>>

    @Query("SELECT * FROM hackathons WHERE id = :id")
    suspend fun getHackathonById(id: String): Hackathon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHackathon(hackathon: Hackathon)

    @Update
    suspend fun updateHackathon(hackathon: Hackathon)

    @Query("DELETE FROM hackathons WHERE id = :id")
    suspend fun deleteHackathon(id: String)

    @Query("SELECT * FROM hackathon_participants WHERE hackathonId = :hackathonId ORDER BY score DESC")
    fun getParticipantsForHackathon(hackathonId: String): Flow<List<HackathonParticipant>>

    @Query("SELECT * FROM hackathon_participants WHERE userId = :userId")
    fun getUserHackathonRegistrations(userId: String): Flow<List<HackathonParticipant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHackathonParticipant(p: HackathonParticipant)

    @Update
    suspend fun updateHackathonParticipant(p: HackathonParticipant)

    // --- Code Sessions ---
    @Query("SELECT * FROM code_sessions ORDER BY createdAt DESC")
    fun getAllCodeSessions(): Flow<List<CodeSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCodeSession(session: CodeSession)

    @Update
    suspend fun updateCodeSession(session: CodeSession)

    // --- Study Plans & Tasks ---
    @Query("SELECT * FROM study_plans ORDER BY createdAt DESC")
    fun getAllStudyPlans(): Flow<List<StudyPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyPlan(plan: StudyPlan)

    @Query("DELETE FROM study_plans WHERE id = :id")
    suspend fun deleteStudyPlan(id: String)

    @Query("SELECT * FROM study_tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<StudyTask>>

    @Query("SELECT * FROM study_tasks WHERE planId = :planId ORDER BY dueDate ASC")
    fun getTasksForPlan(planId: String): Flow<List<StudyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTask)

    @Update
    suspend fun updateTask(task: StudyTask)

    @Query("DELETE FROM study_tasks WHERE id = :id")
    suspend fun deleteTask(id: String)

    // --- Announcements & Notifications ---
    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)

    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<AppNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotification)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: String)
}
