package org.iesra.tallermongo.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.iesra.tallermongo.model.Product
import org.iesra.tallermongo.repository.ProductRepository

class ProductServiceTest : DescribeSpec({
    describe("ProductService") {
        it("debe registrar un producto válido") {
            val repository = mockk<ProductRepository>()
            val product = Product(nombre = "Libro Kotlin", precio = 34.95, stock = 20, categoria = "libros")
            val capturedProduct = slot<Product>()
            every { repository.insert(capture(capturedProduct)) } returns product

            val result = ProductService(repository).register(product)

            result shouldBe product
            capturedProduct.captured.nombre shouldBe "Libro Kotlin"
            verify(exactly = 1) { repository.insert(product) }
        }

        it("debe rechazar un producto con precio negativo") {
            val repository = mockk<ProductRepository>()
            val service = ProductService(repository)
            val product = Product(nombre = "Producto inválido", precio = -1.0, stock = 1, categoria = "test")

            shouldThrow<IllegalArgumentException> {
                service.register(product)
            }
        }

        it("debe buscar productos por encima de un precio mínimo") {
            val repository = mockk<ProductRepository>()
            val products = listOf(Product(nombre = "Tablet", precio = 299.0, stock = 5, categoria = "electronica"))
            every { repository.findByMinimumPrice(50.0) } returns products

            val result = ProductService(repository).findExpensiveProducts(50.0)

            result shouldContainExactly products
            verify(exactly = 1) { repository.findByMinimumPrice(50.0) }
        }

        it("debe actualizar el precio cuando los datos son válidos") {
            val repository = mockk<ProductRepository>()
            every { repository.updatePrice("Libro Kotlin", 29.95) } returns true

            val result = ProductService(repository).updatePrice("Libro Kotlin", 29.95)

            result shouldBe true
            verify(exactly = 1) { repository.updatePrice("Libro Kotlin", 29.95) }
        }

        it("debe rechazar un descuento no válido") {
            val repository = mockk<ProductRepository>()
            val service = ProductService(repository)

            shouldThrow<IllegalArgumentException> {
                service.applyDiscountByCategory("electronica", 120)
            }
        }

        it("debe eliminar un producto por nombre") {
            val repository = mockk<ProductRepository>()
            every { repository.deleteByName("Auriculares") } returns true

            ProductService(repository).deleteByName("Auriculares") shouldBe true

            verify(exactly = 1) { repository.deleteByName("Auriculares") }
        }
    }
})
