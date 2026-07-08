package ar.edu.utn.frba.ddsi.emisor.messaging;

import ar.edu.utn.frba.ddsi.emisor.dto.AlertaClimaEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Publica eventos en el topic exchange de RabbitMQ.
 * Esta es la pieza que reemplaza a una llamada HTTP directa: publica y se desentiende (async).
 */
@Component
public class EventoPublisher {

  private static final Logger log = LoggerFactory.getLogger(EventoPublisher.class);

  private final RabbitTemplate rabbitTemplate;
  private final String exchange;
  private final String routingKey;

  public EventoPublisher(
      RabbitTemplate rabbitTemplate,
      @Value("${app.events.exchange}") String exchange,
      @Value("${app.events.routing-key}") String routingKey) {
    this.rabbitTemplate = rabbitTemplate;
    this.exchange = exchange;
    this.routingKey = routingKey;
  }

  public void publicar(AlertaClimaEvento evento) {
    try {
      rabbitTemplate.convertAndSend(exchange, routingKey, evento);
      log.info("Evento publicado en '{}' con routing key '{}': {}", exchange, routingKey, evento.mensaje());
    } catch (AmqpException e) {
      // best-effort: si falla la publicación, no debería cortar el flujo del servicio
      log.error("No se pudo publicar el evento de alerta climática {}", e.getMessage());
    }
  }
}
