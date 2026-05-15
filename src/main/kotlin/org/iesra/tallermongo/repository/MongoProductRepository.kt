package org.iesra.tallermongo.repository

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.gt
import com.mongodb.client.model.Filters.lte
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Updates.inc
import com.mongodb.client.model.Updates.set
import com.mongodb.client.model.Updates.unset
import com.mongodb.kotlin.client.MongoCollection
import org.bson.Document
import org.iesra.tallermongo.model.Product

/**
 * Implementación MongoDB de operaciones CRUD de productos.
 *
 * @property collection Colección tipada sobre la que se ejecutan las operaciones CRUD de productos.
 */
class MongoProductRepository(
    private val collection: MongoCollection<Product>,
) : ProductRepository {
    /**
     * Inserta un producto en la colección.
     *
     * @param product Producto que se quiere guardar.
     * @return El mismo producto recibido tras completar la inserción.
     */
    override fun insert(product: Product): Product {
        collection.insertOne(product)
        return product
    }

    /**
     * Inserta varios productos en una sola operación.
     *
     * @param products Productos que se quieren guardar.
     * @return La misma lista recibida tras la inserción.
     */
    override fun insertMany(products: List<Product>): List<Product> {
        // En el taller se comprueba primero que haya elementos para dejar claro que
        // `insertMany` solo tiene sentido cuando realmente existen documentos que insertar.
        if (products.isNotEmpty()) collection.insertMany(products)
        return products
    }

    /**
     * Recupera todos los productos almacenados.
     *
     * @return Lista completa de productos de la colección.
     */
    override fun findAll(): List<Product> = collection.find().toList()

    /**
     * Busca el primer producto cuyo nombre coincide con el valor indicado.
     *
     * @param nombre Nombre del producto buscado.
     * @return El producto encontrado o `null` si no existe coincidencia.
     */
    override fun findByName(nombre: String): Product? = collection.find(eq(Product::nombre.name, nombre)).firstOrNull()

    /**
     * Recupera los productos cuyo precio es superior al mínimo indicado.
     *
     * @param minimumPrice Umbral mínimo exclusivo para el filtro.
     * @return Productos cuyo precio es mayor que `minimumPrice`.
     */
    override fun findByMinimumPrice(minimumPrice: Double): List<Product> =
        collection.find(gt(Product::precio.name, minimumPrice)).toList()

    /**
     * Actualiza el precio del producto identificado por su nombre.
     *
     * @param nombre Nombre del producto que se quiere actualizar.
     * @param newPrice Nuevo precio que se guardará.
     * @return `true` si se ha actualizado exactamente un producto.
     */
    override fun updatePrice(nombre: String, newPrice: Double): Boolean =
        collection.updateOne(eq(Product::nombre.name, nombre), set(Product::precio.name, newPrice)).modifiedCount == 1L

    /**
     * Incrementa el stock de todos los productos de una categoría.
     *
     * @param categoria Categoría cuyos productos deben modificarse.
     * @param amount Cantidad que se suma al stock actual.
     * @return Número de documentos modificados.
     */
    override fun increaseStockByCategory(categoria: String, amount: Int): Long =
        collection.updateMany(eq(Product::categoria.name, categoria), inc(Product::stock.name, amount)).modifiedCount

    /**
     * Guarda un descuento en todos los productos de una categoría.
     *
     * @param categoria Categoría sobre la que se aplica el descuento.
     * @param descuento Valor del descuento que se escribirá en cada producto.
     * @return Número de documentos modificados.
     */
    override fun applyDiscountByCategory(categoria: String, descuento: Int): Long =
        collection.updateMany(eq(Product::categoria.name, categoria), set(Product::descuento.name, descuento)).modifiedCount

    /**
     * Elimina el campo `descuento` de todos los productos.
     *
     * @return Número de documentos modificados.
     */
    override fun removeDiscounts(): Long =
        collection.updateMany(Document(), unset(Product::descuento.name)).modifiedCount

    /**
     * Sustituye un producto si ya existe o lo inserta si todavía no está en la colección.
     *
     * @param product Producto que se quiere guardar mediante la operación de upsert.
     * @return `true` si MongoDB reconoce la operación como válida.
     */
    override fun upsert(product: Product): Boolean =
        // El filtro localiza el producto por nombre y `upsert` hace que MongoDB
        // inserte el documento si todavía no existe o lo reemplace si ya está creado.
        collection.replaceOne(
            eq(Product::nombre.name, product.nombre),
            product,
            ReplaceOptions().upsert(true),
        ).wasAcknowledged()

    /**
     * Elimina el primer producto cuyo nombre coincide con el valor indicado.
     *
     * @param nombre Nombre del producto que se quiere borrar.
     * @return `true` si se ha eliminado exactamente un documento.
     */
    override fun deleteByName(nombre: String): Boolean =
        collection.deleteOne(eq(Product::nombre.name, nombre)).deletedCount == 1L

    /**
     * Elimina todos los productos sin stock disponible.
     *
     * @return Número de documentos eliminados.
     */
    override fun deleteWithoutStock(): Long = collection.deleteMany(lte(Product::stock.name, 0)).deletedCount
}
