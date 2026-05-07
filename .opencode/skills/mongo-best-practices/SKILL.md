---
name: mongo-best-practices
description: "Best practices for working with MongoDB and KMongo (Kotlin driver). Use this skill when implementing MongoDB operations, managing databases, collections, or documents in Kotlin."
---

# MongoDB Best Practices with KMongo

## Conexión a MongoDB Atlas

### Configuración Básica

```kotlin
import org.litote.kmongo.KMongo

data class MongoConfig(
    val uri: String,      // mongodb+srv://<user>:<password>@<cluster>.mongodb.net
    val database: String  // Database name
)

// Never hardcode credentials
object MongoConnection {
    private var client: MongoClient? = null

    fun connect(config: MongoConfig): MongoClient {
        return client ?: KMongo.createClient(config.uri).also { client = it }
    }

    fun getDatabase(config: MongoConfig): MongoDatabase {
        return connect(config).getDatabase(config.database)
    }

    fun close() {
        client?.close()
        client = null
    }
}
```

### Buenas Prácticas de Conexión

1. **Usar variables de entorno** para credenciales nunca hardcoded
2. **Reutilizar el cliente** — no crear uno nuevo por operación
3. **Cerrar conexiones** apropiadamente (use `use` o `close()`)
4. **Timeouts adecuados**: configurar `socketTimeoutMS`, `connectTimeoutMS`
5. **DNS SRV** es recomendado para Atlas (usa `mongodb+srv://`)

## Gestión de Bases de Datos

### Listar Bases de Datos

```kotlin
// KMongo
fun listDatabases(client: MongoClient): List<String> {
    return client.listDatabaseNames()
}
```

### Crear/Eliminar Base de Datos

```kotlin
// En KMongo, las BD se crean implícitamente al insertar
// Para forzar creación:
database.getCollection<Document>("_init").insertOne(Document())
```

## Gestión de Colecciones

### Listar Colecciones

```kotlin
fun listCollections(database: MongoDatabase): List<String> {
    return database.listCollectionNames()
}
```

### Crear Colección

```kotlin
// Implícita al insertar
database.getCollection<Product>("productos")

// Explícita con options
database.createCollection("clientes")

// Con validación de esquema (avanzado)
database.createCollection("empleados", CreateCollectionOptions().validator(
    Document(
        "\$jsonSchema" to Document(
            "bsonType" to "object",
            "required" to listOf("nombre", "departamento"),
            "properties" to Document(
                "nombre" to Document("bsonType" to "string"),
                "departamento" to Document("bsonType" to "string")
            )
        )
    )
))
```

### Renombrar/Eliminar Colección

```kotlin
// Renombrar
database.getCollection<Document>("productos").rename("articulos")

// Eliminar
database.getCollection<Document>("articulos").drop()
```

## Gestión de Documentos — CRUD

### Modelos de Datos

```kotlin
// Usar data classes para documentos
data class Product(
    val _id: String = ObjectId().toString(),
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categorias: List<String> = emptyList()
)

data class Author(
    val _id: String = ObjectId().toString(),
    val nombre: String,
    val nacionalidad: String,
    val anioNacimiento: Int
)
```

### Insertar Documentos

```kotlin
import org.litote.kmongo.*

// Insertar uno
val result = collection.insertOne(product)

// Insertar muchos
val documents = listOf(product1, product2, product3)
val insertResult = collection.insertMany(documents)
val insertedIds = insertResult.insertedIds
```

### Leer Documentos

```kotlin
// Obtener todos
val allProducts = collection.find().toList()

// Buscar uno
val product = collection.findOne(Product::nombre eq "Teclado Mecánico")

// Filtrar con operadores
val cheapProducts = collection.find(Product::precio lt 50.0).toList()
val expensiveProducts = collection.find(Product::precio gte 100.0).toList()

// Proyección (solo campos necesarios)
val namesOnly = collection.find()
    .projection(fields(include(Product::nombre, Product::precio), excludeId()))
    .toList()

// Ordenar y limitar
val top3 = collection.find()
    .sort(descending(Product::precio))
    .limit(3)
    .toList()
```

### Actualizar Documentos

```kotlin
import org.litote.kmongo.eq
import org.litote.kmongo.set
import org.litote.kmongo.inc
import org.litote.kmongo.*

// Actualizar uno
val updateResult = collection.updateOne(
    Product::nombre eq "Teclado Mecánico",
    set(Product::precio, 89.99)
)

// $set + $inc combinados
collection.updateOne(
    Product::nombre eq "Teclado Mecánico",
    combine(
        set(Product::precio, 89.99),
        inc(Product::stock, -5)
    )
)

// Actualizar muchos
val updatedCount = collection.updateMany(
    Product::precio lt 50.0,
    mul(Product::precio, 0.9) // 10% descuento
).modifiedCount

// Upsert (actualizar o insertar si no existe)
collection.updateOne(
    Product::nombre eq "Webcam HD",
    set(Product::precio, 45.00).set(Product::stock, 30),
    upsert = true
)

// $unset - eliminar campo
collection.updateMany(
    Product::descuento exists true,
    unset(Product::descuento)
)
```

### Eliminar Documentos

```kotlin
// Eliminar uno
val deletedOne = collection.deleteOne(Product::nombre eq "Auriculares BT")
println("Eliminados: ${deletedOne.deletedCount}")

// Eliminar muchos
val deletedMany = collection.deleteMany(Product::stock lte 0)
println("Eliminados: ${deletedMany.deletedCount}")

// Vaciar colección
collection.deleteMany()
```

## Operadores de Consulta Frecuentes

| Operador | KMongo | Descripción |
|----------|--------|-------------|
| Igual | `eq` | Coincidencia exacta |
| Menor que | `lt` | `<` |
| Menor o igual | `lte` | `<=` |
| Mayor que | `gt` | `>` |
| Mayor o igual | `gte` | `>=` |
| No igual | `ne` | `!=` |
| In | `in` | Valor en lista |
| Y | `and` | Condición AND |
| O | `or` | Condición OR |
| Existe | `exists` | Campo existe |

```kotlin
// Ejemplos
collection.find(Product::precio gt 50.0 and Product::stock gte 10)
collection.find(Product::categoria in listOf("electronica", "ropa"))
collection.find(Product::nombre exists true)
```

## Operadores de Actualización

| Operador | KMongo | Descripción |
|----------|--------|-------------|
| $set | `set` | Establece valor |
| $unset | `unset` | Elimina campo |
| $inc | `inc` | Incrementa/decrementa |
| $mul | `mul` | Multiplica |
| $push | `push` | Añade a array |
| $pull | `pull` | Elimina de array |
| $rename | `rename` | Renombra campo |
| $addToSet | `addToSet` | Añade si no existe |

## Indexación

```kotlin
// Crear índice
collection.createIndex(ascending(Product::nombre))
collection.createIndex(descending(Product::precio))

// Índice compuesto
collection.createIndex(
    compoundIndex(
        ascending(Product::categoria),
        descending(Product::precio)
    )
)
```

## Validación y Manejo de Errores

```kotlin
// Validar antes de insertar
fun insertProduct(product: Product): Result<Product> {
    require(product.nombre.isNotBlank()) { "Nombre no puede estar vacío" }
    require(product.precio >= 0) { "Precio no puede ser negativo" }
    
    return try {
        collection.insertOne(product)
        Result.success(product)
    } catch (e: WriteErrorException) {
        Result.failure(e)
    }
}
```

## Patrones Recomendados

1. **Repository Pattern**: Abstraer acceso a datos
2. **Service Layer**: Lógica de negocio separada de acceso a datos
3. **Configuration**: Usar data classes para configuración
4. **Connection Pooling**: KMongo ya lo hace, pero asegurar una sola instancia

## Errores Comunes a Evitar

1. No usar variables de entorno para credenciales
2. Crear nuevo cliente por cada operación
3. No cerrar conexiones
4. Exponer credenciales en logs
5. No validar datos antes de insertar
6. Usar `find().toString()` en vez de iterar correctamente
7. Ignorar resultados de operaciones de escritura
8. No crear índices para campos frecuentemente consultados