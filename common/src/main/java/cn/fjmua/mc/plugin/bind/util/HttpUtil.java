package cn.fjmua.mc.plugin.bind.util;

import cn.fjmua.mc.plugin.bind.pojo.Pair;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    public static <T> T getJsonObject(String url, @NotNull Map<String, String> headers, Class<T> clazz) throws IOException {
        HashMap<String, String> map = new HashMap<>(headers);
        map.put("Accept", "application/json");
        Pair<Integer, String> pair = get(url, map);
        if (pair.k() != 200) {
            throw new IOException(String.format("Request failed, code: %d, msg: %s", pair.k(), pair.v()));
        }
        Gson gson = new Gson();
        return gson.fromJson(pair.v(), clazz);
    }

    public static Pair<Integer, String> get(String urlStr, @NotNull Map<String, String> headers) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        headers.forEach(conn::setRequestProperty);
        int responseCode = conn.getResponseCode();

        InputStream inputStream;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
        }

        return new Pair<>(responseCode, result.toString());
    }

}
