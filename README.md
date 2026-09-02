# Orizon Spring Boot

Spring Boot application with Docker Compose support for different environments.

## 🚀 Running with Docker Compose

The project provides separate Docker Compose configurations for **Development** and **Production** environments.

### 📋 Prerequisites

Make sure you have installed:

- [Docker](https://docs.docker.com/get-docker/)
- Docker Compose

### 🔧 Development

The development environment runs the application in the foreground, making it easier to view logs during development.

```bash
docker compose --env-file .env.dev -f docker-compose.yml -f docker-compose.dev.yml up -d
```

### 🔧 Production

To run the application with production environment run this command

```bash
docker compose --env-file .env.prod -f docker-compose.yml -f docker-compose.prod.yml up -d
```