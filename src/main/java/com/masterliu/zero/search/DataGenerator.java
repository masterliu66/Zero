package com.masterliu.zero.search;

import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import net.datafaker.transformations.Schema;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.datafaker.transformations.Field.field;

public class DataGenerator {

    public static final int DATA_PARTITION_SIZE = 100;

    public static final int DATA_SIZE = 10000;

    MockDataRepository mockDataRepository = new MockDataRepository();

    public static void main(String[] args) {
        new DataGenerator().generateOrderly(new SchemaFactory().defaultSchema());
    }

    public void generateOrderly(Schema<Name, String> fromScratch) {
        CsvTransformer<Name> transformer = CsvTransformer.<Name>builder().header(false).separator(",").build();
        // Schema for from scratch
        for (int i = 0; i < DATA_PARTITION_SIZE; i++) {
            generate(fromScratch, transformer, i);
        }
    }

    public void generateConcurrently(Schema<Name, String> fromScratch) {
        CsvTransformer<Name> transformer = CsvTransformer.<Name>builder().header(false).separator(",").build();
        // Schema for from scratch
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(processors << 1);
        for (int i = 0; i < DATA_PARTITION_SIZE; i++) {
            final int index = i;
            executorService.execute(() -> generate(fromScratch, transformer, index));
        }
        executorService.shutdown();
    }

    private void generate(Schema<Name, String> fromScratch, CsvTransformer<Name> transformer, int index) {
        System.out.println("开始执行第" + index + "个任务");
        String result = transformer.generate(fromScratch, DATA_SIZE);
        System.out.println("第" + index + "个任务数据准备完成");
        mockDataRepository.writeToPartition(result, index);
        System.out.println("第" + index + "个任务执行完成");
    }

    public void testGenerate(int limit) {
        Faker faker = new Faker(new Locale("zh", "CN"));
        // transformer could be the same for both
        net.datafaker.transformations.CsvTransformer<Name> transformer = net.datafaker.transformations.CsvTransformer.<Name>builder().header(true).separator(",").build();
        // Schema for from scratch
        Schema<Name, String> fromScratch = Schema.of(field("firstName", () -> faker.name().firstName()), field("lastname", () -> faker.name().lastName()));
        System.out.println(transformer.generate(fromScratch, limit));
        // Schema for transformations
        Schema<Name, String> schemaForTransformations = Schema.of(field("firstName", Name::firstName), field("lastname", Name::lastName));
        // Here we pass a collection of Name objects and extract first and lastnames from each element
        System.out.println(transformer.generate(faker.collection(faker::name).maxLen(limit).generate(), schemaForTransformations));
    }

}
