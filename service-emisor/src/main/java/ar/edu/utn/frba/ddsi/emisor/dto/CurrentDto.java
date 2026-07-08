package ar.edu.utn.frba.ddsi.emisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentDto(
        @JsonProperty("temp_c") double tempC, //mapeo
        double humidity
) {
}
