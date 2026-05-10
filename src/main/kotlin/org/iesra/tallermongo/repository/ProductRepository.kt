package org.iesra.tallermongo.repository

import org.iesra.tallermongo.model.Product

/**
 * Define las operaciones de persistencia necesarias para el módulo CRUD de productos.
 *
 * La capa de servicio depende de este contrato para que su lógica de validación
 * pueda probarse sin una conexión real a MongoDB.
 */
interface ProductRepository {
    /**
     * Almacena un producto y devuelve el mismo objeto de dominio al llamador.
     *
     * @param product Producto que se quiere insertar.
     * @return El producto insertado, manteniendo una API simple para el taller.
     */
    fun insert(product: Product): Product

    /**
     * Almacena varios productos en una sola operación.
     *
     * @param products Lista de productos que se quieren insertar.
     * @return La misma lista recibida tras la operación de inserción.
     */
    fun insertMany(products: List<Product>): List<Product>

    /**
     * Devuelve todos los productos almacenados actualmente en la colección.
     *
     * @return Lista completa de productos disponibles en la persistencia.
     */
    fun findAll(): List<Product>

    /**
     * Busca el primer producto con el nombre indicado, o `null` si no existe.
     *
     * @param nombre Nombre del producto que se quiere localizar.
     * @return El producto encontrado o `null` si no hay coincidencia.
     */
    fun findByName(nombre: String): Product?

    /**
     * Devuelve productos cuyo precio es mayor que `minimumPrice`.
     *
     * @param minimumPrice Precio mínimo exclusivo usado como filtro.
     * @return Productos cuyo precio supera el valor indicado.
     */
    fun findByMinimumPrice(minimumPrice: Double): List<Product>

    /**
     * Actualiza el precio del producto identificado por `nombre`.
     *
     * @param nombre Nombre del producto que debe actualizarse.
     * @param newPrice Nuevo precio que se guardará.
     * @return `true` si se ha actualizado exactamente un producto; `false` en caso contrario.
     */
    fun updatePrice(nombre: String, newPrice: Double): Boolean

    /**
     * Incrementa el stock de todos los productos de la categoría indicada.
     *
     * @param categoria Categoría cuyos productos deben actualizarse.
     * @param amount Cantidad que se suma al stock actual.
     * @return Número de productos modificados.
     */
    fun increaseStockByCategory(categoria: String, amount: Int): Long

    /**
     * Aplica un porcentaje de descuento a cada producto de la categoría indicada.
     *
     * @param categoria Categoría sobre la que se aplica el descuento.
     * @param descuento Porcentaje entero de descuento que se guardará en cada producto.
     * @return Número de productos modificados.
     */
    fun applyDiscountByCategory(categoria: String, descuento: Int): Long

    /**
     * Elimina el campo opcional de descuento de todos los productos.
     *
     * @return Número de productos modificados.
     */
    fun removeDiscounts(): Long

    /**
     * Sustituye un producto si existe o lo inserta si no existe.
     *
     * @param product Producto que se quiere guardar mediante una operación de upsert.
     * @return `true` si MongoDB reconoce correctamente la operación.
     */
    fun upsert(product: Product): Boolean

    /**
     * Elimina el primer producto que coincide con el nombre proporcionado.
     *
     * @param nombre Nombre del producto que se quiere eliminar.
     * @return `true` si se ha eliminado exactamente un producto; `false` en caso contrario.
     */
    fun deleteByName(nombre: String): Boolean

    /**
     * Elimina todos los productos con stock menor o igual que cero.
     *
     * @return Número de productos eliminados.
     */
    fun deleteWithoutStock(): Long
}
