# Innotech
перед запуском приложения необходимо заполнить поля:

username: почта с которой будет производиться отправка (gmail.com)
password: пароль (необходимо получить пароль для старых приложений, делается через функционал gmail.com)
mailTo: получатель почты (можно реализовать с использованием бд)
subjectForEmail: заголовок для отправляемого сообщения


для запуска проекта делаем билд ./gradlew build 
после запускаем командой docker-compose up --build
