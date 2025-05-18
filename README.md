# Веб-приложение "Социальная сеть"
## Главная цель веб-приложения: 
###### Предоставить возможность пользователям интернета общаться между собой, создавать беседы и искать новых собеседников

## Инструкция по запуску проекта

###### Сперва, прежде чем запустить проект - нужно установить всё необходимое для работы приложения. 
###### Перейдите на официальный сайт Maven и скачайте последнюю версию: (https://maven.apache.org/download.cgi).

###### После чего нужно будет добавить Maven в переменные среды окружения ОС, как это сделать и на Windows 10 и на Linux, указано по ссылке: (https://javarush.com/quests/lectures/questservlets.level01.lecture00)
###### Так же для запуска проекта требуется java, не ниже версии 17, а для проекта используется версия java - 17.0.12 и её можно скачать на все популярные виды ОС по ссылке: (https://www.java.com/ru/download/manual.jsp)
###### А так же необходима база данных PostgresSQL, которую так же можно скачать на все популярные ОС по ссылке: (https://www.postgresql.org/download/)
###### После чего нужно добавить файл bin базы данных в переменные среды окружения ОС, по примеру Maven.
###### Так же нужно установить программу Git Bash по ссылке: (https://git-scm.com/downloads/win)
## Запуск проекта
###### В первую очередь нужно подключиться к базе данных и для это нужно запустить скрипт scriptForConf.sh, но сперва нужно дать ему права на выполнение, командой: `chmod +x scriptForConf.sh`.
###### После чего нужно будет запустить скрипт командой: `./scriptForConf.sh` и указать логин для вашего сервера SQL и пароль, после чего будет создана таблица userdb и будет изменена конфигурация для подключения в application.yaml

###### Дальше нужно будет собрать проект Maven. Для сборки проекта Maven выполните одну из следующих команд: `mvnd clean package` или `mvn clean package`.
###### После чего, для запуска самого проекта - выполните команду:

   <pre>java -jar target/SimpleChatNewJava17-0.0.1-SNAPSHOT.jar</pre>
###### После этого откройте браузер и перейдите по адресу: (http://localhost:8080/api/v1/entrance).
###### Чтобы завершить работу проекта, в командной строке зажмите комбинацию клавиш Ctrl+C.
###### ВАЖНОЕ ПРЕДУПРЕЖДЕНИЕ: в первую очередь проект оринтирован на ОС Windows 10 и на базу данных postgreSQL, если вы желаете запустить проект на другой ОС, на пример: Linux или MacOC, то никаких изменений почти не потребуется, главное пройтись по интрукции, которая указана выше.
###### Но если вдуруг вы захотите использовать другою БД, то нужно учеть то факт, что проект может работать и на MySQL и SQLite, для них добавлены все зависимости, но только придёться в ручную лезть в конфигурационный файл application.yaml и менять параметры, по примеру:
###### Для SQLite:
```yaml
spring:
  datasource:
    url: jdbc:sqlite:./data/mydb.db # Пример: путь к БД, только укажите после jdbc:sqlite: ваш путь к файлу
    driver-class-name: org.sqlite.JDBC
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.SQLiteDialect
    show-sql: true
    defer-datasource-initialization: true
    properties:
      hibernate:
        format_sql: true
  devtools:
    restart:
      enabled: true
    livereload:
      enabled: true
  sql:
    init:
      mode: always
```
###### И для MySQL:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/userdb # Пример: url, после jdbc:mysql: укажите ваш url
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    database: mysql
    database-platform: org.hibernate.dialect.MySQL8Dialect
    show-sql: true
    defer-datasource-initialization: true
    properties:
      hibernate:
        format_sql: true
  devtools:
    restart:
      enabled: true
    livereload:
      enabled: true
  sql:
    init:
      mode: always
```
###### Так же нужно будет создавать самому БД и уже после этого собирать проект.
###### Но если вы хотите использовать другую БД, то тогда вам придёться в файл pom.xml добавить зависимость для вашей базы данных в блок dependencies, нужную зависимость можно будет найти по ссылке: (https://mvnrepository.com)









