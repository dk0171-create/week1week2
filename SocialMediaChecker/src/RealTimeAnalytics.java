import java.util.*;

class RealTimeAnalytics {

    // pageUrl -> visit count
    HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // source -> visit count
    HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Track traffic source
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("Top Pages:");

        // Sort pages by visit count
        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        int count = 0;

        for (Map.Entry<String, Integer> entry : list) {

            if (count == 10) break;

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((count + 1) + ". " + page +
                    " - " + views + " views (" + unique + " unique)");

            count++;
        }

        System.out.println("\nTraffic Sources:");

        for (String source : trafficSources.keySet()) {
            System.out.println(source + " : " + trafficSources.get(source));
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_111", "google");
        analytics.processEvent("/sports/championship", "user_222", "direct");
        analytics.processEvent("/article/breaking-news", "user_789", "google");

        analytics.getDashboard();
    }
}