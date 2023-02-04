import app.DashboardManager;
import core.util.Pair;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.NetworkTablesJNI;
import edu.wpi.first.util.CombinedRuntimeLoader;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.math.WPIMathJNI;
import edu.wpi.first.cscore.CameraServerJNI;

import javax.swing.*;
import java.io.IOException;

public class App {
    public static void main(String... args) throws IOException {
        NetworkTablesJNI.Helper.setExtractOnStaticLoad(false);
        WPIUtilJNI.Helper.setExtractOnStaticLoad(false);
        WPIMathJNI.Helper.setExtractOnStaticLoad(false);
        CameraServerJNI.Helper.setExtractOnStaticLoad(false);

        CombinedRuntimeLoader.loadLibraries(App.class, "wpiutiljni", "wpimathjni", "ntcorejni",
                "cscorejnicvstatic");

        SwingUtilities.invokeLater(() -> {
            try {
                new DashboardManager();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
