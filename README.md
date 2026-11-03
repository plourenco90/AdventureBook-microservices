# Adventure Book — Microservices

The two backend services share one PostgreSQL instance but use separate schemas and Flyway migrations. Public routes pass through the gateway; the internal Book Service endpoint is not routed by the gateway.

## Run with Docker Compose

From the repository root:

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f
```

On the first run, Docker builds the three Java services and Angular frontend. PostgreSQL and Keycloak are initialized, and Flyway applies each service's migrations. The Keycloak realm is imported from `keycloak/adventure-realm.json` on a fresh Keycloak data volume. Wait for the services to become ready before opening the UI.

Open **http://localhost:4200** to use the application.

To stop containers while keeping database and Keycloak volumes:

```bash
docker compose down
```

To reset **all persisted application and Keycloak data**:

```bash
docker compose down -v
docker compose up --build -d
```

> `down -v` permanently deletes data in the Compose-managed volumes. Use only when a reset is intended. Keycloak realm import is not a reliable way to overwrite an already persisted realm; reset its volume when testing changes to the imported realm.

## Service URLs

| Component | URL | Purpose |
| --- | --- | --- |
| Angular application | http://localhost:4200 | User interface (Nginx proxies `/api/` to the gateway) |
| API Gateway | http://localhost:8080 | Public REST API entry point |
| Book Service | http://localhost:8082 | Direct service access / Swagger |
| Adventure Service | http://localhost:8083 | Direct service access / Swagger |
| Keycloak | http://localhost:8081 | Identity and token service |
| Keycloak admin console | http://localhost:8081/admin/ | Local development administration |
| PostgreSQL | localhost:5432 | Database connection (not a browser URL) |

### Swagger / OpenAPI

- **Book Service Swagger UI:** http://localhost:8082/api/swagger-ui/index.html
- **Book Service OpenAPI JSON:** http://localhost:8082/api/v3/api-docs
- **Adventure Service Swagger UI:** http://localhost:8083/api/swagger-ui/index.html
- **Adventure Service OpenAPI JSON:** http://localhost:8083/api/v3/api-docs

Swagger is exposed by each backend service individually, **not** through the gateway. The `/api` prefix comes from each service's servlet context path. For the internal Book Service operation, use a suitable OAuth2 client-credentials token (the Swagger security scheme is defined in `OpenApiSecurityConfig`).