import React, { useState } from 'react';
import { CalendarCheck, Plus, CheckCircle2, Clock, Trash2, X } from 'lucide-react';
import { INITIAL_TASKS, type StudyTask } from '../lib/store';

export const StudyPlanner: React.FC = () => {
  const [tasks, setTasks] = useState<StudyTask[]>(INITIAL_TASKS);
  const [showAddModal, setShowAddModal] = useState(false);
  const [newTitle, setNewTitle] = useState('');
  const [newSubject, setNewSubject] = useState('Computer Science');
  const [newDue, setNewDue] = useState('Tomorrow, 6:00 PM');
  const [newPriority, setNewPriority] = useState<'High' | 'Medium' | 'Low'>('Medium');
  const [newMinutes, setNewMinutes] = useState(45);

  const toggleTask = (id: string) => {
    setTasks((prev) =>
      prev.map((t) => (t.id === id ? { ...t, completed: !t.completed } : t))
    );
  };

  const deleteTask = (id: string) => {
    setTasks((prev) => prev.filter((t) => t.id !== id));
  };

  const handleAddTask = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim()) return;

    const task: StudyTask = {
      id: 'task_' + Date.now(),
      title: newTitle,
      subject: newSubject,
      dueDate: newDue,
      priority: newPriority,
      completed: false,
      estimatedMinutes: newMinutes,
    };

    setTasks((prev) => [task, ...prev]);
    setNewTitle('');
    setShowAddModal(false);
  };

  const completedCount = tasks.filter((t) => t.completed).length;

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Planner Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-2xl text-[#251C38] flex items-center gap-2">
            Academic Study Planner
            <span className="px-2 py-0.5 rounded-full text-xs font-bold bg-[#D6F36A]/40 text-[#556B16] border border-[#D6F36A]/60">
              {completedCount}/{tasks.length} Completed
            </span>
          </h1>
          <p className="text-xs text-[#645B74] mt-1">
            Organize lectures, problem sets, and milestones with priority-weighted tracking.
          </p>
        </div>

        <button
          onClick={() => setShowAddModal(true)}
          className="px-4 py-2.5 btn-lavender text-xs font-bold flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>New Study Task</span>
        </button>
      </div>

      {/* Task List */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {tasks.map((task) => (
          <div
            key={task.id}
            className={`glass p-5 border transition-all duration-200 flex items-start justify-between gap-3 ${
              task.completed ? 'opacity-65 border-white/70' : 'border-white/95 glass-card-hover'
            }`}
          >
            <div className="flex items-start gap-3 flex-1">
              <button
                onClick={() => toggleTask(task.id)}
                className={`w-6 h-6 rounded-lg border-2 mt-0.5 flex items-center justify-center transition-all ${
                  task.completed
                    ? 'bg-[#556B16] border-[#556B16] text-white'
                    : 'border-[#B8A4F4] bg-white hover:border-[#6C42C4]'
                }`}
              >
                {task.completed && <CheckCircle2 className="w-4 h-4" />}
              </button>

              <div className="flex-1">
                <h3
                  className={`font-heading font-bold text-sm text-[#251C38] ${
                    task.completed ? 'line-through text-[#746589]' : ''
                  }`}
                >
                  {task.title}
                </h3>
                <div className="flex flex-wrap items-center gap-2 text-[11px] text-[#746589] mt-1.5">
                  <span className="font-semibold text-[#6C42C4]">{task.subject}</span>
                  <span>•</span>
                  <span>{task.dueDate}</span>
                  <span>•</span>
                  <span className="flex items-center gap-1">
                    <Clock className="w-3 h-3" /> {task.estimatedMinutes}m
                  </span>
                </div>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <span
                className={`text-[10px] px-2 py-0.5 rounded-md font-bold uppercase ${
                  task.priority === 'High'
                    ? 'bg-[#FF6347]/20 text-[#C34630] border border-[#FF6347]/30'
                    : task.priority === 'Medium'
                    ? 'bg-[#D6F36A]/40 text-[#556B16]'
                    : 'bg-[#B8A4F4]/30 text-[#6C42C4]'
                }`}
              >
                {task.priority}
              </span>

              <button
                onClick={() => deleteTask(task.id)}
                className="p-1 rounded-md text-[#746589] hover:text-[#C34630] hover:bg-[#FF6347]/10"
                title="Delete task"
              >
                <Trash2 className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Add Task Modal */}
      {showAddModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[#251C38]/40 backdrop-blur-sm animate-fade-in">
          <div className="glass max-w-md w-full p-6 shadow-glassFloating border border-white space-y-4">
            <div className="flex items-center justify-between pb-3 border-b border-[#B8A4F4]/20">
              <h3 className="font-heading font-bold text-base text-[#251C38]">Create Study Task</h3>
              <button onClick={() => setShowAddModal(false)} className="p-1 rounded-lg text-[#746589]">
                <X className="w-4 h-4" />
              </button>
            </div>

            <form onSubmit={handleAddTask} className="space-y-3.5 text-xs">
              <div>
                <label className="block font-bold text-[#251C38] mb-1">Task Title</label>
                <input
                  type="text"
                  value={newTitle}
                  onChange={(e) => setNewTitle(e.target.value)}
                  placeholder="e.g. Implement Dijkstra in C++"
                  className="w-full px-3 py-2 rounded-xl glass-input text-xs"
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-[#251C38] mb-1">Subject</label>
                  <input
                    type="text"
                    value={newSubject}
                    onChange={(e) => setNewSubject(e.target.value)}
                    className="w-full px-3 py-2 rounded-xl glass-input text-xs"
                    required
                  />
                </div>
                <div>
                  <label className="block font-bold text-[#251C38] mb-1">Estimated Minutes</label>
                  <input
                    type="number"
                    value={newMinutes}
                    onChange={(e) => setNewMinutes(Number(e.target.value))}
                    className="w-full px-3 py-2 rounded-xl glass-input text-xs"
                    min={5}
                    max={300}
                  />
                </div>
              </div>

              <div>
                <label className="block font-bold text-[#251C38] mb-1">Due Date & Time</label>
                <input
                  type="text"
                  value={newDue}
                  onChange={(e) => setNewDue(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl glass-input text-xs"
                  placeholder="Nov 05, 5:00 PM"
                />
              </div>

              <div>
                <label className="block font-bold text-[#251C38] mb-1">Priority Level</label>
                <div className="flex gap-2">
                  {(['High', 'Medium', 'Low'] as const).map((p) => (
                    <button
                      key={p}
                      type="button"
                      onClick={() => setNewPriority(p)}
                      className={`flex-1 py-1.5 rounded-lg text-xs font-bold transition-all ${
                        newPriority === p
                          ? p === 'High'
                            ? 'bg-[#FF6347] text-white'
                            : p === 'Medium'
                            ? 'bg-[#D6F36A] text-[#251C38]'
                            : 'bg-[#6C42C4] text-white'
                          : 'bg-white/70 text-[#645B74]'
                      }`}
                    >
                      {p}
                    </button>
                  ))}
                </div>
              </div>

              <div className="flex justify-end gap-2 pt-3">
                <button
                  type="button"
                  onClick={() => setShowAddModal(false)}
                  className="px-4 py-2 rounded-xl bg-white text-[#251C38] border border-[#B8A4F4]/30"
                >
                  Cancel
                </button>
                <button type="submit" className="px-4 py-2 btn-lavender font-bold">
                  Save Task
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
