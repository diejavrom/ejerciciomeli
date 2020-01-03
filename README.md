## Sistema de Cobranzas Meli

# componentes del sistema + documentación de las APIs:

* aplicación [charge](https://github.com/diejavrom/ejerciciomeli/tree/master/charge):

Gestión de cargos. A través de esta aplicación se pueden ingresar y consultar cargos. Cada cargo estará relacionado con los pagos
que se utilizaron para saldarlo. Almacena los datos en una base de datos MongoDB en memoria. Cada vez que se ingresa un cargo el mismo
es notificado a una cola de eventos (bill.queue) que luego será consumido por la aplicación bill.
Además, posee un listener (implementado en JMS) que recibe los pagos notificados en la cola de eventos por la aplicación payment.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/charge/doc/charge-api.html

* aplicación [payment](https://github.com/diejavrom/ejerciciomeli/tree/master/payment):

Gestión de pagos. Mediante esta aplicación se pueden ingresar y consultar pagos. Cada pago está relacionado con los cargos que fueron
saldados con dicho pago. Los datos se almacenan en una base de datos MongoDB. Cada vez que se ingresa un pago, el mismo, luego de procesado, es notificado a una cola de eventos (charge.queue) para luego ser consumido por la aplicación charge.
Al momento de crear un pago se exige una clave de idempotencia (idempkey) en el http header. Si esa clave no está presente o bien se envia repetida, el pago será rechazado. Con esto se garantiza la unicidad de los pagos.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/payment/doc/payment-api.html

* aplicación [bill](https://github.com/diejavrom/ejerciciomeli/tree/master/bill):

A través de esta aplicación se pueden recuperar las facturas. En cada factura se informan los cargos que la componen. También utiliza una base de datos MongoDB en memoria. Posee un listener que recibe los cargos ingresados desde una cola de eventos.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/bill/doc/bill-api.html

* aplicación [status](https://github.com/diejavrom/ejerciciomeli/tree/master/status)

Permite consultar el estado de deuda del usuario obteniendo la información de las aplicaciones charge y payment. No posee almacenamiento.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/status/doc/status-api.html

* aplicación [currency](https://github.com/diejavrom/ejerciciomeli/tree/master/currency)

Almacena la configuración de los tipos de moneda que se manejan en el sistema. En esta aplicación además de establecer cuál es la moneda 
por default también permite convertir cualquier importe (configurado) a la moneda por defecto. A efectos de la prueba, 
esta aplicación al iniciarse inserta en su base de datos (Mongo DB en memoria) la moneda ARS (peso argentino) como moneda por defecto y 
también USD (dólar) con una relación 1 USD -> 63 ARS.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/currency/doc/currency-api.html

* cola de eventos "charge.queue":

En esta cola de eventos la aplicación payment informa los pagos procesados que luego serán notificados al listener de la aplicación charge. Se implementa con ActiveMQ.

* cola de eventos "bill.queue":

En esta cola de eventos la aplicación charge informa los cargos procesados que luego serán notificados al listener de la aplicación bill. Se implementa con ActiveMQ.

A continuación se muestra un diagrama donde se visualizan las interacciones de todos los componentes mencionados anteriormente:

![alt text](https://github.com/diejavrom/ejerciciomeli/blob/master/melisystem.png)

# Ejecución en entorno local:

* Requerimientos previos
1) Tener instalado java 1.8
2) Tener maven 3.3.3 instalado 
3) Para ejecutar el sistema en un entorno local primero deberá instalarse una instancia de activeMQ activa en el puerto tcp 61616, con usuario admin y password admin.

* Ejecución luego de haber clonado el repo
1) Iniciar activeMQ 
<br>activemq start

2) Iniciar aplicación charge. Dentro del path ejerciciomeli/charge ejecutar
<br>java -jar target/charge-0.0.1-SNAPSHOT.jar
<br>la API quedará expuesta en http://localhost:8180/charge
   
3) Iniciar aplicación payment. Dentro del path ejerciciomeli/payment ejecutar
<br>java -jar target/payment-0.0.1-SNAPSHOT.jar
<br>la API quedará expuesta en http://localhost:8080/payment
   
4) Iniciar aplicación bill. Dentro del path ejerciciomeli/bill ejecutar
<br>java -jar target/bill-0.0.1-SNAPSHOT.jar
<br>la API quedará expuesta en http://localhost:8480/bill

5) Iniciar aplicación currency. Dentro del path ejerciciomeli/currency ejecutar
<br>java -jar target/currency-0.0.1-SNAPSHOT.jar
<br>la API quedará expuesta en http://localhost:8280/currency

5) Iniciar aplicación status. Dentro del path ejerciciomeli/status ejecutar
<br>java -jar target/status-0.0.1-SNAPSHOT.jar
<br>la API quedará expuesta en http://localhost:8380/status

# Solución con saldo a favor (Bonus 1):

Ver branch [v2](https://github.com/diejavrom/ejerciciomeli/tree/v2).

# Solución Cloud (Bonus 2):

Para la solución cloud se utilizó Azure cloud. Cada aplicación se ejecuta dentro de un contenedor Docker con el perfil "cloud", 
la cola de eventos activemq también corre sobre un contenedor. Además, a diferencia de la solución local, se dispone de un contenedor [nginx](https://github.com/diejavrom/ejerciciomeli/tree/master/nginx) que funciona como un reverse proxy redireccionando los endpoints hacia los otros contenedores en base a las URLs recibidas.
<br>El deploy y la relación de contenedores se realiza mediante el archivo docker-compose.yml, de esta manera todos quedan
ejecutándose dentro de un mismo host por lo que el ambiente no está preparado para soportar pruebas de carga.
<br>Para construir la imagen de cada conteneder se provee un archivo Dockerfile que reside en el path raíz de cada proyecto.
Se utiliza un repositorio de imágenes provisto por Azure Cloud.
<br>La URL base de esta solución es https://meli-multi2.azurewebsites.net, por lo que los endpoints quedan de la siguiente manera:

<br>https://meli-multi2.azurewebsites.net/charge
<br>https://meli-multi2.azurewebsites.net/payment
<br>https://meli-multi2.azurewebsites.net/bill
<br>https://meli-multi2.azurewebsites.net/currency
<br>https://meli-multi2.azurewebsites.net/status

# Mejoras a realizar - Consideraciones:

* Mongo DB "in memory": en todas las soluciones se optó por la opción "in memory" MongoDB, en una implementación real cada base de datos
debería ejecutarse en un host diferente al de cada aplicación y además tendría que estar configurada en modo "cluster" de tal manera de escalar cuando fuese necesario.
* escalamiento + infraestructura ideal (kubernetes + swarm): cada aplicación está pensada en operar de forma independiente, stateless y desacoplada (microservicio), de esta manera, en una implementación real, idealmente, cada una de ellas podría ejecutarse dentro de un cluster kubernetes o swarm para garantizar el escalamiento horizontal en caso de necesidad.     
* Servicios Health: si cada aplicación se ejecutase dentro de un cluster de contenedores se tendría que definir un servicio del tipo "check health" para facilitar la creación/destrucción de pods por parte del orquestador. 
* Seguridad: en una implementación real debería existir una capa de seguridad (oAuth, custom, etc.) en cada microservicio
* Caché en aplicación currency: la aplicación currency se puede entender como una aplicación que maneja datos de configuración lo que la hace ideal para aplicar una política de "caching" distribuida para mejorar la performance. Esto se podría implementar con Redis o Memcaché por ejemplo.
* Testing entre microservicios: en las soluciones se proveen testing unitario dentro de cada aplicación pero se podría mejorar realizando un test de integración entre aplicaciones (end to end testing).
* Aplicación para lockeo de recursos: en ciertas situaciones de mucha concurrencia se puede dar que mas de una instancia acceda al mismo tiempo a una entidad y que una sobreescriba los cambios producidos por la otra dando lugar a inconsistencias. Si en este sistema se diera ese caso, se podría subsanar implementando otra aplicación (aplicación lock) que se encargue de gestionar los permisos de acceso a cada entidad, de esta manera, una instancia solo podrá escribir sobre cierta entidad X sí y sólo sí tiene el lock de X provisto por la aplicación lock, al terminar la escritura, la aplicación, deberá informar a la aplicación lock que ha finalizado para que otra instancia pueda escribir sobre X si así lo requiera. 
