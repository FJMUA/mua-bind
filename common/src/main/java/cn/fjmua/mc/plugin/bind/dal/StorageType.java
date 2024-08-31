package cn.fjmua.mc.plugin.bind.dal;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;

/**
 * 数据源枚举
 * */
public enum StorageType {

    MYSQL,

    SQLITE

    ;

    @Nullable
    public static StorageType parse(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.name().toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElse(null);
    }

}
