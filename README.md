# Moja-ryba

## 📋 O Projekcie

**Moja-ryba** Aplikacja służąca do dodawania swoich złowionych ryb. Możliwość komentowania, oceniania, lajkowania itp. 
Każdy użytkownik ma dostęp do swojego dziennika połowów.

### prod: www.moja-ryba.pl

- **Język**: Java 21
- **Framework**: Spring Boot 3.4.5
- **Baza danych**: MySQL (produkcja) / H2 (development)
- **Szablony**: Thymeleaf
- **ORM**: JPA/Hibernate
- **Migracje**: Liquibase

# 🔐 Konfiguracja email

Konfiguracja dla smtp.poczta.onet.pl

## application.yml

```yaml
app:
  email:
    login: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
```

# 🔐 Konfiguracja DATABASE

## application.prod.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/mojaryba
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
```
