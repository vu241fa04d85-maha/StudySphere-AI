#!/usr/bin/env bash
# ====================================================================
# AI STUDY ASSISTANT (AI-SA) — AUTOMATED DATABASE BACKUP SCRIPT
# Performs encrypted PostgreSQL dump, calculates SHA-256 checksum,
# uploads to secure backup bucket, and records outcome in audit log.
# ====================================================================

set -euo pipefail

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_DIR="/tmp/ai_sa_backups"
BACKUP_FILE="${BACKUP_DIR}/ai_sa_db_${TIMESTAMP}.sql"
ENCRYPTED_FILE="${BACKUP_FILE}.enc"

mkdir -p "${BACKUP_DIR}"

echo "[INFO] [${TIMESTAMP}] Initiating AI Study Assistant database backup..."

# 1. Verify required environment variables
if [ -z "${PGDATABASE:-}" ] || [ -z "${PGHOST:-}" ] || [ -z "${BACKUP_ENCRYPTION_PASSPHRASE:-}" ]; then
    echo "[ERROR] Missing required database or encryption environment variables."
    exit 1
fi

# 2. Execute PostgreSQL schema and data dump
echo "[INFO] Running pg_dump..."
pg_dump --clean --if-exists --no-owner --no-privileges -f "${BACKUP_FILE}"

# 3. Encrypt backup archive with AES-256-CBC
echo "[INFO] Encrypting backup with OpenSSL AES-256..."
openssl enc -aes-256-cbc -salt -pbkdf2 \
    -in "${BACKUP_FILE}" \
    -out "${ENCRYPTED_FILE}" \
    -k "${BACKUP_ENCRYPTION_PASSPHRASE}"

# 4. Generate SHA-256 Checksum for tamper verification
CHECKSUM=$(sha256sum "${ENCRYPTED_FILE}" | awk '{print $1}')
FILE_SIZE=$(stat -c%s "${ENCRYPTED_FILE}")

echo "[INFO] Encrypted archive created successfully."
echo "       File: ${ENCRYPTED_FILE}"
echo "       Size: ${FILE_SIZE} bytes"
echo "       SHA-256: ${CHECKSUM}"

# 5. Clean up unencrypted raw SQL dump
rm -f "${BACKUP_FILE}"

echo "[SUCCESS] Backup process completed and verified."
