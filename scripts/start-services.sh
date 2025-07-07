echo "Starting Library Management System Services..."

# Function to check if a service is running
check_service() {
    local port=$1
    local service_name=$2
    
    if curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
        echo "✅ $service_name is running on port $port"
        return 0
    else
        echo "❌ $service_name is not responding on port $port"
        return 1
    fi
}

# Start Eureka Server first
echo "🚀 Starting Eureka Server..."
cd eureka-server
mvn spring-boot:run > ../logs/eureka-server.log 2>&1 &
EUREKA_PID=$!
cd ..

# Wait for Eureka Server to start
echo "⏳ Waiting for Eureka Server to start..."
sleep 30

# Check if Eureka is running
if check_service 8761 "Eureka Server"; then
    echo "✅ Eureka Server started successfully"
else
    echo "❌ Failed to start Eureka Server"
    exit 1
fi

# Start other services
services=("api-gateway:8080" "book-service:8081" "member-service:8082" "transaction-service:8083" "fine-service:8084" "notification-service:8085")

for service in "${services[@]}"; do
    IFS=':' read -r service_name port <<< "$service"
    echo "🚀 Starting $service_name..."
    
    cd $service_name
    mvn spring-boot:run > ../logs/$service_name.log 2>&1 &
    cd ..
    
    # Wait a bit before starting next service
    sleep 15
done

echo "⏳ Waiting for all services to start..."
sleep 60

# Check all services
echo "🔍 Checking service health..."
all_healthy=true

for service in "${services[@]}"; do
    IFS=':' read -r service_name port <<< "$service"
    if ! check_service $port "$service_name"; then
        all_healthy=false
    fi
done

if $all_healthy; then
    echo "🎉 All services are running successfully!"
    echo "📚 Library Management System is ready!"
    echo ""
    echo "🌐 Access Points:"
    echo "   - API Gateway: http://localhost:8080"
    echo "   - Eureka Dashboard: http://localhost:8761"
    echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
    echo ""
    echo "📖 Service Documentation:"
    echo "   - Book Service: http://localhost:8081/swagger-ui.html"
    echo "   - Member Service: http://localhost:8082/swagger-ui.html"
    echo "   - Transaction Service: http://localhost:8083/swagger-ui.html"
    echo "   - Fine Service: http://localhost:8084/swagger-ui.html"
    echo "   - Notification Service: http://localhost:8085/swagger-ui.html"
else
    echo "⚠️  Some services failed to start. Check the logs in the 'logs' directory."
fi

