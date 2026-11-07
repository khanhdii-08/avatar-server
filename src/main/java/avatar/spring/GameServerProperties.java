package avatar.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "avatar")
public class GameServerProperties {

    private short port = 14444;
    private boolean active = true;
    private boolean debug;
    private String notify = "";
    private String cityName = "";
    private int bigImageVersion;
    private int partVersion;
    private int bigItemImageVersion;
    private int itemTypeVersion;
    private int itemVersion;
    private int objectVersion;
    private String resourcesHdPath = "res/hd/";
    private String resourcesMediumPath = "res/medium/";

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getBigImageVersion() {
        return bigImageVersion;
    }

    public void setBigImageVersion(int bigImageVersion) {
        this.bigImageVersion = bigImageVersion;
    }

    public int getPartVersion() {
        return partVersion;
    }

    public void setPartVersion(int partVersion) {
        this.partVersion = partVersion;
    }

    public int getBigItemImageVersion() {
        return bigItemImageVersion;
    }

    public void setBigItemImageVersion(int bigItemImageVersion) {
        this.bigItemImageVersion = bigItemImageVersion;
    }

    public int getItemTypeVersion() {
        return itemTypeVersion;
    }

    public void setItemTypeVersion(int itemTypeVersion) {
        this.itemTypeVersion = itemTypeVersion;
    }

    public int getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(int itemVersion) {
        this.itemVersion = itemVersion;
    }

    public int getObjectVersion() {
        return objectVersion;
    }

    public void setObjectVersion(int objectVersion) {
        this.objectVersion = objectVersion;
    }

    public String getResourcesHdPath() {
        return resourcesHdPath;
    }

    public void setResourcesHdPath(String resourcesHdPath) {
        this.resourcesHdPath = resourcesHdPath;
    }

    public String getResourcesMediumPath() {
        return resourcesMediumPath;
    }

    public void setResourcesMediumPath(String resourcesMediumPath) {
        this.resourcesMediumPath = resourcesMediumPath;
    }
}
