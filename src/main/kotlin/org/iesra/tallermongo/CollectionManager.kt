package org.iesra.tallermongo

import com.mongodb.client.MongoDatabase
import org.bson.Document

/**
 * Ayudante sencillo para las operaciones de colección del segundo módulo.
 *
 * Sus métodos muestran la creación explícita de colecciones y la creación
 * implícita que ocurre al insertar el primer documento.
 */
class CollectionManager(
    private val database: MongoDatabase,
) {
    /** Lista las colecciones disponibles en la base de datos seleccionada. */
    fun listCollectionNames(): List<String> = database.listCollectionNames().toList()

    /** Crea una colección explícitamente si todavía no existe. */
    fun createCollectionIfMissing(collectionName: String) {
        DatabaseManager.validateName(collectionName, "colección")
        if (collectionName !in listCollectionNames()) {
            database.createCollection(collectionName)
        }
    }

    /** Inserta un documento para crear una colección de forma implícita. */
    fun materializeCollection(collectionName: String, document: Document = Document("creada", true)) {
        DatabaseManager.validateName(collectionName, "colección")
        database.getCollection(collectionName).insertOne(document)
    }

    /**
     * Renombra una colección existente en la base de datos seleccionada.
     *
     * El comando se expresa como documento MongoDB porque es la forma más clara
     * de mantener este ejemplo introductorio independiente de helpers de bajo nivel.
     */
    fun renameCollection(currentName: String, newName: String) {
        DatabaseManager.validateName(currentName, "colección origen")
        DatabaseManager.validateName(newName, "colección destino")
        database.runCommand(
            Document(
                "renameCollection",
                "${database.name}.$currentName",
            ).append("to", "${database.name}.$newName"),
        )
    }

    /** Elimina una colección completa. */
    fun dropCollection(collectionName: String) {
        DatabaseManager.validateName(collectionName, "colección")
        database.getCollection(collectionName).drop()
    }
}
