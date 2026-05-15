package org.iesra.tallermongo.config

/**
 * Proveedor de acceso a MongoDB que usará la aplicación del taller.
 *
 * `MONGODB_KOTLIN` usa el driver oficial de MongoDB para Kotlin. `KMONGO`
 * conserva la implementación histórica del taller para poder comparar ambas
 * formas de acceso durante la migración.
 */
enum class MongoDriverProvider {
    MONGODB_KOTLIN,
    KMONGO,
    ;

    companion object {
        private val kmongoValues = setOf("KMONGO", "K_MONGO")
        private val officialValues = setOf(
            "MONGODB_KOTLIN",
            "MONGODB-KOTLIN",
            "MONGO_KOTLIN",
            "MONGO-KOTLIN",
            "OFFICIAL",
            "DRIVER_OFICIAL",
        )

        /**
         * Convierte un valor de configuración textual en un proveedor soportado.
         *
         * @param value Valor leído desde variables de entorno o configuración externa.
         * @return Proveedor equivalente al texto recibido.
         * @throws IllegalArgumentException Si el valor no corresponde a ningún proveedor.
         */
        fun from(value: String): MongoDriverProvider {
            val normalizedValue = value.trim().uppercase()
            return when (normalizedValue) {
                in officialValues -> MONGODB_KOTLIN
                in kmongoValues -> KMONGO
                else -> throw IllegalArgumentException(
                    "Proveedor MongoDB no soportado: $value. Usa MONGODB_KOTLIN o KMONGO.",
                )
            }
        }
    }
}
