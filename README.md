# Sistema de Cobranzas Meli (versión con saldo a favor) 

En este branch se implementa la solución que no restringe que al momento del ingreso del pago el mismo sea inferior o menor a la deuda del usuario. Las aplicaciones afectadas fueron payment y charge.
Los cambios más relevantes de esta solución con respecto al branch master son los siguientes:
* Se quita la lógica que no permite que el usuario pueda tener saldo "a favor".
* En la aplicación payment se agrega un atributo amountAvailable en la entidad Payment, el mismo registra que cantidad del monto del pago fue utilizado para saldar cargos. Observar que si ese atributo se agregara en el branch master el mismo siempre seria cero.
* En la aplicación payment se agrega un servicio que permite consultar los pagos con monto (amountAvailable) disponible.
* En la aplicación charge, al momento de ingresar un cargo, se buscan los pagos con monto disponible y salda el cargo con el monto de los pagos tanto como sea posible. Luego de esto se envia la información (cargo -> pagos) a una cola de pagos (payment.charge).
* La aplicación payment escucha los eventos de la cola de pagos y actualiza la información de cada pago.
* A continuación se muestra el diagrama de arquitectura modificado donde se puede ver este último punto.

![alt text](https://github.com/diejavrom/ejerciciomeli/blob/v2/melisystem.png)

## Ejecución en entorno local

La ejecución en el entorno local es similar a la versión de master, a excepcion de las aplicaciones charge y payment que se inician como

<br>java -jar target/charge-0.0.2-SNAPSHOT.jar
<br>java -jar target/payment-0.0.2-SNAPSHOT.jar

respectivamente.

## Solución cloud

Esta versión también puede ser probada en un ambiente cloud pero teniendo a la url https://meli-multi2-v2.azurewebsites.net/ como base. De esta manera, la documentación de las APIs se pueden ver en 

<br>https://meli-multi2-v2.azurewebsites.net/charge/doc/charge-api.html
<br>https://meli-multi2-v2.azurewebsites.net/payment/doc/payment-api.html
<br>https://meli-multi2-v2.azurewebsites.net/currency/doc/currency-api.html
<br>https://meli-multi2-v2.azurewebsites.net/bill/doc/bill-api.html
<br>https://meli-multi2-v2.azurewebsites.net/status/doc/status-api.html

(sólo las 2 primeras difieren de la versión que está en master)
