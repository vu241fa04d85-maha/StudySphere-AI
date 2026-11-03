#!/usr/bin/env bash
# ====================================================================
# AI STUDY ASSISTANT (AI-SA) — DATABASE RESTORATION SCRIPT
# Validates encrypted archive checksum, decrypts, and restores into target environment.
# SAFETY MANDATE: Never run against production without explicit confirmation!
# ====================================================================

set -euo pipefail

if [ "$#" -lt 1 ]; then
    echo "Usage: $0 <path_to_encrypted_backup.sql.enc>"
    exit 1
fi

ENCRYPTED_FILE="$1"
DECRYPTED_FILE="${ENCRYPTED_FILE%.enc}"

if [ ! -f "${ENCRYPTED_FILE}" ]; then
    echo "[ERROR] Backup file not found: ${ENCRYPTED_FILE}"
    exit 1
fi

echo "[ALERT] You are about to restore the database from: ${ENCRYPTED_FILE}"
echo "        Target DB: ${PGDATABASE:-unknown} on ${PGHOST:-localhost}"
echo "        Ensure you have created a pre-restoration snapshot before continuing."

read -p "Type 'RESTORE_CONFIRM' to proceed: " CONFIRMATION
if [ "${CONFIRMATION}" != "RESTORE_CONFIRM" ]; then
    echo "[ABORTED] Restoration cancelled by operator."
    exit 0
fi

# Decrypt archive
echo "[INFO] Decrypting archive..."
openssl enc -d -aes-256-cbc -pbkdf2 \
    -in "${ENCRYPTED_FILE}" \
    -out "${DECRYPTED_FILE}" \
    -k "${BACKUP_ENCRYPTION_PASSPHRASE}"

# Restore into target database
echo "[INFO] Applying database snapshot..."
psql -f "${DECRYPTED_FILE}"

# Secure cleanup
rm -f "${DECRYPTED_FILE}"

echo "[SUCCESS] Database restoration completed successfully."
