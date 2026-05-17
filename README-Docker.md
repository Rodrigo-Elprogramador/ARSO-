# Puesta en marcha con Docker Compose

Desde la raiz del repositorio:

```bash
docker-compose up -d
```

La pasarela queda publicada en:

- Backend por pasarela: `http://localhost:8080`
- Usuarios directo: `http://localhost:8081`
- Productos directo: `http://localhost:8082`
- Compraventas directo: `http://localhost:8083`
- RabbitMQ Management: `http://localhost:15672` (`guest` / `guest`)

Servicios desplegados:

- `usuarios`: microservicio JAX-RS desplegado en Tomcat.
- `productos`: microservicio Spring Boot con MySQL.
- `compraventas`: microservicio Spring Boot con MongoDB.
- `pasarela`: API Gateway Zuul con login JWT.
- `mysql`: una instancia con las bases `usuarios_db` y `productos_db`.
- `mongodb`: base de datos de compraventas.
- `rabbitmq`: bus de eventos con exchange `bus`.

## Datos de prueba

Al arrancar se crean datos minimos para ejecutar la coleccion Postman:

- `juan@example.com` / `1234` con rol `USUARIO`
- `maria@example.com` / `1234` con rol `USUARIO`
- `admin@example.com` / `admin` con roles `USUARIO`, `ADMINISTRADOR`
- Categorias iniciales en Productos.

## Postman

Importar:

- `postman/ARSO-Tarea9.postman_collection.json`
- `postman/ARSO-Tarea9.postman_environment.json`

Ejecutar la coleccion completa usando el entorno `ARSO Docker Local`.

Para repetir la prueba desde cero:

```bash
docker-compose down -v
docker-compose up -d
```
