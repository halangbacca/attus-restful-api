package com.attus.api.record.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record PersonRequest(
        @NotBlank String name,
        @NotNull Date birthday,
        @Valid AddressRequest address
) {
}
