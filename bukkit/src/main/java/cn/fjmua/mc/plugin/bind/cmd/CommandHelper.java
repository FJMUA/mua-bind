package cn.fjmua.mc.plugin.bind.cmd;

import cn.fjmua.mc.plugin.bind.MuaBindBukkit;
import cn.fjmua.mc.plugin.bind.UUIDLoadTask;
import cn.fjmua.mc.plugin.bind.api.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CommandHelper implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp() && !sender.hasPermission("muabind.admin")) {
            sender.sendMessage("无权限");
            return true;
        }
        if (args.length == 1) {
            if ("reload".equals(args[0])) {
                Configuration.reload();
                sender.sendMessage("配置文件重载成功");
            }
        } else if (args.length == 2) {
            if ("load".equals(args[0])) {
                UUID uuid = null;
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    uuid = player.getUniqueId();
                }
                if (uuid == null) {
                    try {
                        uuid = UUID.fromString(args[1]);
                    } catch (Exception e) {
                        sender.sendMessage("未知的玩家 / 错误的uuid: " + args[1]);
                        return true;
                    }
                }
                Bukkit.getScheduler().runTaskAsynchronously(MuaBindBukkit.getInstance(), new UUIDLoadTask(uuid));
                sender.sendMessage("发起异步任务加载玩家数据, uuid: " + uuid);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp() && !sender.hasPermission("muabind.admin")) {
            return List.of();
        }
        if (args.length == 1) {
            return List.of("reload", "load");
        } else if (args.length == 2) {
            if ("load".equals(args[0])) {
                return List.of("<playerName or uuid>");
            }
        }
        return List.of();
    }

}
