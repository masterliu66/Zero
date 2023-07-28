package com.masterliu.zero.algorithm.search;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DataSearchTest {

    public static void main(String[] args) {

        String[] searchKeys = {"宇文", "王耀杰", "许思聪"};

        DataSearchTest test = new DataSearchTest();

        print("全表扫描", () -> test.violentSearch(false, searchKeys));
        print("索引扫描 + 回表", () -> test.binarySearch(false, false, searchKeys));
        print("索引扫描 + 覆盖索引", () -> test.binarySearch(true, false, searchKeys));
        print("后模糊匹配全表扫描", () -> test.violentSearch(true, searchKeys));
        print("后模糊匹配(前缀索引扫描) + 回表", () -> test.binarySearch(false, true, searchKeys));
    }

    public static void print(String name, Supplier<List<String>> supplier) {
        StopWatch watch = new StopWatch();
        // 全表扫描
        watch.start();
        List<String> result = supplier.get();
        watch.stop();
        System.out.println(name + "耗时: " + watch.getLastTaskTimeMillis());
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

    public List<String> binarySearch(boolean useIndex, boolean fuzzyLogic, String... searchNames) {
        List<String> results = new ArrayList<>();
        for (String str : searchNames) {
            BinarySearchTree.Node node = binarySearchTree.binarySearch(str);
            if (node == null) {
                continue;
            }
            List<Integer> indexes = new ArrayList<>();
            if (fuzzyLogic) {
                if (!node.value.isEmpty()) {
                    indexes.add(node.value.get(0));
                }
                // 后模糊匹配的情况
                if (!node.isEnd()) {
                    node.inorderTraversal(child -> {
                        if (!child.value.isEmpty()) {
                            indexes.add(child.value.get(0));
                        }
                    });
                }
            }
            if (!fuzzyLogic && node.isEnd() && !node.value.isEmpty()) {
                indexes.add(node.value.get(0));
            }
            for (Integer index : indexes) {
                String result = mockDataRepository.findLeafNodeByIndexName("idx_name", index);
                for (String primaryKey : result.split(",")) {
                    if (useIndex) {
                        results.add(primaryKey + "," + str);
                    } else {
                        results.add(mockDataRepository.findById(Integer.parseInt(primaryKey)));
                    }
                }
            }
        }
        return results;
    }

    public List<String> violentSearch(boolean fuzzyLogic, String... searchNames) {

        Set<String> names = Arrays.stream(searchNames).collect(Collectors.toSet());

        List<String> results = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            // listByPartition(int) 方法读取指定编号的文件, 并将文件中所有行整合成一个String集合进行返回
            List<String> rows = mockDataRepository.listByPartition(i);
            for (String row : rows) {
                // 通过Csv模板生成的数据每个字段用逗号分隔, 其中第2个字段为name
                String[] cols = row.split(",");
                if (fuzzyLogic) {
                    for (String name : names) {
                        if (cols[1].startsWith(name)) {
                            results.add(row);
                        }
                    }
                } else {
                    if (names.contains(cols[1])) {
                        results.add(row);
                    }
                }
            }
        }

        return results;
    }

}
