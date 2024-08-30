package cn.fjmua.mc.plugin.bind.dal;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * 数据源枚举
 * */
public enum DataSourceType {

    MYSQL,

    SQLITE

    ;

    @Nullable
    public static DataSourceType parse(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.name().equals(name))
                .findFirst()
                .orElse(null);
    }

}
