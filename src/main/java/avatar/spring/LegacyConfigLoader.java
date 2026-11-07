package avatar.spring;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Maps config.properties and database.properties onto Spring properties.
 * server.port in the game file is the socket port, so it is not imported as Spring's HTTP port.
 */
public final class LegacyConfigLoader {

    private LegacyConfigLoader() {
    }

    public static void apply(ConfigurableEnvironment environment) {
        Map<String, Object> mapped = new LinkedHashMap<>();
        loadGameConfig(mapped);
        loadDatabaseConfig(mapped);
        if (mapped.isEmpty()) {
            return;
        }
        MapPropertySource source = new MapPropertySource("avatar-legacy-files", mapped);
        MutablePropertySources sources = environment.getPropertySources();
        for (PropertySource<?> existing : sources) {
            String name = existing.getName();
            if (name.startsWith("applicationConfig") || name.contains("application.yml") || name.contains("application.yaml")
                    || name.contains("application.properties")) {
                sources.addBefore(name, source);
                return;
            }
        }
        sources.addLast(source);
    }

    private static void loadGameConfig(Map<String, Object> mapped) {
        Properties props = readIfPresent(new File("config.properties"));
        if (props == null) {
            return;
        }
        copy(props, mapped, "server.port", "avatar.port");
        copy(props, mapped, "server.active", "avatar.active");
        copy(props, mapped, "server.debug", "avatar.debug");
        copy(props, mapped, "game.notify", "avatar.notify");
        copy(props, mapped, "game.city.name", "avatar.city-name");
        copy(props, mapped, "game.big.image.version", "avatar.big-image-version");
        copy(props, mapped, "game.part.version", "avatar.part-version");
        copy(props, mapped, "game.big.item.image.version", "avatar.big-item-image-version");
        copy(props, mapped, "game.itemtype.version", "avatar.item-type-version");
        copy(props, mapped, "game.item.version", "avatar.item-version");
        copy(props, mapped, "game.object.version", "avatar.object-version");
        copy(props, mapped, "game.resources.hd.path", "avatar.resources-hd-path");
        copy(props, mapped, "game.resources.medium.path", "avatar.resources-medium-path");
        System.out.println("Loaded game settings from config.properties");
    }

    private static void loadDatabaseConfig(Map<String, Object> mapped) {
        Properties props = readIfPresent(new File("database.properties"));
        if (props == null) {
            return;
        }
        String host = props.getProperty("host");
        String dbname = props.getProperty("dbname");
        if (host != null && dbname != null) {
            String port = props.getProperty("port", "3306");
            mapped.put("spring.datasource.url", "jdbc:mysql://" + host + ":" + port + "/" + dbname);
        }
        copy(props, mapped, "username", "spring.datasource.username");
        copy(props, mapped, "driver", "spring.datasource.driver-class-name");
        copy(props, mapped, "max_connection", "spring.datasource.hikari.maximum-pool-size");
        copy(props, mapped, "min_connection", "spring.datasource.hikari.minimum-idle");
        if (props.containsKey("password")) {
            mapped.put("spring.datasource.password", props.getProperty("password"));
        } else if (props.containsKey("passeword")) {
            mapped.put("spring.datasource.password", props.getProperty("passeword"));
        }
        System.out.println("Loaded database settings from database.properties");
    }

    private static void copy(Properties props, Map<String, Object> mapped, String from, String to) {
        if (props.containsKey(from)) {
            mapped.put(to, props.getProperty(from));
        }
    }

    private static Properties readIfPresent(File file) {
        if (!file.isFile()) {
            return null;
        }
        Properties props = new Properties();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            props.load(reader);
            return props;
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read " + file.getAbsolutePath(), ex);
        }
    }
}
