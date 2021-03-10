# Web app that remotely controls the solenoid via BraveGATE

This web app is a sample of remotely controls the solenoid via BraveGATE based on Spring Boot (with Kotlin).

- What is [BraveGATE](https://www.braveridge.com/product/archives/7)
  - [日本語](https://www.braveridge.com/product/archives/7)
  - [English](https://www.braveridge.com/en/product/archives/6)

## Requirements
- JDK 11

## How to run on localhost

### Edit your auth key id and secret, device id of BraveGATE
```bash
vi src/main/resources/config/application.yml
```

### run
```bash
./gradlew bootRun
```

access to http://localhost:8080/solenoid

## Receive webhook of solenoid state from BraveGATE
If you need to receive webhook from BraveGATE, you must specify webhook URL.

[see detail](./tools/bravegate/api/curl.md)

### simulate webhook on localhost
```bash
tools/bravegate/webhook/webhook.sh
```

