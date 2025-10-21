package New_Additions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Landing page for the Boba Shop POS System.
 * 
 * This class creates the main entry point for the POS system, providing a clean
 * and intuitive interface for users to choose between Manager and Cashier
 * modes.
 * The landing page features a gradient background, styled buttons, and proper
 * error handling for mode transitions.
 * 
 */
public class LandingPage extends JFrame {

    /**
     * Constructs a new LandingPage instance.
     * 
     * Initializes the user interface components and displays the landing page
     * window in the center of the screen.
     */
    public LandingPage() {
        initializeUI();
    }

    /**
     * Initializes the user interface components and window properties.
     * 
     * Sets up the main window with a gradient background, creates header, center,
     * and footer panels, and configures window properties such as size, position,
     * and close operation.
     */
    private void initializeUI() {
        setTitle("Boba Shop POS System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);

        // Main panel with gradient background
        /**
         * Anonymous inner class extending JPanel to provide custom gradient background painting.
         * Creates a smooth transition from steel blue to light steel blue.
         */
        JPanel mainPanel = new JPanel() {
            /**
             * Custom paint component method to create a gradient background.
             * Implements smooth gradient from top to bottom using steel blue shades.
             * Enables anti-aliasing for smoother rendering.
             * 
             * @param g the Graphics object used for painting
             */
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(70, 130, 180),
                        0, getHeight(), new Color(135, 206, 250));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel with buttons
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Creates and configures the header panel containing the main title and
     * subtitle.
     * 
     * The header panel displays the application name and a brief description to
     * guide
     * the user. It uses white text on a transparent background to work with the
     * gradient background of the main panel.
     * 
     * @return JPanel containing the formatted header components
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        JLabel titleLabel = new JLabel("Boba Shop POS System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Select your role to begin");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    /**
     * Creates and configures the center panel containing role selection buttons.
     * 
     * This panel contains the Manager and Cashier buttons arranged vertically
     * using GridBagLayout. Each button is styled and has appropriate action
     * listeners attached for mode transitions.
     * 
     * @return JPanel containing the role selection buttons
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Manager button
        JButton managerButton = createStyledButton("Manager", "Access management features");
        managerButton.addActionListener(new ActionListener() {
            /**
             * Handles the manager button click event.
             * 
             * @param e the action event triggered by button click
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                openManagerInterface();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(managerButton, gbc);

        // Cashier button
        JButton cashierButton = createStyledButton("Cashier", "Process customer orders");
        cashierButton.addActionListener(new ActionListener() {
            /**
             * Handles the cashier button click event.
             * 
             * @param e the action event triggered by button click
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                openCashierInterface();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(cashierButton, gbc);

        return centerPanel;
    }

    /**
     * Creates a styled button with consistent appearance and hover effects.
     * 
     * This method creates buttons with a uniform style including size, color
     * scheme,
     * border, and hover effects. The buttons are designed to be visually appealing
     * and provide clear feedback to user interactions.
     * 
     * @param text    the text to display on the button
     * @param tooltip the tooltip text to show on hover
     * @return JButton with consistent styling and hover effects
     */
    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 60));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(new Color(70, 130, 180));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        button.setFocusPainted(false);
        button.setToolTipText(tooltip);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Handles mouse enter event to provide visual feedback.
             * 
             * @param e the mouse event
             */
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(245, 245, 245));
            }

            /**
             * Handles mouse exit event to restore original appearance.
             * 
             * @param e the mouse event
             */
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    /**
     * Creates and configures the footer panel containing status information.
     * 
     * The footer panel displays system status information to keep users informed
     * about the application state, particularly database connectivity status.
     * 
     * @return JPanel containing status information
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel statusLabel = new JLabel("Ready - Database connection will be established automatically");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(statusLabel);

        return footerPanel;
    }

    /**
     * Opens the Manager interface and closes the landing page.
     * 
     * This method attempts to create and display the manager interface window.
     * If successful, the current landing page window is disposed. If an error
     * occurs during the transition, an error dialog is displayed to the user.
     * 
     * @throws Exception if the ManagerUI class cannot be found or instantiated
     */
    private void openManagerInterface() {
        try {
            // Create and display the Manager UI
            ManagerGUI.ManagerUI managerUI = new ManagerGUI.ManagerUI();
            managerUI.setVisible(true);
            dispose(); // Close landing page when ManagerUI is ready
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to open Manager interface: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens the Cashier interface and closes the landing page.
     * 
     * This method attempts to create and display the cashier interface window.
     * If successful, the current landing page window is disposed. If an error
     * occurs during the transition, an error dialog is displayed to the user.
     * 
     * @throws Exception if the CashierUI class cannot be found or instantiated
     */
    private void openCashierInterface() {
        try {
            // Create and display the Cashier UI
            CashierUI cashierUI = new CashierUI();
            cashierUI.setVisible(true);
            dispose(); // Close landing page when CashierUI is ready
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to open Cashier interface: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}