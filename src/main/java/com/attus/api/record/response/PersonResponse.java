package com.attus.api.record.response;

import java.util.Date;

public record PersonResponse(
        String name,
        Date birthday,
        AddressResponse address
) {
}
