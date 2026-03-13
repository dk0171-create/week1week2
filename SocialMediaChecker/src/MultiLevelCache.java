import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String id, String content) {
        this.videoId = id;
        this.content = content;
    }
}

public class MultiLevelCache {

    int L1_SIZE = 10000;
    int L2_SIZE = 100000;

    // L1 Cache (LRU)
    LinkedHashMap<String, VideoData> L1 =
            new LinkedHashMap<String, VideoData>(16,0.75f,true){
                protected boolean removeEldestEntry(Map.Entry<String,VideoData> e){
                    return size() > L1_SIZE;
                }
            };

    // L2 Cache (LRU)
    LinkedHashMap<String, VideoData> L2 =
            new LinkedHashMap<String, VideoData>(16,0.75f,true){
                protected boolean removeEldestEntry(Map.Entry<String,VideoData> e){
                    return size() > L2_SIZE;
                }
            };

    // L3 Database (all videos)
    HashMap<String, VideoData> database = new HashMap<>();

    int L1Hits=0, L2Hits=0, L3Hits=0;

    // Get video
    public VideoData getVideo(String videoId){

        // L1 check
        if(L1.containsKey(videoId)){
            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 check
        if(L2.containsKey(videoId)){
            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");
            VideoData video=L2.get(videoId);

            // promote to L1
            L1.put(videoId,video);
            System.out.println("Promoted to L1");

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 database
        if(database.containsKey(videoId)){
            L3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            VideoData video=database.get(videoId);

            // add to L2
            L2.put(videoId,video);

            return video;
        }

        System.out.println("Video not found");
        return null;
    }

    // Add video to database
    public void addVideo(String id,String content){
        database.put(id,new VideoData(id,content));
    }

    // Cache statistics
    public void getStatistics(){

        int total=L1Hits+L2Hits+L3Hits;

        double l1Rate=(L1Hits*100.0)/total;
        double l2Rate=(L2Hits*100.0)/total;
        double l3Rate=(L3Hits*100.0)/total;

        System.out.println("L1 Hit Rate: "+String.format("%.2f",l1Rate)+"%");
        System.out.println("L2 Hit Rate: "+String.format("%.2f",l2Rate)+"%");
        System.out.println("L3 Hit Rate: "+String.format("%.2f",l3Rate)+"%");
    }

    public static void main(String[] args){

        MultiLevelCache cache=new MultiLevelCache();

        cache.addVideo("video_123","Movie Data");
        cache.addVideo("video_999","Sports Video");

        cache.getVideo("video_123");
        System.out.println();

        cache.getVideo("video_123");
        System.out.println();

        cache.getVideo("video_999");
        System.out.println();

        cache.getStatistics();
    }
}