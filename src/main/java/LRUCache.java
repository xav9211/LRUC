import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@EnableAutoConfiguration
public class LRUCache {
    private static Cache cache;

    public static void main(String[] args) {
        SpringApplication.run(LRUCache.class, args);
        cache = new Cache(5);
    }

    @GetMapping(value = "/cache/{id}")
    public @ResponseBody String get(@PathVariable int id) {
        return cache.get(id);
    }

    @PostMapping(value="/cache/{id}")
    public @ResponseBody void put(@PathVariable int id, @RequestBody String value) {
        cache.put(id, value);
    }

    @PutMapping(value = "/cache/capacity/{capacity}")
    public @ResponseBody void changeCapacity(@PathVariable int capacity) {
        cache.changeCapacity(capacity);
    }

    @DeleteMapping(value = "/cache/invalidate")
    public @ResponseBody void invalidate() {
        cache.invalidate();
    }
}
