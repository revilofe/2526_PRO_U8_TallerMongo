package org.iesra.tallermongo.connection

import org.iesra.tallermongo.config.MongoConfig

/**
 * Crea y mantiene un cliente MongoDB reutilizable para los ejemplos del taller.
 *
 * La fábrica de cliente se inyecta para poder usar el driver oficial de MongoDB
 * para Kotlin o KMongo sin duplicar la clase de conexión.
 *
 * @property config Configuración necesaria para crear el cliente y seleccionar la base de datos.
 * @property clientFactory Fábrica responsable de crear el cliente del driver configurado.
 */
class MongoConnection(
    private val config: MongoConfig,
    private val clientFactory: MongoClientFactory = MongoClientFactory.forProvider(config.driverProvider),
) : AutoCloseable {
    /**
     * Cliente MongoDB creado de forma perezosa para evitar conexiones innecesarias.
     */
    private val client: MongoClientAdapter by lazy { clientFactory.create(config.uri) }

    /**
     * Devuelve el cliente MongoDB usado por la aplicación.
     *
     * @return Cliente reutilizable asociado a la configuración actual.
     */
    fun client(): MongoClientAdapter = client

    /**
     * Devuelve la base de datos configurada para la ejecución actual.
     *
     * @return Base de datos indicada en la configuración.
     */
    fun database(): MongoDatabaseAdapter = client.getDatabase(config.databaseName)

    /**
     * Cierra el cliente MongoDB para liberar recursos.
     */
    override fun close() {
        client.close()
    }
}
