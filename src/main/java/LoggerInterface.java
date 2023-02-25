//import edu.wpi.first.networktables.NetworkTable;
//import edu.wpi.first.networktables.NetworkTableInstance;
//import edu.wpi.first.networktables.NetworkTableValue;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class LoggerInterface {
//    private final NetworkTableInstance nt;
//    private final NetworkTable table;
//
//    public LoggerInterface() {
//        nt = NetworkTableInstance.getDefault();
//        nt.setServerTeam(1351);
//        nt.startDSClient();
//
//        table = nt.getTable("Log");
//    }
//
//    public void put(String key, Object value) {
//        if (table.getValue(key) == null || table.getValue(key).getStringArray() == null) {
//            table.putValue(key, NetworkTableValue.makeStringArray(new String[]{System.currentTimeMillis() + "|" + value}));
//        } else {
//            ArrayList<String> s = new ArrayList<>(List.of(table.getValue(key).getStringArray()));
//            s.add(System.currentTimeMillis() + "|" + value);
//            table.putValue(key, NetworkTableValue.makeStringArray((String[]) new ArrayList<String>().toArray()));
//        }
//    }
//}
