package com.gjgs.gjgs.modules.category.services.interfaces;

import java.util.List;

public interface CategoryService {

    List<Long> findAndValidCategories(List<Long> categoryIds);
}
