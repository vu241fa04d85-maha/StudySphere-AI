import { describe, it, expect } from 'vitest';
import { verifyAdminCode, SEEDED_ADMINS } from '../lib/adminSeeds';
import { INITIAL_TASKS, INITIAL_ANNOUNCEMENTS, INITIAL_HACKATHONS } from '../lib/store';

describe('Admin Verification Suite', () => {
  it('seeds exactly 50 administrative credentials', () => {
    expect(SEEDED_ADMINS.length).toBe(50);
  });

  it('validates ADM-7001 super admin correctly', () => {
    const admin = verifyAdminCode('ADM-7001');
    expect(admin).not.toBeNull();
    expect(admin?.code).toBe('ADM-7001');
    expect(admin?.name).toBe('Dr. Sarah Jenkins');
    expect(admin?.role).toBe('super_admin');
  });

  it('validates ADM-7050 boundary admin code', () => {
    const admin = verifyAdminCode('ADM-7050');
    expect(admin).not.toBeNull();
    expect(admin?.code).toBe('ADM-7050');
  });

  it('rejects invalid or unseeded admin codes', () => {
    expect(verifyAdminCode('ADM-9999')).toBeNull();
    expect(verifyAdminCode('INVALID')).toBeNull();
  });
});

describe('Store & Initial Content Suite', () => {
  it('loads default study tasks properly', () => {
    expect(INITIAL_TASKS.length).toBeGreaterThan(0);
    expect(INITIAL_TASKS[0]).toHaveProperty('title');
    expect(INITIAL_TASKS[0]).toHaveProperty('completed');
  });

  it('loads system announcements with priority', () => {
    expect(INITIAL_ANNOUNCEMENTS.length).toBeGreaterThan(0);
  });

  it('loads active hackathons', () => {
    expect(INITIAL_HACKATHONS.length).toBeGreaterThan(0);
  });
});
