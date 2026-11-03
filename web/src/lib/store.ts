import { SEEDED_ADMINS, type SeededAdmin } from './adminSeeds';

export interface UserSession {
  id: string;
  name: string;
  email: string;
  role: 'student' | 'admin';
  adminCode?: string;
  college?: string;
  course?: string;
  year?: string;
  studyStreak: number;
  totalStudyMinutes: number;
}

export interface ChatMessage {
  id: string;
  sender: 'user' | 'assistant';
  content: string;
  timestamp: string;
  mode?: 'concept' | 'math' | 'flashcards' | 'code';
}

export interface StudyFile {
  id: string;
  name: string;
  type: string;
  size: string;
  subject: string;
  summary: string;
  flashcards: Array<{ question: string; answer: string }>;
  uploadedAt: string;
}

export interface QuizItem {
  id: string;
  title: string;
  subject: string;
  difficulty: 'easy' | 'medium' | 'hard';
  questions: Array<{
    id: string;
    text: string;
    options: string[];
    correctIndex: number;
    explanation: string;
  }>;
}

export interface TimedTest {
  id: string;
  title: string;
  subject: string;
  durationMinutes: number;
  totalMarks: number;
  questions: Array<{
    id: string;
    text: string;
    options: string[];
    correctIndex: number;
    marks: number;
  }>;
}

export interface Hackathon {
  id: string;
  title: string;
  theme: string;
  prizePool: string;
  deadline: string;
  registered: boolean;
  participants: number;
  description: string;
  tags: string[];
}

export interface StudyTask {
  id: string;
  title: string;
  subject: string;
  dueDate: string;
  priority: 'High' | 'Medium' | 'Low';
  completed: boolean;
  estimatedMinutes: number;
}

export interface Announcement {
  id: string;
  title: string;
  content: string;
  category: 'Exam' | 'Hackathon' | 'Maintenance' | 'Academic';
  isUrgent: boolean;
  createdAt: string;
}

// Initial seed data
export const INITIAL_FILES: StudyFile[] = [
  {
    id: 'f1',
    name: 'Advanced_Data_Structures_Lecture_4.pdf',
    type: 'PDF',
    size: '2.4 MB',
    subject: 'Computer Science',
    summary: 'Covers Red-Black Trees, AVL tree rebalancing invariants, and B-Tree index structures for low-latency databases.',
    flashcards: [
      { question: 'What is the maximum height of a Red-Black tree with n nodes?', answer: '2 * log2(n + 1)' },
      { question: 'Name the 4 rebalancing rotations in AVL trees.', answer: 'Left-Left, Right-Right, Left-Right, and Right-Left.' },
      { question: 'Why are B-Trees preferred for disk storage?', answer: 'Wide branching factor reduces the number of disk seek operations.' }
    ],
    uploadedAt: 'Today, 10:14 AM'
  },
  {
    id: 'f2',
    name: 'Discrete_Math_Graph_Theory_Notes.docx',
    type: 'DOCX',
    size: '1.1 MB',
    subject: 'Mathematics',
    summary: 'Comprehensive notes on Dijkstra shortest path, Bellman-Ford negative cycle detection, and Hamiltonian paths.',
    flashcards: [
      { question: 'What is the time complexity of Dijkstra with a Fibonacci Heap?', answer: 'O(E + V log V)' },
      { question: 'Can Dijkstra handle negative edge weights?', answer: 'No, it can produce incorrect results or loop indefinitely.' }
    ],
    uploadedAt: 'Yesterday'
  }
];

export const INITIAL_TASKS: StudyTask[] = [
  { id: 't1', title: 'Review Red-Black tree rotations', subject: 'Data Structures', dueDate: 'Tomorrow, 5:00 PM', priority: 'High', completed: false, estimatedMinutes: 45 },
  { id: 't2', title: 'Complete Calculus III practice set 8', subject: 'Mathematics', dueDate: 'Oct 30, 2:00 PM', priority: 'High', completed: false, estimatedMinutes: 60 },
  { id: 't3', title: 'Debug Python async task queue in Code Lab', subject: 'Operating Systems', dueDate: 'Nov 02, 11:59 PM', priority: 'Medium', completed: true, estimatedMinutes: 30 },
  { id: 't4', title: 'Review Database Normalization (3NF & BCNF)', subject: 'Databases', dueDate: 'Nov 04, 6:00 PM', priority: 'Low', completed: false, estimatedMinutes: 40 }
];

export const INITIAL_ANNOUNCEMENTS: Announcement[] = [
  { id: 'a1', title: 'National AI Hackathon 2026 Registration Open', content: 'Submit your team project before Nov 15th to compete for the $15,000 prize pool.', category: 'Hackathon', isUrgent: true, createdAt: '2 hours ago' },
  { id: 'a2', title: 'Midterm Examination Schedule Released', content: 'Computer Science and Mathematics midterms commence on November 10th.', category: 'Exam', isUrgent: false, createdAt: '1 day ago' },
  { id: 'a3', title: 'Scheduled Database Backup Completed', content: 'Automated nightly snapshot verified successfully across cloud instances.', category: 'Maintenance', isUrgent: false, createdAt: 'Yesterday' }
];

export const INITIAL_HACKATHONS: Hackathon[] = [
  {
    id: 'h1',
    title: 'EduTech AI Innovation Challenge',
    theme: 'Next-Gen Learning Tools',
    prizePool: '$15,000',
    deadline: 'Nov 18, 2026',
    registered: true,
    participants: 412,
    description: 'Build an AI-driven educational tool improving comprehension in STEM disciplines.',
    tags: ['AI', 'React', 'TypeScript', 'Machine Learning']
  },
  {
    id: 'h2',
    title: 'Global Cloud Systems Sprint',
    theme: 'Distributed Fault-Tolerant Architectures',
    prizePool: '$25,000',
    deadline: 'Dec 05, 2026',
    registered: false,
    participants: 780,
    description: 'Design resilient microservice pipelines with automated failover and telemetry.',
    tags: ['Cloud', 'Rust', 'Go', 'Docker']
  }
];

export const INITIAL_QUIZZES: QuizItem[] = [
  {
    id: 'q1',
    title: 'Operating Systems & Concurrency',
    subject: 'Computer Science',
    difficulty: 'medium',
    questions: [
      {
        id: 'q1_1',
        text: 'Which synchronization primitive prevents priority inversion using priority inheritance?',
        options: ['Mutex with priority inheritance', 'Counting Semaphore', 'Spinlock', 'Barrier'],
        correctIndex: 0,
        explanation: 'Priority inheritance raises the priority of the low-priority lock holder to that of the highest waiting process.'
      },
      {
        id: 'q1_2',
        text: 'What condition is necessary for a deadlock to occur?',
        options: ['Mutual Exclusion', 'Hold and Wait', 'No Preemption & Circular Wait', 'All of the above'],
        correctIndex: 3,
        explanation: 'Coffman conditions require all 4 conditions to hold simultaneously for a deadlock.'
      }
    ]
  },
  {
    id: 'q2',
    title: 'Database Systems & ACID Properties',
    subject: 'Databases',
    difficulty: 'easy',
    questions: [
      {
        id: 'q2_1',
        text: 'What does the "I" in ACID transactions stand for?',
        options: ['Integrity', 'Isolation', 'Iteration', 'Indexing'],
        correctIndex: 1,
        explanation: 'Isolation ensures concurrent transaction executions leave the database in the same state as if executed serially.'
      }
    ]
  }
];

export const INITIAL_TESTS: TimedTest[] = [
  {
    id: 'tst1',
    title: 'CS 301: Algorithms & Complexity Midterm Exam',
    subject: 'Computer Science',
    durationMinutes: 45,
    totalMarks: 50,
    questions: [
      {
        id: 't_q1',
        text: 'What is the tightest asymptotic upper bound (Big-O) of the Master Theorem case when f(n) = Theta(n^(log_b a))?',
        options: ['O(n^(log_b a))', 'O(n^(log_b a) * log n)', 'O(n^2)', 'O(log n)'],
        correctIndex: 1,
        marks: 10
      },
      {
        id: 't_q2',
        text: 'Which problem is known to be NP-Complete?',
        options: ['Shortest Path (Dijkstra)', 'Minimum Spanning Tree (Kruskal)', 'Traveling Salesperson Problem (Decision)', '2-SAT'],
        correctIndex: 2,
        marks: 10
      },
      {
        id: 't_q3',
        text: 'In a dynamic programming solution for 0/1 Knapsack, what does DP[i][w] represent?',
        options: ['Total weight with i items', 'Maximum value using a subset of first i items with weight limit w', 'Minimum items needed for weight w', 'Number of ways to fill knapsack'],
        correctIndex: 1,
        marks: 15
      },
      {
        id: 't_q4',
        text: 'Which graph algorithm can detect negative cycles in O(V * E) time?',
        options: ['Bellman-Ford Algorithm', 'Floyd-Warshall Algorithm', 'Prim Algorithm', 'Breadth First Search'],
        correctIndex: 0,
        marks: 15
      }
    ]
  }
];

// Offline AI response generator
export function generateHeuristicStudyResponse(prompt: string, mode: string): string {
  const query = prompt.toLowerCase();

  if (query.includes('binary search') || query.includes('search algorithm')) {
    return `### Binary Search Explained
Binary Search is an efficient divide-and-conquer search algorithm operating on **sorted arrays**.

**Key Steps:**
1. Compare target with middle element at \`mid = low + (high - low) / 2\`.
2. If \`target == arr[mid]\`, return index.
3. If \`target < arr[mid]\`, narrow search window to left half: \`high = mid - 1\`.
4. Otherwise, search right half: \`low = mid + 1\`.

**Complexity:**
- **Time Complexity:** $O(\\log n)$
- **Space Complexity:** $O(1)$ iterative, $O(\\log n)$ recursive call-stack.`;
  }

  if (query.includes('acid') || query.includes('database transaction')) {
    return `### ACID Guarantees in Relational Databases
1. **Atomicity (All-or-Nothing):** Either all operations inside the transaction commit, or the transaction is rolled back completely.
2. **Consistency:** Transitions the database from one valid state to another, satisfying all schema constraints, checks, and foreign keys.
3. **Isolation:** Concurrent transactions do not interfere with one another (e.g., Read Committed, Repeatable Read, Serializable).
4. **Durability:** Once committed, changes survive power outages and crashes via write-ahead logging (WAL).`;
  }

  if (query.includes('quick sort') || query.includes('sorting')) {
    return `### Quick Sort Algorithm
Quick Sort is an in-place divide-and-conquer sorting algorithm.

**Algorithm Invariants:**
- Pick a pivot (e.g., median-of-three or randomized).
- Partition the array such that all elements $< pivot$ are on the left, and elements $> pivot$ are on the right.
- Recursively apply quicksort to both partitions.

**Performance:**
- **Best / Average:** $O(n \\log n)$
- **Worst Case:** $O(n^2)$ when pivot is continuously the extreme min/max.
- **Remedy:** Dual-pivot or randomized pivot selection.`;
  }

  // Contextual fallback response
  return `### AI Study Assistant Insights
Here is a structured breakdown based on your query:

1. **Core Concept:** Your inquiry touches fundamental principles in **${mode.toUpperCase()}**.
2. **Key Takeaway:** Always verify preconditions, analyze edge cases (empty inputs, nulls, boundary constraints), and profile algorithmic complexity.
3. **Recommended Next Step:** Test your comprehension using the **Adaptive Quiz Generator** or implement this logic directly in the **14-Language Code Lab**!`;
}
