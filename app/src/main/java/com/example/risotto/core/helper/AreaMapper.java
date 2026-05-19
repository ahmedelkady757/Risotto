package com.example.risotto.core.helper;

import com.example.risotto.data.model.Country;
import com.example.risotto.data.network.dto.AreaDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaMapper {

    // Maps TheMealDB area names to ISO 3166-1 alpha-2 country codes.
    // Only areas with a confirmed code are included — others are silently skipped.
    private static final Map<String, String> AREA_TO_COUNTRY_CODE = new HashMap<>();

    static {
        AREA_TO_COUNTRY_CODE.put("American",   "us");
        AREA_TO_COUNTRY_CODE.put("British",    "gb");
        AREA_TO_COUNTRY_CODE.put("Canadian",   "ca");
        AREA_TO_COUNTRY_CODE.put("Chinese",    "cn");
        AREA_TO_COUNTRY_CODE.put("Croatian",   "hr");
        AREA_TO_COUNTRY_CODE.put("Dutch",      "nl");
        AREA_TO_COUNTRY_CODE.put("Egyptian",   "eg");
        AREA_TO_COUNTRY_CODE.put("Filipino",   "ph");
        AREA_TO_COUNTRY_CODE.put("French",     "fr");
        AREA_TO_COUNTRY_CODE.put("Greek",      "gr");
        AREA_TO_COUNTRY_CODE.put("Indian",     "in");
        AREA_TO_COUNTRY_CODE.put("Irish",      "ie");
        AREA_TO_COUNTRY_CODE.put("Italian",    "it");
        AREA_TO_COUNTRY_CODE.put("Jamaican",   "jm");
        AREA_TO_COUNTRY_CODE.put("Japanese",   "jp");
        AREA_TO_COUNTRY_CODE.put("Kenyan",     "ke");
        AREA_TO_COUNTRY_CODE.put("Malaysian",  "my");
        AREA_TO_COUNTRY_CODE.put("Mexican",    "mx");
        AREA_TO_COUNTRY_CODE.put("Moroccan",   "ma");
        AREA_TO_COUNTRY_CODE.put("Polish",     "pl");
        AREA_TO_COUNTRY_CODE.put("Portuguese", "pt");
        AREA_TO_COUNTRY_CODE.put("Russian",    "ru");
        AREA_TO_COUNTRY_CODE.put("Spanish",    "es");
        AREA_TO_COUNTRY_CODE.put("Thai",       "th");
        AREA_TO_COUNTRY_CODE.put("Tunisian",   "tn");
        AREA_TO_COUNTRY_CODE.put("Turkish",    "tr");
        AREA_TO_COUNTRY_CODE.put("Vietnamese", "vn");
        AREA_TO_COUNTRY_CODE.put("Ukrainian",  "ua");
    }

    public static List<Country> map(List<AreaDto> areaDtos) {
        List<Country> countries = new ArrayList<>();
        if (areaDtos == null) return countries;

        for (AreaDto dto : areaDtos) {
            String areaName = dto.getStrArea();
            if (areaName == null || areaName.isEmpty()) continue;

            String code = AREA_TO_COUNTRY_CODE.get(areaName);

            // Skip countries with no known code — avoids broken image placeholders
            if (code == null) continue;

            // flagcdn.com serves reliable 80-px flag images for every ISO 3166-1 alpha-2 code
            String imageUrl = "https://flagcdn.com/w80/" + code + ".png";
            countries.add(new Country(areaName, imageUrl));
        }

        return countries;
    }
}
