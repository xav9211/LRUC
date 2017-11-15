import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CacheTests {

    private final Cache cache = new Cache(3);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp(){
        cache.put(3, "test1");
        cache.put(1, "test2");
        cache.put(2, "test3");
    }

    @Test(expected = ItemNotFoundException.class)
    public void initializationCacheWithCapacity() {
        cache.put(4, "test5");

        //Shouldn't contain any element least recently used when size is greater than capacity
        cache.get(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeCapacityToLessThanZero() {
        cache.changeCapacity(-1);
    }

    @Test
    public void changeCapacityToLessThanCacheAlreadyHas() {
        final String testValue = "testValue";
        cache.put(9, testValue);
        cache.changeCapacity(1);

        Assert.assertEquals("Should be only last added Item", testValue, cache.get(9));
        exception.expect(ItemNotFoundException.class);
        cache.get(2);
    }

    @Test(expected = ItemNotFoundException.class)
    public void invalidate() {
        cache.invalidate();

        //Shouldn't contain any item
        cache.get(2);
    }

    @Test
    public void getValues() {
        Assert.assertEquals("test2", cache.get(1));
        Assert.assertEquals("test3", cache.get(2));
        Assert.assertEquals("test1", cache.get(3));
    }
}
