package org.iesra.tallermongo.service

import org.iesra.tallermongo.model.Product
import org.iesra.tallermongo.repository.ProductRepository

/**
 * Coordina la validación y las operaciones CRUD de productos del módulo de documentos.
 *
 * Esta clase es intencionadamente pequeña: muestra dónde colocar la validación
 * sin ocultar las operaciones MongoDB detrás de una arquitectura compleja.
 */
class ProductService(
    private val repository: ProductRepository,
) {
    /**
     * Valida y almacena un producto.
     *
     * @throws IllegalArgumentException cuando el nombre o la categoría están vacíos, o el precio/stock son negativos.
     */
    fun register(product: Product): Product {
        validateProduct(product)
        return repository.insert(product)
    }

    /**
     * Valida y almacena varios productos.
     *
     * @throws IllegalArgumentException cuando la lista está vacía o algún producto no es válido.
     */
    fun registerMany(products: List<Product>): List<Product> {
        require(products.isNotEmpty()) { "Debes indicar al menos un producto." }
        products.forEach(::validateProduct)
        return repository.insertMany(products)
    }

    /** Devuelve todos los productos del repositorio. */
    fun findAll(): List<Product> = repository.findAll()

    /**
     * Busca productos cuyo precio es mayor que el mínimo proporcionado.
     *
     * @throws IllegalArgumentException cuando `minimumPrice` es negativo.
     */
    fun findExpensiveProducts(minimumPrice: Double): List<Product> {
        require(minimumPrice >= 0) { "El precio mínimo no puede ser negativo." }
        return repository.findByMinimumPrice(minimumPrice)
    }

    /**
     * Cambia el precio de un producto identificado por nombre.
     *
     * @return `true` cuando se modificó un documento.
     * @throws IllegalArgumentException cuando el nombre está vacío o el precio es negativo.
     */
    fun updatePrice(nombre: String, newPrice: Double): Boolean {
        require(nombre.isNotBlank()) { "El nombre del producto no puede estar vacío." }
        require(newPrice >= 0) { "El precio no puede ser negativo." }
        return repository.updatePrice(nombre, newPrice)
    }

    /**
     * Aplica un porcentaje de descuento a todos los productos de una categoría.
     *
     * @return Número de documentos modificados.
     * @throws IllegalArgumentException cuando la categoría está vacía o el descuento está fuera de `1..100`.
     */
    fun applyDiscountByCategory(categoria: String, descuento: Int): Long {
        require(categoria.isNotBlank()) { "La categoría no puede estar vacía." }
        require(descuento in 1..100) { "El descuento debe estar entre 1 y 100." }
        return repository.applyDiscountByCategory(categoria, descuento)
    }

    /**
     * Incrementa el stock de cada producto de una categoría.
     *
     * @return Número de documentos modificados.
     * @throws IllegalArgumentException cuando la categoría está vacía o `amount` no es positivo.
     */
    fun increaseStockByCategory(categoria: String, amount: Int): Long {
        require(categoria.isNotBlank()) { "La categoría no puede estar vacía." }
        require(amount > 0) { "El incremento de stock debe ser positivo." }
        return repository.increaseStockByCategory(categoria, amount)
    }

    /**
     * Sustituye un producto existente o lo inserta cuando no existe.
     *
     * @throws IllegalArgumentException cuando el producto no es válido.
     */
    fun upsert(product: Product): Boolean {
        validateProduct(product)
        return repository.upsert(product)
    }

    /** Elimina la información de descuento de todos los productos. */
    fun removeDiscounts(): Long = repository.removeDiscounts()

    /**
     * Elimina el primer producto con el nombre proporcionado.
     *
     * @return `true` cuando se eliminó un documento.
     * @throws IllegalArgumentException cuando `nombre` está vacío.
     */
    fun deleteByName(nombre: String): Boolean {
        require(nombre.isNotBlank()) { "El nombre del producto no puede estar vacío." }
        return repository.deleteByName(nombre)
    }

    /** Elimina cada producto cuyo stock es menor o igual que cero. */
    fun deleteWithoutStock(): Long = repository.deleteWithoutStock()

    private fun validateProduct(product: Product) {
        require(product.nombre.isNotBlank()) { "El nombre del producto no puede estar vacío." }
        require(product.precio >= 0) { "El precio no puede ser negativo." }
        require(product.stock >= 0) { "El stock no puede ser negativo." }
        require(product.categoria.isNotBlank()) { "La categoría no puede estar vacía." }
    }
}
