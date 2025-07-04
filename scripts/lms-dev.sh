#!/bin/bash
# LMS Development Helper Script
# Usage: ./scripts/lms-dev.sh [start|stop|run <service-folder>]

set -e

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
LOG_DIR="$PROJECT_ROOT/logs"

run_service() {
  SERVICE_DIR="$1"
  if [ -z "$SERVICE_DIR" ]; then
    echo "Usage: $0 run <service-folder>"
    exit 1
  fi
  cd "$PROJECT_ROOT/$SERVICE_DIR"
  set -a
  [ -f .env ] && . .env
  set +a
  mvn spring-boot:run
}

start_services() {
  mkdir -p "$LOG_DIR"
  # Start Eureka Server
  echo "🚀 Starting Eureka Server..."
  cd "$PROJECT_ROOT/eureka-server"
  mvn spring-boot:run > "$LOG_DIR/eureka-server.log" 2>&1 &
  cd "$PROJECT_ROOT"
  sleep 30
  # Start other services
  declare -a services=("api-gateway:8080" "book-service:8081" "member-service:8082" "transaction-service:8083" "fine-service:8084" "notification-service:8085")
  for entry in "${services[@]}"; do
    IFS=":" read -r service_name port <<< "$entry"
    echo "🚀 Starting $service_name..."
    cd "$PROJECT_ROOT/$service_name"
    set -a
    [ -f .env ] && . .env
    set +a
    mvn spring-boot:run > "$LOG_DIR/$service_name.log" 2>&1 &
    cd "$PROJECT_ROOT"
    sleep 15
  done
  echo "⏳ Waiting for all services to start..."
  sleep 60
  echo "🔍 Checking service health..."
  all_healthy=true
  for entry in "${services[@]}"; do
    IFS=":" read -r service_name port <<< "$entry"
    if ! curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
      echo "❌ $service_name is not responding on port $port"
      all_healthy=false
    else
      echo "✅ $service_name is running on port $port"
    fi
  done
  if $all_healthy; then
    echo "🎉 All services are running successfully!"
    echo "📚 Library Management System is ready!"
    echo "🌐 API Gateway: http://localhost:8080"
    echo "🔗 Eureka Dashboard: http://localhost:8761"
    echo ""
    echo "Swagger UI Links:"
    echo "   - Book Service:         http://localhost:8081/swagger-ui.html"
    echo "   - Member Service:       http://localhost:8082/swagger-ui.html"
    echo "   - Transaction Service:  http://localhost:8083/swagger-ui.html"
    echo "   - Fine Service:         http://localhost:8084/swagger-ui.html"
    echo "   - Notification Service: http://localhost:8085/swagger-ui.html"
  else
    echo "⚠️  Some services failed to start. Check the logs in the 'logs' directory."
  fi
}

stop_services() {
  mkdir -p "$LOG_DIR"
  declare -a services=("notification-service:8085" "fine-service:8084" "transaction-service:8083" "member-service:8082" "book-service:8081" "api-gateway:8080" "eureka-server:8761")
  # Detect OS
  OS_TYPE="$(uname 2>/dev/null || echo $OS)"
  for entry in "${services[@]}"; do
    IFS=":" read -r service_name port <<< "$entry"
    echo "🛑 Stopping $service_name (port $port)..."
    if [[ "$OS_TYPE" =~ (MINGW|MSYS|CYGWIN|Windows_NT) ]]; then
      # Windows: use netstat and taskkill
      PID=$(netstat -ano | grep ":$port" | grep LISTENING | awk '{print $5}' | head -n 1)
      if [ -z "$PID" ]; then
        # Try another way if above fails (for some netstat versions)
        PID=$(netstat -ano | grep ":$port" | awk '{print $5}' | head -n 1)
      fi
      if [ ! -z "$PID" ]; then
        taskkill //PID $PID //F > /dev/null 2>&1
        echo "✅ $service_name stopped (PID $PID)"
      else
        echo "ℹ️  $service_name was not running"
      fi
    else
      # Linux/macOS: use lsof and kill
      PID=$(lsof -ti:$port)
      if [ ! -z "$PID" ]; then
        kill -TERM $PID
        sleep 5
        if kill -0 $PID 2>/dev/null; then
          echo "⚠️  Force killing $service_name..."
          kill -KILL $PID
        fi
        echo "✅ $service_name stopped"
      else
        echo "ℹ️  $service_name was not running"
      fi
    fi
  done
  echo "🏁 All services stopped successfully!"
}

case "$1" in
  start)
    start_services
    ;;
  stop)
    stop_services
    ;;
  run)
    shift
    run_service "$@"
    ;;
  *)
    echo "Usage: $0 [start|stop|run <service-folder>]"
    exit 1
    ;;
esac