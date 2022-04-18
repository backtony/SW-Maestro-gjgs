package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.category.entity.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryDummy {

    public static Category createCategory() {
        return Category.builder()
                //.id(1L)
                .mainCategory("test")
                .subCategory("test")
                .build();
    }

    public static Category createCategoryIncludeId(String categoryName, Long id) {
        return Category.builder()
                .id(id)
                .mainCategory(categoryName)
                .subCategory(categoryName)
                .build();
    }

    public static List<Category> createCategoryList() {
        return new ArrayList<>(Arrays.asList
                        (createCategoryIncludeId("test1", 1L),
                        createCategoryIncludeId("test2", 2L)));
    }

    public static Category createCategory(int i) {
        return Category.builder()
                //.id(1L)
                .mainCategory("test"+ i)
                .subCategory("test"+ i)
                .build();
    }

    public static Category createCategory(Long id) {
        return Category.builder()
                .id(id)
                .mainCategory("test"+ id)
                .subCategory("test"+ id)
                .build();
    }
}
