package cn.fjmua.mc.plugin.bind.api;

import cn.fjmua.mc.plugin.bind.pojo.ConfigEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Configuration {

    private static final Gson GSON;
    private static final JsonObject json;

    private static ConfigEntity entity;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
        json = MuaBindStatic.getInstance().getConfigFile().getConfig();
    }

    /**
     * 获取只读的配置文件实体类
     * */
    public static ConfigEntity getReadOnlyConfig() {
        if (entity == null) {
            entity = GSON.fromJson(json, ConfigEntity.class);
        }
        return entity;
    }

}
