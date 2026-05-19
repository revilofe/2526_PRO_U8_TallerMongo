# Unidad 8. MongoDB con Kotlin

Este repositorio contiene el material de la Unidad 8 de Programación para trabajar con bases de datos NoSQL usando MongoDB Atlas desde Kotlin.

El proyecto está organizado como un taller práctico y progresivo. El objetivo es que aprendas a conectar una aplicación Kotlin con MongoDB, gestionar bases de datos y colecciones, realizar operaciones CRUD y modelar relaciones entre documentos usando referencias por `_id`.

## Contenido del Taller

Documentación principal:

- [Taller práctico de MongoDB con Kotlin](doc/taller_mongodb.md)
- [Taller práctico de MongoDB con Kotlin en PDF](doc/taller_mongodb.pdf)

Documentación de configuración del entorno de trabajo con OpenCode:

- [Guía de configuración de OpenCode](doc/guia_configuracion_opencode.md)
- [Guía de configuración de OpenCode en PDF](doc/guia_configuracion_opencode.pdf)

El taller cubre:

- configuración de conexión con MongoDB Atlas mediante variables de entorno
- gestión de bases de datos
- gestión de colecciones
- inserción, consulta, actualización y eliminación de documentos
- uso de modelos Kotlin con `data class`
- uso del driver de MongoDB para Kotlin
- servicios y repositorios sencillos para separar responsabilidades
- pruebas unitarias con Kotest y MockK
- prueba de integración opcional contra MongoDB real
- ampliaciones sobre referencias por `_id` y consultas con `$lookup`
- proyecto integrado final de mediateca con varias colecciones relacionadas

## Ejercicios

Accede a la documentación del taller, estúdiala y completa todas las actividades. Finalmente, realiza el último ejercicio: **Proyecto integrado**.

**Parte 1:**  Realización del taller práctico de MongoDB con Kotlin, siguiendo la documentación y completando los ejercicios propuestos.
- Lee el documento del taller, desarrolla las soluciones y ejecuta los ejercicios propuestos.
- Los ejercicios deben estar en el paquete `org.iesra.tallermongo.ejercicios` y en los archivos `Ejercicio1.kt`, `Ejercicio2.kt`, ..., `Ejercicio6.kt`.

**Parte 2:**  Elaboración de un proyecto integrado de mediateca, aplicando los conocimientos adquiridos en el taller.
- Lee, comprende y desarrolla el apartado **Proyecto integrado**.
- Este ejercicio debe estar en el paquete `org.iesra.tallermongo.ejercicios` y en el archivo `Ejercicio7.kt`.



## Prueba de Concepto con IA

Además del taller de MongoDB, este repositorio incluye una prueba de concepto sobre cómo configurar un proyecto para trabajar con ayuda de IA.

La configuración local se apoya en OpenCode y está organizada en:

```text
.opencode/
├── agents/
├── skills/
└── instructions/
```

La idea es explorar cómo documentar y guiar el trabajo de la IA mediante:

- **agentes**, con responsabilidades concretas como desarrollo, documentación o commits
- **skills**, con buenas prácticas reutilizables para Kotlin, MongoDB, testing y documentación
- **instrucciones de proyecto**, para mantener coherencia técnica y didáctica

Esta parte no sustituye al taller, pero sirve como ejemplo de cómo preparar un repositorio educativo para colaborar de forma más controlada con herramientas de IA.

## Tecnología

- Kotlin 2.3.0
- JDK 21
- Gradle con Kotlin DSL
- MongoDB Atlas
- Driver oficial de MongoDB para Kotlin
- Kotest
- MockK

## Estructura Principal

```text
src/
├── main/kotlin/org/iesra/tallermongo/
│   ├── config/
│   ├── connection/
│   ├── ejercicios/
│   │   ├── Ejercicio1.kt
│   │   └── Ejercicio2.kt
│   ├── model/
│   ├── repository/
│   ├── service/
│   ├── CollectionManager.kt
│   ├── DatabaseManager.kt
│   └── Main.kt
└── test/kotlin/org/iesra/tallermongo/
    ├── config/
    ├── integration/
    └── service/
```

El paquete `org.iesra.tallermongo.ejercicios` está reservado para que añadas tus soluciones con el formato `Ejercicio1.kt`, `Ejercicio2.kt`, `Ejercicio3.kt`, etc.

## Configuración

No escribas credenciales reales en el código.

Antes de ejecutar la aplicación o la prueba de integración, define la URI de conexión:

```text
export MONGODB_URI="mongodb+srv://usuario:password@cluster.mongodb.net/"
export MONGODB_DATABASE="taller_mongo"
```

El repositorio incluye un fichero de ejemplo:

```text
.env.example
```

## Ejecutar Tests

```text
./gradlew test
```

Si tu sistema no usa JDK 21 por defecto, configura `JAVA_HOME` antes de ejecutar Gradle:

```text
export JAVA_HOME=/ruta/a/jdk-21
./gradlew test
```

La prueba de integración solo se ejecuta realmente si `MONGODB_URI` está definida. Si no lo está, se omite para que puedas ejecutar los tests unitarios sin depender de MongoDB Atlas.

## Ejecutar la Aplicación

Define primero las variables de entorno:

```text
export MONGODB_URI="mongodb+srv://usuario:password@cluster.mongodb.net/"
export MONGODB_DATABASE="taller_mongo"
```

Después ejecuta `Main.kt` desde el IDE.

## Objetivo Didáctico

La unidad no busca construir una aplicación grande, sino entender bien las piezas fundamentales:

- cómo se conecta Kotlin con MongoDB
- qué son bases de datos, colecciones y documentos
- cómo se hacen operaciones CRUD
- cómo organizar el acceso a datos de forma sencilla
- cuándo usar referencias por `_id`
- cómo hacer consultas que relacionan colecciones con `$lookup`

El código intenta ser claro y directo para que sirva como material de aprendizaje y como base para ejercicios de clase.
