package org.iesra.tallermongo

/**
 * Valida nombres usados para bases de datos y colecciones en los ejemplos del taller.
 */
object MongoNameValidator {
    /**
     * Valida nombres usados para bases de datos y colecciones.
     *
     * @param name Nombre que se quiere validar.
     * @param resource Descripción del recurso usada para generar mensajes de error más claros.
     * @throws IllegalArgumentException Cuando el nombre está vacío o contiene `/`.
     */
    fun validateName(name: String, resource: String) {
        require(name.isNotBlank()) { "El nombre de $resource no puede estar vacío." }
        require(!name.contains('/')) { "El nombre de $resource no debe contener '/'." }
    }
}
