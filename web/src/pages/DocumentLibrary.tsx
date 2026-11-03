import React, { useState } from 'react';
import { FileUp, FileText, Sparkles, Layers, Trash2, CheckCircle2, Eye, Download, X } from 'lucide-react';
import { INITIAL_FILES, type StudyFile } from '../lib/store';

export const DocumentLibrary: React.FC = () => {
  const [files, setFiles] = useState<StudyFile[]>(INITIAL_FILES);
  const [selectedFile, setSelectedFile] = useState<StudyFile | null>(null);
  const [activeCardIndex, setActiveCardIndex] = useState(0);
  const [isFlipped, setIsFlipped] = useState(false);
  const [uploadStatus, setUploadStatus] = useState<string | null>(null);

  const handleSimulatedUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileList = e.target.files;
    if (!fileList || fileList.length === 0) return;

    const file = fileList[0];
    setUploadStatus('Analyzing document structure with AI heuristics...');

    setTimeout(() => {
      const newFile: StudyFile = {
        id: 'f_' + Date.now(),
        name: file.name,
        type: file.name.endsWith('.pdf') ? 'PDF' : file.name.endsWith('.docx') ? 'DOCX' : 'TXT',
        size: `${(file.size / (1024 * 1024)).toFixed(1)} MB`,
        subject: 'Uploaded Notes',
        summary: `Automated key concept synthesis extracted from ${file.name}. Highlights definition invariants, key mathematical steps, and summary points.`,
        flashcards: [
          { question: `What is the primary thesis of ${file.name}?`, answer: 'Foundational framework and algorithmic implementations.' },
          { question: 'What edge cases are documented in Section 2?', answer: 'Boundary condition checking and memory allocation constraints.' },
          { question: 'How is performance measured?', answer: 'Through asymptotic execution time and space footprint benchmarks.' }
        ],
        uploadedAt: 'Just now'
      };

      setFiles((prev) => [newFile, ...prev]);
      setUploadStatus(null);
    }, 800);
  };

  const deleteFile = (id: string) => {
    setFiles((prev) => prev.filter((f) => f.id !== id));
    if (selectedFile?.id === id) setSelectedFile(null);
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-2xl text-[#251C38]">
            Document Studio & AI Summaries
          </h1>
          <p className="text-xs text-[#645B74] mt-1">
            Upload lecture slides or research PDFs to extract smart summaries and active recall flashcards.
          </p>
        </div>
      </div>

      {/* Upload Dropzone */}
      <div className="glass p-8 border-2 border-dashed border-[#B8A4F4]/50 hover:border-[#6C42C4] transition-all text-center relative overflow-hidden group">
        <input
          type="file"
          accept=".pdf,.docx,.txt,.pptx"
          onChange={handleSimulatedUpload}
          className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10"
        />
        <div className="max-w-md mx-auto space-y-3 pointer-events-none">
          <div className="w-14 h-14 mx-auto rounded-2xl bg-gradient-to-tr from-[#6C42C4] to-[#B8A4F4] text-white flex items-center justify-center shadow-sm group-hover:scale-105 transition-transform">
            <FileUp className="w-7 h-7" />
          </div>
          <div>
            <h3 className="font-heading font-bold text-base text-[#251C38]">
              Upload Course Materials
            </h3>
            <p className="text-xs text-[#746589] mt-1">
              Drag & drop PDF, DOCX, or TXT notes (up to 50MB). AI automatically indexes text.
            </p>
          </div>
          <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-[#D6F36A]/40 text-[#556B16] text-xs font-bold">
            <Sparkles className="w-3.5 h-3.5" /> Instant Flashcard Generator
          </div>
        </div>
      </div>

      {uploadStatus && (
        <div className="p-4 rounded-2xl bg-[#D6F36A]/30 border border-[#D6F36A] text-xs font-bold text-[#556B16] flex items-center gap-2 animate-pulse">
          <Sparkles className="w-4 h-4" />
          {uploadStatus}
        </div>
      )}

      {/* Document Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {files.map((file) => (
          <div key={file.id} className="glass p-5 border border-white/95 glass-card-hover space-y-4">
            <div className="flex items-start justify-between gap-3">
              <div className="flex items-center gap-3">
                <div className="p-3 rounded-2xl bg-[#B8A4F4]/30 text-[#6C42C4]">
                  <FileText className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="font-heading font-bold text-sm text-[#251C38] leading-tight line-clamp-1">
                    {file.name}
                  </h3>
                  <div className="flex items-center gap-2 text-[11px] text-[#746589] mt-1">
                    <span className="font-bold text-[#6C42C4]">{file.type}</span>
                    <span>•</span>
                    <span>{file.size}</span>
                    <span>•</span>
                    <span>{file.uploadedAt}</span>
                  </div>
                </div>
              </div>

              <button
                onClick={() => deleteFile(file.id)}
                className="p-1.5 rounded-lg text-[#746589] hover:text-[#C34630] hover:bg-[#FF6347]/10 transition-colors"
                title="Delete document"
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </div>

            <div className="p-3 rounded-xl bg-white/60 text-xs text-[#645B74] leading-relaxed border border-white">
              <span className="font-bold text-[#251C38] block mb-0.5">AI Synthesis:</span>
              {file.summary}
            </div>

            <div className="flex items-center justify-between pt-2 border-t border-[#B8A4F4]/20">
              <span className="text-xs font-bold text-[#556B16] flex items-center gap-1">
                <Layers className="w-3.5 h-3.5" />
                {file.flashcards.length} Flashcards
              </span>

              <button
                onClick={() => {
                  setSelectedFile(file);
                  setActiveCardIndex(0);
                  setIsFlipped(false);
                }}
                className="px-3 py-1.5 btn-lavender text-xs flex items-center gap-1.5"
              >
                <Eye className="w-3.5 h-3.5" />
                Study Flashcards
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Flashcard Study Modal */}
      {selectedFile && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[#251C38]/40 backdrop-blur-sm animate-fade-in">
          <div className="glass max-w-lg w-full p-6 shadow-glassFloating border border-white relative">
            <div className="flex items-center justify-between mb-4">
              <div>
                <h3 className="font-heading font-bold text-base text-[#251C38]">
                  {selectedFile.name}
                </h3>
                <p className="text-xs text-[#746589]">
                  Card {activeCardIndex + 1} of {selectedFile.flashcards.length}
                </p>
              </div>
              <button
                onClick={() => setSelectedFile(null)}
                className="p-1.5 rounded-lg text-[#746589] hover:bg-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            {/* Interactive Flip Card */}
            <div
              onClick={() => setIsFlipped(!isFlipped)}
              className="min-h-[220px] p-6 rounded-2xl bg-white/90 border border-[#B8A4F4]/50 shadow-sm flex flex-col justify-between cursor-pointer hover:border-[#6C42C4] transition-all text-center select-none"
            >
              <div className="text-[11px] font-bold uppercase tracking-wider text-[#6C42C4]">
                {isFlipped ? 'Answer' : 'Question (Click to reveal)'}
              </div>

              <div className="font-heading font-bold text-base text-[#251C38] px-4">
                {isFlipped
                  ? selectedFile.flashcards[activeCardIndex].answer
                  : selectedFile.flashcards[activeCardIndex].question}
              </div>

              <div className="text-[10px] text-[#746589]">
                {isFlipped ? 'Tap to see question' : 'Tap to flip card'}
              </div>
            </div>

            {/* Navigation buttons */}
            <div className="flex items-center justify-between mt-5">
              <button
                disabled={activeCardIndex === 0}
                onClick={() => {
                  setActiveCardIndex((prev) => prev - 1);
                  setIsFlipped(false);
                }}
                className="px-4 py-2 rounded-xl text-xs font-bold bg-white text-[#251C38] border border-[#B8A4F4]/40 disabled:opacity-40"
              >
                Previous
              </button>

              <button
                disabled={activeCardIndex === selectedFile.flashcards.length - 1}
                onClick={() => {
                  setActiveCardIndex((prev) => prev + 1);
                  setIsFlipped(false);
                }}
                className="px-4 py-2 btn-lavender text-xs font-bold disabled:opacity-40"
              >
                Next Card
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
