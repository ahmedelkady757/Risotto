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

    public static List<Category> toCategoryList(List<com.example.risotto.data.db.entity.CachedCategoryEntity> entities) {
        if (entities == null) return new java.util.ArrayList<>();
        List<Category> categories = new java.util.ArrayList<>();
        for (com.example.risotto.data.db.entity.CachedCategoryEntity entity : entities) {
            categories.add(toCategory(entity));
        }
        return categories;
    }

    public static List<com.example.risotto.data.db.entity.CachedCategoryEntity> toEntityList(List<Category> categories) {
        if (categories == null) return new java.util.ArrayList<>();
        List<com.example.risotto.data.db.entity.CachedCategoryEntity> entities = new java.util.ArrayList<>();
        for (Category category : categories) {
            entities.add(toEntity(category));
        }
        return entities;
    }
}
