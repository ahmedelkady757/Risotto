package com.example.risotto.data.network.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AreaResponse {

    @SerializedName("meals")
    private List<AreaDto> areas;

    public List<AreaDto> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaDto> areas) {
        this.areas = areas;
    }
}
