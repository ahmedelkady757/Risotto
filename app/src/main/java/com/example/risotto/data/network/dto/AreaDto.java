package com.example.risotto.data.network.dto;

import com.google.gson.annotations.SerializedName;

public class AreaDto {

    @SerializedName("strArea")
    private String strArea;

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }
}
