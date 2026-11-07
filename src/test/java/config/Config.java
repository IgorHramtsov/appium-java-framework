package config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Properties;

public record Config(URI appiumUrl, String udid, String deviceName,
                     Duration waitTimeout, Duration commandTimeout, Duration adbTimeout) {
    public static Config load() {
        Properties properties = new Properties();
        try (InputStream input = Config.class.getResourceAsStream("/config.properties")) {
            if (input == null) {
                throw new IllegalStateException("Missing config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read config.properties", e);
        }
        URI url = URI.create(value(properties, "appium.url"));
        if (!("http".equals(url.getScheme()) || "https".equals(url.getScheme())) || url.getHost() == null) {
            throw new IllegalArgumentException("appium.url must be an absolute HTTP(S) URL");
        }
        return new Config(url, value(properties, "device.udid"), value(properties, "device.name"),
                seconds(properties, "wait.seconds"), seconds(properties, "command.seconds"),
                seconds(properties, "adb.seconds"));
    }

    private static String value(Properties properties, String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Empty configuration: " + key);
        }
        return value.trim();
    }

    private static Duration seconds(Properties properties, String key) {
        long seconds = Long.parseLong(value(properties, key));
        if (seconds <= 0) {
            throw new IllegalArgumentException(key + " must be positive");
        }
        return Duration.ofSeconds(seconds);
    }
}

