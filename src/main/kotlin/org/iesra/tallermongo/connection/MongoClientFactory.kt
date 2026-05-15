package org.iesra.tallermongo.connection

import org.iesra.tallermongo.config.MongoDriverProvider
import org.litote.kmongo.KMongo

/**
 * Fábrica de clientes MongoDB usada por `MongoConnection`.
 */
fun interface MongoClientFactory {
    /**
     * Crea el cliente adaptado a partir de la URI de conexión.
     */
    fun create(uri: String): MongoClientAdapter

    companion object {
        /**
         * Devuelve la fábrica correspondiente al proveedor configurado.
         */
        fun forProvider(driverProvider: MongoDriverProvider): MongoClientFactory =
            when (driverProvider) {
                MongoDriverProvider.MONGODB_KOTLIN -> MongoClientFactory { uri ->
                    OfficialMongoClientAdapter(com.mongodb.kotlin.client.MongoClient.create(uri))
                }

                MongoDriverProvider.KMONGO -> MongoClientFactory { uri ->
                    KMongoClientAdapter(KMongo.createClient(uri))
                }
            }
    }
}
