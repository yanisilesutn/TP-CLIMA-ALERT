package ar.edu.utn.frba.ddsi.emisor.conectors;

import ar.edu.utn.frba.ddsi.emisor.dto.WeatherAPIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class WeatherAPIConector {
    private static final Logger log = LoggerFactory.getLogger(WeatherAPIConector.class);
    private final RestClient restClient;
    private final String apiKey;

    public WeatherAPIConector(RestClient.Builder restClientBuilder,
                              @Value("${weather.api.url}") String apiUrl,
                              @Value("${weather.api.key}") String apiKey
    ) {

        this.restClient = restClientBuilder.baseUrl(apiUrl).build();
        this.apiKey = apiKey;
    }

    public WeatherAPIResponse obtenerClimaActual(String ubicacion) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/current.json")
                            .queryParam("key", apiKey)
                            .queryParam("q", ubicacion)
                            .build())
                    .retrieve()
                    .body(WeatherAPIResponse.class);

        } catch (RestClientException e) {
            log.error("Fallo la comunicación HTTP con WeatherAPI: {}", e.getMessage());
            throw new RuntimeException("Error al consultar el clima externo", e);
        }
    }
}
