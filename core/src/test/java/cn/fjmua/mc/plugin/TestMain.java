package cn.fjmua.mc.plugin;

import cn.fjmua.mc.plugin.bind.dto.QueryEmailResponse;
import cn.fjmua.mc.plugin.bind.util.HttpUtil;

import java.util.Map;

public class TestMain {

    public static void main(String[] args) throws Exception {
        QueryEmailResponse response = HttpUtil.getJsonObject(
                "http://api.fjmua.cn/query/email/uuid/eea6b28d655733b18da8a3bd986388c4",
                Map.of(
                        "Authorization", "Bearer 82d27808203a428384c55d3e378bb4d6",
                        "Accept", "application/json"
                ),
                QueryEmailResponse.class
        );
        System.out.println(response);
    }

}
