package com.masterliu.zero.algorithm.search;

import io.vavr.Tuple2;

import java.util.List;

public class IndexSerializer {

    MockDataRepository mockDataRepository = new MockDataRepository();

    public void generateIndex(String indexName, int index) {

        mockDataRepository.writeIndex(() -> {
            BinarySearchTree binarySearchTree = generateIndex(index);
            return binarySearchTree.root.toString();
        }, indexName);
        //        try (FileOutputStream fos = new FileOutputStream("D:/tmp/data/sort/" + indexName + ".obj");
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject(root);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public BinarySearchTree generateIndex(int index) {
        BinarySearchTree binarySearchTree = new BinarySearchTree();
        for (int i = 0; i < 200; i++) {
            List<String> rows = mockDataRepository.listByPartition(i);
            for (String row : rows) {
                String[] cols = row.split(",");
                int id = Integer.parseInt(cols[0]);
                String name = cols[index];
                binarySearchTree.addNode(name, id);
            }
        }

        return binarySearchTree;
    }

    public BinarySearchTree getNameIndexFromDisk() {
        BinarySearchTree binarySearchTree = new BinarySearchTree();
        binarySearchTree.root = getIndexFromDisk("idx_name");
        return binarySearchTree;
    }

    public BinarySearchTree.Node getIndexFromDisk(String indexName) {

//        BinarySearchTree.Node root = null;
//        try (FileInputStream fis = new FileInputStream("D:/tmp/data/sort/idx_name.obj");
//             ObjectInputStream ois = new ObjectInputStream(fis)) {
//            root = (Node) ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        String result = mockDataRepository.findByIndexName(indexName);
        return deserialization(result, 0)._1();
    }

    private Tuple2<BinarySearchTree.Node, Integer> deserialization(String str, int offset) {

        BinarySearchTree.Node node = new BinarySearchTree.Node();
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
