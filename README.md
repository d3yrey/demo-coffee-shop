# Coffee Shop REST API

RESTful backend for a coffee shop that manages menu items, orders with a simple lifecycle, and business statistics (revenue and sold items).

## Build & run (PostgreSQL in Docker, default)

Prerequisites:
- Docker Desktop
 - Java 21+
 - Gradle wrapper (already in the project)

From the project root, start PostgreSQL + pgAdmin:

```bash
docker compose up -d
```

PostgreSQL:
- Host: `localhost`
- Port: `5433`
- Database: `coffeeshop`
- User: `coffeeshop`
- Password: `coffeeshop`

pgAdmin UI:
- URL: `http://localhost:5050`
- Email: `admin@coffeeshop.local`
- Password: `admin`

In pgAdmin, register a new server:
- Host name/address: `postgres`
- Port: `5432`
- Username: `coffeeshop`
- Password: `coffeeshop`
- Database: `coffeeshop`

Run the Spring Boot app:

```bash
./gradlew bootRun
```

On Windows:

```bash
.\gradlew.bat bootRun
```

The application will be available at `http://localhost:8082` and use the PostgreSQL database running in Docker.

Stop containers when you are done:

```bash
docker compose down
```

## API documentation (Swagger / OpenAPI)

This project uses **springdoc-openapi** to expose Swagger UI for exploring and testing the REST API.

- After starting the app, open:
  - `http://localhost:8082/swagger-ui.html`  
    or  
  - `http://localhost:8082/swagger-ui/index.html`
- The OpenAPI description is available at:
  - `http://localhost:8082/v3/api-docs`

Note: For this assignment project, Swagger / OpenAPI is intended for **local development only**.  
The dependency transitively pulls in a Jackson version (`jackson-core:2.20.2`) that has a known DoS advisory (GHSA-72hv-8253-57qq). In a production system you would pin Jackson to a fixed version (for example 2.21.1+) or follow your organization’s security guidance; here we accept it for simplicity since the service is not exposed publicly.

## How a mobile app would know an order is READY

In a real system, a mobile client needs to be notified when the barista changes an order status from `ACCEPTED` to `READY`. A simple approach is **Server‑Sent Events (SSE)** or **WebSockets**. Both keep a long‑lived connection open from the mobile app to the backend, so the server can push a status change immediately instead of the app constantly polling.

I would choose **WebSockets** for this use case. The mobile app would open a WebSocket connection (for example, `/ws/orders/{orderId}`) after placing an order. When the barista updates the order status on the server, the backend sends a small JSON message like `{"orderId": 123, "status": "READY"}` over the WebSocket to that client. This gives near real‑time updates, avoids wasteful short polling, and works well when the number of concurrent connected clients (waiting for drinks) is relatively small, as in a coffee shop scenario.

