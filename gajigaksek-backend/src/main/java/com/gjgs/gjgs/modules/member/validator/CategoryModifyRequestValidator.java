package com.gjgs.gjgs.modules.member.validator;

import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryModifyRequestValidator implements ConstraintValidator<CheckIsExistCategory, List<Long>> {

    private final CategoryRepository categoryRepository;


    @Override
    public boolean isValid(List<Long> categoryIdList, ConstraintValidatorContext context) {
        if(categoryRepository.countCategoryByIdList(categoryIdList) != categoryIdList.size()){
            return false;
        }
        return true;
    }
}

