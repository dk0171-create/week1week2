import java.util.*;

class UsernameChecker {

    HashMap<String, Integer> users = new HashMap<>();
    HashMap<String, Integer> attempts = new HashMap<>();

    // Check availability
    public boolean checkAvailability(String username) {

        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        if(users.containsKey(username)) {
            return false;
        }

        return true;
    }

    // Add new user
    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for(int i = 1; i <= 5; i++) {
            String newName = username + i;
            if(!users.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        String modified = username.replace("_", ".");
        if(!users.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String maxUser = "";
        int max = 0;

        for(String user : attempts.keySet()) {
            if(attempts.get(user) > max) {
                max = attempts.get(user);
                maxUser = user;
            }
        }

        return maxUser + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        system.registerUser("john_doe", 101);
        system.registerUser("admin", 102);

        System.out.println(system.checkAvailability("john_doe"));
        System.out.println(system.checkAvailability("jane_smith"));

        System.out.println(system.suggestAlternatives("john_doe"));

        System.out.println(system.getMostAttempted());
    }
}
