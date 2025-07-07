:: Initialize databases and seed data
cd "C:\Program Files\MySQL\MySQL Server 8.0\bin" || exit
echo "Starting Library Management System Database Initialization..."

mysql -u root -proot < C:\Users\2407076\OneDrive - Cognizant\Desktop\Frontend\Library-Management-System-ms\scripts\01-create-databases.sql
mysql -u root -proot < C:\Users\2407076\OneDrive - Cognizant\Desktop\Frontend\Library-Management-System-ms\scripts\02-seed-data.sql