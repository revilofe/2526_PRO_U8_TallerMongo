package org.iesra.tallermongo.repository

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.lte
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Updates.inc
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.MongoCollection
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo

/**
 * Implementación MongoDB del ejemplo integrado de biblioteca.
 *
 * Cada colección se inyecta explícitamente para que `BibliotecaService` pueda
 * mantenerse independiente de MongoDB durante las pruebas unitarias.
 *
 * @property autores Colección tipada que almacena los autores del ejemplo.
 * @property libros Colección tipada que almacena los libros del ejemplo.
 * @property prestamos Colección tipada que almacena los préstamos del ejemplo.
 */
class MongoLibraryRepository(
    private val autores: MongoCollection<Autor>,
    private val libros: MongoCollection<Libro>,
    private val prestamos: MongoCollection<Prestamo>,
) : LibraryRepository {
    /**
     * Inserta varios autores en MongoDB en una sola operación.
     *
     * @param autores Autores que se quieren persistir.
     * @return La misma lista recibida para simplificar el flujo didáctico.
     */
    override fun insertAutores(autores: List<Autor>): List<Autor> = autores.also {
        // Se evita llamar a `insertMany` con una lista vacía porque no aporta nada al ejemplo
        // y hace más claro cuándo realmente se escribe en la colección.
        if (it.isNotEmpty()) this.autores.insertMany(it)
    }

    /**
     * Inserta varios libros en MongoDB en una sola operación.
     *
     * @param libros Libros que se quieren persistir.
     * @return La misma lista recibida tras la inserción.
     */
    override fun insertLibros(libros: List<Libro>): List<Libro> = libros.also {
        if (it.isNotEmpty()) this.libros.insertMany(it)
    }

    /**
     * Inserta varios préstamos en MongoDB en una sola operación.
     *
     * @param prestamos Préstamos que se quieren persistir.
     * @return La misma lista recibida tras la inserción.
     */
    override fun insertPrestamos(prestamos: List<Prestamo>): List<Prestamo> = prestamos.also {
        if (it.isNotEmpty()) this.prestamos.insertMany(it)
    }

    /**
     * Recupera los libros disponibles y los ordena por título.
     *
     * @return Libros cuyo campo `disponible` es `true`.
     */
    override fun findAvailableBooks(): List<Libro> =
        libros.find(eq(Libro::disponible.name, true)).sort(ascending(Libro::titulo.name)).toList()

    /**
     * Recupera los préstamos que todavía no se han devuelto.
     *
     * @return Lista de préstamos pendientes.
     */
    override fun findPendingLoans(): List<Prestamo> = prestamos.find(eq(Prestamo::devuelto.name, false)).toList()

    /**
     * Marca como devuelto el préstamo indicado.
     *
     * @param prestamoId Identificador del préstamo que debe actualizarse.
     * @return `true` si se ha modificado exactamente un documento.
     */
    override fun markLoanAsReturned(prestamoId: String): Boolean =
        prestamos.updateOne(eq(Prestamo::_id.name, prestamoId), set(Prestamo::devuelto.name, true)).modifiedCount == 1L

    /**
     * Incrementa el número de copias del libro cuyo título coincide con el indicado.
     *
     * @param titulo Título del libro a actualizar.
     * @param amount Copias que se suman al valor actual.
     * @return `true` si se ha actualizado exactamente un libro.
     */
    override fun increaseCopiesByTitle(titulo: String, amount: Int): Boolean =
        libros.updateOne(eq(Libro::titulo.name, titulo), inc(Libro::copias.name, amount)).modifiedCount == 1L

    /**
     * Desactiva la disponibilidad de los libros que ya no tienen copias.
     *
     * @return Número de libros modificados.
     */
    override fun updateAvailabilityWithoutCopies(): Long =
        libros.updateMany(lte(Libro::copias.name, 0), set(Libro::disponible.name, false)).modifiedCount

    /**
     * Elimina de la colección los préstamos que ya figuran como devueltos.
     *
     * @return Número de préstamos eliminados.
     */
    override fun deleteReturnedLoans(): Long = prestamos.deleteMany(eq(Prestamo::devuelto.name, true)).deletedCount

    /**
     * Cuenta el número total de autores almacenados.
     *
     * @return Total de documentos en la colección `autores`.
     */
    override fun countAutores(): Long = autores.countDocuments()

    /**
     * Cuenta el número total de libros almacenados.
     *
     * @return Total de documentos en la colección `libros`.
     */
    override fun countLibros(): Long = libros.countDocuments()

    /**
     * Cuenta el número total de préstamos almacenados.
     *
     * @return Total de documentos en la colección `prestamos`.
     */
    override fun countPrestamos(): Long = prestamos.countDocuments()
}
