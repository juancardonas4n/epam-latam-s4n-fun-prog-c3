# Programación Funcional Curso 3. Scala_03

## M0. Bienvenida

### U0. Bienvenida

#### Antes de iniciar

![](./images/Map_Beta_Scala_03_U0.gif)

**Importante**

* Te invitamos a ingresar al canal de [Microsoft Teams S4N Campus Students](https://teams.microsoft.com/l/channel/19%3ac42db2d304b64e03a6513494cc550918%40thread.tacv2/S4N%2520Campus%2520students?groupId=a1adcd66-1b55-478a-ad09-2a659c71cc5b&amp;tenantId=b41b72d0-4e9f-4c26-8a69-f949f367c91d). Todas tus preguntas y solicitudes serán respondidas allí.
* El aprendizaje será 100% virtual y desarrollarás las unidades a tu propio ritmo (auto-gestionado).
* Estamos en versión Beta. Nos interesa probar los contenidos y las actividades.
* Esperamos feedback de tu parte. Los comentarios podrás agregarlos en este [Sheets](https://docs.google.com/spreadsheets/d/1bU9sgtsiyLLlSSp8jrS84ZZMJ5IihNNDnbuURxk3hLk/edit?usp=sharing).


## M1. Tipos algebraicos

### U1. Introducción a los tipos de datos inmutables: tuplas

#### Introducción

Los lenguajes de programación tienen muchas formas de guardar información asociada: como *registers* (registros), *class* (clases) y similares.

En esta unidad estudiarás un tipo de dato básico llamado la tupla.

- Las tuplas  te permitirá guardar información asociada con algunos cambios muy interesantes: *no tienen nombres asociados a cada campo*.

- Las tuplas también puede ser utilizadas de forma dinámica, lo que significa que no hay que hacer definiciones de tipo de la tupla, ya que la instanciación de una tupla establece el número de sus elementos (*aridad*) y su correspondiente tipo, cosa que no puedes asi con un *registro* o una *clase*.

- Las tuplas son *inmutables*, esta última característica es la que revisaremos con más detalle en este curso en particular y observaremos las consecuencias directas de utilizarlo a través de *métodos de selección* o *coincidencia de patrones*, la forma de crearlo a partir del soporte que da el lenguaje o los objetos que nos permite otras formas de creación.



  **¡Comencemos!**

##### Guía curso

![](./images/Map_Beta_Scala_03_U0.gif)

##### Vídeo - Introducción a los tipos de datos inmutables

Observarás la importancia de los tipos de datos inmutables en general, también ampliarás tus concepto de inmutabilidad vistos previamente y entenderás los beneficios de utilizar dichos tipos de datos, en particular las tuplas.

[Vídeo pendiente de edición](videos/els4n-fp-scala-c3-m1-u1-01.mp4)

##### Objetivos módulo

###### Lograrás:

* Entender el concepto de tuplas en tus programas
* Utilizaras las tuplas en tus programas.
* Aplicarás la inmutabilidad en tus programas, y podrás "modificar" las tuplas.

###### Lo que debes saber:

* Entender y aplicar el concepto de funciones puras en Scala.
* Entender y aplicar el concepto de tipos de datos inmutables.

###### Esperamos que aprendas a:

* Los beneficios de usar tuplas y la inmutabilidad en tus programas.
* Usar los operadores de selección y la coincidencia de patrones para acceder el contenido de las tuplas.
* Construir nuevas tuplas a partir de tuplas existentes, sin violar la restricción de inmutabilidad.

###### Ruta de aprendizaje:

* Fundamentos
  * Definición de tuplas, creación y acceso
  * Coincidencia de patrones para el manejo de tuplas
  * Otras formas de manejo de tuplas: operador `copy` y funciones de proyección
* Práctica
  * Tuplas definición y uso
  * Ejemplos de tuplas y coincidencia de patrones
  * Manejo y transformación de tulas
* Evaluación
* Cierre

**Tiempo estimado:** 2h 15 min


![](/home/juancardona/Workbench/epam-latam-s4n-fun-prog-c3/images/TitularCastor_comencemos.png)

---------

#### Tuplas

##### Vídeo - La tupla un tipo de dato inmutable muchas veces olvidado

Este vídeo te mostrará la tupla como un tipo de dato inmutable que permite ser creado dinámicamente según lo requieras como programador, permitiéndote crear un tipo nuevo sin necesidad de definirlo explicitámente como lo haces cuando defines una nueva clase. Observa, como declarar una tupla, instanciarla y acceder a sus elementos.

[Vídeo pendiente de edición](videos/ready_pre_edition/EPAM-LATAM-FP-C3-M0-U0-V1-P01-Docente.mp4)


###### Preguntas Vídeo

1.

>>Es un efecto colateral<<

( ). Un programa funcional

(x). Una operación de entrada y salida

( ). Una operación matemática

( ). Una acción llevada por otro procesos



[explanation]

Cada operación que modifique el estado del computador, como memoria, entrada y salidas, es un efecto colateral.

[explanation]



2.

>>Dos tipos de datos inmutables<<

[ ]. Objetos

[x]. Tuplas

[x]. Tipos de datos inmutables

[ ]. Arreglos



[explanation]

Las tuplas y los tipos de datos algebraicos son inmutables

[explanation]



##### Jupyter Notebook - Tuplas definición y uso

Este **Notebook** te permitirá poner en práctica lo aprendido con el vídeo anterior:  con la forma de crear tuplas directamente a través del constructor del lenguaje (el operador parentésis), a través de la instanciación de objetos `TupleX` (donde `X` es el indicador de aridad) y utilizando métodos de acceso para acceder al contenido de los mismos.

[Jupyter Notebook - Tuplas definición y uso - Error técnico](https://hub.gke2.mybinder.org/user/juancardonas4n--s4n-fun-prog-c3-5syyqu6i/tree/notebooks/els4n-fp-c3-m1-u1-nb-01.ipynb)


###### Pregunta Jupyter Notebook

1. Dado el siguiente código

```scala
type T3IID = (Int,Int,Double)
type T3IDI = (Int,Double,Int)

def aplicar(tpl1:T3IID,tpl2:T3IDI,f:(Int,Double) => Int) = f(tpl1._1,tpl2._2) + f(tpl2._3,tpl1._3)
```

   > > Si aplicamos la función `aplicar`  con los siguientes valores que resultado `res0` que obtendremos es<<

```scala
scala> aplicar((1,2,3.0),(1,3.0,2),(x:Int,_) => x * 2)
aplicar((1,2,3.0),(1,3.0,2),(x:Int,_) => x * 2)
val res0: Int =
```
   ( ). 4.

   ( ). 5.

   (x). 6.

   ( ). 7.

   [explanation]

   La función `f` toma solamente el primer argumento, el segundo lo ignora. Ahora toma el `tpl1._1 = 1` y `tpl2._3 = 2`, cada uno es múltiplicado por 2 y sumados obteniendo: `1 * 2 + 2 * 2 = 6`.

   [explanation]

----

#### Coincidencia de patrones

##### Vídeo - Coincidencia de patrones en tuplas

Este vídeo te mostrará el uso de la coincidencia de patrones bajo las tuplas: a través de la coincidencia de variables, de literales y de comodines, cómo también te mostremos el uso etiquetas.

[Vídeo pendiente de edición](videos/els4n-fp-scala-c3-m1-u1-03.mp4)

###### Pregunta vídeo - Coincidencia de patrones de tuplas

1.

> >Los tipos de datos son vistos como:

(x). Conjuntos.

( ). Referencias.

(). Objectos

( ). Funciones

[explanation]

Un tipo es un conjunto con valores.

[explanation]

2.

> > La palabra reservada `type` nos permite

( ). Definir tipos

(x). Da otro nombre a un tipo ya conocido

( ). Constructor básico para tuplas

( ). Declarar tipos

[explanation]

La palabra reservada `type` define un alias, es decir permite identificar un tipo con dos o más nombres.

[explanation]

##### Jupyter Notebook - Ejemplos de tuplas y coincidencia de patrones

Este *Notebook* te ayudará a cimentar los conceptos vistos en el vídeo anterior y ponerlos en práctica a través de una serie de ejercicios sobre el uso de coincidencia de patrones en tuplas.

[Jupyter Notebook - Ejemplos de tuplas y coincidencia de patrones - Error técnico](https://hub.gke2.mybinder.org/user/juancardonas4n--s4n-fun-prog-c3-1u7gjoff/tree/notebooks/els4n-fp-c3-m1-u1-nb-02.ipynb)

###### Pregunta Jupyter Notebook

1. Suponga que se define la siguiente estructura en scala

```scala
type T2D = (Double, Double)
type Est = (String, T3D, T3D)
```

La tupla `Est` tiene la información de un estudiante, donde el segundo elemento son los porcentajes de tres evaluaciones, y el tercero son la notas. Esperamos implementar la función `obtNotaEst` que se muestra su firma a continuación:

```scala
def obtNotaEst(est:Est):Double = ???
```

>>Cuál de las siguiente funciones computa el valor de la nota del estudiante<<

( ) A.
```scala
def obtNotaEst(est:Est):Double = est._2 * est._3
```
( ) B.
```scala
def obtNotaEst(est:Est):Double =
  est._2._0 * est._3._0 + est._2._1 * est._3._1
```
(x) C.
```scala
def obtNotaEst(est:Est):Double = est match {
  case (_,(p1,p2),(n1,n2)) => n1 * p1 + n2 * p2
```
( ) D.
```scala
def obtNotaEst(est:Est):Double =
  est._2._1 + est._3._1 * est._2._2 + est._3._2
```
[explanation]

La primera opción no es válida, por que no se puede multiplicar dos tuplas completas. La segunda opción no es válida por que las funciones de acceso de cada elemento comienza en ._1 y por cada elemento tiene su correspondiente en su elemento. La tercera opción es válida por que accede a cada elemento de la tupla interna y la multiplica con su correspondiente valor ponderado. La cuarta es incorrecta por que la expresión multiplica las ponderaciones y luego se las suma a las notas.

[explanation]

----

#### Manejo de tuplas

##### Vídeo - Manipulación de tuplas

Observarás como se puede manipular las tuplas, en particular al ser tipos de datos inmutables podrás "actualizarlos" creando nuevos a partir de los existentes.

[Vídeo pendiente de edición](videos/els4n-fp-scala-c3-m1-u1-04.mp4)

###### Preguntas - Vídeo - Manipulación de tuplas

1. Según el siguiente guión (*script*) de Scala
```scala
val tpl @ (_,b,3) = (1,2,3)
tpl._2 * b - tpl._3
```
>>Cuál es el resultado de la expresión evaluada en la última línea<<
( ) A. 6
( ) B. 4
( ) C. 2
(x) D. 1

[explanation]
La primera línea en el lado derecho de la asignación instancia una tupla de tres elementos y la asigna completamente a la variable
`tpl`. Asigna a la variable `b` el valor de 2 y las tuplas coinciden en el tercer elemento se realiza la asiganción.
Los valores de `tpl`son `(1,2,3)` respectivamente para las funciones de acceso `._1`, `._2` y `._3`, y la variable `b` contiene el valor de `2`. Por lo tanto la evaluación de expresion `tpl._2 * b - tpl._3` produce `2 * 2 - 3` por lo tanto el resultado es `1`.
[explanation]

2. Según el siguiente guión (*script*) de Scala
```scala
val tpl = (1,2,3)
val tpl2 = tpl.copy(_3 = 1, _1 = 3)
tpl2 match { 
   case (a,1,c) => a * c
   case (a,2,c) => c - a
   case (a,_,c) => a + c
}
```

> > Cuál es el valor retornado por la expresión<<

( ) A. 1
(x) B. 2
( ) C. 3
( ) D. 4

[explanation]
La segunda línea produce una copia de la original, pero intercambiando el primer y tercer elemento de la tupla y viceversa. Por 
lo tanto el resultado de `tpl2` es `(3,2,1)`. Cuando se aplica la coincidencia de patrones en el tercer elemento se obtiene que 
coincide con el tercero por lo tanto realiza la expresión: `c - a` que al sustituir se obtiene `3 - 1` cuyo resultado es `2`.
[explanation]

##### Jupyter Notebook - Manejo y transformación de tuplas

Pondrás en práctica todo lo visto en esta unidad a través del manejo de tuplas y la creación de otras tuplas. De esta forma tendrás los conocimientos y el contexto para aprender a manejar otros tipos de datos inmutables.

[Jupyter Notebook - Manejo y transformación de tuplas - Error técnico](https://hub.gke2.mybinder.org/user/juancardonas4n--s4n-fun-prog-c3-71ef49js/notebooks/notebooks/els4n-fp-c3-m1-u1-nb-03.ipynb)


###### Pregunta Jupyter Notebook - Manejo y transformación de tuplas

1.

>>Cuáles de los siguientes códigos invierte el tipo del parámetro de entrada y su valor<<

[x] A.

```scala
def invertir(tpl:(Int,Double,Bool)) = (tpl._3,tpl._2,tpl._1)
```
[x] B.

```scala
def invertir(tpl:(Int,Double,Bool)) = tpl match {
   case (a,b,c) => new Tuple[Bool,Double,Int](c,b,a)
}
```
[x] C.

```scala
def invertir(tpl:(Int,Double,Bool)) = 
  tpl.copy(_1 = tpl._3, t_3 = tpl._1)
```
[ ] D.

```scala
def invertir(tpl:(Int,Double,Bool)) = tpl match {
    case tpl2 @ (_,_,_) => tpl2
}
```

[explanation]

La primera opción es valida, por que utilizando funciones de acceso, crear una tupla nueva en orden inverso. La segunda opción es válida por que utilizando coincidencia de patrones vuelve a crear una tupla nueva, poniendo los valores de la coincidencia de patrones en orden inverso. La tercera opción es válida por que crea una copia de la original y modifica las primera con el valor de la tercera y la tercera con el valor de la primera. La cuarta opción no es válida, por que devuelve una referencia a la tupla original.

[explanation]

----

## Evaluación

1.
>>La diferencia entre tuplas y las clases es que<<

( ) A. las tuplas son colecciones, mientras que las clases son tipos de datos.
(x) B. las tuplas son tipos sin nombres, mientras que las clases son tipos generalmente tienen nombres excepto las anónimas.
( ) C. las tuplas no son recursivas mientras que las clases permiten que sus campos (o atributos) sean otras clases.
( ) D. las tuplas permiten que sus campos sean modificados a través de la operación de selección mientras que las clases lo hacen a través de los métodos *setters*.
[explanation]
La opción A, las tuplas son tipos de datos pero no son colecciones por que adolecen de iteradores, aunque permitan guardar valores de diferentes tipos y este se construya utilizando una forma normalizada del operador del operador de producto cartesiano; una clase por definición es un tipo de dato. La opción B, una tupla no nombra sus campos, mientras que las clases cuando se crean son ligadas a un identificador excepto cuando se definen clases anónimas. La opción C, las tuplas pueden contener un campo de cualquier tipo y ese tipo puede ser un tipo asociado con una tupla. La opción D, las tuplas son inmutables por lo tanto estas no pueden ser modificadas, aunque es cierto que las clases puede ser modificadas si tienen métodos *setters*.
[explanation]

2. La firma de la siguiente función produce una tupla cuyo primer valor es el mismo de entrada (\) y el segundo es el valor inverso $valor^{-1}$.
>>Construya el cuerpo de la función en scala:<<

```{.scala}
def obtInv(valor:Double):(Double,Double) = ???
```

[explanation]
La idea es construir una tupla a partir de los valores de entrada `valor` y su correspondiente valor inverso.

```{.scala}
def obtInv(valor:Double):(Double,Double) = (valor,1.0/valor)
```
[explanation]

3. La función `distRango` recibe dos valores enteros `a` y `b`, y retorna una tupla binaria donde el segundo campo es otra tupla binaria. El primer campo de tupla externa es computado con la distancia (`dist`) entre ambos valores de entrada cómo se observa en la fórmula a continuación  y  la tupla interna calcula el rango (`rango`) que toma los dos valores iniciales y establece el orden entre ambos, como se observan la función rango a continuación.

>>Implemente `distRango` construyendo las tuplas a partir de `TupleX`.<<

```{.scala}
def distRango(a:Int, b:Int):Tuple2[Int,Tuple2[Int,Int]] = ???
```

$$
dist(a,b) =\ \mid a - b \mid
$$

$$
rango(a,b) = \begin{cases}(a,b) & \text{Si}\ a \leq b\\
                          (b,a) & \text{En caso contrario} \\
             \end{cases}
$$

[explanation]
El siguiente segmento de código muestra cómo se construye el cuerpo de la función:

```{.scala}
def distRango(a:Int, b:Int):Tuple2[Int,Tuple2[Int,Int]] = new     Tuple2(scala.math.abs(a - b), if (a <= b) new Tuple2(a,b) else new Tuple2(b,a))
```

Se construye una tupla de tipo `Tuple2` donde el primer campo es el rango calculado con el valor absoluto. El segundo campo se construye con una expresión de condición que confirme que la condición $a \leq b$ y esto retorna la tupla conteniendo el rango de `a` y `b` en caso contrario se retorna la tupla conteniendo el rango `b` y `a`.
[explanation]

4. Observa la siguiente firma:
>>cuál de la siguientes opciones obtiene retorna la tupla más interna de la tupla de u<<

```{.scala}
def funcion(u:((Int,Int),Int,Int)) = u match {
...
}
```

[x] A. `case (x,_,_) => x`
[ ] B. `case (_,y,z) => u`
[x] C. `case ((w,x),y,z) => (w,x)`
[ ] D. `case (x,y,z) => (x,z)`

[explanation]
La opción A es válida, porque la coincidencia de patrones toma el primer campo de la tupla más externa que es realidad la tupla más interna. La opción B no es válida, porque ignora el valor del primer campo de la tupla más externa y devuelve la tupla original. La opción C es válida porque se obtiene con detalle la tupla más interna y se vuelve a construir una copia de la tupla original. La opción D no es válida, porque aunque se construye una tupla con otra tupla anidada.
[explanation]

5. Observa la siguiente firma de la función (`funcion`)

```{.scala}
def funcion(u:(Int,Int),v:(Int,Int)) = ???
```

>>ésta tiene dos parámetros que son tuplas `u`y `v`. Implemente una función (`funcion` ) que verifique si `u._2`y `v._1` cumplen las siguientes condiciones: si son iguales y este es cero (`0`) compute: `u._1 + v._2`, si ambos son uno (`1`) compute: `u._1 - v._2`, si son iguales sin importar que valor (diferente de `0`ó `1`) compute: `u._1 * v._2`, en cualquier otro caso retorne cero (`0`). **Recuerde:** que esta implementación utiliza _coincidencia de patrones_ y no los operadores de selección.<<

[explanation]
El siguiente es el cuerpo de la función esperada:

```{.scala}
def funcion(u:(Int,Int),v:(Int,Int)) = (u,v) match {
   case ((w,0),(0,z)) => w + z
   case ((w,1),(1,z)) => w - z
   case ((w,x),(y,z)) if x == y => w * z
   case _             => 0
}
```
[explanation]

6. Observa la siguiente expresión:

```{.scala}
val tupla = new Tuple1(1)
```

>>De acuerdo con la información anterior, el tipo de la expresión obtenida es<<

( ) A. `val tupla: Int = 1`
( ) B. `val tupla: (,Int) = (,1)`
( ) C. `val tupla:(Int) = (1)`
(x) D. `val tupla:(Int,) = (1,)`

[explanation]
El rango de una tupla (El número de elementos que ellas puede contener) es desde un único elemento (o *singlenton*) hasta 22 elementos, todos ellos de diferentes tipos.  El objetivo del ejercicio es mostrar cuál es el tipo creado por una tupla *singlenton* de tipo entero cuya  representación en Scala se obtiene de la siguiente forma: `(Int,)`. La opción A establece que la tupla es de tipo entero. La opción B no está permitida por el lenguaje. La opción C el tipo `(Int)`no existe dentro de Scala. La opción D es la correcta como se explicó previamente.
[explanation]

7.
>>Implementa la función (`funcion`) del ejercicio 5 sin utilizar coincidencia de patrones sobre los parámetros sino utilizando únicamente operaciones de selección sobre tuplas.<<

[explanation]
El siguiente es el código que se implementa

```{.scala}
def funcion(u:(Int,Int),v:(Int,Int)) = if (u._2 == v._1)
                                          if (u._2 == 0) u._1 + v._2
                                          else if (u._2 == 1) u._1 - v._2
                                               else u._1 * v._2
                                       else 0
```
[explanation]

---

## Cierre

La mayoría de programadores optamos por utilizar tipos de datos como registros o clases y evitamos utilizar las tuplas, debido a que estas no se equiparan en la flexibilidad que tiene las clases. Pero las tuplas, como lo hemos visto tienen características muy interesantes que las hacen muy útiles, la primera de ellas es la capacidad de *retornar varios valores dentro de una tupla*, esto está en la línea directa con una de las características de las funciones puras, como la de una función pura debe retornar un único valor, y en este caso las tuplas ayudan para retornar es único valor.

La segunda característica es la *inmutabilidad*, esta permite crear valores que no pueden ser modificados, lo que igualmente facilita la construcción de funciones puras.

Una tercera característica es la flexibilidad que tienen para crear colecciones de datos, que pueden ser tipos intermedios, sin necesidad de declaración o definición.

Estas y otras más características nos muestran la importancia las tuplas, y de la inmutabilidad, que más adelante nos facilitará crear aplicaciones que consuman gran cantidad de datos. 

### ¿Quieres saber más?

* [¿Qué son las tuplas? (Inglés)](https://whatis.techtarget.com/definition/tuple)
* [Tuplas definición de Wikipedia](https://es.wikipedia.org/wiki/Tupla)
* [Tuplas en lenguajes de programación (Inglés)](https://weekly-geekly.imtqy.com/articles/276871/index.html)
* [Tuplas en Scala (Inglés)](https://www.geeksforgeeks.org/scala-tuple/)
* [Tuplas en Scala comandos (Inglés)](https://www.tutorialspoint.com/scala/scala_tuples.htm)

### EPAM Insights

---

### U2. Tipos de datos algebraicos

#### Introducción

El nombre tipos de datos algebraicos te puede sonar extraño, en un mundo trabajas preferentemente con clases, registros, estructuras, objetos, etc. En esta unidad te presentaremos los tipos de datos algebraicos y al final de la misma te parecerán que son una consecuencia justa

