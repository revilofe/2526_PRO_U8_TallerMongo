# AGENTS.md - Proyecto Taller MongoDB con Kotlin

## Descripción del Proyecto

Taller práctico de MongoDB adaptado desde Python a Kotlin. El objetivo es crear un taller educativo
que enseñe operaciones CRUD con MongoDB Atlas utilizando Kotlin como lenguaje de programación.

## Tecnología y Dependencias

- **Lenguaje**: Kotlin 2.3.0
- **JDK**: 21
- **Gestor de dependencias**: Gradle con Kotlin DSL
- **Driver MongoDB**: KMongo (driver Kotlin para MongoDB)
- **Conexión**: MongoDB Atlas (cloud.mongodb.com)

## Estructura del Proyecto

```
src/
├── main/kotlin/<paquete>/
│   ├── MongoConnection.kt      # Gestión de conexión a Atlas
│   ├── DatabaseManager.kt    # Gestión de bases de datos
│   ├── CollectionManager.kt   # Gestión de colecciones
│   ├── DocumentManager.kt     # Gestión de documentos (CRUD)
│   └── Main.kt                # Punto de entrada/demo
└── test/kotlin/<paquete>/
    └── DocumentManagerTest.kt # Tests de operaciones CRUD
```

## Reglas de Código

### Kotlin
- Usar la skill `kotlin-best-practices` para todo código Kotlin
- Preferir `val` sobre `var`
- Usar funciones de extensión de Kotlin
- Documentar con KDoc las funciones públicas
- Aplicar principios SOLID

### MongoDB
- Usar la skill `mongo-best-practices` para todas las operaciones
- Usar KMongo (driver Kotlin nativo)
- Validar datos antes de operar
- Manejar errores explícitamente
- No exponer credenciales en código (usar config o variables de entorno)

## Convenciones de Nomenclatura

- **Paquetes**: `org.iesra.tallermongo`
- **Clases**: `PascalCase`
- **Funciones y variables**: `camelCase`
- **Constantes**: `UPPER_SNAKE_CASE`
- **Colecciones MongoDB**: `plural` (ej: `productos`, `clientes`)
- **Bases de datos**: `snake_case` (ej: `tienda_online`)

## Configuración de Conexión

```kotlin
// Usar configuración externa, nunca credenciales hardcoded
data class MongoConfig(
    val uri: String,      // mongodb+srv://...
    val database: String  // nombre de BD
)
```

## Documentación del Taller

- El contenido del taller está en `doc/taller_mongodb.md`
- Mantener la misma estructura de módulos: BDs → Colecciones → Documentos → Proyecto
- Adaptar todos los ejemplos de Python a Kotlin usando KMongo
- Incluir ejercicios con soluciones

## Skills Disponibles en el Proyecto

- `kotlin-best-practices`: Buenas prácticas, clean code, SOLID, KDoc
- `mongo-best-practices`: Buenas prácticas con MongoDB y KMongo

## Workflow de Desarrollo

1. Estudiar el documento `doc/taller_mongodb.md`
2. Crear la conexión a MongoDB Atlas con KMongo
3. Implementar ejemplos CRUD equivalentes a los de Python
4. Crear ejercicios con soluciones en Kotlin
5. Escribir tests unitarios para verificar operaciones
