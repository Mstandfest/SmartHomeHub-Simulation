package ProjektWocheEins;

import java.util.UUID;

public class SmartBulb extends AbstractDevice implements Switchable, Dimmable
{
    private DeviceSettings level = DeviceSettings.OFF;
    private boolean powerState = false;

    public SmartBulb(UUID uniqueID, String deviceName) {
        super(uniqueID, deviceName);
    }

    public SmartBulb(String deviceName) {
        super(deviceName);
    }

    @Override
    public void initialize() {
        while (this.getConnectionStatus() != ConnectionStatus.ONLINE) {
            if (this.getConnectionStatus() == ConnectionStatus.OFFLINE) {
                this.setConnectionStatus(ConnectionStatus.PAIRING);
                System.out.println("Verbindung wird hergestellt...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (this.getConnectionStatus() == ConnectionStatus.PAIRING) {
                this.setConnectionStatus(ConnectionStatus.ONLINE);
                System.out.println("Verbunden!");
            } else {
                this.setConnectionStatus(ConnectionStatus.ERROR);
            }
        }
    }

    @Override
    public void printStatus() {
        System.out.println(getDeviceName() + ", Status: " + (powerState ? "AN (" + level + ")" : "AUS"));
    }

    @Override
    public void setLevel(DeviceSettings level) {
        this.level = level;
    }

    @Override
    public DeviceSettings getLevel() {
        return level;
    }

    @Override
    public void turnOn() {
        if (this.getConnectionStatus() == ConnectionStatus.ONLINE) {
            this.powerState = true;
            this.level = DeviceSettings.MID;
        } else {
            System.out.println("Fehler, das Gerät wurde nicht gefunden.");
        }
    }

    @Override
    public void turnOff() {
        if (this.powerState) {
            this.powerState = false;
        }
    }

    @Override
    public boolean isOn() {
        return powerState;
    }

    public void setPowerState(boolean powerState) {
        this.powerState = powerState;
    }
}
