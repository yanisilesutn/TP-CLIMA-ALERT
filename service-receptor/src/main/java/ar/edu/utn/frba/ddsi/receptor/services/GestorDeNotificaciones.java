package ar.edu.utn.frba.ddsi.receptor.services;

import ar.edu.utn.frba.ddsi.receptor.dto.Mensaje;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GestorDeNotificaciones {
    private static final Logger log = LoggerFactory.getLogger(GestorDeNotificaciones.class);

    private final List<Mensaje> bandejaDeSalida = new ArrayList<>();

    public void enviar(Mensaje mensaje) {
        // Encolamos el mensaje
        bandejaDeSalida.add(mensaje);

        // Simulamos el envío por consola
        log.info("--- SIMULANDO ENVÍO DE CORREO ---");
        log.info("Destinatarios: {}", String.join(", ", mensaje.destinatarios()));
        log.info("Asunto: {}", mensaje.asunto());
        log.info("Cuerpo:\n{}", mensaje.cuerpo());
        log.info("---------------------------------");
    }

    public List<Mensaje> obtenerEnviados() {
        return bandejaDeSalida;
    }
}
