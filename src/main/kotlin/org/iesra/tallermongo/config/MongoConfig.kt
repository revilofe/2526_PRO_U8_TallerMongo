package org.iesra.tallermongo.config

/**
 * Configuración necesaria para conectar la aplicación del taller con MongoDB.
 *
 * Las credenciales deben proporcionarse desde fuera del código fuente,
 * normalmente mediante variables de entorno, para evitar publicar secretos en
 * el repositorio.
 */
data class MongoConfig(
    val uri: String,
    val databaseName: String,
) {
    init {
        require(uri.isNotBlank()) { "La URI de MongoDB no puede estar vacía." }
        require(databaseName.isNotBlank()) { "El nombre de la base de datos no puede estar vacío." }
    }

    companion object {
        private const val URI_VARIABLE = "MONGODB_URI"
        private const val DATABASE_VARIABLE = "MONGODB_DATABASE"
        private const val DEFAULT_DATABASE = "taller_mongo"

        /**
         * Construye la configuración a partir de variables de entorno.
         *
         * @throws IllegalStateException cuando `MONGODB_URI` no está definida.
         */
        fun fromEnvironment(env: (String) -> String? = System::getenv): MongoConfig {
            val uri = env(URI_VARIABLE)
                ?: error("Define la variable de entorno $URI_VARIABLE antes de ejecutar la aplicación.")
            val database = env(DATABASE_VARIABLE) ?: DEFAULT_DATABASE
            return MongoConfig(uri = uri, databaseName = database)
        }
    }
}
