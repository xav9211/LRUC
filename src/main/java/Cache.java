import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {

    private static final int DEFAULT_VALUE = -1;

    private final Map<Integer, Object> cacheMap = new LinkedHashMap<>();

    private int capacity;

    public Cache(int capacity) {
        changeCapacity(capacity);
        cacheMap.put(1, "HEJ");
    }

    public Object get(int id) {
        return cacheMap.getOrDefault(id, DEFAULT_VALUE);
    }

    public void put(int id, Object value) {
        cacheMap.put(id, value);
        System.out.println(cacheMap.entrySet());
    }

    public void changeCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void invalidate() {
        cacheMap.clear();
    }
}
