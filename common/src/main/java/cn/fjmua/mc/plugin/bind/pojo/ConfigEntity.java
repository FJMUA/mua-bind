package cn.fjmua.mc.plugin.bind.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ConfigEntity {

    private Storage storage;

    @SerializedName("default-school")
    private String defaultSchool;

    @SerializedName("school-map")
    private Map<String, String> schoolMap;

    @Data
    @NoArgsConstructor
    public static class Storage {
        private String type;
        private Mysql mysql;

        @Data
        @NoArgsConstructor
        public static class Mysql {
            private String host;
            private int port;
            private String db;
            private String username;
            private String password;
        }
    }
}
