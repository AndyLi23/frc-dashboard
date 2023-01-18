import app.DashboardManager;

import javax.swing.*;
import java.io.IOException;

public class App {
    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new DashboardManager();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
