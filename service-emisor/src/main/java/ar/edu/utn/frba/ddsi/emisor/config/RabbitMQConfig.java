package ar.edu.utn.frba.ddsi.emisor.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para el lado PRODUCTOR.
 *
 * Declara el topic exchange y un RabbitTemplate que serializa los eventos a JSON.
 * El productor NO necesita declarar colas: solo publica al exchange con una routing key.
 */
@Configuration
public class RabbitMQConfig {

  @Bean
  public TopicExchange eventosExchange(@Value("${app.events.exchange}") String name) {
    return new TopicExchange(name, true, false); // durable, no auto-delete
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter);
    return template;
  }
}
