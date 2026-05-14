package org.iesra.tallermongo.repository

import com.mongodb.client.MongoCollection
import org.bson.Document
import org.iesra.tallermongo.model.Product
import org.litote.kmongo.eq
import org.litote.kmongo.gt
import org.litote.kmongo.inc
import org.litote.kmongo.lte
import org.litote.kmongo.replaceOneWithFilter
import org.litote.kmongo.replaceUpsert
import org.litote.kmongo.setValue
import org.litote.kmongo.unset

/**
 * Implementación MongoDB de operaciones CRUD de productos usando una colección tipada con KMongo.
 *
 * Todos los filtros y expresiones de actualización se construyen con helpers de
 * KMongo para mantener la implementación alineada con un taller centrado en Kotlin.
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
    override fun findByName(nombre: String): Product? = collection.find(Product::nombre eq nombre).firstOrNull()

    /**
     * Recupera los productos cuyo precio es superior al mínimo indicado.
     *
     * @param minimumPrice Umbral mínimo exclusivo para el filtro.
     * @return Productos cuyo precio es mayor que `minimumPrice`.
     */
    override fun findByMinimumPrice(minimumPrice: Double): List<Product> =
        collection.find(Product::precio gt minimumPrice).toList()

    /**
     * Actualiza el precio del producto identificado por su nombre.
     *
     * @param nombre Nombre del producto que se quiere actualizar.
     * @param newPrice Nuevo precio que se guardará.
     * @return `true` si se ha actualizado exactamente un producto.
     */
    override fun updatePrice(nombre: String, newPrice: Double): Boolean =
        collection.updateOne(Product::nombre eq nombre, setValue(Product::precio, newPrice)).modifiedCount == 1L

    /**
     * Incrementa el stock de todos los productos de una categoría.
     *
     * @param categoria Categoría cuyos productos deben modificarse.
     * @param amount Cantidad que se suma al stock actual.
     * @return Número de documentos modificados.
     */
    override fun increaseStockByCategory(categoria: String, amount: Int): Long =
        collection.updateMany(Product::categoria eq categoria, inc(Product::stock, amount)).modifiedCount

    /**
     * Guarda un descuento en todos los productos de una categoría.
     *
     * @param categoria Categoría sobre la que se aplica el descuento.
     * @param descuento Valor del descuento que se escribirá en cada producto.
     * @return Número de documentos modificados.
     */
    override fun applyDiscountByCategory(categoria: String, descuento: Int): Long =
        collection.updateMany(Product::categoria eq categoria, setValue(Product::descuento, descuento)).modifiedCount

    /**
     * Elimina el campo `descuento` de todos los productos.
     *
     * @return Número de documentos modificados.
     */
    override fun removeDiscounts(): Long = collection.updateMany(Document(), unset(Product::descuento)).modifiedCount

    /**
     * Sustituye un producto si ya existe o lo inserta si todavía no está en la colección.
     *
     * @param product Producto que se quiere guardar mediante la operación de upsert.
     * @return `true` si MongoDB reconoce la operación como válida.
     */
    override fun upsert(product: Product): Boolean =
        // El filtro localiza el producto por nombre y `replaceUpsert` hace que MongoDB
        // inserte el documento si todavía no existe o lo reemplace si ya está creado.
        collection.replaceOneWithFilter(
            Product::nombre eq product.nombre,
            product,
            replaceUpsert(),
        ).wasAcknowledged()

    /**
     * Elimina el primer producto cuyo nombre coincide con el valor indicado.
     *
     * @param nombre Nombre del producto que se quiere borrar.
     * @return `true` si se ha eliminado exactamente un documento.
     */
    override fun deleteByName(nombre: String): Boolean =
        collection.deleteOne(Product::nombre eq nombre).deletedCount == 1L

    /**
     * Elimina todos los productos sin stock disponible.
     *
     * @return Número de documeOptions and set upsert to true.entos eliminados.
     */
    override fun deleteWithoutStock(): Long = collection.deleteMany(Product::stock lte 0).deletedCount
}
