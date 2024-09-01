package cn.fjmua.mc.plugin.bind.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ConfigEntity {

    private API api;

    @SerializedName("http-request-header")
    private Map<String, String> httpRequestHeader;

    private Boolean velocity;

    private Storage storage;

    private Hook hook;

    @SerializedName("school-map")
    private Map<String, String> schoolMap;

    @Data
    @NoArgsConstructor
    public static class API {
        @SerializedName("query-email-by-uuid")
        private String queryEmailByUUID;
    }

    @Data
    @NoArgsConstructor
    public static class Storage {
        private String type;
        private Mysql mysql;

        @Data
        @NoArgsConstructor
        public static class Mysql {
            private String host;
            private Integer port;
            private String db;
            private String username;
            private String password;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Hook {
        @SerializedName("default-school")
        private String defaultSchool;
        @SerializedName("permission-node-prefix")
        private String permissionNodePrefix;
    }

}
