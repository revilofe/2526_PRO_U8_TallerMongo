package org.iesra.tallermongo.connection

import org.bson.Document
import org.iesra.tallermongo.config.MongoDriverProvider
import org.iesra.tallermongo.repository.LibraryRepository
import org.iesra.tallermongo.repository.ProductRepository

/**
 * Operaciones comunes que necesita el taller independientemente del driver usado.
 */
interface MongoClientAdapter : AutoCloseable {
    val driverProvider: MongoDriverProvider

    /**
     * Lista los nombres de bases de datos visibles en el cluster MongoDB conectado.
     */
    fun listDatabaseNames(): List<String>

    /**
     * Selecciona una base de datos.
     *
     * @param
     */
    fun getDatabase(databaseName: String): MongoDatabaseAdapter
}

/**
 * Operaciones comunes de una base de datos MongoDB para los ejemplos del taller.
 */
interface MongoDatabaseAdapter {
    val name: String

    /**
     * Lista las colecciones disponibles en la base de datos.
     */
    fun listCollectionNames(): List<String>

    /**
     * Crea una colección de forma explícita.
     */
    fun createCollection(collectionName: String)

    /**
     * Inserta un documento genérico en la colección indicada.
     */
    fun insertDocument(collectionName: String, document: Document)

    /**
     * Ejecuta un comando administrativo sobre la base de datos.
     */
    fun runCommand(command: Document)

    /**
     * Elimina la colección indicada.
     */
    fun dropCollection(collectionName: String)

    /**
     * Elimina la base de datos completa.
     */
    fun drop()

    /**
     * Crea el repositorio de productos adecuado para el driver activo.
     */
    fun productRepository(collectionName: String = "productos"): ProductRepository

    /**
     * Crea el repositorio de biblioteca adecuado para el driver activo.
     */
    fun libraryRepository(
        autoresCollection: String = "autores",
        librosCollection: String = "libros",
        prestamosCollection: String = "prestamos",
    ): LibraryRepository
}
