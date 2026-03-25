package com.example.risotto.core.helper;


public final class YoutubeHelper {

    private YoutubeHelper() {  }


    public static String extractVideoId(String url) {
        if (url == null || url.trim().isEmpty()) return null;

        if (url.contains("v=")) {
            String id = url.substring(url.indexOf("v=") + 2);
            int ampersand = id.indexOf("&");
            if (ampersand != -1) id = id.substring(0, ampersand);
            return id.isEmpty() ? null : id;
        }

        if (url.contains("youtu.be/")) {
            String id = url.substring(url.lastIndexOf("/") + 1);
            int query = id.indexOf("?");
            if (query != -1) id = id.substring(0, query);
            return id.isEmpty() ? null : id;
        }

        return null;
    }
}