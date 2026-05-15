package org.iesra.tallermongo.config

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class MongoConfigTest : DescribeSpec({
    describe("MongoConfig") {
        it("debe cargar la URI y la base de datos desde el entorno") {
            val config = MongoConfig.fromEnvironment { key ->
                when (key) {
                    "MONGODB_URI" -> "mongodb+srv://usuario:password@cluster.mongodb.net/"
                    "MONGODB_DATABASE" -> "biblioteca_digital"
                    else -> null
                }
            }

            config.uri shouldBe "mongodb+srv://usuario:password@cluster.mongodb.net/"
            config.databaseName shouldBe "biblioteca_digital"
        }

        it("debe usar la base de datos por defecto cuando la variable no está definida") {
            val config = MongoConfig.fromEnvironment { key ->
                if (key == "MONGODB_URI") "mongodb://localhost:27017" else null
            }

            config.databaseName shouldBe "taller_mongo"
        }

        it("debe rechazar la ausencia de URI") {
            shouldThrow<IllegalStateException> {
                MongoConfig.fromEnvironment { null }
            }
        }

        it("debe rechazar un nombre de base de datos vacío") {
            shouldThrow<IllegalArgumentException> {
                MongoConfig(uri = "mongodb://localhost:27017", databaseName = "")
            }
        }
    }
})
