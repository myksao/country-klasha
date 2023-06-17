package com.myk.countryklasha.country.domain.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CapitalResponse {
    String name;
    String capital;
    String iso2;
    String iso3;
}
