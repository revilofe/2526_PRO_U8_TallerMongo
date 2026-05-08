package org.iesra.tallermongo.service

import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.iesra.tallermongo.repository.LibraryRepository

/**
 * Proporciona las operaciones del ejercicio integrado de biblioteca digital.
 *
 * El servicio valida los datos antes de delegar la persistencia en [LibraryRepository],
 * lo que mantiene los ejemplos del taller fáciles de probar sin MongoDB.
 */
class BibliotecaService(
    private val repository: LibraryRepository,
) {
    /**
     * Valida y almacena autores.
     *
     * @throws IllegalArgumentException cuando la lista está vacía o un autor contiene datos no válidos.
     */
    fun registerAutores(autores: List<Autor>): List<Autor> {
        require(autores.isNotEmpty()) { "Debes indicar al menos un autor." }
        autores.forEach { autor ->
            require(autor.nombre.isNotBlank()) { "El nombre del autor no puede estar vacío." }
            require(autor.nacionalidad.isNotBlank()) { "La nacionalidad del autor no puede estar vacía." }
            require(autor.anioNacimiento > 0) { "El año de nacimiento debe ser positivo." }
        }
        return repository.insertAutores(autores)
    }

    /**
     * Valida y almacena libros.
     *
     * @throws IllegalArgumentException cuando la lista está vacía o un libro contiene datos no válidos.
     */
    fun registerLibros(libros: List<Libro>): List<Libro> {
        require(libros.isNotEmpty()) { "Debes indicar al menos un libro." }
        libros.forEach(::validateLibro)
        return repository.insertLibros(libros)
    }

    /**
     * Valida y almacena préstamos.
     *
     * @throws IllegalArgumentException cuando la lista está vacía o un préstamo contiene datos no válidos.
     */
    fun registerPrestamos(prestamos: List<Prestamo>): List<Prestamo> {
        require(prestamos.isNotEmpty()) { "Debes indicar al menos un préstamo." }
        prestamos.forEach { prestamo ->
            require(prestamo.usuario.isNotBlank()) { "El usuario del préstamo no puede estar vacío." }
            require(prestamo.libroTitulo.isNotBlank()) { "El título del libro no puede estar vacío." }
        }
        return repository.insertPrestamos(prestamos)
    }

    /** Devuelve los libros marcados actualmente como disponibles. */
    fun availableBooks(): List<Libro> = repository.findAvailableBooks()

    /** Devuelve los préstamos que todavía no se han devuelto. */
    fun pendingLoans(): List<Prestamo> = repository.findPendingLoans()

    /**
     * Marca un préstamo como devuelto e incrementa el número de copias de su libro.
     *
     * Las dos operaciones de persistencia son visibles de forma intencionada con
     * fines didácticos; en producción normalmente se usarían transacciones cuando
     * fuese necesaria atomicidad.
     *
     * @return `true` solo cuando ambas actualizaciones tienen éxito.
     * @throws IllegalArgumentException cuando el identificador del préstamo está vacío.
     */
    fun returnLoan(prestamo: Prestamo): Boolean {
        require(prestamo._id.isNotBlank()) { "El identificador del préstamo no puede estar vacío." }
        val loanUpdated = repository.markLoanAsReturned(prestamo._id)
        val copiesUpdated = repository.increaseCopiesByTitle(prestamo.libroTitulo, 1)
        return loanUpdated && copiesUpdated
    }

    /** Marca como no disponibles los libros con cero copias o menos. */
    fun updateAvailabilityWithoutCopies(): Long = repository.updateAvailabilityWithoutCopies()

    /** Elimina los préstamos ya marcados como devueltos. */
    fun deleteReturnedLoans(): Long = repository.deleteReturnedLoans()

    /** Devuelve el recuento de documentos de las tres colecciones de biblioteca. */
    fun counts(): LibraryCounts = LibraryCounts(
        autores = repository.countAutores(),
        libros = repository.countLibros(),
        prestamos = repository.countPrestamos(),
    )

    private fun validateLibro(libro: Libro) {
        require(libro.titulo.isNotBlank()) { "El título del libro no puede estar vacío." }
        require(libro.autorNombre.isNotBlank()) { "El autor del libro no puede estar vacío." }
        require(libro.anio > 0) { "El año del libro debe ser positivo." }
        require(libro.genero.isNotBlank()) { "El género del libro no puede estar vacío." }
        require(libro.copias >= 0) { "El número de copias no puede ser negativo." }
    }
}

/**
 * Resumen del recuento de documentos en las colecciones integradas de biblioteca.
 *
 * @property autores Número de documentos en la colección `autores`.
 * @property libros Número de documentos en la colección `libros`.
 * @property prestamos Número de documentos en la colección `prestamos`.
 */
data class LibraryCounts(
    val autores: Long,
    val libros: Long,
    val prestamos: Long,
)
