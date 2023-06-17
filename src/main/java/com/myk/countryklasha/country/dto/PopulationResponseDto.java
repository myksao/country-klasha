package com.myk.countryklasha.country.dto;

import jakarta.annotation.Nullable;

import java.util.List;

public record PopulationResponseDto<T>(
        @Nullable List<T> italy,
        @Nullable List<T> newZealand,
        @Nullable  List<T> ghana
) {
}
