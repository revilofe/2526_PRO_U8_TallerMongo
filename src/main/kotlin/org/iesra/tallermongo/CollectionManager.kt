package org.iesra.tallermongo

import com.mongodb.kotlin.client.MongoDatabase
import org.bson.Document

/**
 * Ayudante sencillo para las operaciones de colección.
 *
 * La clase expone solo las operaciones necesarias para que el alumnado se centre en cómo MongoDB crea, lista, renombra y elimina colecciones dentro de una base de datos.
 *
 * Las **colecciones** son un concepto fundamental en MongoDB: son los contenedores de documentos dentro de una base de datos.
 * MongoDB crea colecciones de forma implícita al insertar el primer documento, pero también permite crearlas explícitamente.
 * Este módulo muestra ambos enfoques, así como cómo listar, renombrar y eliminar colecciones.
 *
 * Sus métodos muestran la creación explícita de colecciones y la creación
 * implícita que ocurre al insertar el primer documento.
 *
 * @property database Base de datos sobre la que se ejecutan las operaciones de colecciones.
 */
class CollectionManager(
    private val database: MongoDatabase,
) {
    /**
     * Lista las colecciones disponibles en la base de datos seleccionada.
     *
     * @return Una lista con los nombres actuales de las colecciones de la base de datos.
     */
    fun listCollectionNames(): List<String> = database.listCollectionNames().toList()

    /**
     * Crea una colección explícitamente si todavía no existe.
     *
     * @param collectionName Nombre de la colección que se quiere crear.
     */
    fun createCollectionIfMissing(collectionName: String) {
        MongoNameValidator.validateName(collectionName, "colección")
        if (collectionName !in listCollectionNames()) {
            database.createCollection(collectionName)
        }
    }

    /**
     * Inserta un documento para crear una colección de forma implícita.
     *
     * MongoDB crea la colección automáticamente en el momento en que se inserta
     * el primer documento. Este método se usa en el taller para mostrar ese comportamiento.
     *
     * @param collectionName Nombre de la colección que se quiere materializar.
     * @param document Documento inicial que fuerza la creación de la colección.
     */
    fun materializeCollection(collectionName: String, document: Document = Document("creada", true)) {
        MongoNameValidator.validateName(collectionName, "colección")
        database.getCollection<Document>(collectionName).insertOne(document)
    }

    /**
     * Renombra una colección existente en la base de datos seleccionada.
     *
     * El comando se expresa como documento MongoDB porque es la forma más clara
     * de mantener este ejemplo introductorio independiente de helpers de bajo nivel.
     *
     * @param currentName el nombre actual de la colección a renombrar.
     * @param newName el nuevo nombre para la colección.
     * @throws IllegalArgumentException Si alguno de los nombres no es válido.
     */
    fun renameCollection(currentName: String, newName: String) {
        MongoNameValidator.validateName(currentName, "colección origen")
        MongoNameValidator.validateName(newName, "colección destino")
        // `renameCollection` es un comando administrativo que requiere el nombre completo
        // `baseDeDatos.coleccion`, por eso aquí se construye manualmente el documento.
        database.runCommand(
            Document(
                "renameCollection",
                "${database.name}.$currentName",
            ).append("to", "${database.name}.$newName"),
        )
    }

    /**
     * Elimina una colección completa.
     *
     * @param collectionName Nombre de la colección a eliminar.
     */
    fun dropCollection(collectionName: String) {
        MongoNameValidator.validateName(collectionName, "colección")
        database.getCollection<Document>(collectionName).drop()
    }
}
