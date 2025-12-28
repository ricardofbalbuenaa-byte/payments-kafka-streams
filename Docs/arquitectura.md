# Arquitectura del Sistema

El sistema implementa una arquitectura basada en Event Streaming utilizando Apache Kafka Streams.

El objetivo es mantener el saldo actualizado de cada tarjeta mediante el procesamiento de eventos financieros en tiempo real.

---

## Componentes

### 1. Cliente
Aplicaciones que consumen los servicios REST para registrar pagos y consultar saldos.

---

### 2. API REST (Spring Boot)
Exposición de endpoints:

| Método | Endpoint         | Descripción                       |
|--------|------------------|-----------------------------------|
| POST   | /api/v1/payments | Registra un movimiento financiero |
| GET    | /api/v1/search   | Consulta saldos por tarjeta       |

Los eventos recibidos son enviados al broker Kafka como mensajes inmutables.

---

### 3. Apache Kafka (Broker)
Actúa como fuente de la verdad.  
El topic `payments` almacena todos los eventos de forma persistente.

---

### 4. Kafka Streams
Procesa los eventos:

1. Consume los eventos desde `payments`
2. Normaliza montos (A = +, C = -)
3. Agrupa por `cardId`
4. Acumula saldos

---

### 5. State Store (KTable)
Vista materializada que mantiene el saldo actualizado de cada tarjeta

---

### 6. Interactive Queries
El endpoint `/search` consulta directamente el State Store sin reprocesar eventos.
