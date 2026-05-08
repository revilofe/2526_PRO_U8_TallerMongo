package org.iesra.tallermongo.model

import org.bson.types.ObjectId

/**
 * Representa un préstamo almacenado en la colección `prestamos`.
 *
 * Las fechas se almacenan como texto ISO (`yyyy-MM-dd`) para mantener sencillo
 * el mapeo con MongoDB en un taller introductorio.
 *
 * @property _id Identificador de MongoDB almacenado como texto.
 * @property usuario Nombre del usuario que tomó prestado el libro.
 * @property libroTitulo Título del libro prestado.
 * @property fechaPrestamo Fecha del préstamo como texto ISO.
 * @property devuelto Indica si el préstamo ya se ha devuelto.
 */
data class Prestamo(
    val _id: String = ObjectId().toHexString(),
    val usuario: String,
    val libroTitulo: String,
    val fechaPrestamo: String,
    val devuelto: Boolean = false,
)
