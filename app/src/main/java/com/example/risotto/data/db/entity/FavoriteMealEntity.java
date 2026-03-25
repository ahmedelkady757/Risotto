package com.example.risotto.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.risotto.data.model.Ingredient;

import java.util.List;

@Entity(tableName = "favorite_meals", primaryKeys = {"userId", "id"})
public class FavoriteMealEntity {

    @NonNull
    private String userId;

    @NonNull
    private String id;

    private String name;
    private String category;
    private String area;
    private String instructions;
    private String thumbnailUrl;
    private String youtubeUrl;
    private String tags;

    private List<Ingredient> ingredients;

    public FavoriteMealEntity(@NonNull String userId, @NonNull String id, String name, String category, String area, String instructions, String thumbnailUrl, String youtubeUrl, String tags, List<Ingredient> ingredients) {
        this.userId = userId;
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.thumbnailUrl = thumbnailUrl;
        this.youtubeUrl = youtubeUrl;
        this.tags = tags;
        this.ingredients = ingredients;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }


    public String getInstructions() {
        return instructions;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }


    public String getYoutubeUrl() {
        return youtubeUrl;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
