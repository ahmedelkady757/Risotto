package com.example.risotto.data.model;


public class Category {

    private String id;
    private String name;
    private String thumbnailUrl;
    private String description;


    public Category() {}


    public String getId()                       { return id; }
    public void setId(String id)                { this.id = id; }

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public String getThumbnailUrl()             { return thumbnailUrl; }
    public void setThumbnailUrl(String v)       { this.thumbnailUrl = v; }

    public String getDescription()              { return description; }
    public void setDescription(String v)        { this.description = v; }
}