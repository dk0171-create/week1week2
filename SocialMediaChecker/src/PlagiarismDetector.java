import java.util.*;

public class PlagiarismDetector {

    // n-gram size
    static int N = 5;

    // ngram -> set of document IDs
    HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    // document -> its ngrams
    HashMap<String, List<String>> documentNgrams = new HashMap<>();

    // Add document to system
    public void addDocument(String docId, String text) {

        String[] words = text.toLowerCase().split("\\s+");
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }

            String ngram = sb.toString().trim();
            ngrams.add(ngram);

            ngramIndex.putIfAbsent(ngram, new HashSet<>());
            ngramIndex.get(ngram).add(docId);
        }

        documentNgrams.put(docId, ngrams);
    }

    // Analyze document similarity
    public void analyzeDocument(String docId) {

        List<String> ngrams = documentNgrams.get(docId);

        if (ngrams == null) {
            System.out.println("Document not found.");
            return;
        }

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String ngram : ngrams) {

            Set<String> docs = ngramIndex.get(ngram);

            if (docs != null) {
                for (String d : docs) {

                    if (!d.equals(docId)) {
                        matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                    }
                }
            }
        }

        int totalNgrams = ngrams.size();

        System.out.println("Extracted " + totalNgrams + " n-grams");

        for (String otherDoc : matchCount.keySet()) {

            int matches = matchCount.get(otherDoc);

            double similarity = (matches * 100.0) / totalNgrams;

            System.out.println("Found " + matches +
                    " matching n-grams with " + otherDoc);

            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            }
            else if (similarity > 10) {
                System.out.println("Suspicious similarity");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "machine learning is a field of artificial intelligence that allows systems to learn from data";
        String essay2 = "machine learning is a field of artificial intelligence where systems learn from data automatically";
        String essay3 = "data science uses statistics and programming to analyze data and build predictive models";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay3);

        detector.analyzeDocument("essay_123.txt");
    }
}