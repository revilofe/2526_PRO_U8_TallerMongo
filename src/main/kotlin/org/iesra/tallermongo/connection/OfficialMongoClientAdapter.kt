package org.iesra.tallermongo.connection

import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import org.bson.Document
import org.iesra.tallermongo.config.MongoDriverProvider
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.iesra.tallermongo.model.Product
import org.iesra.tallermongo.repository.LibraryRepository
import org.iesra.tallermongo.repository.OfficialMongoLibraryRepository
import org.iesra.tallermongo.repository.OfficialMongoProductRepository
import org.iesra.tallermongo.repository.ProductRepository

internal class OfficialMongoClientAdapter(
    private val client: MongoClient,
) : MongoClientAdapter {
    override val driverProvider: MongoDriverProvider = MongoDriverProvider.MONGODB_KOTLIN

    override fun listDatabaseNames(): List<String> = client.listDatabaseNames().toList()

    override fun getDatabase(databaseName: String): MongoDatabaseAdapter =
        OfficialMongoDatabaseAdapter(client.getDatabase(databaseName))

    override fun close() {
        client.close()
    }
}

internal class OfficialMongoDatabaseAdapter(
    private val database: MongoDatabase,
) : MongoDatabaseAdapter {
    override val name: String = database.name

    override fun listCollectionNames(): List<String> = database.listCollectionNames().toList()

    override fun createCollection(collectionName: String) {
        database.createCollection(collectionName)
    }

    override fun insertDocument(collectionName: String, document: Document) {
        database.getCollection<Document>(collectionName).insertOne(document)
    }

    override fun runCommand(command: Document) {
        database.runCommand(command)
    }

    override fun dropCollection(collectionName: String) {
        database.getCollection<Document>(collectionName).drop()
    }

    override fun drop() {
        database.drop()
    }

    override fun productRepository(collectionName: String): ProductRepository =
        OfficialMongoProductRepository(database.getCollection<Product>(collectionName))

    override fun libraryRepository(
        autoresCollection: String,
        librosCollection: String,
        prestamosCollection: String,
    ): LibraryRepository =
        OfficialMongoLibraryRepository(
            autores = database.getCollection<Autor>(autoresCollection),
            libros = database.getCollection<Libro>(librosCollection),
            prestamos = database.getCollection<Prestamo>(prestamosCollection),
        )
}
