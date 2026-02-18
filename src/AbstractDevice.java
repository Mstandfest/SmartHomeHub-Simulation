package ProjektWocheEins;

import java.util.UUID;

abstract class AbstractDevice
{
    private UUID uniqueID;
    private String deviceName;
    private ConnectionStatus connectionStatus;

    public abstract void initialize();
    public abstract void printStatus();

    public UUID getUniqueID() {
        return uniqueID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public AbstractDevice(UUID uniqueID, String deviceName) {
        this.uniqueID = uniqueID;
        this.deviceName = deviceName;
        this.connectionStatus = ConnectionStatus.OFFLINE;
    }

    public AbstractDevice(String deviceName) {
        this(UUID.randomUUID(), deviceName);
    }
}
