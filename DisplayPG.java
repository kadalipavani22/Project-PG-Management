import java.util.ArrayList;
import java.util.HashMap;

public class DisplayPG {
    private static final HashMap<String, ArrayList<String>> cityPGMap = new HashMap<>();

    public static void addPG(String city, String pgName) {
        cityPGMap.putIfAbsent(city, new ArrayList<>());
        cityPGMap.get(city).add(pgName);
    }

    public static ArrayList<String> getPGList(String city) {
        return cityPGMap.getOrDefault(city, new ArrayList<>());
    }
}
