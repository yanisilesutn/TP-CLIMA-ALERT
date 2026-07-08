package ar.edu.utn.frba.ddsi.emisor.repositories.implementations;

import ar.edu.utn.frba.ddsi.emisor.models.Clima;
import ar.edu.utn.frba.ddsi.emisor.repositories.IClimaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClimaRepository implements IClimaRepository {
    public final List<Clima> registrosClima = new ArrayList<>();

    @Override
    public List<Clima> findAll() {
        return registrosClima;
    }

    @Override
    public Clima save(Clima registroClima) {
        registrosClima.add(registroClima);
        return registroClima;
    }

    @Override
    public Clima obtenerUltimoRegistro() {
        if (registrosClima.isEmpty()) {
            return null;
        }
        return registrosClima.getLast();

    }
}
