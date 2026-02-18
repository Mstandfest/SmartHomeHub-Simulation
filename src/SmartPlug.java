package ProjektWocheEins;

import java.util.UUID;

public class SmartPlug extends AbstractDevice implements Switchable
{
    private boolean powerState = false;

    public SmartPlug(UUID uniqueID, String deviceName) {
        super(uniqueID, deviceName);
    }

    public SmartPlug(String deviceName) {
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
        System.out.println(getDeviceName() + ", Status: " + (powerState ? "AN" : "AUS"));
    }

    @Override
    public void turnOn() {
        if (this.getConnectionStatus() == ConnectionStatus.ONLINE) {
            this.powerState = true;
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
