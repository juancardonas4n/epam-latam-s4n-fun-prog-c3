# EPAM-Latam Programación Funcional Curso 3 (epam-latam-s4n-fun-prog-c3)

## M0. Bienvenida

### U0. Bienvenida

#### Antes de iniciar

![](./images/Map_Beta_Scala_03_U0.gif)

**Importante**

* Te invitamos a ingresar al canal de Microsft Teams [S4N Campus Students](https://teams.microsoft.com/l/channel/19%3ac42db2d304b64e03a6513494cc550918%40thread.tacv2/S4N%2520Campus%2520students?groupId=a1adcd66-1b55-478a-ad09-2a659c71cc5b&amp;tenantId=b41b72d0-4e9f-4c26-8a69-f949f367c91d). Todas tus preguntas y solicitudes serán respondidas allí.
* El aprendizaje será 100% virtual y desarrollarás las unidades a tu propio ritmo (autogestionado).
* Estamos en versión Beta. Nos interesa probar los contenidos y las actividades.
* Esperamos feedback de tu parte. Los comentarios podrás agregarlos en este [Sheets](https://docs.google.com/spreadsheets/d/1bU9sgtsiyLLlSSp8jrS84ZZMJ5IihNNDnbuURxk3hLk/edit?usp=sharing). 

## M1. Tuplas

### U1. Introducción a los tipos de datos inmutables: tuplas

#### Evaluación

>>1. Las tuplas son un tipo de dato que representa una producción de tipos muy similar a las clases. La mayor diferencia entre ambos es<<

( ) A. Las tuplas no son tipos de datos, son colecciones, mientras que las clases si lo son.
(x) B. Las tuplas son tipos sin nombres, mientras que las clases son tipos generalmente tienen nombres excepto las anónimas.
( ) C. Las tuplas no son recursivas, es decir que sus campos no pueden ser tuplas, mientras que las clases permiten que sus campos (o atributos) sean otras clases.
( ) D. Las tuplas son mutables, mientras que las clases son inmutables.

[explanation]
La opción a, las tuplas son tipos de datos, es más es una forma construir un tipo de dato a través del producto cartesiano; una clase por definición es un tipo de dato. La opción b, las tuplas no se nombran dentro de los tipos de datos, mientras que las clases cuando se crean se nombran excepto las anónimas. La opción c, las tuplas pueden contener otras tuplas por lo tanto son recursivas. La opción d, por definición no se puede modificar una tupla, se debe crear una nueva.
[explanation]

>>2. La firma de la siguiente función produce una tupla en la cual devuelve el valor de entrada, más la inversa del mismo valor en el segundo valor. Construya el cuerpo que construye el correspondiente valor<<

[](./programas/scala/c3-m1-u1-eval-p2-prob.scala)

[explanation]
La idea es construir una tupla a partir de los valores de entrada `valor`y su correspondiente valor inverso.

[](./programas/scala/c3-m1-u1-eval-p2-sln.scala)
[explanation]

>>3. <<
