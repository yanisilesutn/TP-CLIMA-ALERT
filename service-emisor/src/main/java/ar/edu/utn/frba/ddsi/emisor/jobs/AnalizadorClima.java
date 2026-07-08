package ar.edu.utn.frba.ddsi.emisor.jobs;

import ar.edu.utn.frba.ddsi.emisor.dto.AlertaClimaEvento;
import ar.edu.utn.frba.ddsi.emisor.messaging.EventoPublisher;
import ar.edu.utn.frba.ddsi.emisor.models.Clima;
import ar.edu.utn.frba.ddsi.emisor.repositories.IClimaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalizadorClima {
    private static final Logger log = LoggerFactory.getLogger(AnalizadorClima.class);

    private final IClimaRepository repository;
    private final EventoPublisher eventoPublisher;

    public AnalizadorClima(IClimaRepository repository, EventoPublisher eventoPublisher) {
        this.repository = repository;
        this.eventoPublisher = eventoPublisher;
    }

    @Scheduled(fixedRate = 60000) //cada 1 minuto
    public void analizarClima() {
        Clima ultimoClima = repository.obtenerUltimoRegistro();

        if (ultimoClima != null && ultimoClima.getTemperatura() > 35 && ultimoClima.getHumedad() > 60) {
            log.warn("¡Alerta! Temperatura: {}°C, Humedad: {}%",
                    ultimoClima.getTemperatura(), ultimoClima.getHumedad());

            // Crear el evento y publicarlo vía RabbitMQ
            AlertaClimaEvento alerta = new AlertaClimaEvento(
                    ultimoClima.getTemperatura(),
                    ultimoClima.getHumedad(),
                    LocalDateTime.now(),
                    "Alerta climática: Temperatura y humedad exceden los límites seguros."
            );

            eventoPublisher.publicar(alerta);
        }
    }
}
