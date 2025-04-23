package com.Kkrap.Util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HTMLScriptExtractor {
    // HTML에서 유의미한 JSON 블록 추출
    public static String extractJsonBlockFromHtml(Document doc) {
        for (Element script : doc.select("script")) {
            String html = script.html();
            if (html.contains("videoPrimaryInfoRenderer") ||
                    html.contains("structuredDescriptionContentRenderer") ||
                    html.contains("playerOverlayVideoDetailsRenderer")) {

                int start = html.indexOf("{\"contents\":");
                int end = findJsonEnd(html, start);
                if (start != -1 && end != -1) {
                    return html.substring(start, end + 1);
                }
            }
        }
        return null;
    }
    // JSON 블록 끝 위치 찾기
    private static int findJsonEnd(String text, int start) {
        int depth = 0;
        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') depth--;
            if (depth == 0) return i;
        }
        return -1;
    }
}
