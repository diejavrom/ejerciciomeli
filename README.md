## Sistema de Cobranzas Meli

# componentes del sistema + documentación de las APIs:

* aplicación charge:

Gestión de cargos. A través de ella se pueden ingresar y consultar cargos. Cada cargo está relacionado con los pagos
que se utilizaron para saldarlo. Almacena los datos en una base de datos MongoDB en memoria. Cada vez que se ingresa un cargo el mismo
es notificado a una cola de eventos (bill queue) que luego será consumido por la aplicación bill.
Además, posee un listener (implementado en JMS) que recibe los pagos notificados en la cola de eventos por la aplicación payment.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/charge/doc/charge-api.html

* aplicación payment:

Gestión de pagos. Mediante esta aplicación se pueden ingresar y consultar pagos. Cada pago está relacionado con los cargos que fueron
saldados con dicho pago. Los datos se almacenan en una base de datos MongoDB. Cada vez que se ingresa un pago, el mismo, luego de procesado, es notificado a una cola de eventos (charge queue) para luego ser consumido por la aplicación charge.
Al momento de crear un pago se exige una clave de idempotencia (idempkey) en el http header. Si esa clave no está presente o bien se envia repetida, el pago será rechazado. Con esto se garantiza que los pagos no se procesen en forma duplicada.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/payment/doc/payment-api.html

* aplicación bill:

A través de esta aplicación se pueden recuperar las facturas. En cada factura se informan los cargos que la componen. También utiliza una base de datos MongoDB en memoria. Posee un listener que recibe los cargos ingresados desde una cola de eventos.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/bill/doc/bill-api.html

* aplicación status

Permite consultar el estado de deuda del usuario obteniendo la información de las aplicaciones charge y payment. No posee almacenamiento.
<br>Documentación de la API en https://meli-multi2.azurewebsites.net/status/doc/status-api.html

* aplicación currency

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

# Solución Cloud (Bonus 2):

Para la solución cloud se utilizó Azure cloud. Cada aplicación se ejecuta dentro de un contenedor Docker como así tambien
la cola de eventos activemq. Además se dispone de un contenedor nginx que funciona como un reverse proxy redireccionando
los endpoints en base a las URLs recibidas.
El deploy y la relación de contenedores se realiza mediante el archivo docker-compose.yml, de esta manera todos quedan
ejecutándose dentro de un mismo host.
Para construir la imagen de cada conteneder se provee un archivo Dockerfile que reside en el path raíz de cada proyecto.
Se utiliza un repositorio de imágenes provisto por Azure Cloud.
La URL base de esta solución es https://meli-multi2.azurewebsites.net, por lo que los endpoints quedan de la siguiente manera:

<br>https://meli-multi2.azurewebsites.net/charge
<br>https://meli-multi2.azurewebsites.net/payment
<br>https://meli-multi2.azurewebsites.net/bill
<br>https://meli-multi2.azurewebsites.net/currency
<br>https://meli-multi2.azurewebsites.net/status

