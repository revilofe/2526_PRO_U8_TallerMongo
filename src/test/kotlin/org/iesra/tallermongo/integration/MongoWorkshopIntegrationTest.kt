package org.iesra.tallermongo.integration

import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.iesra.tallermongo.CollectionManager
import org.iesra.tallermongo.DatabaseManager
import org.iesra.tallermongo.config.MongoConfig
import org.iesra.tallermongo.config.MongoDriverProvider
import org.iesra.tallermongo.connection.MongoConnection
import org.iesra.tallermongo.model.Autor
import org.iesra.tallermongo.model.Libro
import org.iesra.tallermongo.model.Prestamo
import org.iesra.tallermongo.model.Product
import org.iesra.tallermongo.service.BibliotecaService
import org.iesra.tallermongo.service.LibraryCounts
import org.iesra.tallermongo.service.ProductService

/**
 * Prueba de integración del taller contra una instancia real de MongoDB.
 *
 * La prueba solo se ejecuta cuando `MONGODB_URI` está definida. Usa una base de
 * datos aislada y la elimina al finalizar para no afectar a los datos del taller.
 */
@OptIn(ExperimentalKotest::class)
class MongoWorkshopIntegrationTest : DescribeSpec({
    val mongoUri = System.getenv("MONGODB_URI")
    val driverProvider = System.getenv("MONGODB_DRIVER")
        ?.let(MongoDriverProvider::from)
        ?: MongoDriverProvider.MONGODB_KOTLIN

    describe("integración del taller con MongoDB").config(enabled = !mongoUri.isNullOrBlank()) {
        it("debe ejecutar operaciones de base de datos, colección, productos y biblioteca") {
            val databaseName = "taller_mongo_integration_test"
            val config = MongoConfig(
                uri = mongoUri.orEmpty(),
                databaseName = databaseName,
                driverProvider = driverProvider,
            )

            MongoConnection(config).use { connection ->
                val databaseManager = DatabaseManager(connection.client())
                databaseManager.dropDatabase(databaseName)

                try {
                    databaseManager.materializeDatabase(databaseName)
                    databaseManager.listDatabaseNames() shouldContain databaseName

                    val database = databaseManager.selectDatabase(databaseName)
                    val collectionManager = CollectionManager(database)
                    collectionManager.createCollectionIfMissing("productos")
                    collectionManager.createCollectionIfMissing("autores")
                    collectionManager.createCollectionIfMissing("libros")
                    collectionManager.createCollectionIfMissing("prestamos")

                    collectionManager.listCollectionNames() shouldContain "productos"

                    runWorkshopAssertions(
                        productService = ProductService(database.productRepository("productos")),
                        bibliotecaService = BibliotecaService(database.libraryRepository()),
                    )
                } finally {
                    databaseManager.dropDatabase(databaseName)
                }
            }
        }
    }
})

private fun runWorkshopAssertions(
    productService: ProductService,
    bibliotecaService: BibliotecaService,
) {
    val libroKotlin = Product(
        nombre = "Libro Kotlin",
        precio = 34.95,
        stock = 20,
        categoria = "libros",
    )
    productService.register(libroKotlin)
    productService.registerMany(
        listOf(
            Product(nombre = "Tablet", precio = 299.0, stock = 5, categoria = "electronica"),
            Product(nombre = "Camiseta", precio = 19.99, stock = 10, categoria = "ropa"),
        ),
    )

    productService.findAll().size shouldBe 3
    productService.findExpensiveProducts(50.0).map { it.nombre } shouldContainExactly listOf("Tablet")
    productService.updatePrice("Libro Kotlin", 29.95) shouldBe true
    productService.applyDiscountByCategory("electronica", 10) shouldBe 1
    productService.increaseStockByCategory("libros", 5) shouldBe 1
    productService.upsert(Product(nombre = "Auriculares", precio = 89.99, stock = 60, categoria = "electronica")) shouldBe true
    productService.deleteByName("Camiseta") shouldBe true

    val autores = listOf(
        Autor(nombre = "Miguel de Cervantes", nacionalidad = "Española", anioNacimiento = 1547),
    )
    val libros = listOf(
        Libro(titulo = "El Quijote", autorNombre = "Miguel de Cervantes", anio = 1605, genero = "Novela", copias = 3),
    )
    val prestamos = listOf(
        Prestamo(usuario = "Ana García", libroTitulo = "El Quijote", fechaPrestamo = "2026-05-08"),
    )

    bibliotecaService.registerAutores(autores)
    bibliotecaService.registerLibros(libros)
    bibliotecaService.registerPrestamos(prestamos)

    bibliotecaService.availableBooks().map { it.titulo } shouldContainExactly listOf("El Quijote")
    bibliotecaService.pendingLoans().size shouldBe 1
    bibliotecaService.returnLoan(prestamos.first()) shouldBe true
    bibliotecaService.deleteReturnedLoans() shouldBe 1
    bibliotecaService.counts() shouldBe LibraryCounts(autores = 1, libros = 1, prestamos = 0)
}
