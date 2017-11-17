import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@EnableAutoConfiguration
public class LRUCache {

    private static final Logger log = LoggerFactory.getLogger(Cache.class);

    private Cache cache;

    //Used for integration tests
    private static int capacity = 5;

    public LRUCache() {
        cache = new Cache(capacity);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Wrong number of parameters. Correct use: java -jar <jarFile.jar> <initialCacheCapacity>");
            System.exit(1);
        }
        capacity = Integer.parseInt(args[0]);
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
