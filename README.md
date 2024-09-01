# mua-bind

A plugin that automatically detects mua player information and binds related social data. 一个自动检测mua玩家信息并绑定相关社交数据的插件

[[config.yml]](./common/src/main/resources/config.yml)

## notice

1. 本插件使用 mua 官方提供的 `api/union/profile/email/byuuid` 接口，在使用插件前请确保您的服务器域名在 mua api 的**白名单**内
2. 仅支持 java 17+
3. 本插件的部分配置不支持重载

## velocity support

> velocity 版本要求: 3.0.0+

当使用 `velocity` 代理端并需要配置跨服数据互通时，请<u>**在代理端安装本插件**</u>并修改插件的配置文件，使各子服的本插件连接到<u>**同一个MySQL数据库(表)**</u>，并开启以下配置项:

```yaml
# config.yml
velocity: true
```

当服务器核心为 `bukkit` 系且配置项 `velocity` 开启时，插件将进入代理模式。此时位于子服的插件将<u>停止主动调用 MUA api</u>，并定时从数据库读取来自代理端写入的数据，实现同步。

## Command

> 执行所有命令所需要的权限 `muabind.admin`

- `muabind reload` 重载所有配置文件
- `muabind load <uuid / playerName>` 根据 UUID / 玩家名称 获取并加载数据

## Hooks

### PlaceholderAPI

- `%mua-bind%` 返回玩家绑定邮箱对应的学校
- `%mua-bind_<uuid>%` 返回uuid对应的玩家绑定的邮箱对应的学校

### LuckPerms

如果服务器启用了 LuckPerms 插件，则 MuaBind 在更新玩家数据时会额外进行以下操作:

1. 读取玩家邮箱后缀映射, 如 `{10007.mua: 北京大学}`
2. 读取配置 `permission-node-prefix: mua.bind.title.` 作为新增权限节点前缀
3. 玩家在进入服务器时将自动新增权限 `mua.bind.title.10007.mua` 和 `mua.bind.title.北京大学`