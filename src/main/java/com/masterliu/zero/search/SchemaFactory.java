package com.masterliu.zero.search;

import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import net.datafaker.transformations.Schema;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static net.datafaker.transformations.Field.field;

public class SchemaFactory {

    Faker faker = new Faker(new Locale("zh", "CN"));

    AtomicInteger atomicInteger = new AtomicInteger();

    public Schema<Name, String> defaultSchema() {
        // name gender phone address birthday
        return Schema.of(
                field("number", () -> String.valueOf(atomicInteger.getAndIncrement())),
                field("name", () -> faker.name().fullName()),
                field("gender", () -> faker.gender().binaryTypes()),
                field("phone", () -> faker.phoneNumber().cellPhone()),
                field("address", () -> faker.address().cityName()),
                field("birthday", () -> faker.date().birthday("yyyy-MM-dd hh:mm:ss"))
        );
    }

}
