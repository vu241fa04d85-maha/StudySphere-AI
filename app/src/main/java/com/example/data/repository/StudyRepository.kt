package com.example.data.repository

import com.example.data.ai.AIEngine
import com.example.data.local.StudyDao
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.UUID

class StudyRepository(private val dao: StudyDao) {

    val currentUser: Flow<UserProfile?> = dao.getCurrentUser()
    val allUsers: Flow<List<UserProfile>> = dao.getAllUsers()
    val allAdmins: Flow<List<AdminProfile>> = dao.getAllAdmins()
    val allFiles: Flow<List<StudyFile>> = dao.getAllFiles()
    val allConversations: Flow<List<ChatConversation>> = dao.getAllConversations()
    val allQuizzes: Flow<List<Quiz>> = dao.getAllQuizzes()
    val allQuizAttempts: Flow<List<QuizAttempt>> = dao.getAllQuizAttempts()
    val allTests: Flow<List<OfficialTest>> = dao.getAllTests()
    val allTestAttempts: Flow<List<TestAttempt>> = dao.getAllTestAttempts()
    val allHackathons: Flow<List<Hackathon>> = dao.getAllHackathons()
    val allCodeSessions: Flow<List<CodeSession>> = dao.getAllCodeSessions()
    val allStudyPlans: Flow<List<StudyPlan>> = dao.getAllStudyPlans()
    val allTasks: Flow<List<StudyTask>> = dao.getAllTasks()
    val allAnnouncements: Flow<List<Announcement>> = dao.getAllAnnouncements()
    val allNotifications: Flow<List<AppNotification>> = dao.getAllNotifications()

    fun getMessagesForConversation(convId: String): Flow<List<ChatMessage>> =
        dao.getMessagesForConversation(convId)

    fun getSummaryForFile(fileId: String): Flow<FileSummary?> =
        dao.getSummaryForFile(fileId)

    suspend fun getQuestionsForQuiz(quizId: String): List<QuizQuestion> =
        withContext(Dispatchers.IO) { dao.getQuestionsForQuiz(quizId) }

    suspend fun getQuestionsForTest(testId: String): List<TestQuestion> =
        withContext(Dispatchers.IO) { dao.getQuestionsForTest(testId) }

    fun getHackathonParticipants(hackathonId: String): Flow<List<HackathonParticipant>> =
        dao.getParticipantsForHackathon(hackathonId)

    suspend fun verifyAdminCode(code: String): AdminProfile? = withContext(Dispatchers.IO) {
        dao.getAdminByCode(code.trim().uppercase())
    }

    suspend fun updateAdmin(admin: AdminProfile) = withContext(Dispatchers.IO) {
        dao.updateAdmin(admin)
    }

    suspend fun updateUser(user: UserProfile) = withContext(Dispatchers.IO) {
        dao.updateUser(user)
    }

    suspend fun insertUser(user: UserProfile) = withContext(Dispatchers.IO) {
        dao.insertUser(user)
    }

    suspend fun sendChatMessage(conversationId: String, userMessage: String, studyMode: String) = withContext(Dispatchers.IO) {
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            sender = "user",
            message = userMessage
        )
        dao.insertMessage(userMsg)

        // Generate AI response
        val (aiText, codeSnippet) = AIEngine.generateChatResponse(userMessage, studyMode)
        val aiMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            sender = "ai",
            message = aiText,
            codeSnippet = codeSnippet ?: "",
            codeLanguage = if (codeSnippet != null) "python" else ""
        )
        dao.insertMessage(aiMsg)
    }

    suspend fun createNewConversation(title: String, studyMode: String = "Standard"): String = withContext(Dispatchers.IO) {
        val convId = UUID.randomUUID().toString()
        val conv = ChatConversation(
            id = convId,
            userId = "student-1",
            title = title.ifBlank { "Study Session ${System.currentTimeMillis() % 1000}" },
            studyMode = studyMode
        )
        dao.insertConversation(conv)
        // Welcome message
        val welcome = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = convId,
            sender = "ai",
            message = "Hello! I am your AI Study Assistant. Ask me any conceptual question, request study notes, or paste code to debug!"
        )
        dao.insertMessage(welcome)
        convId
    }

    suspend fun deleteConversation(id: String) = withContext(Dispatchers.IO) {
        dao.deleteConversation(id)
        dao.clearMessagesForConversation(id)
    }

    suspend fun uploadStudyFile(fileName: String, fileType: String, subject: String, description: String, textContent: String) = withContext(Dispatchers.IO) {
        val fileId = UUID.randomUUID().toString()
        val file = StudyFile(
            id = fileId,
            userId = "student-1",
            fileName = fileName,
            fileType = fileType,
            fileSizeKb = (40..850).random(),
            subject = subject,
            description = description,
            textContent = textContent,
            processingStatus = "completed"
        )
        dao.insertFile(file)

        // Automatically trigger AI analysis
        val (summaryText, keyPoints, flashcards) = AIEngine.analyzeDocument(fileName, subject, textContent)
        val flashcardsJson = flashcards.joinToString(";;") { "${it.question}::${it.answer}" }
        val summary = FileSummary(
            id = UUID.randomUUID().toString(),
            fileId = fileId,
            userId = "student-1",
            summaryText = summaryText,
            keyPoints = keyPoints,
            flashcardsJson = flashcardsJson
        )
        dao.insertSummary(summary)
    }

    suspend fun deleteFile(id: String) = withContext(Dispatchers.IO) {
        dao.deleteFileById(id)
    }

    suspend fun submitQuizAttempt(quizId: String, quizTitle: String, score: Int, totalMarks: Int, timeTaken: Int) = withContext(Dispatchers.IO) {
        val attempt = QuizAttempt(
            id = UUID.randomUUID().toString(),
            quizId = quizId,
            quizTitle = quizTitle,
            userId = "student-1",
            score = score,
            totalMarks = totalMarks,
            timeTakenSeconds = timeTaken
        )
        dao.insertQuizAttempt(attempt)
    }

    suspend fun submitTestAttempt(testId: String, testTitle: String, score: Int, totalMarks: Int) = withContext(Dispatchers.IO) {
        val passed = score >= (totalMarks * 0.6)
        val attempt = TestAttempt(
            id = UUID.randomUUID().toString(),
            testId = testId,
            testTitle = testTitle,
            userId = "student-1",
            userName = "Prasad K.",
            score = score,
            totalMarks = totalMarks,
            passed = passed
        )
        dao.insertTestAttempt(attempt)
    }

    suspend fun registerForHackathon(hackathonId: String) = withContext(Dispatchers.IO) {
        val p = HackathonParticipant(
            id = UUID.randomUUID().toString(),
            hackathonId = hackathonId,
            userId = "student-1",
            userName = "Prasad K.",
            submissionStatus = "registered"
        )
        dao.insertHackathonParticipant(p)
    }

    suspend fun submitHackathonProject(hackathonId: String, projectName: String, projectUrl: String) = withContext(Dispatchers.IO) {
        val p = HackathonParticipant(
            id = UUID.randomUUID().toString(),
            hackathonId = hackathonId,
            userId = "student-1",
            userName = "Prasad K.",
            projectName = projectName,
            projectUrl = projectUrl,
            submissionStatus = "submitted",
            score = (85..98).random()
        )
        dao.insertHackathonParticipant(p)
    }

    suspend fun createOfficialTest(title: String, subject: String, description: String, duration: Int, totalMarks: Int, passingScore: Int, adminCode: String) = withContext(Dispatchers.IO) {
        val testId = UUID.randomUUID().toString()
        val test = OfficialTest(
            id = testId,
            title = title,
            subject = subject,
            description = description,
            durationMinutes = duration,
            totalMarks = totalMarks,
            passingScore = passingScore,
            createdByAdminCode = adminCode
        )
        dao.insertTest(test)

        // Seed 3 questions
        val questions = listOf(
            TestQuestion(
                id = UUID.randomUUID().toString(),
                testId = testId,
                questionNumber = 1,
                question = "Which asymptotic notation describes the strict upper bound on execution time?",
                options = listOf("Big-O Notation", "Big-Omega Notation", "Big-Theta Notation", "Little-o Notation"),
                correctIndex = 0,
                marks = totalMarks / 3
            ),
            TestQuestion(
                id = UUID.randomUUID().toString(),
                testId = testId,
                questionNumber = 2,
                question = "What fundamental condition causes deadlock in concurrent operating systems?",
                options = listOf("Mutual Exclusion & Hold and Wait", "Premature Thread Termination", "Infinite Loop in User Space", "Cache Thrashing"),
                correctIndex = 0,
                marks = totalMarks / 3
            ),
            TestQuestion(
                id = UUID.randomUUID().toString(),
                testId = testId,
                questionNumber = 3,
                question = "In relational databases, which normal form eliminates transitive dependency?",
                options = listOf("1NF", "2NF", "3NF", "BCNF"),
                correctIndex = 2,
                marks = totalMarks / 3
            )
        )
        dao.insertTestQuestions(questions)
    }

    suspend fun deleteOfficialTest(testId: String) = withContext(Dispatchers.IO) {
        dao.deleteTest(testId)
    }

    suspend fun createHackathon(title: String, theme: String, description: String, rules: String, adminCode: String) = withContext(Dispatchers.IO) {
        val h = Hackathon(
            id = UUID.randomUUID().toString(),
            title = title,
            theme = theme,
            description = description,
            rules = rules,
            createdByAdmin = adminCode
        )
        dao.insertHackathon(h)
    }

    suspend fun deleteHackathon(id: String) = withContext(Dispatchers.IO) {
        dao.deleteHackathon(id)
    }

    suspend fun toggleTaskCompleted(task: StudyTask) = withContext(Dispatchers.IO) {
        dao.updateTask(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun addStudyTask(title: String, description: String, priority: String, minutes: Int) = withContext(Dispatchers.IO) {
        val task = StudyTask(
            id = UUID.randomUUID().toString(),
            planId = "plan-1",
            title = title,
            description = description,
            dueDate = System.currentTimeMillis() + 86400000L,
            priority = priority,
            estimatedMinutes = minutes
        )
        dao.insertTask(task)
    }

    suspend fun deleteTask(id: String) = withContext(Dispatchers.IO) {
        dao.deleteTask(id)
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val existingUser = dao.getCurrentUser().firstOrNull()
        if (existingUser != null) return@withContext

        // 1. Seed Current Student Profile & Demo Peers
        val defaultUser = UserProfile(
            id = "student-1",
            fullName = "Prasad K.",
            email = "prasad.student@university.edu",
            college = "Institute of Technology & Science",
            course = "Computer Science & Engineering",
            year = "3rd Year",
            studyStreak = 7,
            totalStudyHours = 18.5,
            completedQuizzes = 14,
            testsTaken = 5
        )
        dao.insertUser(defaultUser)

        val demoPeers = listOf(
            UserProfile("student-2", "Sarah Jenkins", "sarah.j@university.edu", "Institute of Technology & Science", "Information Systems", "2nd Year", studyStreak = 4),
            UserProfile("student-3", "Alex Rivera", "alex.r@polytech.edu", "State University", "Software Engineering", "4th Year", studyStreak = 12),
            UserProfile("student-4", "Priya Sharma", "priya.s@tech.edu", "National Institute", "Data Science", "3rd Year", studyStreak = 9),
            UserProfile("student-5", "Marcus Vance", "marcus.v@campus.edu", "Metropolitan University", "Computer Engineering", "1st Year", studyStreak = 3)
        )
        demoPeers.forEach { dao.insertUser(it) }

        // 2. Seed 50 Admin Accounts (ADMIN001 through ADMIN050)
        val adminList = mutableListOf<AdminProfile>()
        for (i in 1..50) {
            val code = String.format("ADMIN%03d", i)
            val isSuper = (i == 1)
            adminList.add(
                AdminProfile(
                    id = "admin-$i",
                    adminCode = code,
                    fullName = if (isSuper) "Chief Academic Officer" else "Faculty Proctor #$i",
                    email = "$code.staff@academic-portal.edu",
                    role = if (isSuper) "super_admin" else "admin",
                    status = "active",
                    mustChangePassword = false
                )
            )
        }
        dao.insertAdmins(adminList)

        // 3. Seed Study Files & Summaries
        val sampleDocs = listOf(
            Triple(
                "Data_Structures_Lecture_04.pdf",
                "Computer Science",
                "Covers binary search trees, AVL self-balancing trees, tree traversals, and amortized rotations."
            ),
            Triple(
                "Operating_Systems_Concurrency.pptx",
                "Operating Systems",
                "Explores thread synchronization, semaphores, mutexes, dining philosophers problem, and deadlock prevention."
            ),
            Triple(
                "Computer_Networks_Protocols.docx",
                "Computer Networks",
                "Details OSI 7-layer model, TCP 3-way handshake, sliding window flow control, and UDP streaming mechanics."
            ),
            Triple(
                "Database_Normalization_Guide.pdf",
                "Database Management",
                "Comprehensive tutorial on 1NF, 2NF, 3NF, BCNF, lossless join decomposition, and functional dependencies."
            )
        )

        sampleDocs.forEachIndexed { idx, (fName, subj, desc) ->
            val fId = "file-$idx"
            val fType = when {
                fName.endsWith(".pdf") -> "PDF"
                fName.endsWith(".pptx") -> "PPT"
                fName.endsWith(".docx") -> "DOC"
                else -> "TXT"
            }
            dao.insertFile(
                StudyFile(
                    id = fId,
                    userId = "student-1",
                    fileName = fName,
                    fileType = fType,
                    fileSizeKb = 320 + (idx * 140),
                    subject = subj,
                    description = desc,
                    textContent = desc
                )
            )

            val (sum, pts, cards) = AIEngine.analyzeDocument(fName, subj, desc)
            dao.insertSummary(
                FileSummary(
                    id = "sum-$idx",
                    fileId = fId,
                    userId = "student-1",
                    summaryText = sum,
                    keyPoints = pts,
                    flashcardsJson = cards.joinToString(";;") { "${it.question}::${it.answer}" }
                )
            )
        }

        // 4. Seed Quizzes & Questions
        val quizzes = listOf(
            Triple("Algorithms & Complexity Mastery", "Computer Science", "Hard"),
            Triple("Database Systems & SQL Core", "Database Systems", "Medium"),
            Triple("Operating Systems & Memory", "Systems", "Medium")
        )

        quizzes.forEachIndexed { qIdx, (title, subj, diff) ->
            val quizId = "quiz-$qIdx"
            dao.insertQuiz(
                Quiz(
                    id = quizId,
                    title = title,
                    description = "Sharpen your knowledge with 5 timed practice questions on $subj.",
                    subject = subj,
                    difficulty = diff,
                    questionCount = 5
                )
            )
            val generatedQ = AIEngine.generateQuiz(subj, 5).map { it.copy(quizId = quizId) }
            dao.insertQuizQuestions(generatedQ)
        }

        // 5. Seed Official Tests
        val test1 = OfficialTest(
            id = "test-1",
            title = "CS301: Advanced Data Structures Mid-Term",
            description = "Official proctored timed evaluation covering balanced search trees, graph algorithms, and heap implementations.",
            subject = "Computer Science",
            durationMinutes = 45,
            totalMarks = 50,
            passingScore = 35,
            createdByAdminCode = "ADMIN001"
        )
        val test2 = OfficialTest(
            id = "test-2",
            title = "SYS204: Operating Systems Process Certification",
            description = "Evaluates concurrency, synchronization primitives, page replacement algorithms, and CPU scheduling policies.",
            subject = "Operating Systems",
            durationMinutes = 30,
            totalMarks = 40,
            passingScore = 28,
            createdByAdminCode = "ADMIN002"
        )
        dao.insertTest(test1)
        dao.insertTest(test2)

        dao.insertTestQuestions(
            listOf(
                TestQuestion("tq-1", "test-1", 1, "What is the worst-case time complexity of inserting into a Red-Black Tree?", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 1, 15),
                TestQuestion("tq-2", "test-1", 2, "Which graph algorithm determines shortest paths from a single source with non-negative edge weights?", listOf("Bellman-Ford", "Dijkstra's Algorithm", "Floyd-Warshall", "Prim's Algorithm"), 1, 15),
                TestQuestion("tq-3", "test-1", 3, "In a min-heap with n elements, finding the minimum element takes:", listOf("O(1)", "O(log n)", "O(n)", "O(n²)"), 0, 20),
                TestQuestion("tq-4", "test-2", 1, "Which CPU scheduling algorithm can lead to indefinite starvation of low-priority processes?", listOf("Round Robin", "Priority Scheduling", "First-Come First-Served", "Shortest Job First"), 1, 20),
                TestQuestion("tq-5", "test-2", 2, "What hardware feature prevents a user application from crashing the operating system kernel?", listOf("Dual-Mode Operation (User/Kernel Mode)", "Virtual Memory Swap Space", "Direct Memory Access (DMA)", "L1 Data Cache"), 0, 20)
            )
        )

        // 6. Seed Hackathons
        val hack1 = Hackathon(
            id = "hack-1",
            title = "Campus AI Innovation Hackathon 2026",
            theme = "Generative AI for Student Learning & Productivity",
            description = "Build cutting-edge applications using multimodal AI, automated grading, or intelligent study assistants to empower millions of students.",
            rules = "Teams of 1-4 members. Projects must be open-source with a working demo video and repository link.",
            eligibility = "Undergraduate and graduate students globally",
            prizePool = "$7,500 Cash + Cloud Credits",
            createdByAdmin = "ADMIN001",
            participantCount = 48
        )
        val hack2 = Hackathon(
            id = "hack-2",
            title = "Next-Gen Cloud Architecture Challenge",
            theme = "Resilient Microservices & Real-time Systems",
            description = "Design fault-tolerant, low-latency microservices architectures capable of processing high-volume educational telemetry.",
            rules = "All code must include automated CI tests and Docker configurations.",
            eligibility = "All registered students",
            prizePool = "$4,000 in Grants",
            createdByAdmin = "ADMIN003",
            participantCount = 31
        )
        dao.insertHackathon(hack1)
        dao.insertHackathon(hack2)

        dao.insertHackathonParticipant(
            HackathonParticipant(
                id = "hp-1",
                hackathonId = "hack-1",
                userId = "student-1",
                userName = "Prasad K.",
                projectName = "AI-SA Neural Notebook",
                projectUrl = "https://github.com/aistudio/ai-sa-notebook",
                submissionStatus = "submitted",
                score = 94
            )
        )
        dao.insertHackathonParticipant(
            HackathonParticipant(
                id = "hp-2",
                hackathonId = "hack-1",
                userId = "student-3",
                userName = "Alex Rivera",
                projectName = "StudyFlow Synthesizer",
                projectUrl = "https://github.com/alex/studyflow",
                submissionStatus = "submitted",
                score = 91
            )
        )

        // 7. Seed Initial Conversation
        val convId = "conv-1"
        dao.insertConversation(
            ChatConversation(
                id = convId,
                userId = "student-1",
                title = "Binary Trees & Complexity Review",
                studyMode = "Standard"
            )
        )
        dao.insertMessage(
            ChatMessage(
                id = "m-1",
                conversationId = convId,
                sender = "user",
                message = "Can you explain the difference between AVL trees and Red-Black trees?"
            )
        )
        dao.insertMessage(
            ChatMessage(
                id = "m-2",
                conversationId = convId,
                sender = "ai",
                message = "Great question! Both are self-balancing binary search trees that guarantee **O(log n)** lookup, insertion, and deletion. \n\n" +
                        "• **AVL Trees**: Strictly balanced (balance factor difference ≤ 1). Lookups are faster because tree height is strictly minimal. Ideal for lookup-intensive databases.\n" +
                        "• **Red-Black Trees**: Loosely balanced (black-height rule). Fewer rotations are required during insertions and deletions. Ideal for write-heavy workloads (e.g., C++ `std::map`, Java `TreeMap`)."
            )
        )

        // 8. Seed Study Plan & Tasks
        dao.insertStudyPlan(
            StudyPlan(
                id = "plan-1",
                userId = "student-1",
                title = "Fall Semester Finals Preparation",
                subject = "Computer Science & Systems",
                targetExamDate = System.currentTimeMillis() + 86400000L * 14,
                dailyHours = 3.5
            )
        )
        val defaultTasks = listOf(
            StudyTask("task-1", "plan-1", "Revise Binary Search Tree rotations & balance proofs", "Practice 3 AVL balance factor exercises", System.currentTimeMillis() + 86400000L, "High", true, 45),
            StudyTask("task-2", "plan-1", "Complete 5-Question Quiz on SQL Normalization", "Review 3NF and BCNF differences", System.currentTimeMillis() + 86400000L, "Medium", true, 30),
            StudyTask("task-3", "plan-1", "Code Lab: Solve Two-Sum and QuickSort in Python", "Analyze asymptotic time and space complexity", System.currentTimeMillis() + 86400000L * 2, "High", false, 60),
            StudyTask("task-4", "plan-1", "Read OS Concurrency: Mutex vs Semaphores", "Summarize slides using AI Document Summarizer", System.currentTimeMillis() + 86400000L * 3, "Medium", false, 40)
        )
        defaultTasks.forEach { dao.insertTask(it) }

        // 9. Announcements & Notifications
        dao.insertAnnouncement(
            Announcement(
                id = "ann-1",
                title = "🚀 Campus AI Hackathon 2026 Registration Open",
                content = "Form your teams and submit your AI Study Assistant or productivity tools by November 15. Over $7,500 in prizes available!",
                category = "Hackathon"
            )
        )
        dao.insertAnnouncement(
            Announcement(
                id = "ann-2",
                title = "📅 Official Mid-Term Proctored Tests Announced",
                content = "CS301 and SYS204 tests will open on the exam portal this Friday. Please ensure all study materials are reviewed.",
                category = "Exam"
            )
        )

        dao.insertNotification(
            AppNotification(
                id = "notif-1",
                userId = "student-1",
                title = "Welcome to AI Study Assistant (AI-SA)",
                message = "Your intelligent learning workspace is ready. Try uploading a document or asking the AI chatbot a study question!",
                type = "info"
            )
        )
        dao.insertNotification(
            AppNotification(
                id = "notif-2",
                userId = "student-1",
                title = "New Hackathon Available",
                message = "Registration for Campus AI Innovation Hackathon 2026 is now open.",
                type = "hackathon"
            )
        )
    }
}
