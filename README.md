# Teclead Task Apllication

Diese Applikation ist eine kleine REST Anwendung, mit CRUD Funktionalität. Es kann ein User angelegt, gelöscht und aktualisiert werden. Zusätzlich besteht die Möglichkeit nach dem Vornamen zu filtern.

Beim Starten der Anwendung werden zehn Beispielwerte in die embedded Datenbank geschrieben. 

Das User Objekt ist folgendermaßen aufgebaut:
```json
{
  "name": "Keßel",
  "vorname": "Martin",
  "email": "martin@kessel.de"
}
```

Das Attribute `name` ist notwendig und das Feld `email` wird, wenn es gesetzt wurde nach 
korrekter Syntax validiert.

## Swagger UI
Weitere detaillierte Beschreibungen zu den REST Endpunkten finden sich unter folgendem Link, 
allerdings erst wenn die Anwendung läuft.
http://localhost:8080/swagger-ui/index.html

## H2 Console
Die angelegten Objekte lassen sich unter dem Link einsehen:
http://localhost:8080/h2-console

Die Datenbank Properties sind in der `application.properties` zu finden.

## Profile
In der `application.properties` findet sich folgende Property `spring.profiles.active`.

Über diese Property läßt sich das Profil bestimmen. Es gibt zwei Profile _dev_ und _prod_. 
Der Unterschied besteht in dem Port: _8080_ für _dev_ und _9090_ für _prod_.

## Start der Anwendung
Die Anwendung lässt sich aus dem Repository einmal über das Kommaondo:

```
mvn spring-boot:run
```

starten, oder auch über das Dockerfile:

```
docker build -t teclead-app .
docker run -d -p 8080:8080  -it teclead-app
```
