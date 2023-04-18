package com.masterliu.zero.search;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataSearchTest {

    public static void main(String[] args) {

        String[] searchKeys = {"宇文昊然", "王耀杰", "许思聪"};

        DataSearchTest test = new DataSearchTest();

        StopWatch watch = new StopWatch();
        watch.start();
        List<String> result = test.binarySearch(true, searchKeys);
        watch.stop();
        System.out.println(watch.getLastTaskTimeMillis());
        System.out.println(result);

        watch.start();
        result = test.binarySearch(false, searchKeys);
        watch.stop();
        System.out.println(watch.getLastTaskTimeMillis());
        System.out.println(result);

        watch.start();
        result = test.violentSearch(searchKeys);
        watch.stop();
        System.out.println(watch.getLastTaskTimeMillis());
        System.out.println(result);
    }

    MockDataRepository mockDataRepository = new MockDataRepository();

    IndexSerializer indexSerializer = new IndexSerializer();

    BinarySearchTree binarySearchTree;

    public DataSearchTest() {
        init();
    }

    private void init() {
        this.indexSerializer.generateIndex("idx_name", 1);
        this.binarySearchTree = this.indexSerializer.getNameIndexFromDisk();
    }

    public List<String> binarySearch(boolean useIndex, String... searchNames) {
        List<String> results = new ArrayList<>();
        for (String str : searchNames) {
            BinarySearchTree.Node node = binarySearchTree.binarySearch(str);
            if (node.value.isEmpty()) {
                continue;
            }
            for (Integer primaryKey : node.value) {
                if (useIndex) {
                    results.add(primaryKey + "," + str);
                } else {
                    results.add(mockDataRepository.findById(primaryKey));
                }
            }
        }
        return results;
    }

    public List<String> violentSearch(String... searchNames) {

        Set<String> names = Arrays.stream(searchNames).collect(Collectors.toSet());

        List<String> results = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            // listByPartition(int) 方法读取指定编号的文件, 并将文件中所有行整合成一个String集合进行返回
            List<String> rows = mockDataRepository.listByPartition(i);
            for (String row : rows) {
                // 通过Csv模板生成的数据每个字段用逗号分隔, 其中第2个字段为name
                String[] cols = row.split(",");
                if (names.contains(cols[1])) {
                    results.add(row);
                }
            }
        }

        return results;
    }

}
