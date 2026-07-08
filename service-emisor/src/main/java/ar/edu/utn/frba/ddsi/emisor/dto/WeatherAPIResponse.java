package ar.edu.utn.frba.ddsi.emisor.dto;

public record WeatherAPIResponse(
        LocationDto location,
        CurrentDto current
) {
}
