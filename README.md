# ExamenPromass
## Prueba técnica de programación
Bienvenido a mi examen. Esta aplicación cumple con los siguientes requerimientos:
## Alta de entradas:
- El usuario podrá guardar entradas en la aplicación. Una entrada deberá estar
compuesta por los siguientes elementos:
  - Título: enuncia el contenido de la entrada.
  - Autor: el nombre de quien publica la entrada
  - Fecha de publicación: la fecha en que la entrada fue guardada.
Contenido: un escrito breve.
(Todos los campos son obligatorios)

## Mostrar un listado de entradas
- Deberá de mostrar un listado con todas las entradas en el blog, pero del campo
contenido, solo se mostrarán los primeros setenta caracteres.
## Búsquedas
- La aplicación debe permitir realizar búsquedas de entradas permitiendo filtrar por
título, contenido o autor.
## Mostrar detalle de una entrada.
- Al seleccionar una entrada del listado, deberá mostrarse el contenido de la
entrada. Es decir, deberá ser visible todo el texto del contenido.
## Obtención de recursos
- La obtención de los datos se hará por medio de un servicio REST que la
aplicación debe consumir. Este proporcionará los métodos necesarios para
consultar y guardar las entradas del sitio a través de internet en un servidor.
## Extra - Modo offline
- En caso de que el dispositivo no cuente con conexión a internet, el sistema web
deberá de tomar los siguientes puntos:
  - La aplicación permitirá ver las entradas que se hayan descargado previamente.
  - Se bloqueará la opción de dar alta entradas.
  - Se mostrará un mensaje de que no se tiene conexión a internet.
 
## Otros Datos

Esta app consume una API que hice en php vanilla y aloje en un host gratuito (000.webhost.com), por lo que no es necesario modificar el código para consumirla de forma local.

Utilice las siguientes dependencias en el programa:

- retrofit
    - implementation "com.squareup.retrofit2:retrofit:2.9.0"
    - implementation "com.squareup.retrofit2:converter-gson:2.9.0"

- coroutines
    - implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2'
