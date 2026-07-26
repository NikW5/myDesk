# myDesk

Eine moderne Java-basierte Anwendung für Desktop-Management und Produktivität.

## Übersicht

**myDesk** ist ein Spring Boot Projekt mit Vaadin UI-Framework, das eine benutzerfreundliche Webanwendung zur Verwaltung von Desktop-Aufgaben und Produktivität bietet.

## Technologie-Stack

- **Backend:** Java 21, Spring Boot 4.1.0
- **Frontend:** Vaadin 25.2.0
- **Build-Tool:** Maven
- **Framework:** Spring Boot WebMVC

## Anforderungen

- Java 21 oder höher
- Maven 3.6+

## Installation & Setup

### Projekt klonen
```bash
git clone https://github.com/NikW5/myDesk.git
cd myDesk
```

### Anwendung starten
```bash
./mvnw spring-boot:run
```

Die Anwendung ist dann verfügbar unter: http://localhost:8080

## Projektstruktur
```
myDesk/
├── src/
│   ├── main/java/          # Java Source Code
│   ├── main/resources/     # Konfigurationsdateien
│   └── test/               # Unit Tests
├── pom.xml                 # Maven Konfiguration
├── mvnw / mvnw.cmd         # Maven Wrapper
└── .mvn/                   # Maven Wrapper Konfiguration
```

## Features

- Benutzerfreundliche Web-Oberfläche mit Vaadin
- Spring Boot Backend für schnelle Entwicklung
- Responsive Design für verschiedene Geräte
