package ar.edu.utn.frba.ddsi.receptor.services;

import ar.edu.utn.frba.ddsi.receptor.dto.Mensaje;

import java.util.List;

public interface IGestorDeNotificaciones {
    void enviar(Mensaje mensaje);
    List<Mensaje> obtenerEnviados();
}
