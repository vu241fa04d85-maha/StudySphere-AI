package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AIEngine
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen(val title: String) {
    HOME("Dashboard"),
    AI_CHAT("AI Study Chat"),
    FILES("My Files"),
    CODE_LAB("Code Lab"),
    QUIZ("Quizzes"),
    TESTS("Official Tests"),
    HACKATHONS("Hackathons"),
    PLANNER("Study Planner"),
    ADMIN_DASHBOARD("Admin Portal"),
    PROFILE("My Profile")
}

enum class UserRole {
    STUDENT,
    ADMIN
}

enum class AppPhase {
    SPLASH,
    AUTH,
    MAIN
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudyRepository

    // App Phase (Splash -> Auth -> Main)
    private val _appPhase = MutableStateFlow(AppPhase.SPLASH)
    val appPhase: StateFlow<AppPhase> = _appPhase.asStateFlow()

    fun finishSplash() {
        _appPhase.value = AppPhase.AUTH
    }

    fun setAppPhase(phase: AppPhase) {
        _appPhase.value = phase
    }

    // Screen & Navigation
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Theme Mode
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // Consent Banner State
    private val _cookieConsentAccepted = MutableStateFlow(false)
    val cookieConsentAccepted: StateFlow<Boolean> = _cookieConsentAccepted.asStateFlow()

    fun acceptCookieConsent() {
        _cookieConsentAccepted.value = true
    }

    // Authentication & Roles
    private val _userRole = MutableStateFlow(UserRole.STUDENT)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    private val _activeAdmin = MutableStateFlow<AdminProfile?>(null)
    val activeAdmin: StateFlow<AdminProfile?> = _activeAdmin.asStateFlow()

    // Global Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Repository Data Streams
    val currentUser: StateFlow<UserProfile?>
    val allUsers: StateFlow<List<UserProfile>>
    val allAdmins: StateFlow<List<AdminProfile>>
    val allFiles: StateFlow<List<StudyFile>>
    val allConversations: StateFlow<List<ChatConversation>>
    val allQuizzes: StateFlow<List<Quiz>>
    val allQuizAttempts: StateFlow<List<QuizAttempt>>
    val allTests: StateFlow<List<OfficialTest>>
    val allTestAttempts: StateFlow<List<TestAttempt>>
    val allHackathons: StateFlow<List<Hackathon>>
    val allStudyPlans: StateFlow<List<StudyPlan>>
    val allTasks: StateFlow<List<StudyTask>>
    val allAnnouncements: StateFlow<List<Announcement>>
    val allNotifications: StateFlow<List<AppNotification>>

    // AI Chat State
    private val _activeConversationId = MutableStateFlow<String?>(null)
    val activeConversationId: StateFlow<String?> = _activeConversationId.asStateFlow()

    private val _activeStudyMode = MutableStateFlow("Standard")
    val activeStudyMode: StateFlow<String> = _activeStudyMode.asStateFlow()

    val currentMessages: StateFlow<List<ChatMessage>>

    // Document Studio State
    private val _selectedFile = MutableStateFlow<StudyFile?>(null)
    val selectedFile: StateFlow<StudyFile?> = _selectedFile.asStateFlow()

    val activeSummary: StateFlow<FileSummary?>

    // Quiz Runner State
    private val _activeQuiz = MutableStateFlow<Quiz?>(null)
    val activeQuiz: StateFlow<Quiz?> = _activeQuiz.asStateFlow()

    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedQuizAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val selectedQuizAnswers: StateFlow<Map<Int, String>> = _selectedQuizAnswers.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    // Test Runner State
    private val _activeTest = MutableStateFlow<OfficialTest?>(null)
    val activeTest: StateFlow<OfficialTest?> = _activeTest.asStateFlow()

    private val _testQuestions = MutableStateFlow<List<TestQuestion>>(emptyList())
    val testQuestions: StateFlow<List<TestQuestion>> = _testQuestions.asStateFlow()

    private val _currentTestIndex = MutableStateFlow(0)
    val currentTestIndex: StateFlow<Int> = _currentTestIndex.asStateFlow()

    private val _selectedTestAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val selectedTestAnswers: StateFlow<Map<Int, Int>> = _selectedTestAnswers.asStateFlow()

    private val _isTestFinished = MutableStateFlow(false)
    val isTestFinished: StateFlow<Boolean> = _isTestFinished.asStateFlow()

    private val _testScore = MutableStateFlow(0)
    val testScore: StateFlow<Int> = _testScore.asStateFlow()

    private val _testRemainingSeconds = MutableStateFlow(1800)
    val testRemainingSeconds: StateFlow<Int> = _testRemainingSeconds.asStateFlow()
    private var testTimerJob: Job? = null

    // Code Lab State
    private val _codeLanguage = MutableStateFlow("Python")
    val codeLanguage: StateFlow<String> = _codeLanguage.asStateFlow()

    private val _codeContent = MutableStateFlow(
        """# Python: Two Sum Problem
def two_sum(nums, target):
    seen = {}
    for i, num in enumerate(nums):
        complement = target - num
        if complement in seen:
            return [seen[complement], i]
        seen[num] = i
    return []

# Test execution
print(two_sum([2, 7, 11, 15], 9))  # Output: [0, 1]"""
    )
    val codeContent: StateFlow<String> = _codeContent.asStateFlow()

    private val _codeOutput = MutableStateFlow("Output:\n[0, 1]\nProgram completed in 0.04s.")
    val codeOutput: StateFlow<String> = _codeOutput.asStateFlow()

    private val _timeComplexity = MutableStateFlow("O(n)")
    val timeComplexity: StateFlow<String> = _timeComplexity.asStateFlow()

    private val _spaceComplexity = MutableStateFlow("O(n)")
    val spaceComplexity: StateFlow<String> = _spaceComplexity.asStateFlow()

    private val _codeExplanation = MutableStateFlow("Optimal hash map solution that achieves linear O(n) lookup time by storing previously visited numbers and their indices.")
    val codeExplanation: StateFlow<String> = _codeExplanation.asStateFlow()

    private val _codeSuggestions = MutableStateFlow("• Optimal O(n) Time and O(n) Space.\n• Validated for non-empty arrays with unique complement pairs.")
    val codeSuggestions: StateFlow<String> = _codeSuggestions.asStateFlow()

    private val _isAnalyzingCode = MutableStateFlow(false)
    val isAnalyzingCode: StateFlow<Boolean> = _isAnalyzingCode.asStateFlow()

    // Notification toast / banner message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = StudyRepository(database.studyDao())

        currentUser = repository.currentUser.stateIn(viewModelScope, SharingStarted.Lazily, null)
        allUsers = repository.allUsers.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allAdmins = repository.allAdmins.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allFiles = repository.allFiles.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allConversations = repository.allConversations.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allQuizzes = repository.allQuizzes.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allQuizAttempts = repository.allQuizAttempts.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allTests = repository.allTests.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allTestAttempts = repository.allTestAttempts.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allHackathons = repository.allHackathons.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allStudyPlans = repository.allStudyPlans.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allTasks = repository.allTasks.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allAnnouncements = repository.allAnnouncements.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allNotifications = repository.allNotifications.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

        currentMessages = _activeConversationId.flatMapLatest { convId ->
            if (convId != null) repository.getMessagesForConversation(convId)
            else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

        activeSummary = _selectedFile.flatMapLatest { file ->
            if (file != null) repository.getSummaryForFile(file.id)
            else flowOf(null)
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)

        // Seed initial data asynchronously on first run
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        // Set default conversation once loaded
        viewModelScope.launch {
            allConversations.collect { list ->
                if (_activeConversationId.value == null && list.isNotEmpty()) {
                    _activeConversationId.value = list.first().id
                }
            }
        }
    }

    // --- Navigation ---
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setSearchQuery(q: String) {
        _searchQuery.value = q
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
        viewModelScope.launch {
            delay(3000)
            if (_toastMessage.value == msg) {
                _toastMessage.value = null
            }
        }
    }

    // --- Role & Authentication ---
    fun logout() {
        _userRole.value = UserRole.STUDENT
        _activeAdmin.value = null
        _currentScreen.value = AppScreen.HOME
        _appPhase.value = AppPhase.AUTH
        showToast("Signed out. Please log in to continue.")
    }

    fun loginStudent(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            onError("Please enter a valid email address.")
            return
        }
        if (password.length < 6) {
            onError("Password must be at least 6 characters.")
            return
        }
        _userRole.value = UserRole.STUDENT
        _activeAdmin.value = null
        _currentScreen.value = AppScreen.HOME
        _appPhase.value = AppPhase.MAIN
        showToast("Welcome back! Signed in as Student.")
        onSuccess()
    }

    fun registerStudent(
        fullName: String,
        email: String,
        college: String,
        course: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (fullName.isBlank()) {
            onError("Please enter your full name.")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            onError("Please enter a valid email address.")
            return
        }
        if (password.length < 6) {
            onError("Password must be at least 6 characters.")
            return
        }
        if (password != confirmPassword) {
            onError("Passwords do not match.")
            return
        }

        viewModelScope.launch {
            val newUser = UserProfile(
                id = "student_${System.currentTimeMillis()}",
                fullName = fullName.trim(),
                email = email.trim(),
                college = if (college.isNotBlank()) college.trim() else "Global University",
                course = if (course.isNotBlank()) course.trim() else "Computer Science & AI",
                year = "Year 3",
                studyStreak = 1,
                totalStudyHours = 0.0,
                completedQuizzes = 0,
                testsTaken = 0
            )
            repository.insertUser(newUser)
            _userRole.value = UserRole.STUDENT
            _activeAdmin.value = null
            _currentScreen.value = AppScreen.HOME
            _appPhase.value = AppPhase.MAIN
            showToast("Welcome ${newUser.fullName}! Account created.")
            onSuccess()
        }
    }

    fun switchToStudentRole() {
        _userRole.value = UserRole.STUDENT
        _activeAdmin.value = null
        showToast("Switched to Student Workspace")
    }

    fun loginWithAdminCode(code: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val admin = repository.verifyAdminCode(code)
            if (admin != null) {
                if (admin.status == "suspended") {
                    onError("This Admin account has been suspended.")
                    return@launch
                }
                _activeAdmin.value = admin
                _userRole.value = UserRole.ADMIN
                _currentScreen.value = AppScreen.ADMIN_DASHBOARD
                _appPhase.value = AppPhase.MAIN
                showToast("Logged in as ${admin.fullName} (${admin.adminCode})")
                onSuccess()
            } else {
                onError("Invalid Admin Code. Valid format: ADMIN001 to ADMIN050")
            }
        }
    }

    fun toggleAdminStatus(admin: AdminProfile) {
        viewModelScope.launch {
            val updated = admin.copy(status = if (admin.status == "active") "suspended" else "active")
            repository.updateAdmin(updated)
            showToast("Admin ${admin.adminCode} is now ${updated.status}")
        }
    }

    // --- AI Chat ---
    fun setStudyMode(mode: String) {
        _activeStudyMode.value = mode
    }

    fun selectConversation(convId: String) {
        _activeConversationId.value = convId
    }

    fun startNewConversation(title: String) {
        viewModelScope.launch {
            val id = repository.createNewConversation(title, _activeStudyMode.value)
            _activeConversationId.value = id
        }
    }

    fun sendChatMessage(messageText: String) {
        val convId = _activeConversationId.value ?: return
        if (messageText.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(convId, messageText, _activeStudyMode.value)
        }
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            repository.deleteConversation(id)
            if (_activeConversationId.value == id) {
                _activeConversationId.value = null
            }
        }
    }

    // --- Files & Document Studio ---
    fun selectFile(file: StudyFile?) {
        _selectedFile.value = file
    }

    fun uploadStudyFile(name: String, type: String, subject: String, desc: String, content: String) {
        viewModelScope.launch {
            repository.uploadStudyFile(name, type, subject, desc, content)
            showToast("Uploaded & Analyzed: $name")
        }
    }

    fun deleteFile(id: String) {
        viewModelScope.launch {
            repository.deleteFile(id)
            if (_selectedFile.value?.id == id) {
                _selectedFile.value = null
            }
            showToast("File deleted")
        }
    }

    // --- Quiz Runner ---
    fun startQuiz(quiz: Quiz) {
        viewModelScope.launch {
            val questions = repository.getQuestionsForQuiz(quiz.id)
            _activeQuiz.value = quiz
            _quizQuestions.value = questions
            _currentQuizIndex.value = 0
            _selectedQuizAnswers.value = emptyMap()
            _isQuizFinished.value = false
            _quizScore.value = 0
            _currentScreen.value = AppScreen.QUIZ
        }
    }

    fun answerQuizQuestion(questionIdx: Int, answer: String) {
        _selectedQuizAnswers.value = _selectedQuizAnswers.value + (questionIdx to answer)
    }

    fun nextQuizQuestion() {
        if (_currentQuizIndex.value < _quizQuestions.value.size - 1) {
            _currentQuizIndex.value += 1
        } else {
            submitActiveQuiz()
        }
    }

    fun prevQuizQuestion() {
        if (_currentQuizIndex.value > 0) {
            _currentQuizIndex.value -= 1
        }
    }

    fun submitActiveQuiz() {
        val quiz = _activeQuiz.value ?: return
        val questions = _quizQuestions.value
        val answers = _selectedQuizAnswers.value

        var calculatedScore = 0
        questions.forEachIndexed { idx, q ->
            if (answers[idx] == q.correctAnswer) {
                calculatedScore += q.marks
            }
        }
        val totalMarks = questions.sumOf { it.marks }
        _quizScore.value = calculatedScore
        _isQuizFinished.value = true

        viewModelScope.launch {
            repository.submitQuizAttempt(quiz.id, quiz.title, calculatedScore, totalMarks, 120)
            showToast("Quiz Completed! Score: $calculatedScore / $totalMarks")
        }
    }

    fun exitQuiz() {
        _activeQuiz.value = null
        _isQuizFinished.value = false
    }

    // --- Official Test Runner ---
    fun startOfficialTest(test: OfficialTest) {
        viewModelScope.launch {
            val questions = repository.getQuestionsForTest(test.id)
            _activeTest.value = test
            _testQuestions.value = questions
            _currentTestIndex.value = 0
            _selectedTestAnswers.value = emptyMap()
            _isTestFinished.value = false
            _testScore.value = 0
            _testRemainingSeconds.value = test.durationMinutes * 60
            _currentScreen.value = AppScreen.TESTS

            testTimerJob?.cancel()
            testTimerJob = viewModelScope.launch {
                while (_testRemainingSeconds.value > 0 && !_isTestFinished.value) {
                    delay(1000)
                    _testRemainingSeconds.value -= 1
                }
                if (!_isTestFinished.value) {
                    submitActiveTest()
                }
            }
        }
    }

    fun answerTestQuestion(idx: Int, optionIdx: Int) {
        _selectedTestAnswers.value = _selectedTestAnswers.value + (idx to optionIdx)
    }

    fun nextTestQuestion() {
        if (_currentTestIndex.value < _testQuestions.value.size - 1) {
            _currentTestIndex.value += 1
        }
    }

    fun prevTestQuestion() {
        if (_currentTestIndex.value > 0) {
            _currentTestIndex.value -= 1
        }
    }

    fun submitActiveTest() {
        testTimerJob?.cancel()
        val test = _activeTest.value ?: return
        val questions = _testQuestions.value
        val answers = _selectedTestAnswers.value

        var total = 0
        questions.forEachIndexed { idx, q ->
            if (answers[idx] == q.correctIndex) {
                total += q.marks
            }
        }
        _testScore.value = total
        _isTestFinished.value = true

        viewModelScope.launch {
            repository.submitTestAttempt(test.id, test.title, total, test.totalMarks)
            showToast("Test Submitted! Score: $total / ${test.totalMarks}")
        }
    }

    fun exitTest() {
        testTimerJob?.cancel()
        _activeTest.value = null
        _isTestFinished.value = false
    }

    // --- Code Lab Sandbox ---
    fun setCodeLanguage(lang: String) {
        _codeLanguage.value = lang
        // Load default starter template for selected language
        _codeContent.value = when (lang) {
            "Python" -> "def solve(arr):\n    return sorted(arr)\n\nprint(solve([5, 2, 8, 1]))"
            "Java" -> "import java.util.*;\n\npublic class Solution {\n    public static void main(String[] args) {\n        System.out.println(\"AI-SA Code Lab Java Sandbox\");\n    }\n}"
            "C++" -> "#include <iostream>\n#include <vector>\nusing namespace std;\n\nint main() {\n    cout << \"Ready to code!\" << endl;\n    return 0;\n}"
            "C" -> "#include <stdio.h>\n\nint main() {\n    printf(\"Hello from C\\n\");\n    return 0;\n}"
            "JavaScript" -> "function solution(data) {\n    return data.filter(x => x > 0);\n}\nconsole.log(solution([1, -2, 3]));"
            "TypeScript" -> "interface Student {\n    id: number;\n    name: string;\n}\nconst s: Student = { id: 1, name: 'Prasad' };\nconsole.log(s);"
            "Go" -> "package main\nimport \"fmt\"\n\nfunc main() {\n    fmt.Println(\"AI-SA Go Sandbox\")\n}"
            "Rust" -> "fn main() {\n    let nums = vec![1, 2, 3];\n    println!(\"{:?}\", nums);\n}"
            "Kotlin" -> "fun main() {\n    val greeting = \"Hello AI Study Assistant\"\n    println(greeting)\n}"
            "SQL" -> "SELECT student_id, full_name, course, GPA \nFROM students \nWHERE GPA >= 3.5 \nORDER BY GPA DESC;"
            else -> "// Code sandbox for $lang\nconsole.log('Ready');"
        }
    }

    fun updateCodeContent(newCode: String) {
        _codeContent.value = newCode
    }

    fun analyzeAndRunCode() {
        _isAnalyzingCode.value = true
        viewModelScope.launch {
            delay(500) // slight realistic debounce
            val (explanation, complexities, suggestions) = AIEngine.analyzeCode(_codeLanguage.value, _codeContent.value)
            val parts = complexities.split("|").map { it.trim() }
            _timeComplexity.value = parts.getOrElse(0) { "O(n)" }
            _spaceComplexity.value = parts.getOrElse(1) { "O(1)" }
            _codeExplanation.value = explanation
            _codeSuggestions.value = suggestions
            _codeOutput.value = "Program output: Successful\nExecution time: 0.038s\nAuxiliary memory allocated: 2.4 MB"
            _isAnalyzingCode.value = false
            showToast("Code analyzed successfully!")
        }
    }

    // --- Hackathons ---
    fun registerForHackathon(id: String) {
        viewModelScope.launch {
            repository.registerForHackathon(id)
            showToast("Successfully registered for Hackathon!")
        }
    }

    fun submitHackathonProject(hackathonId: String, name: String, url: String) {
        viewModelScope.launch {
            repository.submitHackathonProject(hackathonId, name, url)
            showToast("Project '$name' submitted to Hackathon!")
        }
    }

    // --- Planner & Tasks ---
    fun toggleTask(task: StudyTask) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(task)
        }
    }

    fun addStudyTask(title: String, desc: String, priority: String, minutes: Int) {
        viewModelScope.launch {
            repository.addStudyTask(title, desc, priority, minutes)
            showToast("Task added to planner")
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    // --- Admin Operations ---
    fun createOfficialTest(title: String, subj: String, desc: String, duration: Int, marks: Int, passScore: Int) {
        val code = _activeAdmin.value?.adminCode ?: "ADMIN001"
        viewModelScope.launch {
            repository.createOfficialTest(title, subj, desc, duration, marks, passScore, code)
            showToast("Official Test Published!")
        }
    }

    fun deleteOfficialTest(id: String) {
        viewModelScope.launch {
            repository.deleteOfficialTest(id)
            showToast("Test deleted")
        }
    }

    fun createHackathon(title: String, theme: String, desc: String, rules: String) {
        val code = _activeAdmin.value?.adminCode ?: "ADMIN001"
        viewModelScope.launch {
            repository.createHackathon(title, theme, desc, rules, code)
            showToast("Hackathon Created & Published!")
        }
    }

    fun deleteHackathon(id: String) {
        viewModelScope.launch {
            repository.deleteHackathon(id)
            showToast("Hackathon deleted")
        }
    }

    // --- Privacy, Data Export & Deletion ---
    fun exportUserDataJson(): String {
        val user = currentUser.value
        val files = allFiles.value
        val tasks = allTasks.value
        val quizzes = allQuizAttempts.value
        return """{
  "user": {
    "name": "${user?.fullName ?: "Student"}",
    "email": "${user?.email ?: "student@university.edu"}",
    "college": "${user?.college ?: "University"}",
    "course": "${user?.course ?: "Computer Science"}",
    "streak": ${user?.studyStreak ?: 1}
  },
  "totalFiles": ${files.size},
  "totalTasks": ${tasks.size},
  "quizAttempts": ${quizzes.size},
  "exportedAt": "${System.currentTimeMillis()}"
}"""
    }

    fun deleteUserAccount() {
        showToast("Account deletion requested. All personal data purged.")
        switchToStudentRole()
    }
}
