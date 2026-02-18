package ProjektWocheEins;

public class DeviceOfflineException extends Exception {
    public DeviceOfflineException(String deviceName) {
        super("Das Gerät '" + deviceName + "' ist offline und kann nicht gesteuert werden!");
    }
}

