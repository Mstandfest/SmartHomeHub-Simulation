package ProjektWocheEins;

public enum DeviceSettings
{
    OFF (0),
    LOW (25),
    MID (50),
    HIGH (75),
    FULL (100);

    private final int level;

    DeviceSettings(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
