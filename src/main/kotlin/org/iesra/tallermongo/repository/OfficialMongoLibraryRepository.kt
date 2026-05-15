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
 * Implementación MongoDB del ejemplo integrado de biblioteca con el driver oficial de MongoDB para Kotlin.
 *
 * @property autores Colección tipada que almacena los autores del ejemplo.
 * @property libros Colección tipada que almacena los libros del ejemplo.
 * @property prestamos Colección tipada que almacena los préstamos del ejemplo.
 */
class OfficialMongoLibraryRepository(
    private val autores: MongoCollection<Autor>,
    private val libros: MongoCollection<Libro>,
    private val prestamos: MongoCollection<Prestamo>,
) : LibraryRepository {
    override fun insertAutores(autores: List<Autor>): List<Autor> = autores.also {
        if (it.isNotEmpty()) this.autores.insertMany(it)
    }

    override fun insertLibros(libros: List<Libro>): List<Libro> = libros.also {
        if (it.isNotEmpty()) this.libros.insertMany(it)
    }

    override fun insertPrestamos(prestamos: List<Prestamo>): List<Prestamo> = prestamos.also {
        if (it.isNotEmpty()) this.prestamos.insertMany(it)
    }

    override fun findAvailableBooks(): List<Libro> =
        libros.find(eq(Libro::disponible.name, true)).sort(ascending(Libro::titulo.name)).toList()

    override fun findPendingLoans(): List<Prestamo> =
        prestamos.find(eq(Prestamo::devuelto.name, false)).toList()

    override fun markLoanAsReturned(prestamoId: String): Boolean =
        prestamos.updateOne(eq(Prestamo::_id.name, prestamoId), set(Prestamo::devuelto.name, true)).modifiedCount == 1L

    override fun increaseCopiesByTitle(titulo: String, amount: Int): Boolean =
        libros.updateOne(eq(Libro::titulo.name, titulo), inc(Libro::copias.name, amount)).modifiedCount == 1L

    override fun updateAvailabilityWithoutCopies(): Long =
        libros.updateMany(lte(Libro::copias.name, 0), set(Libro::disponible.name, false)).modifiedCount

    override fun deleteReturnedLoans(): Long =
        prestamos.deleteMany(eq(Prestamo::devuelto.name, true)).deletedCount

    override fun countAutores(): Long = autores.countDocuments()

    override fun countLibros(): Long = libros.countDocuments()

    override fun countPrestamos(): Long = prestamos.countDocuments()
}
