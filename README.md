# Moja-ryba

## 📋 O projekcie

**Moja-ryba**   Aplikacja dla wędkarzy służąca do dodawania zdjęć, opisów złowionych ryb. 
                Możliwość komentowania, oceniania, lajkowania, dodawania do ulubionych.
                Każdy użytkownik ma dostęp do swojego dziennika połowów.
                Panel administratora umożliwia zarządzanie użytkownikami oraz powiadomieniami mailowymi do użytkowników.

**### Produkcja: www.moja-ryba.pl**

- **Język**: Java 21
- **Framework**: Spring Boot 3.4.5
- **Baza danych**: MySQL (produkcja) / H2 (development)
- **Szablony**: Thymeleaf
- **ORM**: JPA/Hibernate
- **Migracje**: Liquibase

**Uruchomienie aplikacji na serwerze VPS**

Aplikacja może zostać uruchomiona na serwerze VPS przy użyciu Dockera. Poniżej przedstawiono przykładową konfigurację środowiska produkcyjnego.

**1. Przygotowanie serwera**

Do uruchomienia aplikacji wymagany jest serwer VPS z zainstalowanym Dockerem oraz MySQL.

**2. Utworzenie bazy danych**

W MySQL należy utworzyć bazę danych o nazwie: mojaryba

**3. Utworzenie sieci Docker**

Tworzymy dedykowaną sieć Docker, która umożliwi komunikację pomiędzy kontenerem aplikacji oraz kontenerem MySQL:
```bash
docker network create mojaryba-network
```
Następnie należy dodać kontener MySQL do utworzonej sieci:
```bash
docker network connect mojaryba-network <mysql-container-name>
```
Dzięki temu aplikacja może komunikować się z bazą danych poprzez sieć Docker.

**4. Uruchomienie aplikacji**

Aplikację uruchamiamy jako kontener Docker, przekazując wymagane zmienne środowiskowe.

Aplikacja wymaga również dwóch wolumenów:

mojaryba-volume:/uploads/photos – przechowywanie zdjęć
mojaryba-volume-miniature:/uploads/miniatures – przechowywanie miniaturek zdjęć.

**Przykładowa komenda:**
```bash
docker run -d \
--name=mojaryba \
-v mojaryba-volume:/uploads/photos \
-v mojaryba-volume-miniature:/uploads/miniatures \
-e EMAIL_USERNAME=<EMAIL_USERNAME> \
-e EMAIL_PASSWORD=<EMAIL_PASSWORD> \
-e DATABASE_USERNAME=<DATABASE_USERNAME> \
-e DATABASE_PASSWORD=<DATABASE_PASSWORD> \
-e SPRING_PROFILES_ACTIVE=prod \
--network mojaryba-network \
--restart unless-stopped \
mojaryba
```
**Zmienne środowiskowe**

EMAIL_USERNAME	        Adres e-mail wykorzystywany przez aplikację do wysyłania wiadomości
EMAIL_PASSWORD	        Hasło do konta e-mail
DATABASE_USERNAME	    Użytkownik bazy danych
DATABASE_PASSWORD	    Hasło użytkownika bazy danych
SPRING_PROFILES_ACTIVE	Aktywny profil Spring Boot, w tym przypadku prod


# 🔐 Konfiguracja email
Konfiguracja dla smtp.poczta.onet.pl
## application.yml

```yaml
app:
  email:
    login: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
```

# 🔐 Konfiguracja bazy danych
## application-prod.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/mojaryba
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
```
