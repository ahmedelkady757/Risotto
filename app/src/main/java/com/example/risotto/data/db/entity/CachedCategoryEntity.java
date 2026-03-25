package com.example.risotto.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cached_categories")
public class CachedCategoryEntity {

    @PrimaryKey
    @NonNull
    private String idCategory;
    private String strCategory;
    private String strCategoryThumb;
    private String strCategoryDescription;

    public CachedCategoryEntity(@NonNull String idCategory, String strCategory, String strCategoryThumb, String strCategoryDescription) {
        this.idCategory = idCategory;
        this.strCategory = strCategory;
        this.strCategoryThumb = strCategoryThumb;
        this.strCategoryDescription = strCategoryDescription;
    }

    @NonNull
    public String getIdCategory() { return idCategory; }
    public String getStrCategory() { return strCategory; }
    public String getStrCategoryThumb() { return strCategoryThumb; }
    public String getStrCategoryDescription() { return strCategoryDescription; }
}
