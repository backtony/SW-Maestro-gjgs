package com.gjgs.gjgs.modules.category.validators;

import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.team.dtos.CreateTeamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryValidator implements Validator {

    private final CategoryRepository categoryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateTeamRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateTeamRequest request = (CreateTeamRequest) target;
        List<Long> categoryIdList = request.getCategoryList();
        Long count = categoryRepository.countCategoryByIdList(categoryIdList);
        if (count != categoryIdList.size()) {
            errors.rejectValue("categoryList", "invalid.category", "유효하지 않은 취미 카테고리가 포함되어 있습니다.");
        }
    }
}
