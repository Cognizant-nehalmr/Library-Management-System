:: Initialize databases and seed data
cd "C:\Program Files\MySQL\MySQL Server 8.0\bin" || exit
echo "Starting Library Management System Database Initialization..."

mysql -u root -proot < C:\Users\2407071\Desktop\GenC-Project\Library-Management-System-Microservices-Architecture\scripts\01-create-databases.sql
mysql -u root -proot < C:\Users\2407071\Desktop\GenC-Project\Library-Management-System-Microservices-Architecture\scripts\02-seed-data.sql