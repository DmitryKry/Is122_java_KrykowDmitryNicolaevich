#!/bin/bash

# Запросим пароль от пользователя
echo "Введите пароль для пользователя PostgreSQL: "
read -s password

# Запрашиваем название базы данных
echo "Введите логин для вашего сервера SQL: "
read db_name


# Создаём базу данных и пользователя
psql -U postgres -c "CREATE DATABASE userdb;"
psql -U postgres -c "CREATE USER $db_user WITH ENCRYPTED PASSWORD '$password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE $db_name TO $db_user;"

# Обновляем application.yaml (предположим, что файл в текущей директории)
echo "Обновляем application.yaml..."

# Убедитесь, что указали правильный путь к файлу application.yaml
config_file="src/main/resources/application.yaml"

# Изменение username и password в application.yaml
sed -i "s/username: .*/username: $db_user/" $config_file
sed -i "s/password: .*/password: $password/" $config_file

echo "База данных и пользователь созданы, а файл application.yaml обновлен."
