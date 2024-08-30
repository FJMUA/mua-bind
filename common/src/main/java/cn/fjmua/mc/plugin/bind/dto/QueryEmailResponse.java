package cn.fjmua.mc.plugin.bind.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QueryEmailResponse {

    /**
     * 邮箱域名 (sjtu.edu.cn)
     * */
    @SerializedName("email_domain")
    private String emailDomain;

}
