# Lab: Pipeline DevOps con GitFlow y Docker
**Ingeniería de Software · 3er año · Ingeniería Informática**

> **Modalidad:** Parejas · **Duración:** 60 minutos · **Entrega:** Repositorio GitHub público

---
aaaa
## Contexto

Su equipo ha sido asignado para desarrollar la primera versión de una API REST de gestión de tareas (*to-do list*) para una startup. El tech lead exige que el equipo trabaje con **GitFlow**, que la aplicación esté **dockerizada** y que el pipeline de CI/CD publique automáticamente la imagen en el registro de contenedores de GitHub (GHCR) cada vez que se integre código a `main`.

No existe un servidor de despliegue, por lo que el alcance de este lab es llegar hasta la publicación de la imagen Docker en GHCR.

El proyecto base en Spring Boot será entregado al inicio de la sesión. Contiene únicamente el esqueleto de la aplicación: dependencias Maven configuradas y un controlador vacío. **No modifique el `pom.xml`.**

---

## Modelo de datos

Cada tarea tiene los siguientes atributos:

| Campo        | Tipo       | Requerido | Descripción                                      |
|--------------|------------|-----------|--------------------------------------------------|
| `id`         | `Long`     | Sí        | Identificador único, auto-incremental            |
| `titulo`     | `String`   | Sí        | Título descriptivo de la tarea                   |
| `prioridad`  | `String`   | Sí        | Uno de: `ALTA`, `MEDIA`, `BAJA`                  |
| `fechaLimite`| `String`   | No        | Fecha en formato `YYYY-MM-DD`; puede ser `null`  |
| `completada` | `boolean`  | Sí        | `false` por defecto al crear                     |

El estado de las tareas **se mantendrá en memoria** durante la ejecución de la aplicación. No se requiere base de datos.

---

## Endpoints requeridos

### `POST /tasks`
Crea una nueva tarea. Retorna `201 Created` con el objeto creado.

**Body (JSON):**
```json
{
  "titulo": "Revisar documentación de la API",
  "prioridad": "ALTA",
  "fechaLimite": "2025-06-30"
}
```

**Respuesta exitosa:**
```json
{
  "id": 1,
  "titulo": "Revisar documentación de la API",
  "prioridad": "ALTA",
  "fechaLimite": "2025-06-30",
  "completada": false
}
```

---

### `GET /tasks`
Retorna la lista de tareas. Soporta filtros opcionales por query params. Retorna `200 OK`.

| Query param  | Tipo     | Ejemplo                       | Comportamiento                              |
|--------------|----------|-------------------------------|---------------------------------------------|
| `prioridad`  | `String` | `?prioridad=ALTA`             | Filtra por nivel de prioridad               |
| `titulo`     | `String` | `?titulo=doc`                 | Filtra tareas cuyo título **contenga** el valor (insensible a mayúsculas) |
| `fechaLimite`| `String` | `?fechaLimite=2025-06-30`     | Filtra tareas con esa fecha límite exacta   |

Los filtros son **acumulables**: `GET /tasks?prioridad=ALTA&titulo=doc` retorna tareas de alta prioridad cuyo título contenga "doc".

Si no se pasan filtros, retorna todas las tareas.

---

### `PATCH /tasks/{id}/complete`
Marca la tarea con el `id` indicado como completada (`completada: true`). Retorna `200 OK` con la tarea actualizada. Si el `id` no existe, retorna `404 Not Found`.

---

## Estructura de ramas (GitFlow simplificado)

```
main
└── develop
    ├── feature/post-task          ← Integrante A
    └── feature/get-tasks-filter   ← Integrante B
    └── feature/complete-task      ← Ambos (al finalizar las anteriores)
```

> Las ramas `feature/post-task` y `feature/get-tasks-filter` se desarrollan **en paralelo** desde el inicio.

---

## Entregables

Al finalizar la sesión, el repositorio debe contener:

1. **Historial de ramas y PRs visible**: al menos 3 Pull Requests mergeados (las 3 features) más el PR final `develop` → `main`.
2. **Ambos integrantes con commits**: cada uno debe tener al menos un commit visible en el historial de `main`.
3. **`Dockerfile`** en la raíz del proyecto en `main`.
4. **`.github/workflows/docker-publish.yml`** en `main`.
5. **Al menos un workflow de GitHub Actions** en estado `success` en la pestaña Actions.
6. **Imagen publicada** y visible en GHCR del repositorio (paquete público).

Entregar la URL del repositorio al docente a través del canal indicado.

---

## Rúbrica de evaluación

| Criterio                        | Descripción                                                                  | Puntos |
|---------------------------------|------------------------------------------------------------------------------|--------|
| Estructura GitFlow              | Ramas correctas, 3+ PRs mergeados, mensajes de commit descriptivos          | 25     |
| `POST /tasks` funcional         | Crea tarea con todos los campos, retorna 201 y objeto completo               | 15     |
| `GET /tasks` con filtros        | Lista correcta; los 3 filtros funcionan y son acumulables                    | 15     |
| `PATCH /tasks/{id}/complete`    | Marca como completada; retorna 404 si el id no existe                        | 10     |
| Estado compartido en memoria    | Los 3 endpoints operan sobre el mismo estado (no listas independientes)      | 5      |
| Dockerfile correcto             | Multi-stage, imagen construye y corre sin errores                            | 15     |
| Pipeline CI/CD                  | Workflow ejecuta en push a main y publica imagen en GHCR                     | 10     |
| Colaboración visible            | Ambos integrantes tienen commits y al menos 1 PR aprobado por cada uno       | 5      |
| **Total**                       |                                                                              | **100**|

---

## Notas técnicas

- Para publicar en GHCR sin configurar secretos adicionales, usen `GITHUB_TOKEN`, disponible automáticamente en GitHub Actions. El repositorio y el paquete deben ser **públicos**.
- El estado en memoria se pierde al detener el contenedor; esto es esperado y no se evalúa.
- No se requiere manejo de errores avanzado: un `404` para IDs inexistentes es suficiente.
- Si terminan antes del tiempo, pueden agregar como bonus un endpoint `DELETE /tasks/{id}` siguiendo el mismo flujo GitFlow (rama `feature/delete-task` desde `develop`).
