package New_Additions;
import javax.swing.*;

/**
 * Main entry point for the Boba Shop POS System
 * Displays landing page with Manager/Cashier options
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LandingPage();
        });
    }
}