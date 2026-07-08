package ar.edu.utn.frba.ddsi.emisor.services.implementations;

import ar.edu.utn.frba.ddsi.emisor.conectors.WeatherAPIConector;
import ar.edu.utn.frba.ddsi.emisor.dto.WeatherAPIResponse;
import ar.edu.utn.frba.ddsi.emisor.models.Clima;
import ar.edu.utn.frba.ddsi.emisor.services.IWeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherService implements IWeatherService {
    private final WeatherAPIConector conectorWeatherApi;
    private final String ubicacionFija;

    public WeatherService(
            WeatherAPIConector conectorWeatherApi,
            @Value("${weather.api.location}") String ubicacionFija
    ) {
        this.conectorWeatherApi = conectorWeatherApi;
        this.ubicacionFija = ubicacionFija;
    }

    public Clima obtenerYMapearClima() {
        // llamamos a la api
        WeatherAPIResponse response = conectorWeatherApi.obtenerClimaActual(ubicacionFija);

        if (response == null || response.current() == null) {
            return null;
        }

        return new Clima(response.current().tempC(),response.current().humidity());
    }
}
