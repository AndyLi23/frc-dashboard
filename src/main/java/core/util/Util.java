package core.util;

import java.util.ArrayList;

public class Util {
    public static int search(ArrayList<Pair> array, double value, boolean greater) {
        int start = 0, end = array.size() - 1;

        int ans = -1;
        while (start <= end) {
            int mid = (start + end) / 2;

            if (greater) {
                if (array.get(mid).getKey() < value) {
                    start = mid + 1;
                } else {
                    ans = mid;
                    end = mid - 1;
                }
            } else {
                if (array.get(mid).getKey() > value) {
                    end = mid - 1;
                } else {
                    ans = mid;
                    start = mid + 1;
                }
            }
        }
        if (ans == -1) return start >= array.size() ? array.size() - 1 : 0;
        return ans;
    }
}
