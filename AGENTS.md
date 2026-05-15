# AGENTS.md - Proyecto Taller MongoDB con Kotlin

## Descripción del Proyecto

Este proyecto consiste en adaptar un taller práctico de MongoDB originalmente escrito en Python a una versión equivalente en Kotlin.

El objetivo es construir un taller educativo, progresivo y claro, que enseñe operaciones CRUD con MongoDB Atlas utilizando Kotlin y el driver de MongoDB para Kotlin.

La fuente principal del contenido funcional y didáctico está en:

```text
doc/taller_mongodb.md
```

## Estado del Proyecto

### Estado actual

El repositorio está en fase de adaptación.

Actualmente puede no estar implementada todavía toda la estructura Kotlin, las dependencias definitivas o los tests descritos como objetivo del taller.

Por tanto:

- La documentación Python es la referencia principal.
- La implementación Kotlin se construirá progresivamente.
- Antes de asumir que una clase, test o documento ya existe, hay que comprobarlo en el repositorio.

### Objetivo del proyecto

El objetivo final es disponer de:

- Una implementación en Kotlin de los ejemplos principales del taller.
- Una estructura clara para trabajar con MongoDB Atlas.
- Ejercicios adaptados a Kotlin.
- Soluciones de referencia.
- Documentación didáctica orientada a alumnado.
- Una base de código sencilla, coherente y mantenible.

## Tecnología y Dependencias

### Tecnología actual

- **Lenguaje**: Kotlin 2.3.0
- **JDK**: 21
- **Gestor de dependencias**: Gradle con Kotlin DSL

### Tecnología objetivo

- **Driver MongoDB objetivo**: driver de MongoDB para Kotlin
- **Base de datos**: MongoDB Atlas
- **Testing objetivo**: Kotest + MockK
- **Estilo de tests objetivo**: `DescribeSpec` en tests nuevos, cuando el proyecto ya tenga esa base preparada

## Configuración Local de OpenCode

La configuración local de OpenCode de este proyecto vive en:

```text
.opencode/
```

Estructura principal:

```text
.opencode/
├── opencode.json
├── agents/
├── skills/
└── instructions/
```

Reglas importantes:

- Los agentes locales del proyecto están en `.opencode/agents/`
- Las skills locales del proyecto están en `.opencode/skills/`
- Las instrucciones específicas del proyecto están en `.opencode/instructions/`
- No asumir el uso de `.agents/` o `.skills/` en la raíz del proyecto si no existen realmente

## Estructura del Proyecto

### Estructura actual

La estructura real del proyecto debe comprobarse en el repositorio antes de tomar decisiones.

### Estructura objetivo recomendada

Como guía de evolución, el proyecto puede organizarse de forma similar a esta:

```text
src/
├── main/kotlin/org/iesra/tallermongo/
│   ├── MongoConnection.kt
│   ├── DatabaseManager.kt
│   ├── CollectionManager.kt
│   ├── DocumentManager.kt
│   └── Main.kt
└── test/kotlin/org/iesra/tallermongo/
    └── DocumentManagerTest.kt
```

Esta estructura es una referencia de trabajo, no una garantía de que todos esos archivos existan ya.

Si el proyecto evoluciona, también puede organizarse por paquetes más específicos como `config`, `connection`, `model`, `repository` o `service`, siempre que se mantenga la claridad didáctica.

## Reglas de Código

### Kotlin

- Usar la skill `kotlin-best-practices` para todo código Kotlin
- Preferir `val` sobre `var`
- Usar nombres claros y expresivos
- Documentar con KDoc las funciones y clases públicas cuando tenga sentido
- Aplicar principios de clean code y SOLID sin sobrecomplicar el código
- Mantener el código comprensible para alumnado

### MongoDB

- Usar la skill `mongo-best-practices` para las operaciones con MongoDB
- Usar el driver de MongoDB para Kotlin como única librería de acceso a MongoDB
- Evitar credenciales hardcoded
- Leer configuración desde variables de entorno o configuración externa
- Validar datos antes de operar cuando sea razonable
- Manejar errores de forma explícita
- Mantener el foco en objetivos didácticos CRUD, evitando abstracciones innecesarias

### Testing

- Cuando el proyecto incorpore la infraestructura de testing prevista, usar `Kotest` y `MockK`
- En tests nuevos, preferir `DescribeSpec` si la configuración ya lo soporta
- Separar pruebas unitarias de pruebas que dependan de MongoDB real
- Mantener tests pequeños, deterministas y fáciles de entender
- Usar `MockK` para mocks, stubs y verificación de interacciones
- Usar `Kotest` como framework principal de testing
- Usar la skill `kotlin-unit-testing` para todas las pruebas unitarias

### Documentación

- Mantener explicaciones progresivas, claras y orientadas a alumnado
- Separar claramente enunciados, ejemplos y soluciones
- Conservar la intención pedagógica del taller original en Python
- Adaptar ejemplos y explicaciones al ecosistema Kotlin con el driver de MongoDB para Kotlin
- Usar la skill `pedagogical-documentation` para redactar contenido didáctico
- Usar la skill `workshop-markdown-structure` para estructurar documentación y ejercicios

## Convenciones de Nomenclatura

### Convención objetivo recomendada

- **Paquete objetivo recomendado**: `org.iesra.tallermongo`
- **Clases**: `PascalCase`
- **Funciones y variables**: `camelCase`
- **Constantes**: `UPPER_SNAKE_CASE`
- **Colecciones MongoDB**: nombres en plural, por ejemplo `productos`, `clientes`, `prestamos`
- **Bases de datos**: `snake_case`, por ejemplo `tienda_online`, `biblioteca_digital`

Estas convenciones deben aplicarse especialmente en el código nuevo o refactorizado.

## Configuración de Conexión

Nunca escribir credenciales reales en el código.

Usar configuración externa. Por ejemplo:

```kotlin
data class MongoConfig(
    val uri: String,
    val database: String
)
```

Ejemplo de variables de entorno:

```text
MONGODB_URI=mongodb+srv://usuario:password@cluster.mongodb.net/
MONGODB_DATABASE=taller_mongo
```

Si hace falta documentarlo, preferir un fichero de ejemplo como:

```text
.env.example
```

## Documentación del Taller

La documentación fuente del taller está en:

```text
doc/taller_mongodb.md
```

Reglas para trabajar con ella:

- Mantener la misma progresión didáctica general: bases de datos, colecciones, documentos y proyecto integrado
- Traducir los ejemplos de Python a Kotlin usando el driver de MongoDB para Kotlin
- Adaptar ejercicios y soluciones al estilo Kotlin
- No limitarse a una traducción literal cuando Kotlin requiera una estructura más clara o idiomática

## Skills Disponibles en el Proyecto

### Skills locales del proyecto

- `kotlin-best-practices`
- `mongo-best-practices`
- `professional-commits`

### Skills adicionales que pueden utilizarse si están disponibles en el entorno

- `kotlin-unit-testing`
- `pedagogical-documentation`
- `workshop-markdown-structure`

Si alguna skill no está disponible, aplicar sus principios manualmente cuando resulte razonable.

## Agentes Disponibles en el Proyecto

La configuración local de agentes está en `.opencode/agents/`.

Agentes definidos para este proyecto:

- `taller-developer`: desarrolla la parte Kotlin del taller MongoDB
- `taller-documenter`: adapta y redacta la documentación didáctica
- `git-committer`: prepara commits claros y profesionales siguiendo Conventional Commits

## Relación Entre Agentes y Skills

- `taller-developer` usa preferentemente:
  - `kotlin-best-practices`
  - `mongo-best-practices`
  - `kotlin-unit-testing` si está disponible

- `taller-documenter` usa preferentemente:
  - `pedagogical-documentation` si está disponible
  - `workshop-markdown-structure` si está disponible

- `git-committer` usa:
  - `professional-commits`

## Workflow de Desarrollo

### Fase 1: Alineación técnica

1. Revisar `doc/taller_mongodb.md`
2. Comprobar la estructura real existente del repositorio
3. Preparar o ajustar dependencias del proyecto cuando se vaya a implementar MongoDB desde Kotlin
4. Definir la estructura base Kotlin adecuada para el taller

### Fase 2: Desarrollo del taller

1. Crear la conexión a MongoDB Atlas con el driver de MongoDB para Kotlin
2. Implementar ejemplos CRUD equivalentes a los de Python
3. Crear ejercicios con soluciones en Kotlin
4. Añadir tests cuando la infraestructura del proyecto lo soporte
5. Redactar o adaptar la documentación didáctica
6. Preparar commits limpios y profesionales cuando se solicite

## Principio General de Trabajo

- Leer primero el código y la documentación existentes
- Preferir el cambio más pequeño correcto
- No asumir que la estructura objetivo ya está implementada
- No introducir complejidad innecesaria
- Mantener el proyecto útil para enseñar y aprender
