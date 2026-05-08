package org.iesra.tallermongo.repository

import com.mongodb.client.MongoCollection
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import org.litote.kmongo.inc
import org.litote.kmongo.lte
import org.litote.kmongo.setValue

/**
 * Implementación MongoDB del ejemplo integrado de biblioteca usando colecciones tipadas con KMongo.
 *
 * Cada colección se inyecta explícitamente para que `BibliotecaService` pueda
 * mantenerse independiente de MongoDB durante las pruebas unitarias.
 */
class MongoLibraryRepository(
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
        libros.find(Libro::disponible eq true).sort(ascending(Libro::titulo)).toList()

    override fun findPendingLoans(): List<Prestamo> = prestamos.find(Prestamo::devuelto eq false).toList()

    override fun markLoanAsReturned(prestamoId: String): Boolean =
        prestamos.updateOne(Prestamo::_id eq prestamoId, setValue(Prestamo::devuelto, true)).modifiedCount == 1L

    override fun increaseCopiesByTitle(titulo: String, amount: Int): Boolean =
        libros.updateOne(Libro::titulo eq titulo, inc(Libro::copias, amount)).modifiedCount == 1L

    override fun updateAvailabilityWithoutCopies(): Long =
        libros.updateMany(Libro::copias lte 0, setValue(Libro::disponible, false)).modifiedCount

    override fun deleteReturnedLoans(): Long = prestamos.deleteMany(Prestamo::devuelto eq true).deletedCount
    override fun countAutores(): Long = autores.countDocuments()
    override fun countLibros(): Long = libros.countDocuments()
    override fun countPrestamos(): Long = prestamos.countDocuments()
}
