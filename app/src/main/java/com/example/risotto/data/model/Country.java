package com.example.risotto.data.model;

import java.util.Objects;

public class Country {

    private String name;
    private String imageUrl;

    public Country(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name) && Objects.equals(imageUrl, country.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imageUrl);
    }
}
