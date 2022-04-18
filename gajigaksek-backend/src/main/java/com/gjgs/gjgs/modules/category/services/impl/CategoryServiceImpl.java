package com.gjgs.gjgs.modules.category.services.impl;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.category.services.interfaces.CategoryService;
import com.gjgs.gjgs.modules.exception.category.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Long> findAndValidCategories(List<Long> categoryIds) {
        List<Category> categoryList = categoryRepository.findAllById(categoryIds);

        if (categoryList.size() != categoryIds.size()) {
            throw new CategoryNotFoundException();
        }

        return categoryList.stream().map(Category::getId).collect(toList());
    }
}
