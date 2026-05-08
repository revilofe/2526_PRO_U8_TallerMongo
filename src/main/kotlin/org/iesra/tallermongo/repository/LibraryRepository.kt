package org.iesra.tallermongo.repository

import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo

/**
 * Define las operaciones de persistencia necesarias para el módulo integrado de biblioteca.
 *
 * La interfaz mantiene el acceso a MongoDB detrás de un contrato pequeño para
 * que el servicio pueda probarse con MockK y el taller se centre en el comportamiento aprendido.
 */
interface LibraryRepository {
    /** Almacena los autores usados por el ejercicio de biblioteca. */
    fun insertAutores(autores: List<Autor>): List<Autor>

    /** Almacena los libros usados por el ejercicio de biblioteca. */
    fun insertLibros(libros: List<Libro>): List<Libro>

    /** Almacena los préstamos usados por el ejercicio de biblioteca. */
    fun insertPrestamos(prestamos: List<Prestamo>): List<Prestamo>

    /** Devuelve los libros disponibles ordenados según la implementación del repositorio. */
    fun findAvailableBooks(): List<Libro>

    /** Devuelve los préstamos que todavía no se han devuelto. */
    fun findPendingLoans(): List<Prestamo>

    /** Marca un préstamo como devuelto usando su identificador. */
    fun markLoanAsReturned(prestamoId: String): Boolean

    /** Aumenta el número de copias del libro con el título indicado. */
    fun increaseCopiesByTitle(titulo: String, amount: Int): Boolean

    /** Marca como no disponibles los libros sin copias. */
    fun updateAvailabilityWithoutCopies(): Long

    /** Elimina los préstamos que ya están marcados como devueltos. */
    fun deleteReturnedLoans(): Long

    /** Cuenta documentos en la colección `autores`. */
    fun countAutores(): Long

    /** Cuenta documentos en la colección `libros`. */
    fun countLibros(): Long

    /** Cuenta documentos en la colección `prestamos`. */
    fun countPrestamos(): Long
}
