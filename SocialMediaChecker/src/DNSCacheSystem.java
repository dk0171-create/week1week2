import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttl) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttl * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCacheSystem {

    private int MAX_CACHE_SIZE = 5;

    // LRU cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            };

    private int cacheHits = 0;
    private int cacheMisses = 0;

    // Resolve domain
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            cacheHits++;
            return "Cache HIT → " + entry.ipAddress;
        }

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
        }

        cacheMisses++;

        String ip = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 300);
        cache.put(domain, newEntry);

        return "Cache MISS → " + ip;
    }

    // Simulated upstream DNS lookup
    private String queryUpstreamDNS(String domain) {
        Random rand = new Random();
        return "172.217.14." + rand.nextInt(255);
    }

    // Remove expired entries
    public void cleanExpiredEntries() {
        Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, DNSEntry> entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {
        int total = cacheHits + cacheMisses;
        double hitRate = (total == 0) ? 0 : (cacheHits * 100.0 / total);

        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) {

        DNSCacheSystem dns = new DNSCacheSystem();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("facebook.com"));
        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}