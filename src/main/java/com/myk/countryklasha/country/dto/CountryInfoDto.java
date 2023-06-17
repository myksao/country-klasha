package com.myk.countryklasha.country.dto;

import java.util.Map;

public record CountryInfoDto (
        String country,
        Map<String, Object> info

) {}