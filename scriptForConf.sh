#!/bin/bash

db_name=postgres
password=z7J.6q

# Создаём базу данных и пользователя
psql -U postgres -c "CREATE DATABASE userdb;"
psql -U postgres -c "CREATE USER $db_user WITH ENCRYPTED PASSWORD '$password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE $db_name TO $db_user;"

# Обновляем application.yaml
echo "Обновляем application.yaml..."

config_file="src/main/resources/application.yaml"

sed -i "s/username: .*/username: $db_user/" $config_file
sed -i "s/password: .*/password: $password/" $config_file

echo "База данных и пользователь созданы, а файл application.yaml обновлен."
