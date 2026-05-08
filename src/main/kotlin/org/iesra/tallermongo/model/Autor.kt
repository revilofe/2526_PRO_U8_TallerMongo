package org.iesra.tallermongo.model

import org.bson.types.ObjectId

/**
 * Representa un autor almacenado en la colección `autores`.
 *
 * @property _id Identificador de MongoDB almacenado como texto.
 * @property nombre Nombre del autor usado en los ejemplos de biblioteca.
 * @property nacionalidad Nacionalidad del autor.
 * @property anioNacimiento Año de nacimiento; el servicio espera un valor positivo.
 */
data class Autor(
    val _id: String = ObjectId().toHexString(),
    val nombre: String,
    val nacionalidad: String,
    val anioNacimiento: Int,
)
