import app.DashboardManager;
import core.util.NTInstance;

import javax.swing.*;
import java.io.IOException;

public class App {
    public static void main(String... args) {
        NTInstance.getInstance().initNT();

        SwingUtilities.invokeLater(() -> {
            try {
                new DashboardManager();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
