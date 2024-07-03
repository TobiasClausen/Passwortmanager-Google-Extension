# Basis-Image mit Java-Runtime verwenden
FROM openjdk:8-jdk-alpine

# Arbeitsverzeichnis erstellen
WORKDIR /app

# JAR-Datei in das Arbeitsverzeichnis kopieren
COPY target/Passwort-Manager-Backend-1.0-SNAPSHOT.jar app.jar

# Port (optional) freigeben, falls die JAR-Datei einen Webserver startet
EXPOSE 8080

# Befehl zum Ausführen der JAR-Datei
ENTRYPOINT ["java", "-jar", "app.jar"]