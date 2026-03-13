import java.util.*;

public class AutocompleteSystem {

    // query -> frequency
    HashMap<String, Integer> queryFrequency = new HashMap<>();

    // Add or update query
    public void updateFrequency(String query) {
        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);
    }

    // Search suggestions for prefix
    public void search(String prefix) {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {

            if (entry.getKey().startsWith(prefix)) {

                pq.offer(entry);

                if (pq.size() > 10) {
                    pq.poll();
                }
            }
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        Collections.reverse(result);

        int rank = 1;

        for (Map.Entry<String, Integer> entry : result) {
            System.out.println(rank + ". " + entry.getKey() +
                    " (" + entry.getValue() + " searches)");
            rank++;
        }
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println("Search results for prefix 'jav':");
        system.search("jav");
    }
}