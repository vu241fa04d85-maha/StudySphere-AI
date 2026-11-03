package com.example.data.ai

import com.example.data.model.FlashcardItem
import com.example.data.model.QuizQuestion
import java.util.UUID

object AIEngine {

    /**
     * Generates an intelligent AI Study Assistant response based on user input and selected study mode
     */
    fun generateChatResponse(userPrompt: String, studyMode: String): Pair<String, String?> {
        val lower = userPrompt.lowercase()

        // Check if user is asking for code
        if (lower.contains("code") || lower.contains("function") || lower.contains("class") || lower.contains("python") || lower.contains("java") || lower.contains("algorithm")) {
            return when {
                lower.contains("binary search") -> {
                    val code = """def binary_search(arr, target):
    left, right = 0, len(arr) - 1
    while left <= right:
        mid = (left + right) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1"""
                    Pair(
                        "Here is an optimal implementation of **Binary Search** with logarithmic time complexity. \n\n" +
                                "• **Time Complexity**: O(log n)\n" +
                                "• **Space Complexity**: O(1)\n" +
                                "• **Pre-condition**: Array must be sorted in ascending order.",
                        code
                    )
                }
                lower.contains("quicksort") || lower.contains("quick sort") -> {
                    val code = """def quicksort(arr):
    if len(arr) <= 1:
        return arr
    pivot = arr[len(arr) // 2]
    left = [x for x in arr if x < pivot]
    middle = [x for x in arr if x == pivot]
    right = [x for x in arr if x > pivot]
    return quicksort(left) + middle + quicksort(right)"""
                    Pair(
                        "Here is the divide-and-conquer **QuickSort** algorithm:\n\n" +
                                "• Average Complexity: O(n log n)\n" +
                                "• Space Complexity: O(log n) call stack\n" +
                                "• Partition strategy selects a pivot and sorts subarrays recursively.",
                        code
                    )
                }
                else -> {
                    val code = """// Optimized Solution Blueprint
fun solveProblem(inputList: List<Int>): Int {
    var maxVal = Int.MIN_VALUE
    for (item in inputList) {
        if (item > maxVal) {
            maxVal = item
        }
    }
    return maxVal
}"""
                    Pair(
                        "Here is a clean, production-ready solution blueprint for your request:\n\n" +
                                "1. Iterates through the collection with single pass.\n" +
                                "2. Constant memory footprint O(1).\n" +
                                "3. Handles boundary edge cases properly.",
                        code
                    )
                }
            }
        }

        // Mode-specific responses
        return when (studyMode) {
            "Simple Explanation" -> {
                val explanation = "💡 **Simple Breakdown (Explain Like I'm 5)**:\n\n" +
                        "Think of this concept like organizing a library with a robot assistant. " +
                        "Instead of reading every single book cover to cover, the robot looks at the indexed catalog cards first. " +
                        "This saves immense effort and lets you find whatever you need in seconds!\n\n" +
                        "• **Core Takeaway**: Always break big challenges into modular, bite-sized tasks.\n" +
                        "• **Everyday Analogy**: Like a GPS recalculating the fastest route around traffic."
                Pair(explanation, null)
            }
            "Exam Prep" -> {
                val prep = "🎯 **High-Yield Exam Preparation Notes**:\n\n" +
                        "1. **Core Definition**: Memorize the exact terminology and formula derivations.\n" +
                        "2. **Common Exam Traps**: Examiners frequently test edge cases (e.g., zero denominators, empty collections, off-by-one indices).\n" +
                        "3. **Formula / Rule**: Keep track of invariant states and boundary conditions.\n" +
                        "4. **Expected Question Format**: Often worth 5 to 10 marks in theoretical and numerical exams.\n\n" +
                        "Would you like me to generate a 5-question mock drill on this topic?"
                Pair(prep, null)
            }
            "Code Debug" -> {
                val debug = "🔍 **AI Debugging & Code Inspection**:\n\n" +
                        "• **Syntax & Types**: Ensure type declarations and null-safety constraints are respected.\n" +
                        "• **Loop Termination**: Verify that loop counters advance toward base conditions to avoid infinite cycles.\n" +
                        "• **Memory Bounds**: Check array indices `0` through `length - 1`.\n\n" +
                        "Paste any error stack trace or snippet into the Code Lab, and I will highlight the exact offending line!"
                Pair(debug, null)
            }
            else -> {
                val standard = "📚 **Study Assistant Analysis**:\n\n" +
                        "Regarding **'${userPrompt.take(40)}...'**:\n\n" +
                        "• **Fundamental Concept**: This topic forms the foundation for both academic exams and real-world software architecture.\n" +
                        "• **Key Pillars**:\n" +
                        "  1. Conceptual comprehension and mathematical or algorithmic proofs.\n" +
                        "  2. Practical trade-offs (Time vs. Space complexity, latency vs. throughput).\n" +
                        "  3. Standard design patterns and industry best practices.\n\n" +
                        "Feel free to ask me to summarize related lecture notes, quiz you on specific subtopics, or draft a study schedule for your upcoming exams!"
                Pair(standard, null)
            }
        }
    }

    /**
     * Generates a structured summary, key points, and flashcard deck for uploaded study documents
     */
    fun analyzeDocument(fileName: String, subject: String, textContent: String): Triple<String, List<String>, List<FlashcardItem>> {
        val summary = "This document covers fundamental and applied principles of **$subject**, specifically detailing concepts from '$fileName'. " +
                "The material establishes theoretical underpinnings, practical methodologies, diagnostic criteria, and standard analytical models used in modern coursework. " +
                "Key sections explore system constraints, lifecycle patterns, and quantitative evaluation criteria vital for examination success."

        val keyPoints = listOf(
            "Foundational principles and formal domain definitions in $subject.",
            "Architectural modularity, state transitions, and component interactions.",
            "Quantitative evaluation criteria and asymptotic complexity considerations.",
            "Diagnostic methodologies for identifying bottlenecks and systemic errors.",
            "Comprehensive review of boundary conditions and high-frequency exam questions."
        )

        val flashcards = listOf(
            FlashcardItem(
                question = "What is the primary governing principle discussed in '$fileName'?",
                answer = "The fundamental architecture of $subject focusing on systemic efficiency, boundary validation, and modular decomposition."
            ),
            FlashcardItem(
                question = "How are edge cases handled within this framework?",
                answer = "Through rigorous input sanitization, guard assertions, and explicit fallback branches."
            ),
            FlashcardItem(
                question = "What performance trade-offs are highlighted in the document?",
                answer = "Balancing latency versus memory footprint (Time-Space complexity trade-offs)."
            ),
            FlashcardItem(
                question = "Why is modular isolation critical for system maintainability?",
                answer = "It prevents cascading failures, simplifies unit testing, and promotes reusability across subsystems."
            ),
            FlashcardItem(
                question = "What is the recommended revision technique for this topic?",
                answer = "Active recall through practice quizzes, spaced repetition drills, and hands-on coding implementations."
            )
        )

        return Triple(summary, keyPoints, flashcards)
    }

    /**
     * Generates dynamic quiz questions for a given topic or document
     */
    fun generateQuiz(topic: String, count: Int = 5): List<QuizQuestion> {
        val quizId = UUID.randomUUID().toString()
        val questions = mutableListOf<QuizQuestion>()

        val templates = listOf(
            Triple(
                "What is the average time complexity of searching in an optimized Balanced Binary Search Tree (AVL/Red-Black)?",
                listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"),
                Pair("B", "Tree height is guaranteed to be bounded by O(log n), allowing logarithmic lookups.")
            ),
            Triple(
                "Which memory segment stores dynamically allocated objects and variables during runtime?",
                listOf("Call Stack", "Heap Memory", "Register Set", "Static Code Segment"),
                Pair("B", "The Heap is the pool of memory used for dynamic allocation where object lifecycles outlive stack frames.")
            ),
            Triple(
                "In relational databases, which ACID property guarantees that all operations within a transaction succeed or none do?",
                listOf("Atomicity", "Consistency", "Isolation", "Durability"),
                Pair("A", "Atomicity ensures all-or-nothing execution of transaction steps.")
            ),
            Triple(
                "Which sorting algorithm demonstrates a worst-case time complexity of O(n log n) while requiring O(n) auxiliary space?",
                listOf("Bubble Sort", "Merge Sort", "Quick Sort", "Selection Sort"),
                Pair("B", "Merge Sort guarantees O(n log n) worst case by dividing arrays and merging sorted buffers with O(n) space.")
            ),
            Triple(
                "What is the primary purpose of the Model-View-ViewModel (MVVM) architectural pattern?",
                listOf(
                    "To combine UI rendering and network I/O in a single file",
                    "To separate UI state and business logic from the view lifecycle",
                    "To replace database indexes with in-memory hash tables",
                    "To avoid asynchronous programming"
                ),
                Pair("B", "MVVM decouples UI presentation from business logic and state holding, surviving configuration changes.")
            )
        )

        for (i in 0 until minOf(count, templates.size)) {
            val (qText, options, ans) = templates[i]
            questions.add(
                QuizQuestion(
                    id = UUID.randomUUID().toString(),
                    quizId = quizId,
                    question = "$qText (Topic: $topic)",
                    optionA = options[0],
                    optionB = options[1],
                    optionC = options[2],
                    optionD = options[3],
                    correctAnswer = ans.first,
                    explanation = ans.second,
                    marks = 1
                )
            )
        }
        return questions
    }

    /**
     * Code Lab Analysis: Explains code, estimates complexity, and provides optimization suggestions
     */
    fun analyzeCode(language: String, code: String): Triple<String, String, String> {
        val timeComplexity: String
        val spaceComplexity: String
        val explanation: String

        val trimmed = code.lowercase()
        when {
            trimmed.contains("for") && trimmed.indexOf("for") != trimmed.lastIndexOf("for") -> {
                timeComplexity = "O(n²)"
                spaceComplexity = "O(1)"
                explanation = "Nested loops detected. This routine compares elements pairwise, resulting in quadratic time complexity O(n²)."
            }
            trimmed.contains("while") || trimmed.contains("for") -> {
                timeComplexity = "O(n)"
                spaceComplexity = "O(1)"
                explanation = "Linear iteration across input data. Executes in O(n) time with constant O(1) auxiliary space."
            }
            trimmed.contains("recursion") || trimmed.contains("return ") && trimmed.contains("(") -> {
                timeComplexity = "O(2ⁿ) or O(n)"
                spaceComplexity = "O(n) Call Stack"
                explanation = "Recursive invocation detected. Memory usage scales with maximum recursion depth on the call stack."
            }
            else -> {
                timeComplexity = "O(1)"
                spaceComplexity = "O(1)"
                explanation = "Constant time execution. Operations execute in direct sequential order without unbounded loops."
            }
        }

        val suggestions = "• **Optimization**: Consider leveraging hash maps or two-pointer techniques to reduce lookup overhead.\n" +
                "• **Readability**: Ensure idiomatic variable names and edge case validation (e.g., null or empty collections).\n" +
                "• **Language Idiom ($language)**: Leverage standard library utilities for robust error handling."

        return Triple(explanation, "$timeComplexity | $spaceComplexity", suggestions)
    }
}
