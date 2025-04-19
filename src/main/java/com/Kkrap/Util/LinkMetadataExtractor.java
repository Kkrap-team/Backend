package com.Kkrap.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            // 제목
            String title = doc.title();

            // 썸네일
            Element ogImage = doc.selectFirst("meta[property=og:image]");
            String thumbnail = ogImage != null ? ogImage.attr("content") : null;

            // 파비콘
            String favicon = null;
            Element iconLink = doc.select("link[rel~=(?i)^(shortcut icon|icon)$]").first();
            if (iconLink != null) {
                favicon = iconLink.attr("href");

                // 상대 경로라면 절대 경로로 변환
                if (favicon != null && !favicon.startsWith("http")) {
                    URL base = new URL(url);
                    favicon = new URL(base, favicon).toString();
                }
            }

            return new Metadata(title, thumbnail, favicon);

        } catch (Exception e) {
            e.printStackTrace();
            return new Metadata(null, null, null);
        }
    }
}
