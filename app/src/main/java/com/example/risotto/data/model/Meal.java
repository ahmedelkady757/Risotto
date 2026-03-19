package com.example.risotto.data.model;

import java.util.List;


public class Meal {

    private String id;
    private String name;
    private String category;
    private String area;
    private String instructions;
    private String thumbnailUrl;
    private String youtubeUrl;
    private String tags;
    private List<Ingredient> ingredients;
    private boolean isFavorite;


    public Meal() {}


    public String getId()                       { return id; }
    public void setId(String id)                { this.id = id; }

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public String getCategory()                 { return category; }
    public void setCategory(String category)    { this.category = category; }

    public String getArea()                     { return area; }
    public void setArea(String area)            { this.area = area; }

    public String getInstructions()             { return instructions; }
    public void setInstructions(String v)       { this.instructions = v; }

    public String getThumbnailUrl()             { return thumbnailUrl; }
    public void setThumbnailUrl(String v)       { this.thumbnailUrl = v; }

    public String getYoutubeUrl()               { return youtubeUrl; }
    public void setYoutubeUrl(String v)         { this.youtubeUrl = v; }

    public String getTags()                     { return tags; }
    public void setTags(String tags)            { this.tags = tags; }

    public List<Ingredient> getIngredients()    { return ingredients; }
    public void setIngredients(List<Ingredient> v) { this.ingredients = v; }

    public boolean isFavorite()                 { return isFavorite; }
    public void setFavorite(boolean v)          { this.isFavorite = v; }
}