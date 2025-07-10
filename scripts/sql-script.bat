:: Initialize databases and seed data
cd "C:\Program Files\MySQL\MySQL Server 8.0\bin" || exit
echo "Starting Library Management System Database Initialization..."

mysql -u root -proot < \scripts\01-create-databases.sql
mysql -u root -proot < \scripts\02-seed-data.sql