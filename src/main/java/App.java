import app.DashboardManager;
//import edu.wpi.first.networktables.NetworkTable;
//import edu.wpi.first.networktables.NetworkTableInstance;

import javax.swing.*;
import java.io.IOException;

public class App {
//    public static NetworkTableInstance nt;
//    public static NetworkTable table;

    public static void main(String... args) {
//        nt = NetworkTableInstance.getDefault();
//        nt.setServerTeam(1351);
//        nt.startDSClient();
//
//        table = nt.getTable("Log");

        SwingUtilities.invokeLater(() -> {
            try {
                new DashboardManager();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
