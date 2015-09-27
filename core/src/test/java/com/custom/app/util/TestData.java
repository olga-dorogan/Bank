package com.custom.app.util;

import com.custom.app.entity.Client;

/**
 * Created by olga on 27.09.15.
 */
public class TestData {
    private static com.custom.app.entity.Client predefinedClientEntity = new Client(1, "Ivan", "Ivanov", "ЕР123456");

    public static Client getPredefinedClientEntity() {
        return TestData.predefinedClientEntity;
    }

    public static com.custom.app.dto.Client getPredefinedClientDto() {
        return new com.custom.app.dto.Client(
                predefinedClientEntity.getId(),
                predefinedClientEntity.getFirstName(),
                predefinedClientEntity.getLastName(),
                predefinedClientEntity.getPassport());
    }

    private TestData() {

    }
}
