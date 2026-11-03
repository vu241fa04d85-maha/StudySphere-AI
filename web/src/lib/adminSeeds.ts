export interface SeededAdmin {
  code: string;
  name: string;
  email: string;
  role: 'super_admin' | 'academic_admin' | 'test_manager' | 'hackathon_manager' | 'support_admin';
  roleLabel: string;
  department: string;
  status: 'active' | 'verified';
  mfaEnabled: boolean;
}

const ROLES: Array<{ role: SeededAdmin['role']; label: string; dept: string }> = [
  { role: 'super_admin', label: 'Super Administrator', dept: 'System Engineering & Operations' },
  { role: 'academic_admin', label: 'Academic Director', dept: 'Curriculum & Accreditation' },
  { role: 'test_manager', label: 'Assessment Lead', dept: 'Examination & Proctoring' },
  { role: 'hackathon_manager', label: 'Innovation Director', dept: 'Competitions & Hackathons' },
  { role: 'support_admin', label: 'Senior Student Support', dept: 'Student Affairs & Mentorship' },
];

const NAMES = [
  "Dr. Sarah Jenkins", "Prof. Marcus Vance", "Elena Rostova", "Dr. David Kim", "Amira Patel",
  "James O'Connor", "Dr. Lin Zhang", "Carlos Mendez", "Amina Bello", "Alexander Wright",
  "Dr. Priya Sharma", "Takahiro Sato", "Chloe Dupont", "Omer Farooq", "Maya Lindqvist",
  "Arthur Pendelton", "Fatima Al-Sayed", "Lucas Silva", "Dr. Robert Mueller", "Keiko Tanaka",
  "Zubair Khan", "Hannah Abbott", "Vikram Malhotra", "Soraya Haddad", "Leonid Volkov",
  "Evelyn Reed", "Dr. Chidi Anagonye", "Nadia Petrova", "Guillermo Gomez", "Dr. Rachel Green",
  "Samuel Mensah", "Li Wei", "Sophia Lorenzen", "Hassan Mansoor", "Camila Fernandez",
  "Dr. Alan Turing", "Grace Hopper", "Donald Knuth", "Ada Lovelace", "Claude Shannon",
  "Barbara Liskov", "Dennis Ritchie", "Ken Thompson", "Margaret Hamilton", "Leslie Lamport",
  "Tim Berners-Lee", "Vint Cerf", "Radia Perlman", "Andrew Ng", "Geoffrey Hinton"
];

export const SEEDED_ADMINS: SeededAdmin[] = Array.from({ length: 50 }, (_, i) => {
  const codeNum = 7001 + i;
  const code = `ADM-${codeNum}`;
  const name = NAMES[i % NAMES.length];
  const roleObj = ROLES[i % ROLES.length];
  const email = `${name.toLowerCase().replace(/[^a-z]/g, '.')}@ai-sa.edu`;

  return {
    code,
    name,
    email,
    role: roleObj.role,
    roleLabel: roleObj.label,
    department: roleObj.dept,
    status: 'active',
    mfaEnabled: i % 2 === 0,
  };
});

export function verifyAdminCode(code: string): SeededAdmin | null {
  const normalized = code.trim().toUpperCase();
  return SEEDED_ADMINS.find(admin => admin.code === normalized) || null;
}
