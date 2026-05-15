package org.iesra.tallermongo

import org.iesra.tallermongo.config.MongoConfig
import org.iesra.tallermongo.connection.MongoConnection
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.iesra.tallermongo.model.Product
import org.iesra.tallermongo.service.BibliotecaService
import org.iesra.tallermongo.service.ProductService
import java.time.LocalDate

/**
 * Punto de entrada con una pequeña demostración ejecutable del código del taller.
 *
 * El objetivo es mostrar una secuencia mínima de uso: leer la configuración,
 * abrir la conexión, trabajar con productos y ejecutar el ejemplo integrado de biblioteca.
 */
fun main() {
    // Se intenta leer la configuración desde el entorno y, si falta, se muestra
    // un mensaje claro para que el alumnado pueda preparar la conexión.
    val config = runCatching { MongoConfig.fromEnvironment() }.getOrElse { error ->
        println(error.message)
        println("Ejemplo: export MONGODB_URI='mongodb+srv://usuario:password@cluster.mongodb.net/'")
        println("Driver opcional: export MONGODB_DRIVER='MONGODB_KOTLIN' o 'KMONGO'")
        return
    }

    MongoConnection(config).use { connection ->
        val database = connection.database()
        val databaseManager = DatabaseManager(connection.client())
        println("Driver seleccionado: ${config.driverProvider}")
        println("Bases de datos visibles: ${databaseManager.listDatabaseNames()}")

        val productService = ProductService(database.productRepository("productos"))
        val bibliotecaService = BibliotecaService(database.libraryRepository())

        runWorkshopDemo(productService, bibliotecaService)
    }
}

private fun runWorkshopDemo(productService: ProductService, bibliotecaService: BibliotecaService) {
    productService.register(Product(nombre = "Libro Kotlin", precio = 34.95, stock = 20, categoria = "libros"))
    println("Productos registrados: ${productService.findAll().size}")

    val autores = listOf(Autor(nombre = "Miguel de Cervantes", nacionalidad = "Española", anioNacimiento = 1547))
    val libros = listOf(
        Libro(
            titulo = "El Quijote",
            autorNombre = "Miguel de Cervantes",
            anio = 1605,
            genero = "Novela",
            copias = 3,
        ),
    )
    val prestamos = listOf(
        Prestamo(usuario = "Ana García", libroTitulo = "El Quijote", fechaPrestamo = LocalDate.now().toString()),
    )

    bibliotecaService.registerAutores(autores)
    bibliotecaService.registerLibros(libros)
    bibliotecaService.registerPrestamos(prestamos)
    println("Libros disponibles: ${bibliotecaService.availableBooks().map { it.titulo }}")
}
