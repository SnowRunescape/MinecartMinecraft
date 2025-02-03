package br.com.minecart;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Cache
{
    private static final Map<String, CacheEntry> cache = new HashMap<>();

    public static final int TTL_MIN = 60;
    public static final int TTL_5_MIN = 5 * Cache.TTL_MIN;

    public static <T> T fetch(String key, Supplier<T> fetchFunction, int ttlInSeconds)
    {
        CacheEntry entry = cache.get(key);
        long currentTime = System.currentTimeMillis() / 1000;

        if (entry == null || entry.timestamp + ttlInSeconds < currentTime) {
            T value = fetchFunction.get();
            cache.put(key, new CacheEntry(value, currentTime));
            return value;
        }

        return (T) entry.value;
    }

    private static class CacheEntry
    {
        Object value;
        long timestamp;

        CacheEntry(Object value, long timestamp)
        {
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}