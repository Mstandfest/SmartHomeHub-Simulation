package ProjektWocheEins;

public class AutomationRule
{
    private Sensor triggerDevice;
    private Switchable actorDevice;

    public AutomationRule(Sensor trigger, Switchable actor) {
        this.triggerDevice = trigger;
        this.actorDevice = actor;
    }

    public void executeRule() {
        if (triggerDevice.getReadings() > 0) {
            System.out.println("Gerät getriggert durch: " + ((AbstractDevice)triggerDevice).getDeviceName());

            try {
                AbstractDevice device = (AbstractDevice) actorDevice;

                if (device.getConnectionStatus() != ConnectionStatus.ONLINE) {
                    throw new DeviceOfflineException(device.getDeviceName());
                }

                actorDevice.turnOn();
                System.out.println("Aktion ausgeführt auf: " + device.getDeviceName());

            } catch (DeviceOfflineException e) {
                System.err.println("Fehler: " + e.getMessage());
            }
        }
    }
}
