import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LRUCache.class)
@WebAppConfiguration
public class LRUCacheTests {

    private final MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(), MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8"));

    private final Map<Integer, String> cacheMap = new LinkedHashMap<>();

    private final int firstId = 1;

    private final int secondId = 2;

    private final int thirdId = 3;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        cacheMap.put(firstId, "test1");
        cacheMap.put(secondId, "test2");
        cacheMap.put(thirdId, "test3");
    }

    @Test
    public void putItemWithIllegalKey() throws Exception {
        final long key = 3000000000L;
        final String stringKey = "abc";

        mockMvc.perform(post("/cache/" + key)).andExpect(status().isBadRequest());
        mockMvc.perform(post("/cache/" + stringKey)).andExpect(status().isBadRequest());
    }

    @Test
    public void getItemWithIllegalKey() throws Exception {
        final long key = 3000000000L;
        final String stringKey = "abc";

        mockMvc.perform(get("/cache/" + key)).andExpect(status().isBadRequest());
        mockMvc.perform(get("/cache/" + stringKey)).andExpect(status().isBadRequest());
    }

    @Test
    public void putAndGetItem() throws Exception {
        mockMvc.perform(post("/cache/1").content(cacheMap.get(1)).contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cache/1").accept(contentType)).andExpect(status().isOk())
            .andExpect(content().string(cacheMap.get(1)));
    }

    @Test
    public void putItemsMoreThanCapacityAllows() throws Exception {
        mockMvc.perform(put("/cache/capacity/1")).andExpect(status().isOk());

        putItems();

        mockMvc.perform(get("/cache/" + firstId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/cache/" + secondId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/cache/" + thirdId).accept(contentType)).andExpect(status().isOk())
                .andExpect(content().string(cacheMap.get(thirdId)));
    }

    @Test
    public void changeCapacityToLessThanZero() throws Exception {
        mockMvc.perform(put("/cache/capacity/-1")).andExpect(status().isBadRequest());
    }

    @Test
    public void invalidate() throws Exception {
        putItems();

        mockMvc.perform(get("/cache/" + firstId).accept(contentType)).andExpect(status().isOk())
                .andExpect(content().string(cacheMap.get(firstId)));
        mockMvc.perform(get("/cache/" + secondId).accept(contentType)).andExpect(status().isOk())
                .andExpect(content().string(cacheMap.get(secondId)));
        mockMvc.perform(get("/cache/" + thirdId).accept(contentType)).andExpect(status().isOk())
                .andExpect(content().string(cacheMap.get(thirdId)));

        mockMvc.perform(delete("/cache/invalidate")).andExpect(status().isOk());

        mockMvc.perform(get("/cache/" + firstId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/cache/" + secondId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/cache/" + thirdId)).andExpect(status().isNotFound());
    }

    private void putItems() throws Exception {
        mockMvc.perform(post("/cache/" + firstId).content(cacheMap.get(firstId)).contentType(contentType))
                .andExpect(status().isOk());
        mockMvc.perform(post("/cache/" + secondId).content(cacheMap.get(secondId)).contentType(contentType))
                .andExpect(status().isOk());
        mockMvc.perform(post("/cache/" + thirdId).content(cacheMap.get(thirdId)).contentType(contentType))
                .andExpect(status().isOk());
    }
}
