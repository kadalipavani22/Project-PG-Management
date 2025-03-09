import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class DisplayCitiesFrame extends JFrame {
    private static final String FILE_NAME = "pg_data.txt";

    public DisplayCitiesFrame() {
        setTitle("PG Finder");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Select a City", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBackground(Color.cyan);
        
        add(titleLabel, BorderLayout.NORTH);

        JPanel cityPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        cityPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cities = {"Hyderabad", "Chennai", "Bangalore", "Delhi", "Mumbai"};

        for (String city : cities) {
            JButton cityButton = new JButton(city);
            cityButton.setFont(new Font("Arial", Font.BOLD, 16));
            cityButton.setPreferredSize(new Dimension(50, 30)); 
            cityButton.setBackground(Color.WHITE);   // Set button background color to white
            cityButton.setForeground(new Color(128, 0, 128));
            cityButton.addActionListener(new CityButtonListener(city));
            cityPanel.add(cityButton);
        }

        JScrollPane scrollPane = new JScrollPane(cityPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private class CityButtonListener implements ActionListener {
        private final String city;

        public CityButtonListener(String city) {
            this.city = city;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            displayPGList(city);
        }
    }

    private void displayPGList(String city) {
        ArrayList<String[]> pgList = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6 && data[2].equalsIgnoreCase(city)) {
                    pgList.add(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading PG data!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (pgList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No PGs found in " + city, "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        // Create a properly sized frame
        JFrame pgFrame = new JFrame(city + " PGs");
        pgFrame.setSize(400, 400);  // Small & compact window
        pgFrame.setResizable(false);  // Disable resizing
        pgFrame.setLocationRelativeTo(null);  // Center on screen
        pgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        // Panel with reduced spacing
        JPanel pgPanel = new JPanel(new GridLayout(0, 1, 2, 2)); // Minimal spacing between buttons
    
        for (String[] pg : pgList) {
            JButton bookButton = new JButton(pg[1] + " | " + pg[3] + " Vacancies | ₹" + pg[5]);
            bookButton.setFont(new Font("Arial", Font.PLAIN, 12));
            bookButton.setPreferredSize(new Dimension(250, 25)); // Compact button
            bookButton.addActionListener(e -> showPGDetails(pg));
            pgPanel.add(bookButton);
        }
    
        JScrollPane scrollPane = new JScrollPane(pgPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Minimal border
    
        pgFrame.add(scrollPane);
        pgFrame.setVisible(true);
    }
    
    
    private void showPGDetails(String[] pgData) {
        JFrame detailsFrame = new JFrame("PG Details");
        detailsFrame.setSize(400, 400);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detailsPanel.add(new JLabel("Gender: " + pgData[0]));
        detailsPanel.add(new JLabel("PG Name: " + pgData[1]));
        detailsPanel.add(new JLabel("City: " + pgData[2]));
        detailsPanel.add(new JLabel("Vacancies: " + pgData[3]));
        detailsPanel.add(new JLabel("<html>Food Menu: " + pgData[4] + "</html>"));
        detailsPanel.add(new JLabel("Price per Room: ₹" + pgData[5]));

        JButton proceedButton = new JButton("Proceed to Book");
        proceedButton.addActionListener(e -> bookPG(pgData));
        detailsPanel.add(proceedButton);

        detailsFrame.add(detailsPanel, BorderLayout.CENTER);
        detailsFrame.setVisible(true);
    }

    private void bookPG(String[] pgData) {
        int availableRooms = Integer.parseInt(pgData[3]);
        int roomPrice = Integer.parseInt(pgData[5]);

        if (availableRooms == 0) {
            JOptionPane.showMessageDialog(this, "No vacancies available!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Enter number of rooms to book (Max: " + availableRooms + "):");

        if (input == null || input.trim().isEmpty()) return;

        int roomsToBook;
        try {
            roomsToBook = Integer.parseInt(input);
            if (roomsToBook <= 0 || roomsToBook > availableRooms) {
                JOptionPane.showMessageDialog(this, "Invalid room count!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int totalCost = roomsToBook * roomPrice;
        int confirm = JOptionPane.showConfirmDialog(this, "Total Price: ₹" + totalCost + "\nProceed with booking?", "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            pgData[3] = String.valueOf(availableRooms - roomsToBook);
            updatePGData(pgData);
            PGBookingPage.addBooking("User", pgData[1], pgData[2], roomsToBook, roomPrice);
            JOptionPane.showMessageDialog(this, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updatePGData(String[] updatedPG) {
        ArrayList<String[]> allPGs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(updatedPG[1])) {
                    allPGs.add(updatedPG);
                } else {
                    allPGs.add(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating PG data!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] pg : allPGs) {
                writer.write(String.join(",", pg) + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving PG data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new DisplayCitiesFrame();
    }
} 