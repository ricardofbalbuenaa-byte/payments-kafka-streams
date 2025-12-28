# Guía de Pruebas – Payments Kafka Streams

Este documento describe cómo validar el correcto funcionamiento del sistema de procesamiento de pagos basado en Kafka Streams.

---

## 1. Levantar infraestructura Kafka

-- Desde la ruta ...\Fuentes\payments-kafka\fuentes:

docker compose up -d


-- Verificar contenedores:

docker ps

-- Crear el tópico payments:

docker exec -it payments-kafka-kafka-1 kafka-topics --create --topic payments --bootstrap-server localhost:9092 -

## 2. Ejecutar la aplicación

-- Levantar la aplicación (intelliJ Idea Recomendable)

## 3. Enviar eventos de pago (POST)

Conectar con POSTMAN

-- URL:
http://localhost:8080/api/v1/payments

-- Body:

{
  "timestamp": 123123,
  "cardId": "12345678",
  "amount": 100,
  "type": "A"
}

## 4. Consultar saldos (GET)

-- URL:
http://localhost:8080/api/v1/search


Respuesta esperada:

[
  {
    "cardId": "12345678",
    "total": 100.0
  }
]