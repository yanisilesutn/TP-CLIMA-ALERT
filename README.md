# Broker Template (Spring Boot + RabbitMQ)

Template genérico multi-servicio con **RabbitMQ ya configurado**. La idea es clonarlo y
construir la lógica de tu TP encima, sin tener que pelearte con la configuración del broker.

Trae dos servicios de ejemplo que demuestran el patrón **productor / consumidor**:

- **`service-emisor`** (puerto 8080) → publica eventos en RabbitMQ.
- **`service-receptor`** (puerto 8081) → consume esos eventos con un `@RabbitListener`.

---

## Requisitos previos (instalar antes de arrancar)

| Herramienta | Para qué | Cómo conseguirla |
|---|---|---|
| **Docker Desktop** | Levantar RabbitMQ (y opcionalmente los servicios) | https://www.docker.com/products/docker-desktop/ |
| **Java 21 (JDK)** | Compilar y correr los servicios | https://adoptium.net (Temurin 21) |
| **IntelliJ IDEA** (recomendado) | Correr los servicios y compilar (trae Maven incluido) | https://www.jetbrains.com/idea/ |

> **No hace falta instalar Maven aparte**: IntelliJ ya lo trae. Y si levantás todo con Docker
> (`docker compose up --build`), Maven y Java se usan **dentro** del contenedor, así que en ese
> caso ni siquiera necesitás Java/Maven en tu máquina — solo Docker.

**Importante:** antes de correr cualquier comando `docker`, abrí **Docker Desktop** y esperá a que
diga "running" (el ícono de la ballena). Si no, los comandos `docker` fallan con "cannot connect to
the Docker daemon".

> En Docker moderno el comando es `docker compose` (con espacio). Si tenés una versión vieja,
> puede ser `docker-compose` (con guión).

> **En Windows:** Docker Desktop usa **WSL2** por detrás. Al instalarlo, aceptá que active WSL2
> (si te lo pide, seguí el instalador). Los comandos `docker` funcionan igual desde PowerShell o CMD.
> Todo lo demás (Java, IntelliJ, los puertos) es idéntico en Mac y Windows.

---

## Arquitectura

```
[service-emisor]  --AMQP publish-->  [RabbitMQ: exchange "eventos" (topic)]  --AMQP consume-->  [service-receptor]
   EventoPublisher                        routing key: demo.mensaje.creado                          EventoListener -> ProcesadorEventoService
```

- **Exchange**: `eventos`, tipo **topic**, durable.
- **Routing key**: `demo.mensaje.creado`.
- **Cola**: `receptor.eventos.queue` (durable), bindeada al exchange con esa routing key.
- Serialización de mensajes: **JSON** (`JacksonJsonMessageConverter`).

El emisor solo **publica** al exchange (no conoce colas). El receptor **declara** el exchange +
cola + binding y **escucha** la cola. Si agregás más consumidores con otra routing key, cada uno
tiene su propia cola.

---

## Estructura del repo

```
broker-template/
├── pom.xml                  # POM padre (multi-módulo). Agregá cada servicio nuevo en <modules>.
├── docker-compose.yml       # RabbitMQ + los servicios
├── service-emisor/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/ar/edu/utn/frba/ddsi/emisor/
│       ├── EmisorApplication.java
│       ├── config/          # RabbitMQConfig (exchange + template + converter)
│       ├── controllers/     # endpoint de ejemplo para publicar
│       ├── messaging/       # EventoPublisher (publica al broker)
│       ├── dto/             # el evento que viaja
│       ├── services/        # (tu lógica)
│       ├── models/          # (tu dominio)
│       └── repositories/    # (tu persistencia)
└── service-receptor/
    └── src/main/java/ar/edu/utn/frba/ddsi/receptor/
        ├── ReceptorApplication.java
        ├── config/          # RabbitMQConfig (exchange + cola + binding + converter)
        ├── messaging/       # EventoListener (@RabbitListener)
        ├── services/        # ProcesadorEventoService (tu lógica al recibir)
        ├── dto/             # copia del evento
        ├── controllers/     # (si expone REST)
        ├── models/          # (tu dominio)
        └── repositories/    # (tu persistencia)
```

---

## Cómo levantarlo

### 0. Configurar variables de entorno
El repo trae un **`.env.example`** con las credenciales de RabbitMQ (y espacio para las de tu TP).
Copialo a `.env` y ajustá lo que quieras:
```bash
# Mac / Linux
cp .env.example .env

# Windows (PowerShell)
Copy-Item .env.example .env

# Windows (CMD)
copy .env.example .env
```
- El `.env` **no se sube a git** (está en el `.gitignore`) — ahí van los secretos.
- Igual funciona **sin `.env`**: el `docker-compose` tiene defaults (`guest`/`guest`, puertos estándar).
- Si cambiás `RABBITMQ_USER`/`RABBITMQ_PASS`, tanto Rabbit como los servicios los toman del `.env`
  automáticamente.

### 1. Levantar RabbitMQ
Con Docker Desktop abierto, parado en la raíz del proyecto (donde está el `docker-compose.yml`):
```bash
docker compose up -d rabbitmq
```
La primera vez baja la imagen (tarda un minuto). Verificá que quedó corriendo:
```bash
docker ps            # tenés que ver el contenedor "rabbitmq" en estado Up
```
Panel de administración: http://localhost:15672 (usuario `guest`, pass `guest`).

Para frenarlo: `docker compose stop` · Para frenar y borrar (incluye datos): `docker compose down -v`

### 2. Correr los servicios
**Opción A — desde el IDE (recomendado para desarrollar):** corré `EmisorApplication` y
`ReceptorApplication`. Apuntan a `localhost:5672` por default.

**Opción B — todo en Docker:**
```bash
docker compose up --build
```

### 3. Probar el flujo
Publicá un evento con el endpoint de ejemplo del emisor:
```bash
# Mac / Linux
curl -X POST http://localhost:8080/emisor/eventos \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Hola","contenido":"Primer evento por el broker"}'
```
```powershell
# Windows (PowerShell) — ojo: usá "curl.exe", porque "curl" a secas es un alias distinto.
curl.exe -X POST http://localhost:8080/emisor/eventos -H "Content-Type: application/json" -d "{\"titulo\":\"Hola\",\"contenido\":\"Primer evento\"}"
```
> Lo más fácil para todos: usar el botón **"Try it out"** de la API en el navegador, o Postman,
> y evitar líos de comillas entre sistemas operativos.
En la consola del **receptor** deberías ver:
```
Evento recibido de la cola: Hola
Procesando evento -> titulo='Hola', ...
```
En el panel de Rabbit → **Queues** vas a ver `receptor.eventos.queue`, y en **Exchanges** el
exchange `eventos` con su binding.

---

## Cómo adaptarlo a tu TP

1. **Renombrá** los servicios/paquetes según tu dominio (`emisor`/`receptor` son solo ejemplos).
2. Cambiá el evento `MensajeEvento` por el/los evento(s) de tu dominio (mismos nombres de campo en
   emisor y receptor para que deserialice).
3. Definí tus **exchange / routing keys / colas** en los `application.properties` (que coincidan
   entre productor y consumidor).
4. Meté tu lógica: en el emisor, llamá a `EventoPublisher.publicar(...)` desde tu service cuando
   ocurra el evento; en el receptor, poné la lógica real en `ProcesadorEventoService`.
5. Para **agregar un servicio nuevo**: copiá una carpeta de servicio, agregala en `<modules>` del
   POM padre y en el `docker-compose.yml`, y ajustá el puerto.

---

## Notas
- Requiere **Java 21** y **Docker**.
- Spring Boot 4.0.5 / Spring AMQP 4 (por eso el converter es `JacksonJsonMessageConverter`; en
  Spring Boot 3 sería `Jackson2JsonMessageConverter`).
- Las colas y el exchange son **durables**: sobreviven a reinicios del broker.
