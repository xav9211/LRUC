import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Cache {

    private static final Logger log = LoggerFactory.getLogger(Cache.class);

    private final Map<Integer, String> cacheMap = new LinkedHashMap<>();

    private int capacity;

    Cache(int capacity) {
        log.info("Cache initialized");
        changeCapacity(capacity);
    }

    String get(int key) {
        String lastGetValue;
        if (cacheMap.containsKey(key)) {
            log.info("Get value for key: {}", key);
            lastGetValue = cacheMap.get(key);
            putAsLastUsedItem(key, lastGetValue);
        } else {
            log.error("Value with key: {} does not exist. Error code -1.", key);
            throw new ItemNotFoundException(String.valueOf(key));
        }
        return lastGetValue;
    }

    void put(int key, String value) {
        putAsLastUsedItem(key, value);
        removeLeastRecentlyUsedEntries();
        log.info("Put new item to cache with key: {} and value size: {}", key, value.length());
    }

    void changeCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
            log.info("Capacity set to: {}", capacity);
        } else {
            log.error("Invalid capacity: {}. Capacity should be greater than 0", capacity);
            throw new IllegalArgumentException(String.format("Invalid capacity: %s. Capacity should be greater than 0", capacity));
        }
        removeLeastRecentlyUsedEntries();
    }

    void invalidate() {
        cacheMap.clear();
        log.info("Cleared whole cache memory");
    }

    private void removeLeastRecentlyUsedEntries() {
        int numberOfRemovedLRUEntries = 0;
        while (isCacheOverflowed()) {
            final int index = cacheMap.keySet().iterator().next();
            cacheMap.remove(index);
            numberOfRemovedLRUEntries++;
        }
        if (numberOfRemovedLRUEntries > 0) log.info("Removed {} least recently used items.", numberOfRemovedLRUEntries);
    }

    private boolean isCacheOverflowed() {
        return cacheMap.size() > capacity;
    }

    private void putAsLastUsedItem(int key, String value) {
        //Remove of existing item to put it on the end map
        cacheMap.remove(key);
        cacheMap.put(key, value);
    }
}
