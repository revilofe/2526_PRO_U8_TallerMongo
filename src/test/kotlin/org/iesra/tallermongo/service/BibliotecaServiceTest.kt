package org.iesra.tallermongo.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.iesra.tallermongo.repository.LibraryRepository
import java.time.LocalDate

class BibliotecaServiceTest : DescribeSpec({
    describe("BibliotecaService") {
        it("debe registrar libros válidos") {
            val repository = mockk<LibraryRepository>()
            val books = listOf(Libro(titulo = "El Quijote", autorNombre = "Cervantes", anio = 1605, genero = "Novela", copias = 3))
            every { repository.insertLibros(books) } returns books

            val result = BibliotecaService(repository).registerLibros(books)

            result shouldContainExactly books
            verify(exactly = 1) { repository.insertLibros(books) }
        }

        it("debe rechazar un libro con copias negativas") {
            val repository = mockk<LibraryRepository>()
            val books = listOf(Libro(titulo = "Sin copias", autorNombre = "Autor", anio = 2024, genero = "Ensayo", copias = -1))

            shouldThrow<IllegalArgumentException> {
                BibliotecaService(repository).registerLibros(books)
            }
        }

        it("debe devolver un préstamo e incrementar las copias del libro") {
            val repository = mockk<LibraryRepository>()
            val loan = Prestamo(_id = "prestamo-1", usuario = "Ana", libroTitulo = "El Quijote", fechaPrestamo = LocalDate.now().toString())
            every { repository.markLoanAsReturned("prestamo-1") } returns true
            every { repository.increaseCopiesByTitle("El Quijote", 1) } returns true

            val result = BibliotecaService(repository).returnLoan(loan)

            result shouldBe true
            verify(exactly = 1) { repository.markLoanAsReturned("prestamo-1") }
            verify(exactly = 1) { repository.increaseCopiesByTitle("El Quijote", 1) }
        }

        it("debe informar del recuento de colecciones") {
            val repository = mockk<LibraryRepository>()
            every { repository.countAutores() } returns 3
            every { repository.countLibros() } returns 6
            every { repository.countPrestamos() } returns 2

            val counts = BibliotecaService(repository).counts()

            counts shouldBe LibraryCounts(autores = 3, libros = 6, prestamos = 2)
        }

        it("debe registrar autores cuando los datos son válidos") {
            val repository = mockk<LibraryRepository>()
            val authors = listOf(Autor(nombre = "Cervantes", nacionalidad = "Española", anioNacimiento = 1547))
            every { repository.insertAutores(authors) } returns authors

            BibliotecaService(repository).registerAutores(authors) shouldContainExactly authors
            verify(exactly = 1) { repository.insertAutores(authors) }
        }
    }
})
