import React, { useState } from 'react';
import { Bot, Send, Sparkles, BookOpen, Calculator, Code2, Layers, RefreshCw } from 'lucide-react';
import { generateHeuristicStudyResponse, type ChatMessage } from '../lib/store';

export const AIChatbot: React.FC = () => {
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: 'm1',
      sender: 'assistant',
      content: `Hello! I am your **AI Study Assistant** powered by light glassmorphic intelligence. 
I can help break down complex computer science concepts, solve mathematics equations, generate instant study flashcards, or inspect algorithmic code.

What would you like to review today?`,
      timestamp: 'Just now',
      mode: 'concept'
    }
  ]);

  const [input, setInput] = useState('');
  const [selectedMode, setSelectedMode] = useState<'concept' | 'math' | 'flashcards' | 'code'>('concept');
  const [isTyping, setIsTyping] = useState(false);

  const quickPrompts = [
    'Explain Binary Search invariant',
    'What are ACID properties in databases?',
    'Analyze Quick Sort time complexity',
    'Generate flashcards for AVL tree rotations'
  ];

  const handleSend = (textToSend?: string) => {
    const prompt = textToSend || input;
    if (!prompt.trim()) return;

    const userMsg: ChatMessage = {
      id: 'u_' + Date.now(),
      sender: 'user',
      content: prompt,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      mode: selectedMode,
    };

    setMessages((prev) => [...prev, userMsg]);
    setInput('');
    setIsTyping(true);

    setTimeout(() => {
      const reply = generateHeuristicStudyResponse(prompt, selectedMode);
      const assistantMsg: ChatMessage = {
        id: 'a_' + Date.now(),
        sender: 'assistant',
        content: reply,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        mode: selectedMode,
      };
      setMessages((prev) => [...prev, assistantMsg]);
      setIsTyping(false);
    }, 450);
  };

  return (
    <div className="h-[calc(100vh-120px)] flex flex-col glass overflow-hidden border border-white/95 shadow-glass">
      {/* Chat Header with Mode Selector */}
      <div className="p-4 border-b border-[#B8A4F4]/20 bg-white/50 flex flex-wrap items-center justify-between gap-3">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-2xl bg-gradient-to-tr from-[#6C42C4] to-[#B8A4F4] flex items-center justify-center text-white shadow-xs">
            <Bot className="w-5 h-5" />
          </div>
          <div>
            <h2 className="font-heading font-bold text-base text-[#251C38] flex items-center gap-2">
              AI Study Assistant
              <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-[#D6F36A]/40 text-[#556B16] border border-[#D6F36A]/60">
                Online • Heuristics Ready
              </span>
            </h2>
            <p className="text-xs text-[#746589]">Interactive academic tutoring & formula breakdowns</p>
          </div>
        </div>

        {/* Study Mode Pills */}
        <div className="flex items-center gap-1.5 p-1 rounded-xl bg-white/70 border border-[#B8A4F4]/30">
          <button
            onClick={() => setSelectedMode('concept')}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5 transition-all ${
              selectedMode === 'concept'
                ? 'bg-[#6C42C4] text-white shadow-xs'
                : 'text-[#645B74] hover:text-[#251C38]'
            }`}
          >
            <BookOpen className="w-3.5 h-3.5" /> Concept
          </button>
          <button
            onClick={() => setSelectedMode('math')}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5 transition-all ${
              selectedMode === 'math'
                ? 'bg-[#6C42C4] text-white shadow-xs'
                : 'text-[#645B74] hover:text-[#251C38]'
            }`}
          >
            <Calculator className="w-3.5 h-3.5" /> Math
          </button>
          <button
            onClick={() => setSelectedMode('flashcards')}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5 transition-all ${
              selectedMode === 'flashcards'
                ? 'bg-[#6C42C4] text-white shadow-xs'
                : 'text-[#645B74] hover:text-[#251C38]'
            }`}
          >
            <Layers className="w-3.5 h-3.5" /> Cards
          </button>
          <button
            onClick={() => setSelectedMode('code')}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5 transition-all ${
              selectedMode === 'code'
                ? 'bg-[#6C42C4] text-white shadow-xs'
                : 'text-[#645B74] hover:text-[#251C38]'
            }`}
          >
            <Code2 className="w-3.5 h-3.5" /> Code
          </button>
        </div>
      </div>

      {/* Messages Scroll Area */}
      <div className="flex-1 p-4 sm:p-6 overflow-y-auto space-y-4">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`flex items-start gap-3 ${
              msg.sender === 'user' ? 'justify-end' : 'justify-start'
            }`}
          >
            {msg.sender === 'assistant' && (
              <div className="w-8 h-8 rounded-xl bg-[#B8A4F4]/30 border border-[#B8A4F4]/60 flex items-center justify-center text-[#6C42C4] flex-shrink-0 mt-1">
                <Sparkles className="w-4 h-4" />
              </div>
            )}

            <div
              className={`max-w-2xl p-4 rounded-2xl text-sm leading-relaxed ${
                msg.sender === 'user'
                  ? 'bg-gradient-to-r from-[#6C42C4] to-[#B8A4F4] text-white rounded-tr-none shadow-sm'
                  : 'bg-white/85 text-[#251C38] border border-white/90 rounded-tl-none shadow-glass'
              }`}
            >
              <div className="whitespace-pre-line prose prose-sm">{msg.content}</div>
              <div
                className={`text-[10px] mt-2 font-medium ${
                  msg.sender === 'user' ? 'text-white/70 text-right' : 'text-[#746589]'
                }`}
              >
                {msg.timestamp}
              </div>
            </div>
          </div>
        ))}

        {isTyping && (
          <div className="flex items-center gap-2 p-3 rounded-2xl bg-white/70 border border-white max-w-xs text-xs text-[#6C42C4] font-medium">
            <RefreshCw className="w-3.5 h-3.5 animate-spin" />
            Synthesizing academic explanation...
          </div>
        )}
      </div>

      {/* Suggested Quick Question Prompts */}
      <div className="px-4 py-2 border-t border-[#B8A4F4]/15 bg-white/40 flex items-center gap-2 overflow-x-auto">
        <span className="text-[11px] font-bold text-[#746589] flex-shrink-0 flex items-center gap-1">
          <Sparkles className="w-3 h-3 text-[#6C42C4]" /> Quick Prompts:
        </span>
        {quickPrompts.map((p, idx) => (
          <button
            key={idx}
            onClick={() => handleSend(p)}
            className="px-3 py-1 rounded-full text-xs bg-white/80 hover:bg-white text-[#251C38] border border-[#B8A4F4]/30 hover:border-[#6C42C4] flex-shrink-0 transition-colors shadow-2xs"
          >
            {p}
          </button>
        ))}
      </div>

      {/* Input Box */}
      <div className="p-4 bg-white/60 border-t border-[#B8A4F4]/20">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSend();
          }}
          className="flex items-center gap-2"
        >
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder={`Ask a question in ${selectedMode.toUpperCase()} mode... (e.g. explain time complexity)`}
            className="flex-1 px-4 py-3 rounded-xl glass-input text-sm text-[#251C38] placeholder:text-[#746589]"
          />
          <button
            type="submit"
            disabled={!input.trim() || isTyping}
            className="px-5 py-3 btn-lavender text-sm flex items-center gap-2 disabled:opacity-50"
          >
            <span>Ask</span>
            <Send className="w-4 h-4" />
          </button>
        </form>
      </div>
    </div>
  );
};
