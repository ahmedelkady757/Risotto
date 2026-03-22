package com.example.risotto.core.helper;

import com.example.risotto.data.db.entity.CachedCategoryEntity;
import com.example.risotto.data.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CachedCategoryMapper {

    public static Category toCategory(CachedCategoryEntity entity) {
        if (entity == null) return null;
        Category category = new Category();
        category.setId(entity.getIdCategory());
        category.setName(entity.getStrCategory());
        category.setThumbnailUrl(entity.getStrCategoryThumb());
        category.setDescription(entity.getStrCategoryDescription());
        return category;
    }

    public static CachedCategoryEntity toEntity(Category category) {
        if (category == null || category.getId() == null) return null;
        return new CachedCategoryEntity(
                category.getId(),
                category.getName(),
                category.getThumbnailUrl(),
                category.getDescription()
        );
    }
}
