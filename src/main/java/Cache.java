import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {

    private static final int DEFAULT_VALUE = -1;

    private final Map<Integer, String> cacheMap = new LinkedHashMap<>();

    private int capacity;

    public Cache(int capacity) {
        changeCapacity(capacity);
    }

    public String get(int id) {
        return cacheMap.getOrDefault(id, String.valueOf(DEFAULT_VALUE));
    }

    public void put(int id, String value) {
        cacheMap.remove(id);
        cacheMap.put(id, value);
        removeLeastRecentlyUsedEntries();
        System.out.println(cacheMap.entrySet());
    }

    public void changeCapacity(int capacity) {
        this.capacity = capacity > 0 ? capacity : 0;
        removeLeastRecentlyUsedEntries();
    }

    public void invalidate() {
        cacheMap.clear();
    }

    private void removeLeastRecentlyUsedEntries() {
        while (isCacheOverflowed()) {
            final int index = cacheMap.keySet().iterator().next();
            cacheMap.remove(index);
        }
    }

    private boolean isCacheOverflowed() {
        return cacheMap.size() > capacity;
    }
}
