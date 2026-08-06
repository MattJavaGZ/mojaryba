# env
## prod: www.moja-ryba.pl

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
