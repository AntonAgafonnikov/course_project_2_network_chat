# Курсовой проект "Сетевой чат"

## Описание проекта

Проект представляет собой два приложения для обмена
текстовыми сообщениями по сети с помощью консоли (терминала)
между двумя и более пользователями.

Первое приложение - сервер чата, ожидает подключения 
пользователей.

Второе приложение - клиент чата, подключается к серверу 
чата и осуществляет доставку и получение новых сообщений.

Все сообщения записываются в file.log как на сервере,
так и на клиентах. File.log дополняется при 
каждом запуске, а также при отправленном или 
полученном сообщении. Выход из чата быть осуществляется по
команде exit.

## Краткая характеристика

 - Java 11;
 - Модульная структура проекта;
 - Каждый новый клиент подключается и обрабатывается в новом потоке;
 - Файловый логер.