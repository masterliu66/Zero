package com.masterliu;

import com.masterliu.zero.algorithm.search.DataSearchService;
import org.junit.jupiter.api.Test;

public class DataSearchTest {

    @Test
    public void test() {
        String[] searchKeys = {"宇文", "王耀杰", "许思聪"};

        DataSearchService service = new DataSearchService();

        service.print("全表扫描", () -> service.violentSearch(false, searchKeys));
        service.print("索引扫描 + 回表", () -> service.search(false, false, searchKeys));
        service.print("索引扫描 + 覆盖索引", () -> service.search(true, false, searchKeys));
        service.print("后模糊匹配全表扫描", () -> service.violentSearch(true, searchKeys));
        service.print("后模糊匹配(前缀索引扫描) + 回表", () -> service.search(false, true, searchKeys));
    }

}
