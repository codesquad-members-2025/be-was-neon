#!/usr/bin/env bash
set -e

# --------------------------------------------------
# Deployment script for Codesquad application
# --------------------------------------------------

# 1. Build the project
echo "🔨 Building project..."
./gradlew clean build -x test

# 2. Stop any running instance
SERVICE_NAME="codesquad"
JAR_NAME="be-was-neon-1.0-SNAPSHOT.jar"
if pgrep -f "$JAR_NAME" >/dev/null; then
  echo "🛑 Stopping existing service..."
  pkill -f "$JAR_NAME"
  sleep 2
fi

# 3. Prepare directories
BASE_DIR=$(cd "$(dirname "$0")" && pwd)
DEPLOY_DIR="$BASE_DIR/deploy"
LOG_DIR="$BASE_DIR/logs"
DB_DIR="$BASE_DIR/db"

mkdir -p "$DEPLOY_DIR" "$LOG_DIR" "$DB_DIR"

# 4. Copy new JAR
echo "📦 Deploying new build..."
cp "$BASE_DIR/build/libs/$JAR_NAME" "$DEPLOY_DIR/"

# 5. (Optional) export environment variables
# export JDBC_URL="jdbc:h2:./db/test;DB_CLOSE_DELAY=-1"
# export JDBC_USERNAME="sa"
# export JDBC_PASSWORD=""

# 6. Start the service
echo "🚀 Starting service..."
nohup java -jar "$DEPLOY_DIR/$JAR_NAME" \
    > "$LOG_DIR/$SERVICE_NAME.out.log" 2> "$LOG_DIR/$SERVICE_NAME.err.log" &

echo "✅ Deployment complete. Logs -> $LOG_DIR"
