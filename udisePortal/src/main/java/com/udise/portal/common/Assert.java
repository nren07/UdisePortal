package com.udise.portal.common;

import io.micrometer.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

public abstract class Assert {
    public static void notNull(Object arg, String message) {
        if (arg == null) {
            throw new NullPointerException(message);
        }
    }
    public static void notBlank(String arg, String message) {
        if (StringUtils.isBlank(arg)) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void nonZero(Integer number, String message) {
        if (number == null || number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void nonZero(Long number, String message) {
        if (number == null || number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void nonZero(Double number, String message) {
        if (number == null || number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void nonZero(Short number, String message) {
        if (number == null || number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(@SuppressWarnings("rawtypes") Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<? extends Object, ? extends Object> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void inRange(int start, int end, Integer value, String message) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

}
