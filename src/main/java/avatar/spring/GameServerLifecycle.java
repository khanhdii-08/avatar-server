package avatar.spring;

import avatar.db.DbManager;
import avatar.server.Avatar;
import avatar.server.ServerManager;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class GameServerLifecycle implements SmartLifecycle {

    private final DataSource dataSource;
    private final GameServerProperties properties;
    private Thread serverThread;
    private volatile boolean running;

    public GameServerLifecycle(DataSource dataSource, GameServerProperties properties) {
        this.dataSource = dataSource;
        this.properties = properties;
    }

    @Override
    public void start() {
        DbManager.bind(dataSource);
        ServerManager.applyConfig(
                properties.getPort(),
                properties.getCityName(),
                properties.isActive(),
                properties.isDebug(),
                properties.getNotify(),
                properties.getBigImageVersion(),
                properties.getPartVersion(),
                properties.getBigItemImageVersion(),
                properties.getItemTypeVersion(),
                properties.getItemVersion(),
                properties.getObjectVersion(),
                properties.getResourcesHdPath(),
                properties.getResourcesMediumPath());
        Avatar.printBanner();
        ServerManager.init();
        serverThread = new Thread(ServerManager::start, "avatar-server");
        serverThread.start();
        running = true;
    }

    @Override
    public void stop() {
        if (!running && serverThread == null) {
            ServerManager.stop();
            return;
        }
        System.out.println("Shutdown Server!");
        running = false;
        ServerManager.stop();
        if (serverThread != null) {
            try {
                serverThread.join(15_000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
