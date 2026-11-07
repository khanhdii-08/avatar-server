package avatar;

import avatar.spring.GameServerProperties;
import avatar.spring.LegacyConfigLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GameServerProperties.class)
public class AvatarApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AvatarApplication.class);
        application.addInitializers(context -> LegacyConfigLoader.apply(context.getEnvironment()));
        application.run(args);
    }
}
