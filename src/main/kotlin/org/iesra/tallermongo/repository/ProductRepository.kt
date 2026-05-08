package org.iesra.tallermongo.repository

import org.iesra.tallermongo.model.Product

/**
 * Define las operaciones de persistencia necesarias para el módulo CRUD de productos.
 *
 * La capa de servicio depende de este contrato para que su lógica de validación
 * pueda probarse sin una conexión real a MongoDB.
 */
interface ProductRepository {
    /** Almacena un producto y devuelve el mismo objeto de dominio al llamador. */
    fun insert(product: Product): Product

    /** Almacena varios productos en una sola operación. */
    fun insertMany(products: List<Product>): List<Product>

    /** Devuelve todos los productos almacenados actualmente en la colección. */
    fun findAll(): List<Product>

    /** Busca el primer producto con el nombre indicado, o `null` si no existe. */
    fun findByName(nombre: String): Product?

    /** Devuelve productos cuyo precio es mayor que `minimumPrice`. */
    fun findByMinimumPrice(minimumPrice: Double): List<Product>

    /** Actualiza el precio del producto identificado por `nombre`. */
    fun updatePrice(nombre: String, newPrice: Double): Boolean

    /** Incrementa el stock de todos los productos de la categoría indicada. */
    fun increaseStockByCategory(categoria: String, amount: Int): Long

    /** Aplica un porcentaje de descuento a cada producto de la categoría indicada. */
    fun applyDiscountByCategory(categoria: String, descuento: Int): Long

    /** Elimina el campo opcional de descuento de todos los productos. */
    fun removeDiscounts(): Long

    /** Sustituye un producto si existe o lo inserta si no existe. */
    fun upsert(product: Product): Boolean

    /** Elimina el primer producto que coincide con el nombre proporcionado. */
    fun deleteByName(nombre: String): Boolean

    /** Elimina todos los productos con stock menor o igual que cero. */
    fun deleteWithoutStock(): Long
}
