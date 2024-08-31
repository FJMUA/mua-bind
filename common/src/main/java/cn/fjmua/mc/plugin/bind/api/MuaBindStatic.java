package cn.fjmua.mc.plugin.bind.api;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;

public class MuaBindStatic {

    @Setter
    @Getter
    private static MuaBindInst instance;

    @Setter
    @Getter
    private static DataSource dataSource;

}
