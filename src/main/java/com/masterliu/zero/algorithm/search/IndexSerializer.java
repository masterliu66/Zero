package com.masterliu.zero.algorithm.search;

import io.vavr.Tuple2;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IndexSerializer {

    MockDataRepository mockDataRepository = new MockDataRepository();

    public void generateIndex(String indexName, int index) {

        // 每个叶子结点的值以逗号分隔, 并单独成一行
        StringBuilder builder = new StringBuilder();
        mockDataRepository.writeIndex(() -> {
            TrieTree trieTree = generateTree(index);
            AtomicInteger atomicInteger = new AtomicInteger();
            // 把叶子节点存储的数据单独取出, 并其值指向索引序号
            trieTree.inorderTraversal(node -> {
                if (!node.value.isEmpty()) {
                    String values = node.value.stream().map(String::valueOf).collect(Collectors.joining(","));
                    builder.append(values).append("\n");
                    node.value = Collections.singletonList(atomicInteger.getAndIncrement());
                }
            });
            return trieTree.root.toString();
        }, builder::toString, indexName);
    }

    public TrieTree generateTree(int index) {
        TrieTree trieTree = new TrieTree();
        for (int i = 0; i < 200; i++) {
            List<String> rows = mockDataRepository.listByPartition(i);
            for (String row : rows) {
                String[] cols = row.split(",");
                int id = Integer.parseInt(cols[0]);
                String name = cols[index];
                trieTree.addNode(name, id);
            }
        }

        return trieTree;
    }

    public TrieTree getNameIndexFromDisk() {
        TrieTree trieTree = new TrieTree();
        trieTree.root = getIndexFromDisk("idx_name");
        return trieTree;
    }

    public TrieTree.Node getIndexFromDisk(String indexName) {
        String result = mockDataRepository.findByIndexName(indexName);
        return deserialization(result, 0)._1();
    }

    private Tuple2<TrieTree.Node, Integer> deserialization(String str, int offset) {

        TrieTree.Node node = new TrieTree.Node();
        // isEnd
        node.isEnd = str.charAt(++offset) == '1';
        offset++;
        // value
        StringBuilder builder = new StringBuilder();
        for (;;) {
            char c = str.charAt(++offset);
            if (c == '[') {
                continue;
            }
            if (str.charAt(offset) == ',' || str.charAt(offset) == ']') {
                if (builder.length() > 0) {
                    node.value.add(Integer.parseInt(builder.toString().trim()));
                    builder.setLength(0);
                }
            } else {
                builder.append(c);
            }
            if (c ==']') {
                break;
            }
        }
        offset++;
        offset++;
        // left
        if (str.charAt(offset) == '-') {
            offset++;
            offset++;
        } else if (str.charAt(offset) == '{') {
            var result = deserialization(str, offset);
            node.left = result._1();
            offset = result._2();
            offset++;
        }
        // right
        if (str.charAt(offset) == '-') {
            offset++;
            offset++;
        } else if (str.charAt(offset) == '{') {
            var result = deserialization(str, offset);
            node.right = result._1();
            offset = result._2();
            offset++;
        }
        return new Tuple2<>(node, offset);
    }

}
