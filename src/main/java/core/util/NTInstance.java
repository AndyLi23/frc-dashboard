package core.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTInstance {
    public static NTInstance instance;

    private NetworkTableInstance nt;
    private NetworkTable table;

    public static NTInstance getInstance() {
        if (instance == null) instance = new NTInstance();
        return instance;
    }

    public void initNT() {
        nt = NetworkTableInstance.getDefault();
        nt.startClientTeam(1351);
        nt.startDSClient();

        table = nt.getTable("dashboard");
    }

    public void stopNT() {
        nt.stopDSClient();
        nt.stopClient();
    }

    public NetworkTable getDashboardTable() {
        return table;
    }

}
