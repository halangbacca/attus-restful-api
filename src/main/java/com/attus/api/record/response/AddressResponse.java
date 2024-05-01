package com.attus.api.record.response;

public record AddressResponse(
        String street,
        String city,
        String state,
        String zip,
        String number
) {
}
