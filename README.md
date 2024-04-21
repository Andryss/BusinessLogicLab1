# Пародия на rutube

Бэкенд видео-хостинга. Позволяет загружать видео, смотреть, оставлять комментарии и реакции.

Все загруженные видео отправляются на модерацию и могут быть опубликованы только после успешного ее прохождения.

Бэкенд разбит на два сервиса: публичный сервис (для посетителей сайта и авторов), админка (для модераторов).

## Спецификация Swagger

[Публичный сервис](rutube/src/main/resources/api.yaml)

[Админка](rutube-admin/src/main/resources/api.yaml)

## Запуск

Для корректной работы сервисов потребуется: 
* [PostgreSQL](https://www.postgresql.org/download/)
* [ZooKeeper + Apache Kafka](https://kafka.apache.org/downloads)
* [Nginx](https://nginx.org/en/download.html)
* [Wildfly](https://www.wildfly.org/downloads/)
* Минимум один почтовый ящик `@yandex.ru` (для отправки писем от лица сервиса)

### Публичный сервис

* Собрать war-файл (из директории `rutube`):
```bash
./mvnw package
```

* Сделать копию `standalone` директории внутри Wildfly и назвать `rutube`

* Положить собранный war-файл по пути `<path_to_wildfly>/rutube/deployments`

* Запустить сервис:
```bash
<path_to_wildfly>/bin/standalone -Djboss.server.base.dir=<absolute_path_to_wildfly>/rutube
```

Сервис будет слушать порт `8080`.

### Админка

* Собрать war-файл (из директории `rutube-admin`):
```bash
./mvnw package
```

* Сделать копию `standalone` директории внутри Wildfly и назвать `rutube-admin`

* Положить собранный war-файл по пути `<path_to_wildfly>/rutube-admin/deployments`

* Запустить сервис (в двух копиях):
```bash
<path_to_wildfly>/bin/standalone.sh -Djboss.server.base.dir=<absolute_path_to_wildfly>/rutube-admin -Djboss.socket.binding.port-offset=1111 -Dspring.profiles.active=master
<path_to_wildfly>/bin/standalone.sh -Djboss.server.base.dir=<absolute_path_to_wildfly>/rutube-admin -Djboss.socket.binding.port-offset=1212
```

* Запустить Nginx с [заданной конфигурацией](rutube-admin/nginx.conf)

Два экземпляра сервиса будут слушать порты `9191` и `9292`, а Nginx-балансер будет слушать порт `9090`.

### Конфигурация

Конфигурировать сервисы можно перед запуском через переменные окружения.

Пример переменных окружения для сервисов:

```text
RUT_DB_URL=jdbc:postgresql://localhost:5432/rutube
RUT_DB_USER=rutube
RUT_DB_PASS=<some-pass>
RUT_JWT_SECRET=<some-secret>
RUT_KFK_SERVERS=localhost:9092
RUT_MAIL_USER=<some-user>
RUT_MAIL_PASS=<some-pass>

RUTA_DB_URL=jdbc:postgresql://localhost:5432/rutube
RUTA_DB_USER=rutube_admin
RUTA_DB_PASS=<some-pass>
RUTA_JWT_SECRET=<some-secret>
RUTA_KFK_SERVERS=localhost:9092
RUTA_MAIL_USER=<some-user>
RUTA_MAIL_PASS=<some-pass>
```

Также часть конфигурации можно осуществлять через `.properties` файлы:

[Публичный сервис](rutube/src/main/resources/application.properties)

[Админка](rutube-admin/src/main/resources/application.properties)
