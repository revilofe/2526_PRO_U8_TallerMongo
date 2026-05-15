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
 * Implementación MongoDB de operaciones CRUD de productos usando el driver oficial de MongoDB para Kotlin.
 *
 * @property collection Colección tipada sobre la que se ejecutan las operaciones CRUD de productos.
 */
class OfficialMongoProductRepository(
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

    override fun findByName(nombre: String): Product? = collection.find(eq(Product::nombre.name, nombre)).firstOrNull()

    override fun findByMinimumPrice(minimumPrice: Double): List<Product> =
        collection.find(gt(Product::precio.name, minimumPrice)).toList()

    override fun updatePrice(nombre: String, newPrice: Double): Boolean =
        collection.updateOne(eq(Product::nombre.name, nombre), set(Product::precio.name, newPrice)).modifiedCount == 1L

    override fun increaseStockByCategory(categoria: String, amount: Int): Long =
        collection.updateMany(eq(Product::categoria.name, categoria), inc(Product::stock.name, amount)).modifiedCount

    override fun applyDiscountByCategory(categoria: String, descuento: Int): Long =
        collection.updateMany(eq(Product::categoria.name, categoria), set(Product::descuento.name, descuento)).modifiedCount

    override fun removeDiscounts(): Long =
        collection.updateMany(Document(), unset(Product::descuento.name)).modifiedCount

    override fun upsert(product: Product): Boolean =
        collection.replaceOne(
            eq(Product::nombre.name, product.nombre),
            product,
            ReplaceOptions().upsert(true),
        ).wasAcknowledged()

    override fun deleteByName(nombre: String): Boolean =
        collection.deleteOne(eq(Product::nombre.name, nombre)).deletedCount == 1L

    override fun deleteWithoutStock(): Long =
        collection.deleteMany(lte(Product::stock.name, 0)).deletedCount
}
