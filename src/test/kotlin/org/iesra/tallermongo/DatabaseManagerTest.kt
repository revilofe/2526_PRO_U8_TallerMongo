package org.iesra.tallermongo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec

class DatabaseManagerTest : DescribeSpec({
    describe("validación de nombres en DatabaseManager") {
        it("debe rechazar nombres de recursos vacíos") {
            shouldThrow<IllegalArgumentException> {
                DatabaseManager.validateName("", "base de datos")
            }
        }

        it("debe rechazar nombres con barra") {
            shouldThrow<IllegalArgumentException> {
                DatabaseManager.validateName("tienda/online", "base de datos")
            }
        }
    }
})
