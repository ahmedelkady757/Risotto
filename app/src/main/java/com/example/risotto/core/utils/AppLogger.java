package com.example.risotto.core.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class AppLogger {

    // ── Tags ───

    private static final String TAG      = "RISOTTO";
    private static final String TAG_NET  = "RISOTTO_NET";
    private static final String TAG_DB   = "RISOTTO_DB";
    private static final String TAG_AUTH = "RISOTTO_AUTH";
    private static final String TAG_UI   = "RISOTTO_UI";

    private static final boolean ENABLED = true;

    private AppLogger() {  }

    // ── General ───

    public static void d(String message) {
        if (ENABLED) Log.d(TAG, message);
    }

    public static void i(String message) {
        if (ENABLED) Log.i(TAG, message);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String message, Throwable t) {
        Log.e(TAG, message, t);
    }

    // ── Network ───


    public static void logRequest(String method, String url,
                                  String headers, String body) {
        if (!ENABLED) return;

        StringBuilder sb = new StringBuilder();
        sb.append("\n╔══════════════════════════════════════════╗\n");
        sb.append("║  ➡  REQUEST\n");
        sb.append("╠══════════════════════════════════════════╣\n");
        sb.append("║  ").append(method).append("  ").append(url).append("\n");

        if (headers != null && !headers.isEmpty()) {
            sb.append("╠── Headers ────────────────────────────────\n");
            for (String line : headers.split("\n")) {
                sb.append("║  ").append(line).append("\n");
            }
        }

        if (body != null && !body.isEmpty()) {
            sb.append("╠── Body ───────────────────────────────────\n");
            sb.append(indentJson(prettyJson(body))).append("\n");
        }

        sb.append("╚══════════════════════════════════════════╝");
        Log.d(TAG_NET, sb.toString());
    }


    public static void logResponse(int statusCode, String url, String body) {
        if (!ENABLED) return;

        StringBuilder sb = new StringBuilder();
        sb.append("\n╔══════════════════════════════════════════╗\n");
        sb.append("║  ⬅  RESPONSE  [").append(statusCode).append("]\n");
        sb.append("╠══════════════════════════════════════════╣\n");
        sb.append("║  URL: ").append(url).append("\n");

        if (body != null && !body.isEmpty()) {
            // Truncate very large bodies so Logcat doesn't drop the line
            String display = body.length() > 3000
                    ? body.substring(0, 3000) + "\n… (truncated)"
                    : body;
            sb.append("╠── Body ───────────────────────────────────\n");
            sb.append(indentJson(prettyJson(display))).append("\n");
        }

        sb.append("╚══════════════════════════════════════════╝");
        Log.d(TAG_NET, sb.toString());
    }


    public static void logNetworkError(String url, Throwable t) {
        Log.e(TAG_NET, "Network error → " + url + "\n   Cause: " + t.getMessage(), t);
    }

    // ── Database ───

    public static void logDbInsert(String table, String info) {
        if (ENABLED) Log.d(TAG_DB, "INSERT  [" + table + "]  " + info);
    }

    public static void logDbDelete(String table, String info) {
        if (ENABLED) Log.d(TAG_DB, "DELETE  [" + table + "]  " + info);
    }

    public static void logDbQuery(String table, String info) {
        if (ENABLED) Log.d(TAG_DB, "QUERY   [" + table + "]  " + info);
    }

    public static void logDbError(String table, Throwable t) {
        Log.e(TAG_DB, "DB error [" + table + "] → " + t.getMessage(), t);
    }

    // ── Auth ───

    public static void logAuth(String event) {
        if (ENABLED) Log.i(TAG_AUTH, "Key" + event);
    }

    public static void logAuthError(String event, Throwable t) {
        Log.e(TAG_AUTH, "Auth error — " + event + ": " + t.getMessage(), t);
    }

    // ── UI / Navigation ───

    public static void logNav(String destination) {
        if (ENABLED) Log.d(TAG_UI, "Navigate→ " + destination);
    }

    public static void logFragment(String fragmentName, String lifecycle) {
        if (ENABLED) Log.d(TAG_UI, "Fragment [" + fragmentName + "] " + lifecycle);
    }

    // ── Private helpers ───

    private static String prettyJson(String raw) {
        if (raw == null || raw.isEmpty()) return raw;
        try {
            String trimmed = raw.trim();
            if (trimmed.startsWith("{")) return new JSONObject(trimmed).toString(2);
            if (trimmed.startsWith("[")) return new JSONArray(trimmed).toString(2);
        } catch (JSONException ignored) { /* not JSON */ }
        return raw;
    }

    private static String indentJson(String json) {
        if (json == null) return "";
        StringBuilder sb = new StringBuilder();
        for (String line : json.split("\n")) {
            sb.append("║  ").append(line).append("\n");
        }
        return sb.toString();
    }
}