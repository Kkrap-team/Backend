package com.kkrap.Data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public final class KeysetCursor {
    private LocalDateTime time;
    private Long id;

    public KeysetCursor(LocalDateTime time, Long id) {
        this.time = time;
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public LocalDateTime getTime(){
        return time;
    }

    /** "yyyy-MM-ddTHH:mm:ss[.SSS]_123" 형식 파싱 */
    public static KeysetCursor parse(String cursor) {
        if (cursor == null || cursor.isBlank()) return null;
        int idx = cursor.lastIndexOf('_');
        if (idx <= 0) return null;
        try {
            LocalDateTime t = LocalDateTime.parse(cursor.substring(0, idx));
            Long i = Long.parseLong(cursor.substring(idx + 1));
            return new KeysetCursor(t, i);
        } catch (Exception e) {
            return null; // 잘못된 커서는 첫 페이지 취급
        }
    }

    public static String build(LocalDateTime time, Long id) {
        return time.toString() + "_" + id;
    }


}
