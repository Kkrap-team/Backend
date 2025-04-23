package com.Kkrap.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class LinkMetadataExtractor {

    public static class Metadata {
        public String title;
        public String thumbnailUrl;
        public String faviconUrl;

        public Metadata(String title, String thumbnailUrl, String faviconUrl) {
            this.title = title;
            this.thumbnailUrl = thumbnailUrl;
            this.faviconUrl = faviconUrl;
        }
    }

    public static Metadata extract(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .get();

            // 제목
            String title = doc.title();
            String json = HTMLScriptExtractor.extractJsonBlockFromHtml(doc);
            if (json != null) {
                String jsonTitle = CustomJsonParser.extractTitleFromJson(json);
                if (jsonTitle != null) {
                    title = jsonTitle;
                }
            }

            // 썸네일
            Element ogImage = doc.selectFirst("meta[property=og:image]");
            String thumbnail = ogImage != null ? ogImage.attr("content") : null;
            if (thumbnail == null) {
                Element preloadImage = doc.selectFirst("link[rel=preload][as=image]");
                if (preloadImage != null) {
                    thumbnail = preloadImage.attr("href");
                }
            }
            // 파비콘
            String favicon = null;
            Elements appleOrIconElements = doc.select("link[rel~=^(apple-touch-icon|icon)$]");
            if (!appleOrIconElements.isEmpty()) {
                favicon = appleOrIconElements.first().attr("href");
            } else {
                Element shortcutIcon = doc.selectFirst("link[rel~=(?i)^(shortcut icon)$]");
                if (shortcutIcon != null) {
                    favicon = shortcutIcon.attr("href");
                }
            }

            if (favicon != null && !favicon.startsWith("http")) {
                URL base = new URL(url);
                favicon = new URL(base, favicon).toString();
            }

            return new Metadata(title, thumbnail, favicon);
        } catch (Exception e) {
            e.printStackTrace();
            return new Metadata(null, null, null);
        }
    }
}
