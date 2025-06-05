package com.lyq.bookManageSystem.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 将字符串数组转为LocalDateTime数组（用于范围查询）
    public static LocalDateTime[] parseRange(String[] dateRange) {
        if (dateRange == null || dateRange.length != 2) {
            return null;
        }
        try {
            LocalDateTime start = LocalDateTime.parse(dateRange[0] + " 00:00:00", DATE_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(dateRange[1] + " 23:59:59", DATE_FORMATTER);
            return new LocalDateTime[]{start, end};
        } catch (Exception e) {
            throw new IllegalArgumentException("日期格式应为yyyy-MM-dd");
        }
    }
}
