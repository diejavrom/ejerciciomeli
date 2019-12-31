# Sistema de Cobranzas Meli

Los componentes del sistema son los siguientes:

* aplicación charge:

Gestión de cargos. A través de ella se pueden ingresar y consultar cargos. Cada cargo está relacionado con los pagos
que se utilizaron para saldarlo. Almacena los datos en una base de datos MongoDB en memoria. Cada vez que se ingresa un cargo el mismo
es notificado a una cola de eventos (bill queue) que luego será consumido por la aplicación bill.
Además, posee un listener (implementado en JMS) que recibe los pagos notificados en la cola de eventos por la aplicación payment.

* aplicación payment:

Gestión de pagos. Mediante esta aplicación se pueden ingresar y consultar pagos. Cada pago está relacionado con los cargos que fueron
saldados con dicho pago. Los datos se almacenan en una base de datos MongoDB. Cada vez que se ingresa un pago, el mismo, luego de procesado,
es notificado a una cola de eventos (charge queue) para luego ser consumido por la aplicación charge.

* aplicación bill:

A través de esta aplicación se pueden recuperar las facturas. En cada factura se informan los cargos que la componen. También utiliza una
base de datos MongoDB en memoria. Posee un listener que recibe los cargos ingresados desde una cola de eventos.

* aplicación status

Permite consultar el estado de deuda del usuario obteniendo la información de las aplicaciones charge y payment. No posee almacenamiento.

* aplicación currency

Almacena la configuración de los tipos de moneda que se manejan en el sistema. En esta aplicación además de establecer cuál es la moneda 
por default también permite convertir cualquier importe (configurado) a la moneda por defecto. A efectos de la prueba, 
esta aplicación al iniciarse inserta en su base de datos (Mongo DB en memoria) la moneda ARS (peso argentino) como moneda por defecto y 
también USD (dólar) con una relación 1 USD -> 63 ARS. 

* cola de eventos "charge.queue":

En esta cola de eventos la aplicación payment informa los pagos procesados que luego serán notificados al listener de la aplicación charge.

* cola de eventos "bill.queue":

En esta cola de eventos la aplicación charge informa los cargos procesados que luego serán notificados al listener de la aplicación bill.

A continuación se muestra un diagrama donde se visualizan las interacciones de todos los componentes mencionados anteriormente:

![alt text](https://github.com/diejavrom/ejerciciomeli/blob/master/melisystem.png)


