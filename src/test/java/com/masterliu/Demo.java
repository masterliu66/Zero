package com.masterliu;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Demo {

    // 返回用例数据
    static Stream<Arguments> testcase() {
        return Stream.of(
                Arguments.arguments(2533, new int[] {1,2,3,5,9}, 2532),
                Arguments.arguments(2533, new int[] {1,2,4,9}, 2499),
                Arguments.arguments(2033, new int[] {1,2,4,9}, 1999),
                Arguments.arguments(2533, new int[] {3,4,9}, 999),
                Arguments.arguments(2222, new int[] {2}, 222),
                Arguments.arguments(2, new int[] {2}, -1),
                Arguments.arguments(-12, new int[] {1}, -1)
        );
    }

    /**
     * 数组A给定一些1~9的数。返回由A数组中的元素组成的小于n的最大数。例如A={1,24,9}，x=2533，返回2499
     */
    @ParameterizedTest
    @MethodSource("testcase")
    void test(int n, int[] arr, int ans)  {

        int res = getMaxNumbers2(arr, n);

        assert res == ans;
    }

    int max = -1;

    private int getMaxNumbers2(int[] arr, int n) {

        if (n <= 0) {
            return -1;
        }

        StringBuilder builder = new StringBuilder();
        backtracking(arr, n, String.valueOf(n).length(), builder);

        return max;
    }

    private void backtracking(int[]arr, int n, int length, StringBuilder current) {

        if (current.length() > length) {
            return;
        }
        // 小于n的数位直接跳过
        if (current.length() >= Math.max(length - 1, 1)) {
            int num = Integer.parseInt(current.toString());
            if (num >= n) {
                return;
            }
            max = Math.max(max, num);
        }

        for (int num : arr) {
            // 选择当前数
            current.append(num);
            backtracking(arr, n, length, current);
            current.deleteCharAt(current.length() - 1);
        }
    }

    private int getMaxNumbers(int[] arr, int n) {

        if (n <= 0) {
            return -1;
        }

        // 将数组从小到大排序, min = arr[0], max = arr[arr.length - 1]
        Arrays.sort(arr);

        Set<Integer> set = new HashSet<>();
        for (int num : arr) {
            set.add(num);
        }

        String str = String.valueOf(n);
        int[] digits = new int[str.length()];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
        }
        /* 使用贪心算法, 找出符合条件的最大数
         * 1. 除最后一位外, 找出第一个不能在数组找到相同值的数位i
         * 2. 数组中存在小于数位i的值, 数位i取数组中小于当前值的最大值, 后续所有数位取数组中的最大值, 此时已经找到了答案
         * 3. 数组中不存在小于数位i的值, 需要从i-1数位中找出一个值降低一个档位
         * 4. 可以找到数位j存在降低空间, 数位j取数组中小于当前值的最大值, 后续所有数位取数组中的最大值, 此时已经找到了答案
         * 5. 前面i-1个数位全都没有降低空间, 说明不能找到与n相同长度的结果, 使用n-1长度全部拼接数组中最大值即可, 如果n的长度为1, 直接返回-1
         */
        int i = 0;
        while (i < digits.length - 1 && set.contains(digits[i])) {
            i++;
        }

        if (arr[0] < digits[i]) {
            return buildResult(i, digits, arr);
        }

        // 数组中最小值都比当前数位大, 需要降低前面数位的值
        return backAndBuildResult(i, digits, arr);
    }

    private int backAndBuildResult(int i, int[] digits, int[] arr) {
        int res;
        // 向前回溯, 找出存在下降空间的最近一个数位
        int j = i - 1;
        while (j >= 0 && digits[j] <= arr[0]) {
            j--;
        }
        // 前面所有数位都无下降空间
        if (j < 0) {
            // n长度等于1, 无法找到一个数小于n, 直接返回-1
            if (digits.length == 1) {
               return -1;
            }
            // 长度降低一位, 使用数组最大值拼接剩余数位
            res = Integer.parseInt(String.valueOf(arr[arr.length - 1]).repeat(digits.length - 1));
        } else {
            res = buildResult(j, digits, arr);
        }
        return res;
    }

    private int buildResult(int i, int[] digits, int[] arr) {
        int res;
        StringBuilder builder = new StringBuilder();
        // 前i-1数位取原值
        for (int j = 0; j < i; j++) {
            builder.append(digits[j]);
        }
        // 第i数位取小于原值的最大值
        for (int j = arr.length - 1; j >= 0; j--) {
            if (arr[j] < digits[i]) {
                builder.append(arr[j]);
                break;
            }
        }
        // 后面所有数位取数组中最大值
        if (i < digits.length - 1) {
            builder.append(String.valueOf(arr[arr.length - 1]).repeat(digits.length - (i + 1)));
        }
        res = Integer.parseInt(builder.toString());
        return res;
    }

}
