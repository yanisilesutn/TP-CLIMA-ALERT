package ar.edu.utn.frba.ddsi.receptor.services;

import ar.edu.utn.frba.ddsi.receptor.dto.AlertaClimaEvento;
import ar.edu.utn.frba.ddsi.receptor.dto.MensajeEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Acá va la lógica de negocio que se ejecuta al recibir un evento
 * (por ejemplo: enviar un mail, guardar en base, actualizar métricas, etc.).
 * Por ahora solo loguea el evento como demostración.
 */
@Service
public class ProcesadorEventoService {

  private static final Logger log = LoggerFactory.getLogger(ProcesadorEventoService.class);

  public void procesar(AlertaClimaEvento evento) {
    // TODO: reemplazar por la lógica real del TP.
    log.info("Procesando evento -> titulo='{}', contenido='{}', fecha={}",
        evento.titulo(), evento.contenido(), evento.fechaHora());
  }
}
