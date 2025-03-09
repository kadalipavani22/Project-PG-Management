import java.awt.*;
import java.io.*;
import javax.swing.*;

public class PGBookingPage extends JFrame {
    private static final String BOOKING_FILE = "bookings.txt";

    public PGBookingPage() {
        setTitle("PG Booking Details");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Booked PG Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JTextArea bookingDetails = new JTextArea();
        bookingDetails.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingDetails);
        add(scrollPane, BorderLayout.CENTER);

        loadBookingDetails(bookingDetails);
        setVisible(true);
    }

    private void loadBookingDetails(JTextArea bookingDetails) {
        StringBuilder details = new StringBuilder();
        File file = new File(BOOKING_FILE);

        try {
            if (!file.exists()) {
                file.createNewFile(); // Create file if it doesn't exist
                details.append("No bookings available.");
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        details.append(line).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            details.append("Error loading bookings.");
        }

        bookingDetails.setText(details.toString());
    }

    public static void addBooking(String userName, String pgName, String city, int rooms, int price) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE, true))) {
            writer.write("User: " + userName + ", PG: " + pgName + ", City: " + city + ", Rooms: " + rooms + ", Total Price: ₹" + (rooms * price));
            writer.newLine(); // Ensure proper formatting
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving booking details!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PGBookingPage();
    }
}
