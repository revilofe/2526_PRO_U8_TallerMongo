package org.iesra.tallermongo

import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import org.bson.Document

/**
 * Ayudante sencillo para las operaciones de base de datos del primer módulo.
 *
 * La clase expone solo las operaciones necesarias para que el alumnado se centre
 * en cómo MongoDB crea, lista y elimina bases de datos.
 *
 * @property client Cliente MongoDB desde el que se consultan y gestionan las bases de datos.
 */
class DatabaseManager(
    private val client: MongoClient,
) {
    /**
     * Lista los nombres de bases de datos visibles en el cluster MongoDB conectado.
     *
     * @return Lista con los nombres de las bases de datos accesibles con la conexión actual.
     */
    fun listDatabaseNames(): List<String> = client.listDatabaseNames().toList()

    /**
     * Selecciona una base de datos; MongoDB la creará al insertar el primer documento.
     *
     * @param databaseName Nombre de la base de datos que se quiere seleccionar.
     * @return La referencia a la base de datos indicada.
     * @throws IllegalArgumentException Cuando el nombre de la base de datos es inválido.
     */
    fun selectDatabase(databaseName: String): MongoDatabase {
        MongoNameValidator.validateName(databaseName, "base de datos")
        return client.getDatabase(databaseName)
    }

    /**
     * Inserta un documento pequeño para que MongoDB materialice la base de datos.
     *
     * MongoDB no crea una base de datos vacía inmediatamente. Este método hace
     * visible ese comportamiento en el primer módulo del taller.
     *
     * @param databaseName Nombre de la base de datos que se quiere materializar.
     * @param collectionName Nombre de la colección donde se insertará el documento inicial.
     * @throws IllegalArgumentException Cuando el nombre de la base de datos o de la colección es inválido.
     */
    fun materializeDatabase(databaseName: String, collectionName: String = "prueba") {
        val database = selectDatabase(databaseName)
        // La base de datos aparece realmente en MongoDB cuando se escribe al menos
        // un documento en alguna colección. Por eso aquí se inserta un marcador mínimo.
        database.getCollection<Document>(collectionName).insertOne(Document("creada", true))
    }

    /**
     * Elimina una base de datos completa; úsalo solo en ejercicios o entornos controlados.
     *
     * @param databaseName Nombre de la base de datos que se quiere eliminar.
     * @throws IllegalArgumentException Cuando el nombre de la base de datos es inválido.
     */
    fun dropDatabase(databaseName: String) {
        selectDatabase(databaseName).drop()
    }

    companion object {
        /**
         * Valida nombres usados para bases de datos y colecciones en los ejemplos del taller.
         *
         * @param name Nombre que se quiere validar.
         * @param resource Descripción del recurso usada para generar mensajes de error más claros.
         * @throws IllegalArgumentException Cuando el nombre está vacío o contiene `/`.
         */
        fun validateName(name: String, resource: String) {
            MongoNameValidator.validateName(name, resource)
        }
    }
}
