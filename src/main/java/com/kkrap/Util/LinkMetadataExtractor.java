package com.kkrap.Util;

import com.kkrap.Entity.Links;
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

    public static Links extractAndBuildLink(String url) {
        Metadata meta = extract(url);

        String linkName = (meta.title != null && !meta.title.isBlank()) ? meta.title : null;
        String thumbnailUrl = (meta.thumbnailUrl != null && !meta.thumbnailUrl.isBlank())
                ? normalizeUrlSlashes(meta.thumbnailUrl)
                : null;
        String faviconUrl = (meta.faviconUrl != null && !meta.faviconUrl.isBlank()) ? meta.faviconUrl : null;

        return Links.of(url, linkName, thumbnailUrl, faviconUrl);
    }

    public static String normalizeUrlSlashes(String url) {
        if (url == null) return null;

        // 프로토콜 구분
        int protocolIndex = url.indexOf("://");
        if (protocolIndex == -1) {
            // 프로토콜 없음 → 전체 처리
            return url.replaceAll("/{2,}", "/");
        }

        String protocolPart = url.substring(0, protocolIndex + 3);
        String rest = url.substring(protocolIndex + 3);

        int firstSlash = rest.indexOf('/');
        if (firstSlash == -1) {
            // 슬래시가 없음 → 경로가 없음
            return protocolPart + rest;
        }
        String host = rest.substring(0, firstSlash);        // 호스트 이름
        String path = rest.substring(firstSlash);           // 경로 전체
        // 경로에서 슬래시 중복 제거
        String normalizedPath = path.replaceAll("/{2,}", "/");
        return protocolPart + host + normalizedPath;
    }

}
