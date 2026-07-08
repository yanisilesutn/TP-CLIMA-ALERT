package ar.edu.utn.frba.ddsi.receptor.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para el lado CONSUMIDOR.
 *
 * Declara el mismo topic exchange, una cola durable y el binding (exchange + routing key -> cola).
 * El @RabbitListener escucha esa cola. Al arrancar, estos beans se crean solos en RabbitMQ
 * (los vas a ver en el panel http://localhost:15672).
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

  @Value("${app.events.exchange}")
  private String exchangeName;

  @Bean
  public TopicExchange eventosExchange() {
    return new TopicExchange(exchangeName, true, false);
  }

  @Bean
  public Queue eventosQueue(@Value("${app.events.queue}") String name) {
    return QueueBuilder.durable(name).build();
  }

  @Bean
  public Binding eventosBinding(Queue eventosQueue, TopicExchange eventosExchange,
                                @Value("${app.events.routing-key}") String routingKey) {
    return BindingBuilder.bind(eventosQueue).to(eventosExchange).with(routingKey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }
}
