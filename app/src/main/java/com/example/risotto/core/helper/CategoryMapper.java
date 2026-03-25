package com.example.risotto.core.helper;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.network.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;


public final class CategoryMapper {

    private CategoryMapper() { }


    public static Category fromDto(CategoryDto dto) {
        Category category = new Category();

        category.setId(dto.getIdCategory());
        category.setName(dto.getStrCategory());
        category.setThumbnailUrl(dto.getStrCategoryThumb());
        category.setDescription(dto.getStrCategoryDescription());

        return category;
    }

    public static List<Category> fromDtoList(List<CategoryDto> dtos) {
        List<Category> categories = new ArrayList<>();
        if (dtos == null) return categories;
        for (CategoryDto dto : dtos) {
            if (dto != null) categories.add(fromDto(dto));
        }
        return categories;
    }
}