package cn.fjmua.mc.plugin.bind.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Slf4j
public class ConfigFile {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String name;
    private final Path configFolder;
    @Getter
    private JsonObject config;

    public ConfigFile(String name, Path configFolder) {
        this.name = name;
        this.configFolder = configFolder;
        this.config = this.load();
    }

    @SneakyThrows
    private JsonObject load() {
        if (!Files.exists(configFolder)) {
            Files.createDirectories(configFolder);
        }

        Path configFilePath = configFolder.resolve(name);
        if (!Files.exists(configFilePath)) {
            try (InputStream in = getClass().getResourceAsStream(name)) {
                if (in == null) {
                    throw new RuntimeException("Unknown resource file: " + name);
                }
                Files.copy(in, configFilePath, StandardCopyOption.REPLACE_EXISTING);
                log.info("已创建默认配置 {}", name);
            }
        }

        Yaml yaml = new Yaml();
        try (FileInputStream inputStream = new FileInputStream(configFilePath.toFile())) {
            Map<?, ?> map = yaml.loadAs(inputStream, Map.class);
            JsonElement element = gson.toJsonTree(map);
            if (!element.isJsonObject()) {
                throw new IllegalStateException("YAML root is not a JSON object in file: " + name);
            }
            return element.getAsJsonObject();
        }
    }

    /**
     * 保存配置
     * 将内存中的数据写入硬盘
     * */
    public void save() {
        Map<?, ?> map = gson.fromJson(gson.toJson(config), Map.class);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        String filePath = configFolder.resolve(name).toAbsolutePath().toString();
        try (FileWriter writer = new FileWriter(filePath)) {
            yaml.dump(map, writer);
        } catch (IOException e) {
            log.error("Failed to save config '{}'", name,  e);
        }
    }

    /**
     * 重载配置
     * 将硬盘中的数据重新读取到内存
     * */
    public void reload() {
        this.config = this.load();
    }

    public <T> T getConfig(Class<T> clazz) {
        return gson.fromJson(config, clazz);
    }

}
