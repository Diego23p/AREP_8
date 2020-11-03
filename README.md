# Disponibilidad y Desempeño

Creación de una solución autoescalable en EC2 que escala bajo ciertas condiciones de carga

## Inicialización

Herramientas requeridas para ejecutar el programa

### Prerrequisitos

#### Ejecutando el programa localmente

##### Java

Es una plataforma necesaria para que Maven ejecute, desde la linea de comandos comprobamos que se encuentre instalado por medio del comando:
```
> java -version

java version "1.8.0_101"
Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
Java HotSpot(TM) Client VM (build 25.101-b13, mixed mode)
```

##### Maven

La estructura está estandalizada con Maven, desde la linea de comandos comprobamos que se encuentre instalado por medio del comando:
```
> mvn -v

Apache Maven 3.6.0 (97c98ec64a1fdfee7767ce5ffb20918da4f719f3; 2018-10-24T13:41:47-05:00)
Maven home: C:\apache-maven-3.6.0\bin\..
Java version: 1.8.0_201, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk1.8.0_201\jre
Default locale: es_CO, platform encoding: Cp1252
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

##### Git

Se usará el sistema de control de versiones Git, desde la linea de comandos comprobamos que se encuentre instalado por medio del comando:
```
> git --version

git version 2.21.0.windows.1
```

## Instalación

Para descargar localmente el repositorio se utiliza el comando como sigue:
```
> git clone https://github.com/Diego23p/AREP_8.git
```

Para compilar el proyecto usando Maven:
```
> mvn package
```

Para ejecutar el proyecto:
```
> java -cp target/classes:target/dependency/* edu.escuelaing.arep.hardapp.factorialIneficiente
```

# Pasos

**1. Idee un problema interesante que necesite una solución distribida y que necesite alto consumo de procesamiento.**

El problema que se plantea es el de hallar el valor de cierta posición (dada por el usuario), de un número de la secuencia de Fibonacci.

**Construya un prototipo de la solución.**

Se plantea un solución usando Spark la cual, luego de consultar el puerto 4567, requiere el ingreso de un número de la forma ```/?data=<número>```. Esta usa una solución altamente ineficiente para hallar el valor, no usa memoria ni fórmulas, usa la recursión plena al hacer la sumatoria de los números anteriores hasta llegar a las condiciones de parada.

La solución ejecutándose en localhost:

![](/img/1.jpg)

Se observa que con una solicitud de la posición ```1´000.000``` el tiempo de respuesta es de ```51.86``` segundos:

![](/img/2-responseTime.jpg)

La solución en detalle, está en la carpeta ```src```

**Depliegue la solución en AWS en EC2.**

![](/img/3-inAWS.jpg)

**Cree un AMI a partir de esta máquina.**

![](/img/4-AMI.jpg)

**Despliegue la solución en un grupo de autoescalamiento.**
1. El primer paso es crear un balanceador de carga:

![](/img/5-LB.jpg)

2. Ya en la sección de autoescalamiento, se debe crear una nueva configuración de la misma, que posteriormente será asociada al grupo:

![](/img/6-confDeLanzamiento.jpg)

3. Se puede hacer la configuración de varios parámetros, uno de ellos es el Health Check, este define las reglas con el cual se determinará si las nuevas instancias EC2 están o no respondiendo correctamente:

![](/img/7-healthCheck.jpg)

4. Un elemento adicional es la configuración de una notificación, específicamente el envío de un correo notificando cambios en el balanceador:

![](/img/8-createNotify.jpg)

5. Llega un correo con la solicitud de ingreso al grupo:
 
![](/img/10-notificacion2.jpg)

6. Y también uno con la notificación de suscripción al tema: 

![](/img/9-notificacion.jpg)

7. En este punto ya se pueden hacer solicitudes del servicio directamente al Balanceador de Carga debido a que este provee un ```DNS name``` que puede ser consultado:

![](/img/11-peticionALB1.jpg)

Su funcionamiento y su impresión en consola:

![](/img/12-peticionALB2.jpg)

8. Se debe crear un nuevo grupo de auto escalamiento, y configurar todos los parámetros requeridos. Uno de ellos es asignarle el balanceador de carga creado previemente:

![](/img/13-asigLB.jpg)

9. Se configuran politicas de escalamiento como mínimos, máximos y deseados, de las intancias EC2 dentro de el:

![](/img/14-cantidad.jpg)

10. Y la política de la regla que se va a monitorear constantemente para aumentar o disminuir el número instancias EC2:

![](/img/15-politicas.jpg)

**Cargue la solución con muchas solicitudes para generar alta carga en el servidor.**

Se realizan 15 peticiones de la posición ```1´000.000``` al balanceador de carga, se podría automatizar este proceso con la librería ```Newman``` de ```Postman```, pero debido a que la carga se realiza fácilmente mediante un Browser, se decidió hacerlo de este modo.

**Monitoree y verifique que se creen más instancias.**

1. Este supera la métrica establecida de mantenerce por debajo de 50% del consumo de CPU y automaticamente crea una nueva instancia dentro del grupo, con la AMI preseleccionada:

![](/img/16-newEC2.jpg)

![](/img/17-newEC2-2.jpg)

2. Llega una nueva notificación al correo:

![](/img/18-notifyOK.jpg)

# Autores

- [Diego Alejandro Puerto Gómez](https://github.com/Diego23p)

# Licencia

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
