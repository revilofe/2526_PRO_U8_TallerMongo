package org.iesra.tallermongo.connection

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.iesra.tallermongo.config.MongoConfig
import org.litote.kmongo.KMongo

/**
 * Crea y mantiene un cliente MongoDB reutilizable para los ejemplos del taller.
 *
 * La conexión se encapsula en una clase pequeña para que el código de ejemplo
 * pueda reutilizar el cliente y cerrar los recursos con `use` al finalizar.
 *
 * @property config Configuración necesaria para crear el cliente y seleccionar la base de datos.
 */
class MongoConnection(
    private val config: MongoConfig,
) : AutoCloseable {
    /**
     * Cliente MongoDB creado de forma perezosa para evitar conexiones innecesarias.
     */
    private val client: MongoClient by lazy { KMongo.createClient(config.uri) }

    /**
     * Devuelve el cliente MongoDB usado por la aplicación.
     *
     * @return Cliente reutilizable asociado a la configuración actual.
     */
    fun client(): MongoClient = client

    /**
     * Devuelve la base de datos configurada para la ejecución actual.
     *
     * @return Base de datos indicada en la configuración.
     */
    fun database(): MongoDatabase = client.getDatabase(config.databaseName)

    /**
     * Cierra el cliente MongoDB para liberar recursos.
     */
    override fun close() {
        client.close()
    }
}
