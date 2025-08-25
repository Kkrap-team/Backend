package com.kkrap.Util;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;

public class CustomJsonParser {

    // JSON에서 title 추출
    public static String extractTitleFromJson(String jsonText) {
        try {
            JsonElement root = JsonParser.parseString(jsonText);
            return findFirstTitle(root);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String findFirstTitle(JsonElement element) {
        if (element == null || element.isJsonNull()) return null;

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("title") && obj.get("title").isJsonObject()) {
                JsonObject titleObj = obj.getAsJsonObject("title");
                if (titleObj.has("runs")) {
                    JsonArray runs = titleObj.getAsJsonArray("runs");
                    if (runs.size() > 0 && runs.get(0).isJsonObject()) {
                        JsonObject run = runs.get(0).getAsJsonObject();
                        if (run.has("text")) {
                            return run.get("text").getAsString();
                        }
                    }
                }
                if (titleObj.has("simpleText")) {
                    return titleObj.get("simpleText").getAsString();
                }
            }

            for (String key : obj.keySet()) {
                String title = findFirstTitle(obj.get(key));
                if (title != null) return title;
            }
        } else if (element.isJsonArray()) {
            for (JsonElement item : element.getAsJsonArray()) {
                String title = findFirstTitle(item);
                if (title != null) return title;
            }
        }
        return null;
    }
}