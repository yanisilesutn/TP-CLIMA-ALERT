package ar.edu.utn.frba.ddsi.receptor.services;

import ar.edu.utn.frba.ddsi.receptor.dto.AlertaClimaEvento;

public interface IProcesadorEventosService {
    void procesar(AlertaClimaEvento evento);
}
