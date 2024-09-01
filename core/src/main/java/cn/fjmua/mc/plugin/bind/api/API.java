package cn.fjmua.mc.plugin.bind.api;

import cn.fjmua.mc.plugin.bind.pojo.ConfigEntity;
import cn.fjmua.mc.plugin.bind.util.HttpUtil;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class API {

    public static final API QUERY_EMAIL_BY_UUID;

    private final Function<ConfigEntity, String> urlFunc;

    static {
        QUERY_EMAIL_BY_UUID = new API((config) ->
                config.getApi().getQueryEmailByUUID());
    }

    public <T> T invoke(Class<T> responseClazz, String ...params) throws IOException {
        ConfigEntity config = Configuration.getReadOnlyConfig();
        Map<String, String> header = config.getHttpRequestHeader();
        return HttpUtil.getJsonObject(String.format(urlFunc.apply(config), (Object[]) params), header, responseClazz);
    }

}
