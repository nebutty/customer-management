
# Customer Management — Local Deployment

## Prerequisites
- Docker Desktop
- Git
- Java 21 and Maven Wrapper for local builds

## Environment variables
Create a local `.env` file using `.env.example` as a template.

Required variables:
- POSTGRES_ADMIN_PASSWORD
- DB_USERNAME
- DB_PASSWORD
- DB_MIGRATION_USERNAME
- DB_MIGRATION_PASSWORD

Never commit `.env` or put real credentials in the Docker image.

## Start the application
Run from the project root:

```powershell
docker compose up --build -d
```

## Check containers
```powershell
docker compose ps
```

## Health endpoints
- http://localhost:8080/actuator/health
- http://localhost:8080/actuator/health/liveness
- http://localhost:8080/actuator/health/readiness

## View logs
```powershell
docker compose logs --tail=100 api
docker compose logs -f api
```

The application writes structured JSON logs to the console.
The correlation ID is carried in the `X-Correlation-ID` header.

## Stop the application
```powershell
docker compose down
```

This preserves the PostgreSQL named volume. Do not use `down -v`
unless you intentionally want to delete the database volume.

## CI
GitHub Actions runs Maven verification and builds a Docker image
for pushes and pull requests targeting `main` or `develop`.
