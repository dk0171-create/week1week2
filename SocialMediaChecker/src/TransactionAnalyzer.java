import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    long time;
    String account;

    Transaction(int id, int amount, String merchant, long time, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
        this.account = account;
    }
}

public class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println("TwoSum → (" + other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two Sum within 1 hour window
    public void findTwoSumTimeWindow(int target, long windowMillis) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= windowMillis) {
                    System.out.println("TwoSumTimeWindow → (" + other.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate → " + key + " Accounts: ");

                for (Transaction t : list) {
                    System.out.print(t.account + " ");
                }

                System.out.println();
            }
        }
    }

    // K-Sum
    public void findKSum(int k, int target) {

        List<Integer> nums = new ArrayList<>();

        for (Transaction t : transactions) {
            nums.add(t.amount);
        }

        kSum(nums, k, target, 0, new ArrayList<>());
    }

    void kSum(List<Integer> nums, int k, int target, int start, List<Integer> path) {

        if (k == 0 && target == 0) {
            System.out.println("KSum → " + path);
            return;
        }

        if (k == 0) return;

        for (int i = start; i < nums.size(); i++) {

            path.add(nums.get(i));

            kSum(nums, k - 1, target - nums.get(i), i + 1, path);

            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer system = new TransactionAnalyzer();

        long baseTime = System.currentTimeMillis();

        system.addTransaction(new Transaction(1, 500, "StoreA", baseTime, "acc1"));
        system.addTransaction(new Transaction(2, 300, "StoreB", baseTime + 1000, "acc2"));
        system.addTransaction(new Transaction(3, 200, "StoreC", baseTime + 2000, "acc3"));
        system.addTransaction(new Transaction(4, 500, "StoreA", baseTime + 3000, "acc4"));

        system.findTwoSum(500);

        system.findTwoSumTimeWindow(500, 3600000);

        system.detectDuplicates();

        system.findKSum(3, 1000);
    }
}