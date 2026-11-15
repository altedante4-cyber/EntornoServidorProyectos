# D1-UD3 · Formularios encadenados

Proyecto Spring Boot del quizz de la UD3. Mantiene las respuestas entre pasos sin utilizar campos ocultos, con dos versiones independientes:

- `/v3`: guarda las respuestas en cookies.
- `/v1`: guarda las respuestas en `HttpSession`.

## Ejecución

Requisitos: Java 21.

```bash
./gradlew bootRun
```

Abrir http://localhost:8080

Si el puerto 8080 está ocupado:

```bash
./gradlew bootRun --args='--server.port=8081'
```

## Rutas principales

- `/` — pantalla de inicio con las dos versiones.
- `/v3` — quizz con cookies.
- `/v1` — quizz con `HttpSession`.
