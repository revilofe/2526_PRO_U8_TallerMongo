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
    /**
     * Almacena los autores usados por el ejercicio de biblioteca.
     *
     * @param autores Autores que se quieren guardar en la colección correspondiente.
     * @return La misma lista recibida, para que la capa superior pueda seguir trabajando con ella.
     */
    fun insertAutores(autores: List<Autor>): List<Autor>

    /**
     * Almacena los libros usados por el ejercicio de biblioteca.
     *
     * @param libros Libros que se quieren guardar en la colección correspondiente.
     * @return La misma lista recibida, para mantener una API sencilla y didáctica.
     */
    fun insertLibros(libros: List<Libro>): List<Libro>

    /**
     * Almacena los préstamos usados por el ejercicio de biblioteca.
     *
     * @param prestamos Préstamos que se quieren guardar en la colección correspondiente.
     * @return La misma lista recibida tras la operación de inserción.
     */
    fun insertPrestamos(prestamos: List<Prestamo>): List<Prestamo>

    /**
     * Devuelve los libros disponibles ordenados según la implementación del repositorio.
     *
     * @return Lista de libros que todavía pueden prestarse.
     */
    fun findAvailableBooks(): List<Libro>

    /**
     * Devuelve los préstamos que todavía no se han devuelto.
     *
     * @return Lista de préstamos marcados como pendientes.
     */
    fun findPendingLoans(): List<Prestamo>

    /**
     * Marca un préstamo como devuelto usando su identificador.
     *
     * @param prestamoId Identificador del préstamo que debe actualizarse.
     * @return `true` si se ha actualizado exactamente un préstamo; `false` en caso contrario.
     */
    fun markLoanAsReturned(prestamoId: String): Boolean

    /**
     * Aumenta el número de copias del libro con el título indicado.
     *
     * @param titulo Título del libro que se quiere actualizar.
     * @param amount Número de copias que se suman al valor actual.
     * @return `true` si se ha actualizado exactamente un libro; `false` en caso contrario.
     */
    fun increaseCopiesByTitle(titulo: String, amount: Int): Boolean

    /**
     * Marca como no disponibles los libros sin copias.
     *
     * @return Número de libros cuyo campo de disponibilidad se ha modificado.
     */
    fun updateAvailabilityWithoutCopies(): Long

    /**
     * Elimina los préstamos que ya están marcados como devueltos.
     *
     * @return Número de préstamos eliminados.
     */
    fun deleteReturnedLoans(): Long

    /**
     * Cuenta documentos en la colección `autores`.
     *
     * @return Número total de autores almacenados.
     */
    fun countAutores(): Long

    /**
     * Cuenta documentos en la colección `libros`.
     *
     * @return Número total de libros almacenados.
     */
    fun countLibros(): Long

    /**
     * Cuenta documentos en la colección `prestamos`.
     *
     * @return Número total de préstamos almacenados.
     */
    fun countPrestamos(): Long
}
