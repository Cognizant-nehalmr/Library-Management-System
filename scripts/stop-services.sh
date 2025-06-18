echo "🛑 Stopping Library Management System Services..."

# Create logs directory if it doesn't exist
mkdir -p logs

# Function to stop service by port
stop_service_by_port() {
    local port=$1
    local service_name=$2
    
    echo "🛑 Stopping $service_name (port $port)..."
    
    # Find process using the port
    PID=$(lsof -ti:$port)
    
    if [ ! -z "$PID" ]; then
        kill -TERM $PID
        sleep 5
        
        # Check if process is still running
        if kill -0 $PID 2>/dev/null; then
            echo "⚠️  Force killing $service_name..."
            kill -KILL $PID
        fi
        
        echo "✅ $service_name stopped"
    else
        echo "ℹ️  $service_name was not running"
    fi
}

# Stop services in reverse order
services=("notification-service:8085" "fine-service:8084" "transaction-service:8083" "member-service:8082" "book-service:8081" "api-gateway:8080" "eureka-server:8761")

for service in "${services[@]}"; do
    IFS=':' read -r service_name port <<< "$service"
    stop_service_by_port $port "$service_name"
done

echo "🏁 All services stopped successfully!"
