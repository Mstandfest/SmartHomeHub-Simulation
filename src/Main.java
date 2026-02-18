package ProjektWocheEins;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("FlatLaf konnte nicht geladen werden, nutze Standard.");
        }
        System.out.println("Smart Home gestartet.");

        Controller hub = new Controller();
        hub.loadFromJson();
        SwingUtilities.invokeLater(() -> new Interface(hub));
    }
}
