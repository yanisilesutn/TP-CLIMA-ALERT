package ar.edu.utn.frba.ddsi.emisor.repositories;

import ar.edu.utn.frba.ddsi.emisor.models.Clima;

import java.util.List;

public interface IClimaRepository {
    List<Clima> findAll();
    Clima save(Clima registroClima);
    Clima obtenerUltimoRegistro();
}
