# Skilland-backend

Чтобы запустить проект, необходимо склонировать репозиторий. Далее запустить сервис базы данных выполнив команду "docker-compose up db"
в терминале корневой дирректории проекта. 
Далее запустить сам проект DemoApplication.

смотреть API на http://localhost:8080/swagger-ui/index.html

Весь новый функционал будет отображаться посредством пополнения endpoints , которые можно увидеть на сваггере. Для этого переодически
запрашивать "git pull".

Общение клиент-сервис проиходит через HTTP и Websockets. Websockets используются только во время процесса игры для обмена текущим состоянием игры между
всеми учасниками. Тестирование websockets осуществляется по URI http://localhost:8080/ (пока что добавлены базовые функции). Тестирование HTTP запросов 
на http://localhost:8080/swagger-ui/index.html
