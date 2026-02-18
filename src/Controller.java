package ProjektWocheEins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Controller
{
    private List<AbstractDevice> devices = new ArrayList<>();

    public void addDevice(AbstractDevice device) {
        devices.add(device);
        device.initialize();
        System.out.println(device.getDeviceName() + " wurde dem HUB hinzugefügt.");
    }

    public List<AbstractDevice> getDevicesByInterface(Class<?> interfaceClass) {
        List<AbstractDevice> byClass = new ArrayList<AbstractDevice>();
        for (AbstractDevice device : devices) {
            if (interfaceClass.isInstance(device)) {
                byClass.add(device);
            }
        }
        return byClass;
    }

    public void shutdownAll() {
        for (AbstractDevice device : devices) {
            if (device instanceof Switchable) {
                ((Switchable) device).turnOff();
            } else if (device instanceof Sensor) {
                ((Sensor) device).shutDown();
            }
        }
    }

    public void showDashboard() {
        for (AbstractDevice device : devices) {
            device.printStatus();
        }
    }

    public List<AbstractDevice> getAllDevices() {
        return devices;
    }

    public void removeDevice(java.util.UUID id) {
        devices.removeIf(device -> device.getUniqueID().equals(id));
        System.out.println("Gerät mit ID " + id + " wurde entfernt.");
    }

    public AbstractDevice getDeviceById(java.util.UUID id) {
        return devices.stream()
                .filter(d -> d.getUniqueID().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void loadFromJson() {
        File file = new File("config.json");
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Wir suchen nur Zeilen, die Objektdaten enthalten (beginnen mit { )
                if (line.contains("{")) {
                    // Parsing der erstellten JSON-Zeile
                    String name = extractValue(line, "name");
                    String idStr = extractValue(line, "id");
                    String type = extractValue(line, "type");

                    if (name != null && idStr != null && type != null) {
                        UUID id = UUID.fromString(idStr);
                        AbstractDevice device;

                        // Erstellen des richtigen Typs
                        switch (type) {
                            case "SmartBulb": device = new SmartBulb(id, name); break;
                            case "SmartPlug": device = new SmartPlug(id, name); break;
                            case "MotionSensor": device = new MotionSensor(id, name); break;
                            default: continue;
                        }

                        // Zur Liste hinzufuegen
                        this.devices.add(device);
                        device.setConnectionStatus(ConnectionStatus.ONLINE);
                    }
                }
            }
            System.out.println("Konfiguration erfolgreich geladen.");
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der JSON: " + e.getMessage());
        }
    }

    // Werte aus JSON File herausholen
    private String extractValue(String line, String key) {
        try {
            String search = "\"" + key + "\": \"";
            int start = line.indexOf(search) + search.length();
            int end = line.indexOf("\"", start);
            return line.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }
}
