package ar.edu.utn.frba.ddsi.emisor.jobs;

import ar.edu.utn.frba.ddsi.emisor.models.Clima;
import ar.edu.utn.frba.ddsi.emisor.repositories.implementations.ClimaRepository;
import ar.edu.utn.frba.ddsi.emisor.services.IWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CargarWeather {
    private static final Logger log = LoggerFactory.getLogger(CargarWeather.class);

    private final IWeatherService weatherService;
    private final ClimaRepository repository;

    public CargarWeather(IWeatherService weatherService, ClimaRepository repository) {
        this.weatherService = weatherService;
        this.repository = repository;
    }

    // 5 minutos = 300000 milisegundos
    @Scheduled(fixedRate = 300000)
    public void obtenerYGuardarClima() {
        try {
            // Llama al servicio, que llama al conector, que llama a la API
            Clima climaActual = weatherService.obtenerYMapearClima();

            if (climaActual != null) {
                repository.save(climaActual);
                log.info("Registro de clima guardado. Temp: {}°C, Humedad: {}%", climaActual.getTemperatura(), climaActual.getHumedad());
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener el registro climático: {}", e.getMessage());
        }
    }
}
