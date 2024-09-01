package cn.fjmua.mc.plugin.bind.api;

import cn.fjmua.mc.plugin.bind.dal.PlayerBindDAO;
import lombok.Getter;
import lombok.Setter;

public class MuaBindStatic {

    public static final String VERSION = "1.0.0";

    @Setter
    @Getter
    private static MuaBindInst instance;

    @Setter
    @Getter
    private static PlayerBindDAO playerBindDAO;

    @Setter
    @Getter
    private static boolean luckPermsHook;

}
