package org.iesra.tallermongo.model

import org.bson.types.ObjectId

/**
 * Representa un libro almacenado en la colección `libros`.
 *
 * @property _id Identificador de MongoDB almacenado como texto.
 * @property titulo Título del libro usado para relacionar préstamos y libros en el taller.
 * @property autorNombre Nombre del autor como referencia denormalizada sencilla.
 * @property anio Año de publicación; el servicio espera un valor positivo.
 * @property genero Género literario o categoría temática.
 * @property disponible Indica si el libro puede prestarse actualmente.
 * @property copias Número de copias disponibles; el servicio rechaza valores negativos.
 */
data class Libro(
    val _id: String = ObjectId().toHexString(),
    val titulo: String,
    val autorNombre: String,
    val anio: Int,
    val genero: String,
    val disponible: Boolean = true,
    val copias: Int,
)
