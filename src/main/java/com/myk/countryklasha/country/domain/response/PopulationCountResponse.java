package com.myk.countryklasha.country.domain.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PopulationCountResponse {
    public String year;
    public String value;
    public String sex;
    public String reliabilty;
}
