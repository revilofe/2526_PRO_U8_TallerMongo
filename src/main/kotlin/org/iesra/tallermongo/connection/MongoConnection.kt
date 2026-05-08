package org.iesra.tallermongo.connection

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.iesra.tallermongo.config.MongoConfig
import org.litote.kmongo.KMongo

/**
 * Crea y mantiene un cliente MongoDB reutilizable para los ejemplos del taller.
 */
class MongoConnection(
    private val config: MongoConfig,
) : AutoCloseable {
    private val client: MongoClient by lazy { KMongo.createClient(config.uri) }

    /** Devuelve el cliente MongoDB usado por la aplicación. */
    fun client(): MongoClient = client

    /** Devuelve la base de datos configurada para la ejecución actual. */
    fun database(): MongoDatabase = client.getDatabase(config.databaseName)

    override fun close() {
        client.close()
    }
}
