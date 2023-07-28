package com.masterliu.zero.algorithm.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BinarySearchTree {

    Node root;

    public BinarySearchTree() {
        this.root = new Node();
    }

    public Node getRoot() {
        return root;
    }

    public Node binarySearch(String str) {
        Node node = root;
        for (int i = 0; i < str.length(); i++) {
            int codePoint = str.charAt(i);
            for (int j = 31; j >= 0; j--) {
                int bit = (codePoint >> j) & 1;
                if (bit == 0) {
                    node = node.left;
                } else {
                    node = node.right;
                }
                // 无法匹配的场景
                if (node == null) {
                    break;
                }
            }
        }

        return node;
    }

    public void addNode(String str, int primaryKey) {
        Node node = root;
        for (int i = 0; i < str.length(); i++) {
            // 将字符转换为Unicode码
            int codePoint = str.charAt(i);
            // 从左到右, 遍历Unicode码的每一位Bit
            for (int j = 31; j >= 0; j--) {
                // 取出当前位Bit的值
                int bit = (codePoint >> j) & 1;
                if (bit == 0) {
                    if (node.left == null) {
                        node.left = new Node();
                    }
                    // 将当前结点指向左子结点
                    node = node.left;
                } else {
                    if (node.right == null) {
                        node.right = new Node();
                    }
                    // 将当前结点指向右子结点
                    node = node.right;
                }
            }
        }
        // 标记字符结束
        node.isEnd = true;
        // 叶子结点添加行编号的值
        node.value.add(primaryKey);
    }

    public void inorderTraversal(Consumer<Node> consumer) {
        root.inorderTraversal(consumer);
    }

    public static class Node implements Serializable {

        boolean isEnd;
        // 由于名称可能重复, 所以存在多个行编号
        List<Integer> value;
        Node left;
        Node right;
        public Node() {
            this.value = new ArrayList<>();
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void inorderTraversal(Consumer<Node> consumer) {
            if (left != null) {
                left.inorderTraversal(consumer);
            }
            consumer.accept(this);
            if (right != null) {
                right.inorderTraversal(consumer);
            }
        }

        @Override
        public String toString() {
            int isEnd = this.isEnd ? 1 : 0;
            String left = this.left == null ? "-" : this.left.toString();
            String right = this.right == null ? "-" : this.right.toString();
            return "{" +
                    isEnd +
                    "," + value +
                    "," + left +
                    "," + right +
                    '}';
        }
    }

}
