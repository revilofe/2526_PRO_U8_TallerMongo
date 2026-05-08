package org.iesra.tallermongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.Document

/**
 * Ayudante sencillo para las operaciones de base de datos del primer módulo.
 *
 * La clase expone solo las operaciones necesarias para que el alumnado se centre
 * en cómo MongoDB crea, lista y elimina bases de datos.
 */
class DatabaseManager(
    private val client: MongoClient,
) {
    /** Lista los nombres de bases de datos visibles en el cluster MongoDB conectado. */
    fun listDatabaseNames(): List<String> = client.listDatabaseNames().toList()

    /** Selecciona una base de datos; MongoDB la creará al insertar el primer documento. */
    fun selectDatabase(databaseName: String): MongoDatabase {
        validateName(databaseName, "base de datos")
        return client.getDatabase(databaseName)
    }

    /**
     * Inserta un documento pequeño para que MongoDB materialice la base de datos.
     *
     * MongoDB no crea una base de datos vacía inmediatamente. Este método hace
     * visible ese comportamiento en el primer módulo del taller.
     */
    fun materializeDatabase(databaseName: String, collectionName: String = "prueba") {
        val database = selectDatabase(databaseName)
        database.getCollection(collectionName).insertOne(Document("creada", true))
    }

    /** Elimina una base de datos completa; úsalo solo en ejercicios o entornos controlados. */
    fun dropDatabase(databaseName: String) {
        selectDatabase(databaseName).drop()
    }

    companion object {
        /**
         * Valida nombres usados para bases de datos y colecciones en los ejemplos del taller.
         *
         * @throws IllegalArgumentException cuando el nombre está vacío o contiene `/`.
         */
        fun validateName(name: String, resource: String) {
            require(name.isNotBlank()) { "El nombre de $resource no puede estar vacío." }
            require(!name.contains('/')) { "El nombre de $resource no debe contener '/'." }
        }
    }
}
