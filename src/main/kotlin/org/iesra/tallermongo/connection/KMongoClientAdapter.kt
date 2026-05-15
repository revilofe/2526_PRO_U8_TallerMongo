package org.iesra.tallermongo.connection

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.iesra.tallermongo.config.MongoDriverProvider
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.iesra.tallermongo.model.Product
import org.iesra.tallermongo.repository.LibraryRepository
import org.iesra.tallermongo.repository.MongoLibraryRepository
import org.iesra.tallermongo.repository.MongoProductRepository
import org.iesra.tallermongo.repository.ProductRepository
import org.litote.kmongo.getCollection

internal class KMongoClientAdapter(
    private val client: MongoClient,
) : MongoClientAdapter {
    override val driverProvider: MongoDriverProvider = MongoDriverProvider.KMONGO

    override fun listDatabaseNames(): List<String> = client.listDatabaseNames().toList()

    override fun getDatabase(databaseName: String): MongoDatabaseAdapter =
        KMongoDatabaseAdapter(client.getDatabase(databaseName))

    override fun close() {
        client.close()
    }
}

internal class KMongoDatabaseAdapter(
    private val database: MongoDatabase,
) : MongoDatabaseAdapter {
    override val name: String = database.name

    override fun listCollectionNames(): List<String> = database.listCollectionNames().toList()

    override fun createCollection(collectionName: String) {
        database.createCollection(collectionName)
    }

    override fun insertDocument(collectionName: String, document: Document) {
        database.getCollection(collectionName).insertOne(document)
    }

    override fun runCommand(command: Document) {
        database.runCommand(command)
    }

    override fun dropCollection(collectionName: String) {
        database.getCollection(collectionName).drop()
    }

    override fun drop() {
        database.drop()
    }

    override fun productRepository(collectionName: String): ProductRepository =
        MongoProductRepository(database.getCollection<Product>(collectionName))

    override fun libraryRepository(
        autoresCollection: String,
        librosCollection: String,
        prestamosCollection: String,
    ): LibraryRepository =
        MongoLibraryRepository(
            autores = database.getCollection<Autor>(autoresCollection),
            libros = database.getCollection<Libro>(librosCollection),
            prestamos = database.getCollection<Prestamo>(prestamosCollection),
        )
}
