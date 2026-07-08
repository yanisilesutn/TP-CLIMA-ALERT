# TP CLIMA ALERT

Se compone de dos microservicios:

* **Un Emisor** que consulta periódicamente los datos de WeatherAPI y evalúa si la temperatura supera los 35°C junto con una humedad mayor al 60%.
* **Un Receptor** asincrónico que escucha estos eventos críticos en el *broker* de mensajería para el envío automático de correos electrónicos de emergencia a los emails correspondientes.
