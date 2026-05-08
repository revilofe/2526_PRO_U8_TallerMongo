package org.iesra.tallermongo

import org.iesra.tallermongo.config.MongoConfig
import org.iesra.tallermongo.connection.MongoConnection
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.iesra.tallermongo.model.Product
import org.iesra.tallermongo.repository.MongoLibraryRepository
import org.iesra.tallermongo.repository.MongoProductRepository
import org.iesra.tallermongo.service.BibliotecaService
import org.iesra.tallermongo.service.ProductService
import org.litote.kmongo.getCollection
import java.time.LocalDate

/** Punto de entrada con una pequeña demostración ejecutable del código del taller. */
fun main() {
    val config = runCatching { MongoConfig.fromEnvironment() }.getOrElse { error ->
        println(error.message)
        println("Ejemplo: export MONGODB_URI='mongodb+srv://usuario:password@cluster.mongodb.net/'")
        return
    }

    MongoConnection(config).use { connection ->
        val database = connection.database()
        val databaseManager = DatabaseManager(connection.client())
        println("Bases de datos visibles: ${databaseManager.listDatabaseNames()}")

        val productService = ProductService(
            MongoProductRepository(database.getCollection<Product>("productos")),
        )
        productService.register(Product(nombre = "Libro Kotlin", precio = 34.95, stock = 20, categoria = "libros"))
        println("Productos registrados: ${productService.findAll().size}")

        val bibliotecaService = BibliotecaService(
            MongoLibraryRepository(
                autores = database.getCollection<Autor>("autores"),
                libros = database.getCollection<Libro>("libros"),
                prestamos = database.getCollection<Prestamo>("prestamos"),
            ),
        )

        val autores = listOf(Autor(nombre = "Miguel de Cervantes", nacionalidad = "Española", anioNacimiento = 1547))
        val libros = listOf(Libro(titulo = "El Quijote", autorNombre = "Miguel de Cervantes", anio = 1605, genero = "Novela", copias = 3))
        val prestamos = listOf(Prestamo(usuario = "Ana García", libroTitulo = "El Quijote", fechaPrestamo = LocalDate.now().toString()))

        bibliotecaService.registerAutores(autores)
        bibliotecaService.registerLibros(libros)
        bibliotecaService.registerPrestamos(prestamos)
        println("Libros disponibles: ${bibliotecaService.availableBooks().map { it.titulo }}")
    }
}
