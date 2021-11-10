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


## M1. Tipos algebraicos

### U1. Introducción a los tipos de datos inmutables: tuplas

#### Evaluación

>>1. La mayor diferencia entre las tuplas y las clases son<<

( ) A. Las tuplas no son tipos de datos, son colecciones, mientras que las clases si lo son.
(x) B. Las tuplas son tipos sin nombres, mientras que las clases son tipos generalmente tienen nombres excepto las anónimas.
( ) C. Las tuplas no son recursivas, es decir que sus campos no pueden ser tuplas, mientras que las clases permiten que sus campos (o atributos) sean otras clases.
( ) D. Las tuplas permiten que sus campos sean modificados a través de la operación de selección mientras que las clases lo hacen a través de los métodos *setters*.

[explanation]
La opción a, las tuplas son tipos de datos, es más es una forma construir un tipo de dato a través del producto cartesiano; una clase por definición es un tipo de dato. La opción b, las tuplas no se nombran dentro de los tipos de datos, mientras que las clases cuando se crean se nombran excepto las anónimas. La opción c, las tuplas pueden contener otras tuplas por lo tanto son recursivas. La opción d, las tuplas son imutables por lo tanto estas no pueden ser modificadas, aunque es cierto que las clases puede ser modificadas si tienen métodos *setters* esta opción no es válida por la primera parte de la opción.
[explanation]

>>2. La firma de la siguiente función produce una tupla cuyo primer valor es el mismo de entrada ([mathjaxinline]valor[/mathjaxinline]) y el segundo es el valor inverso [mathjaxinline]valor^{-1}[/mathjaxinline]. Construya el cuerpo de la función en scala:<<

```{.scala}
def obtInv(valor:Double):(Double,Double) = ???
```

[explanation]
La idea es construir una tupla a partir de los valores de entrada `valor` y su correspondiente valor inverso.

```{.scala}
def obtInv(valor:Double):(Double,Double) = (valor,1.0/valor)
```
[explanation]

>>3. La firma de la siguiente función `distRango` produce una tupla binaria donde el segundo campo es otra tupla binaria. El valor del primera tupla es la distancia entre los valores de entrada y la tupla interna devuelve el rango como se puede observar en las siguiente fórmulas:<<

```{.scala}
def distRango(a:Int, b:Int):Tuple2[Int,Tuple2[Int,Int]] = ???
```

[mathjax]
dist(a,b) = \mid a - b \mid
[/mathjax]

[mathjax]
rango(a,b) = \begin{case}(a,b) & \text{Si} a \leq b\\
             (b,a) & \text{En caso contrario} \\
             \end{case}
[/mathjax]



[explanation]
El siguiente segmento de código muestra como se construye el cuerpo de la función:

```{.scala}
def distRango(a:Int, b:Int):Tuple2[Int,Tuple2[Int,Int]] = new Tuple2(scala.math.abs(a - b), if (a <= b) new Tuple2(a,b) else new Tuple2(b,a)) 
```

Se construye una tulpa de tipo `Tuple2` donde el primer campo es el rango calculado con el valor absoluto. El segundo campo se contruye
con una expresión de condición que confirme que la condición [mathjaxinline]a \leq b[/mathjaxinline] y esto retorna la tupla conteniendo 
el rango de `a` y `b` en caso contrario se retorna la tupla conteniendo el rango `b` y `a`.
[explanation]

>>4. Tomando la siguiente firma, cuál de la siguientes opciones obtiene retorna la tupla más interna de la tupla de u<<

```{.scala}
def funcion(u:((Int,Int),Int,Int)) = u match {
...
}
```

(x) A. `case (x,_,_) => x`
( ) B. `case (_,y,z) => u`
(x) C. `case ((w,x),y,z) => (w,x)`
( ) D. `case (x,y,z) => (x,z)`

[explanation]
La opción A es válida, por que la coincidencia de patrones toma el primer campo de la tupla más externa que es realidad la tupla más interna. La opción B no es válida, por que ignora el valor del primer campo de la tupla más externa y devuelve la tupla original. La opción C es válida por que se obtiene con detalle la tupla más interna y se vuelve a construir una copia de la tupla original. La opción D no es válida, por que aunque se contruye una tupla con otra tupla anidada. 
[explanation]

>>5. Observe la siguiente firma de la función (`funcion`) esta tiene dos parámetros que son tuplas. Implemente una función donde: si el segundo parámetro de la primera tupla y el primer parámetro de la segunda tupla son cero, sumar los dos restantes elementos restantes, si son uno restarlos y en los demás casos que coincidan multiplicarlos y el caso por omisión retorna cero<<

```{.scala}
def funcion(u:(Int,Int),v:(Int,Int)) = ???
```

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

>> 6. Observe la siguiente expresión e indique el tipo de la expresión obtenida<<

```{.scala}
val tupla = new Tuple1(1)
```

( ) A. `val tupla: Int = 1`
( ) B. `val tupla: (,Int) = (,1)`
( ) C. `val tupla:(Int) = (1)`
(x) D. `val tupla:(Int,) = (1,)`

[explanation]
El rango de una tupla (El número de elementos que ellas puede contener) es desde un único elemento (o *singlenton*) hasta 22 elementos, todos
ellos de diferentes tipos. El caso particular es la creación de una tupla *singlenton* de tipo entero, por definición del lenguaje el tipo creado es `(Int,)`. La opción A establece que la tupla es de tipo entero. La opción B no es permitida por el lenguaje. La opción C el tipo `(Int)`no existe dentro de Scala. La opción D es la correcta como se explicó previamente.
[explanation]

>>7. Implemente la función (`funcion`) del ejercicio 5. sin utilizar conincidencia de patrones únicamente a través de las operaciones de selección<<

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
