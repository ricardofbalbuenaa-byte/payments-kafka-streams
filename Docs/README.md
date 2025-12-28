Payments Kafka Streams

Sistema de procesamiento de pagos en tiempo real basado en Apache Kafka Streams, desarrollado con Spring Boot 3.2.x y Kafka 3.x.

Implementa una arquitectura de event streaming con vistas materializadas que permite registrar movimientos financieros y consultar saldos actualizados por tarjeta.

- Arquitectura

Patrones implementados:

Event Sourcing

CQRS

Kafka Streams

State Store materializado

Interactive Queries

POST /api/v1/payments  →  Kafka Topic  →  Kafka Streams  →  KTable (State Store)
GET  /api/v1/search    ←----------------------------------------------←

- Requisitos
| Software     | Versión          |
| ------------ | ---------------- |
| Java         | 17               |
| Spring Boot  | 3.2.x            |
| Apache Kafka | 3.x              |
| Docker       | Última           |
| Maven        | Wrapper incluido |

- Levantar Kafka

Desde la ruta ...\Fuentes\payments-kafka\fuentes::

docker compose up -d

Crear topic payments

Después de levantar Kafka, ejecutar:

docker exec -it payments-kafka-kafka-1 kafka-topics --create \
  --topic payments \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1


Verificar que el topic fue creado:

docker exec -it payments-kafka-kafka-1 kafka-topics --list --bootstrap-server localhost:9092

- Ejecutar la aplicación
./mvnw spring-boot:run


(O ejecutar desde IntelliJ)

- Probar el sistema
- Registrar un pago
POST http://localhost:8080/api/v1/payments

{
  "timestamp": 123123,
  "cardId": "12345678",
  "amount": 100,
  "type": "A"
}

| Tipo | Descripción |
| ---- | ----------- |
| A    | Abono       |
| C    | Consumo     |

 - Consultar saldos
GET http://localhost:8080/api/v1/search


Respuesta:

[
  {
    "cardId": "12345678",
    "total": 100.0
  }
]

- Estructura del Proyecto
payments-kafka/
 ├─ src/
 ├─ docker-compose.yml
 ├─ docs/
 ├─ pom.xml
 └─ README.md

- Resultado

El sistema procesa eventos financieros en tiempo real, mantiene saldos consistentes y demuestra el uso de Kafka Streams como motor de agregación distribuida.
