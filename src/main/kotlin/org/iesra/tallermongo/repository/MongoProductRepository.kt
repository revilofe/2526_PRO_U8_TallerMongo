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
 */
class MongoProductRepository(
    private val collection: MongoCollection<Product>,
) : ProductRepository {
    override fun insert(product: Product): Product {
        collection.insertOne(product)
        return product
    }

    override fun insertMany(products: List<Product>): List<Product> {
        if (products.isNotEmpty()) collection.insertMany(products)
        return products
    }

    override fun findAll(): List<Product> = collection.find().toList()

    override fun findByName(nombre: String): Product? = collection.find(Product::nombre eq nombre).firstOrNull()

    override fun findByMinimumPrice(minimumPrice: Double): List<Product> =
        collection.find(Product::precio gt minimumPrice).toList()

    override fun updatePrice(nombre: String, newPrice: Double): Boolean =
        collection.updateOne(Product::nombre eq nombre, setValue(Product::precio, newPrice)).modifiedCount == 1L

    override fun increaseStockByCategory(categoria: String, amount: Int): Long =
        collection.updateMany(Product::categoria eq categoria, inc(Product::stock, amount)).modifiedCount

    override fun applyDiscountByCategory(categoria: String, descuento: Int): Long =
        collection.updateMany(Product::categoria eq categoria, setValue(Product::descuento, descuento)).modifiedCount

    override fun removeDiscounts(): Long = collection.updateMany(Document(), unset(Product::descuento)).modifiedCount

    override fun upsert(product: Product): Boolean =
        collection.replaceOneWithFilter(
            Product::nombre eq product.nombre,
            product,
            replaceUpsert(),
        ).wasAcknowledged()

    override fun deleteByName(nombre: String): Boolean =
        collection.deleteOne(Product::nombre eq nombre).deletedCount == 1L

    override fun deleteWithoutStock(): Long = collection.deleteMany(Product::stock lte 0).deletedCount
}
