package cn.fjmua.mc.plugin.bind.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class PlayerBind {

    private Integer id;

    private String uuid;

    private String emailDomain;

    private Date createTime;

    private Date modifiedTime;

}
