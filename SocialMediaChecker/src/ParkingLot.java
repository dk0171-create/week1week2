import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        occupied = false;
    }
}

public class ParkingLot {

    static final int SIZE = 500;
    ParkingSpot[] spots = new ParkingSpot[SIZE];

    int occupiedCount = 0;
    int totalProbes = 0;
    int parkOperations = 0;

    public ParkingLot() {
        for (int i = 0; i < SIZE; i++) {
            spots[i] = new ParkingSpot();
        }
    }

    // Hash function
    int hash(String plate) {
        return Math.abs(plate.hashCode()) % SIZE;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (spots[index].occupied) {
            index = (index + 1) % SIZE; // linear probing
            probes++;
        }

        spots[index].licensePlate = plate;
        spots[index].entryTime = System.currentTimeMillis();
        spots[index].occupied = true;

        occupiedCount++;
        totalProbes += probes;
        parkOperations++;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (spots[index].occupied) {

            if (spots[index].licensePlate.equals(plate)) {

                long duration = System.currentTimeMillis() - spots[index].entryTime;

                double hours = duration / (1000.0 * 60 * 60);
                double fee = Math.ceil(hours * 5); // $5 per hour

                spots[index].occupied = false;
                occupiedCount--;

                System.out.println("exitVehicle(\"" + plate + "\") → Spot #" + index +
                        " freed, Duration: " + String.format("%.2f", hours) +
                        "h, Fee: $" + fee);

                return;
            }

            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found.");
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupiedCount * 100.0) / SIZE;

        double avgProbes = parkOperations == 0 ? 0 :
                (double) totalProbes / parkOperations;

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Average Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Total Vehicles Parked: " + parkOperations);
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}