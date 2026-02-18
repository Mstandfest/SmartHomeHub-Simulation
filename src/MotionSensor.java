package ProjektWocheEins;

import java.util.UUID;

public class MotionSensor extends AbstractDevice implements Sensor
{
    private boolean motionDetected = false;

    public MotionSensor(UUID uniqueID, String deviceName) {
        super(uniqueID, deviceName);
    }

    public MotionSensor(String deviceName) {
        super(deviceName);
    }

    public boolean triggerMotion () {
        System.out.println("Bewegung erkannt.");
        return motionDetected = true;
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
        System.out.println(getDeviceName() + ", Status: " + (motionDetected ? "Bewegung erkannt." : "Keine Bewegung."));
    }

    @Override
    public double getReadings() {
        if (motionDetected == true) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    @Override
    public String getUnit() {
        return "Bewegung";
    }

    @Override
    public boolean shutDown() {
        return motionDetected = false;
    }
}
