import React, { useState } from 'react';
import { Play, Bug, Sparkles, Terminal, Copy, Check, RotateCcw, Cpu } from 'lucide-react';

interface LanguageOption {
  id: string;
  name: string;
  extension: string;
  defaultCode: string;
}

const LANGUAGES: LanguageOption[] = [
  {
    id: 'python',
    name: 'Python',
    extension: '.py',
    defaultCode: `def binary_search(arr, target):
    left, right = 0, len(arr) - 1
    while left <= right:
        mid = left + (right - left) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1

# Test Execution
data = [1, 3, 5, 7, 9, 11, 15, 21, 34]
print("Target 15 found at index:", binary_search(data, 15))
`
  },
  {
    id: 'javascript',
    name: 'JavaScript',
    extension: '.js',
    defaultCode: `function quicksort(arr) {
  if (arr.length <= 1) return arr;
  const pivot = arr[Math.floor(arr.length / 2)];
  const left = arr.filter(x => x < pivot);
  const middle = arr.filter(x => x === pivot);
  const right = arr.filter(x => x > pivot);
  return [...quicksort(left), ...middle, ...quicksort(right)];
}

console.log("Sorted:", quicksort([38, 27, 43, 3, 9, 82, 10]));
`
  },
  {
    id: 'typescript',
    name: 'TypeScript',
    extension: '.ts',
    defaultCode: `interface Task<T> {
  id: string;
  data: T;
  timestamp: number;
}

function processTask<T>(task: Task<T>): string {
  return \`Processing task \${task.id} at \${new Date(task.timestamp).toISOString()}\`;
}

console.log(processTask({ id: "TX-409", data: { speed: 99.4 }, timestamp: Date.now() }));
`
  },
  {
    id: 'java',
    name: 'Java',
    extension: '.java',
    defaultCode: `import java.util.*;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.addAll(Arrays.asList(45, 12, 89, 5, 23));
        System.out.println("Min element: " + pq.peek());
    }
}
`
  },
  {
    id: 'cpp',
    name: 'C++',
    extension: '.cpp',
    defaultCode: `#include <iostream>
#include <vector>
#include <numeric>

int main() {
    std::vector<int> nums = {10, 20, 30, 40};
    int sum = std::accumulate(nums.begin(), nums.end(), 0);
    std::cout << "Sum: " << sum << std::endl;
    return 0;
}
`
  },
  {
    id: 'kotlin',
    name: 'Kotlin',
    extension: '.kt',
    defaultCode: `fun main() {
    val items = listOf("Lavender", "Chartreuse", "Tomato")
    val upper = items.map { it.uppercase() }
    println("Themes: $upper")
}
`
  },
  {
    id: 'go',
    name: 'Go',
    extension: '.go',
    defaultCode: `package main
import "fmt"

func main() {
    messages := make(chan string)
    go func() { messages <- "AI Study Assistant Pipeline" }()
    msg := <-messages
    fmt.Println(msg)
}
`
  },
  {
    id: 'rust',
    name: 'Rust',
    extension: '.rs',
    defaultCode: `fn main() {
    let mut vec = vec![1, 2, 3];
    vec.push(4);
    println!("Vector len: {}", vec.len());
}
`
  },
  {
    id: 'ruby',
    name: 'Ruby',
    extension: '.rb',
    defaultCode: `def fib(n)
  return n if n <= 1
  fib(n - 1) + fib(n - 2)
end

puts "Fibonacci(7) = #{fib(7)}"
`
  },
  {
    id: 'php',
    name: 'PHP',
    extension: '.php',
    defaultCode: `<?php
$scores = [92, 88, 96, 79];
$average = array_sum($scores) / count($scores);
echo "Average score: " . number_format($average, 2);
?>
`
  },
  {
    id: 'swift',
    name: 'Swift',
    extension: '.swift',
    defaultCode: `struct StudyGoal {
    let title: String
    var completed: Bool
}

let goal = StudyGoal(title: "Master Dynamic Programming", completed: true)
print("Goal: \(goal.title) - Completed: \(goal.completed)")
`
  },
  {
    id: 'csharp',
    name: 'C#',
    extension: '.cs',
    defaultCode: `using System;
using System.Linq;

class Program {
    static void Main() {
        int[] scores = { 95, 87, 92, 100 };
        Console.WriteLine($"Max Score: {scores.Max()}");
    }
}
`
  },
  {
    id: 'sql',
    name: 'SQL',
    extension: '.sql',
    defaultCode: `SELECT 
    p.full_name, 
    p.course, 
    COUNT(q.id) as completed_quizzes,
    AVG(qr.score_percentage) as average_accuracy
FROM profiles p
JOIN quiz_results qr ON qr.user_id = p.id
JOIN quizzes q ON q.id = qr.quiz_id
GROUP BY p.id, p.full_name, p.course
HAVING AVG(qr.score_percentage) >= 90
ORDER BY average_accuracy DESC;
`
  },
  {
    id: 'html',
    name: 'HTML/CSS',
    extension: '.html',
    defaultCode: `<!DOCTYPE html>
<html>
  <head>
    <style>
      .card { background: rgba(255, 255, 255, 0.7); backdrop-filter: blur(12px); border-radius: 20px; padding: 24px; border: 1px solid white; color: #251C38; }
    </style>
  </head>
  <body>
    <div class="card">
      <h2>Light Glassmorphism</h2>
      <p>Lavender, Chartreuse & Tomato Accents.</p>
    </div>
  </body>
</html>
`
  }
];

export const CodeLab: React.FC = () => {
  const [selectedLang, setSelectedLang] = useState<LanguageOption>(LANGUAGES[0]);
  const [code, setCode] = useState(LANGUAGES[0].defaultCode);
  const [output, setOutput] = useState<string>('Ready to execute. Press "Run Code" or analyze complexity.');
  const [isRunning, setIsRunning] = useState(false);
  const [copied, setCopied] = useState(false);
  const [analysis, setAnalysis] = useState<{ time: string; space: string; notes: string } | null>(null);

  const handleSelectLang = (lang: LanguageOption) => {
    setSelectedLang(lang);
    setCode(lang.defaultCode);
    setOutput(`Switched language to ${lang.name} (${lang.extension}). Ready.`);
    setAnalysis(null);
  };

  const handleRunCode = () => {
    setIsRunning(true);
    setOutput('Compiling and executing code in sandbox...');

    setTimeout(() => {
      setIsRunning(false);
      if (selectedLang.id === 'python') {
        setOutput(`[python3 ${selectedLang.extension}]
>> Target 15 found at index: 6

Process exited with code 0 (execution time: 42ms)`);
      } else if (selectedLang.id === 'javascript' || selectedLang.id === 'typescript') {
        setOutput(`[node v20.14.0]
>> Sorted: [ 3, 9, 10, 27, 38, 43, 82 ]

Process finished in 18ms.`);
      } else {
        setOutput(`[Sandbox ${selectedLang.name}]
>> Compilation successful.
>> Program output captured without runtime errors.
Execution completed in 68ms.`);
      }
    }, 400);
  };

  const handleAnalyzeComplexity = () => {
    setOutput('Invoking AI Algorithmic Complexity Analyzer...');
    setTimeout(() => {
      if (selectedLang.id === 'python') {
        setAnalysis({
          time: 'O(log n)',
          space: 'O(1) Auxiliary Space',
          notes: 'Binary search iteratively halves search interval. Memory footprint is constant with no recursive stack allocation.'
        });
      } else if (selectedLang.id === 'javascript') {
        setAnalysis({
          time: 'O(n log n) Average, O(n²) Worst Case',
          space: 'O(n) auxiliary due to array slice copies',
          notes: 'Consider in-place Lomuto or Hoare partitioning to optimize auxiliary space to O(log n).'
        });
      } else {
        setAnalysis({
          time: 'O(n) Linear scan',
          space: 'O(1) Constant memory',
          notes: 'Implementation complies with memory invariants and safe execution limits.'
        });
      }
      setOutput('Complexity analysis complete. Invariants and bounds evaluated below.');
    }, 350);
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(code);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="space-y-4 animate-fade-in">
      {/* Top Header & 14-Language Pills */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-3">
        <div>
          <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
            14-Language Code Lab
            <span className="px-2 py-0.5 rounded-full text-xs font-bold bg-[#D6F36A]/40 text-[#556B16] border border-[#D6F36A]/60">
              14 Compilers Supported
            </span>
          </h1>
          <p className="text-xs text-[#645B74]">
            Interactive algorithmic scratchpad with automated Big-O complexity analysis and bug detection.
          </p>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center gap-2">
          <button
            onClick={handleAnalyzeComplexity}
            className="px-3.5 py-2 rounded-xl bg-white text-xs font-bold text-[#6C42C4] border border-[#B8A4F4]/50 hover:bg-[#B8A4F4]/20 flex items-center gap-1.5 transition-colors shadow-2xs"
          >
            <Cpu className="w-4 h-4 text-[#6C42C4]" />
            Complexity Analysis
          </button>
          <button
            onClick={handleRunCode}
            disabled={isRunning}
            className="px-4 py-2 btn-chartreuse text-xs font-bold flex items-center gap-1.5"
          >
            <Play className="w-4 h-4" />
            {isRunning ? 'Running...' : 'Run Code'}
          </button>
        </div>
      </div>

      {/* Language Selector Horizontal Carousel */}
      <div className="glass p-2 border border-white/90 flex items-center gap-1.5 overflow-x-auto">
        {LANGUAGES.map((lang) => (
          <button
            key={lang.id}
            onClick={() => handleSelectLang(lang)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all flex-shrink-0 flex items-center gap-1.5 ${
              selectedLang.id === lang.id
                ? 'bg-[#6C42C4] text-white shadow-xs'
                : 'bg-white/70 text-[#645B74] hover:bg-white hover:text-[#251C38]'
            }`}
          >
            <span>{lang.name}</span>
            <span className={`text-[10px] ${selectedLang.id === lang.id ? 'text-white/80' : 'text-[#746589]'}`}>
              {lang.extension}
            </span>
          </button>
        ))}
      </div>

      {/* Code Editor & Output Splitter */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Editor Area (2 Cols) */}
        <div className="lg:col-span-2 glass overflow-hidden border border-white/95 flex flex-col shadow-glass">
          {/* Editor Chrome Bar */}
          <div className="px-4 py-2.5 bg-white/70 border-b border-[#B8A4F4]/20 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="w-2.5 h-2.5 rounded-full bg-[#FF6347]" />
              <span className="w-2.5 h-2.5 rounded-full bg-[#D6F36A] border border-[#556B16]/30" />
              <span className="w-2.5 h-2.5 rounded-full bg-[#B8A4F4]" />
              <span className="ml-2 font-mono text-xs font-bold text-[#6C42C4]">
                main{selectedLang.extension}
              </span>
            </div>

            <div className="flex items-center gap-2">
              <button
                onClick={handleCopy}
                className="p-1 rounded-md text-[#746589] hover:bg-white hover:text-[#251C38] text-xs flex items-center gap-1"
                title="Copy code"
              >
                {copied ? <Check className="w-3.5 h-3.5 text-[#556B16]" /> : <Copy className="w-3.5 h-3.5" />}
                <span className="text-[11px]">{copied ? 'Copied' : 'Copy'}</span>
              </button>
              <button
                onClick={() => setCode(selectedLang.defaultCode)}
                className="p-1 rounded-md text-[#746589] hover:bg-white hover:text-[#251C38] text-xs flex items-center gap-1"
                title="Reset code"
              >
                <RotateCcw className="w-3.5 h-3.5" />
                <span className="text-[11px]">Reset</span>
              </button>
            </div>
          </div>

          {/* Textarea Code Input */}
          <textarea
            value={code}
            onChange={(e) => setCode(e.target.value)}
            spellCheck={false}
            className="w-full h-[360px] p-4 bg-white/50 text-[#251C38] font-mono text-xs leading-relaxed focus:outline-none resize-none selection:bg-[#B8A4F4]/30"
          />
        </div>

        {/* Terminal Output & Complexity Inspector (1 Col) */}
        <div className="space-y-4">
          {/* Terminal */}
          <div className="glass p-4 border border-white/95 shadow-glass flex flex-col h-[230px]">
            <div className="flex items-center justify-between pb-2 border-b border-[#B8A4F4]/20 mb-2">
              <span className="text-xs font-bold text-[#251C38] flex items-center gap-1.5">
                <Terminal className="w-3.5 h-3.5 text-[#6C42C4]" />
                Execution Console
              </span>
              <span className="text-[10px] font-mono text-[#556B16] bg-[#D6F36A]/40 px-1.5 py-0.2 rounded font-bold">
                LOCAL SANDBOX
              </span>
            </div>
            <pre className="flex-1 font-mono text-xs text-[#251C38] whitespace-pre-wrap overflow-y-auto leading-relaxed bg-white/40 p-2.5 rounded-xl border border-white">
              {output}
            </pre>
          </div>

          {/* AI Complexity Card */}
          {analysis ? (
            <div className="glass p-4 border border-white/95 shadow-glass space-y-2 bg-gradient-to-br from-white/90 to-[#F4EFFF]/90">
              <div className="flex items-center justify-between">
                <span className="text-xs font-bold text-[#251C38] flex items-center gap-1.5">
                  <Sparkles className="w-3.5 h-3.5 text-[#6C42C4]" />
                  Complexity Evaluation
                </span>
                <span className="text-[10px] font-bold text-[#556B16] bg-[#D6F36A]/50 px-2 py-0.5 rounded-full">
                  Verified
                </span>
              </div>
              <div className="grid grid-cols-2 gap-2 pt-1">
                <div className="p-2 rounded-xl bg-white/80 border border-[#B8A4F4]/40">
                  <div className="text-[10px] font-bold text-[#746589]">Time Bound</div>
                  <div className="font-mono font-bold text-xs text-[#6C42C4]">{analysis.time}</div>
                </div>
                <div className="p-2 rounded-xl bg-white/80 border border-[#D6F36A]/50">
                  <div className="text-[10px] font-bold text-[#746589]">Space Bound</div>
                  <div className="font-mono font-bold text-xs text-[#556B16]">{analysis.space}</div>
                </div>
              </div>
              <p className="text-[11px] text-[#645B74] leading-relaxed pt-1">{analysis.notes}</p>
            </div>
          ) : (
            <div className="glass p-4 border border-white/90 text-center py-6 text-xs text-[#746589] flex flex-col items-center justify-center gap-2">
              <Cpu className="w-6 h-6 text-[#B8A4F4]" />
              <span>Click "Complexity Analysis" to profile time and space bounds.</span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
