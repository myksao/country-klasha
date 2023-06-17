package com.myk.countryklasha.country.domain.response;


import com.squareup.moshi.Json;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PositionResponse {
    String name;
    String iso2;
    @Json(name = "long")
    String longitude;
    String lat;
}
