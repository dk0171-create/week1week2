import java.util.*;

class TokenBucket {

    int maxTokens;
    double tokens;
    double refillRate; // tokens per second
    long lastRefillTime;

    TokenBucket(int maxTokens, int refillPerHour) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillPerHour / 3600.0;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on time passed
    void refill() {
        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        double refillTokens = seconds * refillRate;

        tokens = Math.min(maxTokens, tokens + refillTokens);

        lastRefillTime = now;
    }

    // Check if request allowed
    synchronized boolean allowRequest() {

        refill();

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }

        return false;
    }

    int remainingTokens() {
        return (int) tokens;
    }
}

public class RateLimiter {

    // clientId -> TokenBucket
    HashMap<String, TokenBucket> clients = new HashMap<>();

    int LIMIT = 1000;

    // Check rate limit
    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT, LIMIT));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.remainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining)";
        }
    }

    // Status info
    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = LIMIT - bucket.remainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + LIMIT +
                ", remaining: " + bucket.remainingTokens() + "}");
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        limiter.getRateLimitStatus("abc123");
    }
}