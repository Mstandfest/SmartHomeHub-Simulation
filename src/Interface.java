package ProjektWocheEins;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class Interface extends JFrame {
    private Controller hub;
    private JPanel deviceListPanel;
    private JTextArea logArea;
    private DefaultListModel<String> overviewModel;

    public Interface(Controller hub) {
        this.hub = hub;
        setTitle("Marc's Smart Home Hub");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Eingabe ---
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(12);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"SmartBulb", "SmartPlug", "MotionSensor"});
        JButton addBtn = new JButton("Hinzufügen");

        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Typ:"));
        addPanel.add(typeCombo);
        addPanel.add(addBtn);
        add(addPanel, BorderLayout.NORTH);

        // --- Dashboard (Steuerung) ---
        deviceListPanel = new JPanel();
        deviceListPanel.setLayout(new BoxLayout(deviceListPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(deviceListPanel), BorderLayout.CENTER);

        // --- UUID Liste ---
        overviewModel = new DefaultListModel<>();
        JList<String> overviewList = new JList<>(overviewModel);
        overviewList.setBorder(BorderFactory.createTitledBorder("Geräte-Register (UUID)"));
        overviewList.setPreferredSize(new Dimension(300, 0));
        add(new JScrollPane(overviewList), BorderLayout.EAST);

        // --- Logs ---
        logArea = new JTextArea(6, 20);
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // --- Hinzufügen ---
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) return;

            AbstractDevice newDevice;
            String selection = (String) typeCombo.getSelectedItem();

            switch (selection) {
                case "SmartBulb": newDevice = new SmartBulb(name); break;
                case "SmartPlug": newDevice = new SmartPlug(name); break;
                default: newDevice = new MotionSensor(name); break;
            }

            hub.addDevice(newDevice);
            log("System: " + name + " hinzugefügt.");
            refreshUI();
            saveToJson();
        });

        refreshUI();
        setVisible(true);
    }

    private void log(String msg) {
        logArea.append("> " + msg + "\n");
    }

    private void refreshUI() {
        deviceListPanel.removeAll();
        overviewModel.clear();

        for (AbstractDevice device : hub.getAllDevices()) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setBorder(BorderFactory.createEtchedBorder());

            JLabel nameLabel = new JLabel(device.getDeviceName() + " [" + device.getConnectionStatus() + "]");
            row.add(nameLabel);

            // Switchable Logik
            if (device instanceof Switchable) {
                Switchable s = (Switchable) device;
                JButton toggle = new JButton(s.isOn() ? "AUS" : "AN");
                toggle.addActionListener(al -> {
                    if (s.isOn()) s.turnOff(); else s.turnOn();
                    refreshUI();
                });
                row.add(toggle);
            }

            // Dimmable Logik
            if (device instanceof Dimmable) {
                Dimmable d = (Dimmable) device;
                String[] settings = {"OFF", "LOW", "MID", "HIGH", "FULL"};
                JComboBox<String> levelBox = new JComboBox<>(settings);
                levelBox.setSelectedItem(d.getLevel().toString());
                levelBox.addActionListener(al -> {
                    d.setLevel(DeviceSettings.valueOf((String)levelBox.getSelectedItem()));
                    refreshUI();
                });
                row.add(levelBox);
            }

            // Löschen Button
            JButton delBtn = new JButton("Löschen");
            delBtn.addActionListener(al -> {
                hub.removeDevice(device.getUniqueID());
                log(device.getDeviceName() + " entfernt.");
                refreshUI();
                saveToJson();
            });
            row.add(delBtn);

            deviceListPanel.add(row);
            overviewModel.addElement(device.getDeviceName() + " | " + device.getUniqueID());
        }

        deviceListPanel.revalidate();
        deviceListPanel.repaint();
    }

    private void saveToJson() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("config.json"))) {
            writer.println("[");
            List<AbstractDevice> list = hub.getAllDevices();
            for (int i = 0; i < list.size(); i++) {
                AbstractDevice d = list.get(i);
                writer.print(String.format("  {\"name\": \"%s\", \"id\": \"%s\", \"type\": \"%s\"}",
                        d.getDeviceName(), d.getUniqueID(), d.getClass().getSimpleName()));
                if (i < list.size() - 1) writer.print(",");
                writer.println();
            }
            writer.println("]");
        } catch (IOException e) {
            log("Speicherfehler: " + e.getMessage());
        }
    }
}