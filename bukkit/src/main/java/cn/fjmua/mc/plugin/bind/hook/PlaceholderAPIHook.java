package cn.fjmua.mc.plugin.bind.hook;

import cn.fjmua.mc.plugin.bind.UUIDCache;
import cn.fjmua.mc.plugin.bind.api.Configuration;
import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import cn.fjmua.mc.plugin.bind.pojo.ConfigEntity;
import cn.fjmua.mc.plugin.bind.pojo.PlayerBind;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    /**
     * %mua-bind%
     * %mua-bind_<uuid>%
     * */
    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        ConfigEntity config = Configuration.getReadOnlyConfig();
        UUID uuid;
        if (params.isEmpty()) {
            uuid = player.getUniqueId();
        } else {
            try {
                uuid = UUID.fromString(params);
            } catch (Exception e) {
                return config.getHook().getDefaultSchool();
            }
        }
        PlayerBind playerBind = UUIDCache.getCache().asMap().get(uuid);
        if (playerBind == null) {
            return config.getHook().getDefaultSchool();
        }
        String school = config.getSchoolMap().get(playerBind.getEmailDomain());
        return school == null ? config.getHook().getDefaultSchool() : school;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mua-bind";
    }

    @Override
    public @NotNull String getAuthor() {
        return "IllTamer";
    }

    @Override
    public @NotNull String getVersion() {
        return MuaBindStatic.VERSION;
    }

}
