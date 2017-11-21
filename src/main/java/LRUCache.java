import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@EnableAutoConfiguration
@PropertySource("classpath:/config/config.properties")
public class LRUCache {

    private Cache cache;

    public LRUCache(@Value("${capacity}") int initialCapacity) {
        cache = new Cache(initialCapacity);
    }

    public static void main(String[] args) {
        SpringApplication.run(LRUCache.class, args);
    }

    @GetMapping(value = "/cache/{key}")
    public @ResponseBody String get(@PathVariable int key) {
        return cache.get(key);
    }

    @PostMapping(value="/cache/{key}")
    public @ResponseBody void put(@PathVariable int key, @RequestBody String value) {
        cache.put(key, value);
    }

    @PutMapping(value = "/cache/capacity/{capacity}")
    public @ResponseBody void changeCapacity(@PathVariable int capacity) {
        cache.changeCapacity(capacity);
    }

    @DeleteMapping(value = "/cache")
    public @ResponseBody void invalidate() {
        cache.invalidate();
    }

    @ExceptionHandler
    public @ResponseBody String handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return e.getMessage();
    }

    @ExceptionHandler
    public @ResponseBody int handleItemNotFoundException(ItemNotFoundException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return -1;
    }
}
