package org.iesra.tallermongo.model

import org.bson.types.ObjectId

/**
 * Representa un producto almacenado en la colección `productos`.
 *
 * El identificador se genera como `String` para mantener los ejemplos sencillos
 * y fáciles de imprimir, comparar y reutilizar en los ejercicios.
 *
 * @property _id Identificador de MongoDB almacenado como texto.
 * @property nombre Nombre visible usado en filtros y actualizaciones.
 * @property precio Precio del producto en euros; el servicio rechaza valores negativos.
 * @property stock Unidades disponibles; el servicio rechaza valores negativos.
 * @property categoria Categoría principal usada en los ejemplos de actualización.
 * @property categorias Lista opcional de categorías secundarias.
 * @property descuento Porcentaje de descuento opcional usado en ejercicios de inventario.
 */
data class Product(
    val _id: String = ObjectId().toHexString(),
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val categorias: List<String> = emptyList(),
    val descuento: Int? = null,
)
