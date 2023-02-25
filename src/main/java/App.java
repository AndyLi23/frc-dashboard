import app.DashboardManager;
import core.util.Pair;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.NetworkTablesJNI;

import javax.swing.*;
import java.io.IOException;

public class App {
    public static void main(String... args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                new DashboardManager();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
