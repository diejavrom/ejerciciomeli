# Sistema de Cobranzas Meli (versión con saldo a favor) 

En este branch se implementa la solución que no restringe que al momento del ingreso del pago el mismo sea inferior o menor a la deuda del usuario. Las aplicaciones afectadas fueron payment y charge.
Los cambios más relevantes de esta solución con respecto al branch master son los siguientes:
* Se quitan los controles que restringen la condición previamente explicada
* En la aplicación payment se agrega un atributo amountAvailable en la entidad Payment, el mismo registra que cantidad de monto del pago fue utilizado. Observar que si ese atributo se agregara en el branch master siempre seria cero.
* En la aplicación payment se agrega un servicio que permite consultar los pagos con monto disponible.
* En la aplicación charge, al momento de ingresar un cargo, se buscan los pagos con monto disponible y se actualiza ese cargo con los pagos que pueden saldar el mismo. Luego de esto se envia la información en una cola de pagos (payment.charge). 
* A continuación se muestra el diagrama de arquitectura modificado donde se puede ver este último punto

![alt text](https://github.com/diejavrom/ejerciciomeli/blob/v2/melisystem.png)

## Ejecución en entorno local

La ejecución en el entorno local es similar a la versión de master, a excepcion de las aplicaciones charge y payment que se inician como

java -jar target/charge-0.0.2-SNAPSHOT.jar
java -jar target/payment-0.0.2-SNAPSHOT.jar

respectivamente.

## Solución cloud

Esta versión también puede ser probada en un ambiente cloud pero teniendo a la url https://app-meli-multi.azurewebsites.net/ como base. De esta manera, la documentación de las APIs se pueden ver en 

https://app-meli-multi.azurewebsites.net/charge/doc/charge-api.html
https://app-meli-multi.azurewebsites.net/payment/doc/payment-api.html
https://app-meli-multi.azurewebsites.net/currency/doc/currency-api.html
https://app-meli-multi.azurewebsites.net/bill/doc/bill-api.html
https://app-meli-multi.azurewebsites.net/status/doc/status-api.html

(sólo las 2 primeras difieren de la versión que está en master)
