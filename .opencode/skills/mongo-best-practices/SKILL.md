---
name: mongo-best-practices
description: "Best practices for working with MongoDB from Kotlin using the official MongoDB Kotlin driver. Use this skill when implementing MongoDB operations, managing databases, collections, or documents in Kotlin."
---

# MongoDB Best Practices with the Kotlin Driver

## What I do

- Provide concrete guidance for connecting to MongoDB Atlas from Kotlin.
- Cover database, collection and document operations, including CRUD, validation and error handling.
- Emphasize safe configuration, reusable connections and clear examples for the workshop.

## When to use me

- Use me when implementing or reviewing MongoDB access in Kotlin.
- Use me when working on connection setup, repositories, collection management, CRUD examples or data validation.

## Conexión a MongoDB Atlas

Usa siempre configuración externa para credenciales:

```kotlin
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase

data class MongoConfig(
    val uri: String,
    val databaseName: String,
)

class MongoConnection(private val config: MongoConfig) : AutoCloseable {
    private val client: MongoClient by lazy { MongoClient.create(config.uri) }

    fun database(): MongoDatabase = client.getDatabase(config.databaseName)

    override fun close() {
        client.close()
    }
}
```

Buenas prácticas:

1. Usa variables de entorno para credenciales.
2. Reutiliza el cliente mientras la aplicación esté activa.
3. Cierra el cliente con `use` o `close()`.
4. Mantén nombres de bases de datos y colecciones validados.
5. Usa `mongodb+srv://` para MongoDB Atlas cuando corresponda.

## Colecciones Tipadas

Prefiere colecciones tipadas con `data class` cuando el ejemplo trabaje con entidades del dominio:

```kotlin
data class Product(
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
)

val products = database.getCollection<Product>("productos")
```

Usa `Document` para comandos administrativos, documentos de inicialización o ejemplos en los que sea más didáctico mostrar la estructura BSON directamente.

## Modelado de Relaciones

MongoDB no funciona igual que una base de datos relacional, pero eso no significa que debas relacionar documentos usando campos poco estables como nombres o títulos.

Usa referencias por `_id` cuando las entidades tengan vida propia y puedan consultarse o modificarse por separado:

```kotlin
data class Autor(
    val _id: String,
    val nombre: String,
)

data class Libro(
    val _id: String,
    val titulo: String,
    val autorId: String,
)

data class Prestamo(
    val _id: String,
    val usuario: String,
    val libroId: String,
)
```

Buenas prácticas para referencias:

1. Referencia por `_id` cuando un documento apunta a otro documento independiente.
2. Evita usar `nombre`, `titulo` o campos editables como si fueran claves externas.
3. Guarda una copia de un dato descriptivo solo si necesitas histórico o lectura rápida, por ejemplo `libroTituloSnapshot`.
4. Valida en la capa de servicio que el documento referenciado existe antes de guardar la referencia.
5. Crea índices en campos de referencia consultados con frecuencia, como `libroId` o `autorId`.

Usa documentos embebidos cuando los datos dependan claramente del documento principal y normalmente se lean o escriban juntos. Por ejemplo, puede tener sentido embeber líneas de dirección dentro de un usuario, pero no embeber todos los préstamos dentro de un libro si los préstamos crecen continuamente.

## Consultas con Relaciones

El equivalente más cercano a un `JOIN` en MongoDB es una agregación con `$lookup`.

Ejemplo conceptual para recuperar préstamos junto con su libro:

```javascript
db.prestamos.aggregate([
  {
    $lookup: {
      from: "libros",
      localField: "libroId",
      foreignField: "_id",
      as: "libro"
    }
  },
  {
    $unwind: "$libro"
  }
])
```

Para un taller progresivo:

1. Enseña primero CRUD con una sola colección.
2. Introduce referencias por `_id` cuando aparezcan relaciones entre entidades.
3. Explica `$lookup` en un módulo posterior de agregaciones, no como requisito del CRUD inicial.
4. Si solo necesitas los préstamos de un libro, consulta directamente por `libroId` antes de añadir `$lookup`.

## CRUD

Ejemplos básicos:

```kotlin
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.gt
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Updates.inc
import com.mongodb.client.model.Updates.set

products.insertOne(product)

val allProducts = products.find().toList()
val expensiveProducts = products.find(gt(Product::precio.name, 50.0)).toList()

products.updateOne(
    eq(Product::nombre.name, "Libro Kotlin"),
    set(Product::precio.name, 29.95),
)

products.updateMany(
    eq(Product::categoria.name, "libros"),
    inc(Product::stock.name, 5),
)

products.replaceOne(
    eq(Product::nombre.name, product.nombre),
    product,
    ReplaceOptions().upsert(true),
)

products.deleteOne(eq(Product::nombre.name, "Libro Kotlin"))
```

## Patrones Recomendados

1. **Repository Pattern**: Abstraer acceso a datos
2. **Service Layer**: Lógica de negocio separada de acceso a datos
3. **Configuration**: Usar data classes para configuración
4. **Connection reuse**: Mantener una sola instancia de `MongoClient` mientras la aplicación esté activa

## Errores Comunes a Evitar

1. No usar variables de entorno para credenciales
2. Crear nuevo cliente por cada operación
3. No cerrar conexiones
4. Exponer credenciales en logs
5. No validar datos antes de insertar
6. Usar `find().toString()` en vez de iterar correctamente
7. Ignorar resultados de operaciones de escritura
8. No crear índices para campos frecuentemente consultados
9. Usar nombres o títulos como referencias entre colecciones cuando existe un `_id` estable
10. Introducir `$lookup` antes de que el alumnado entienda bien filtros, inserciones y actualizaciones
