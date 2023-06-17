package com.myk.countryklasha.country.domain.response;


import com.squareup.moshi.Json;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class IsoResponse {
    String name;
    @Json(name = "Iso2")
    String iso2;

    @Json(name = "Iso3")
    String iso3;
}
