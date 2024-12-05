package library.utils.BorrowingQue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BorrowingQue {

    // A map to track borrowing queues for each resource.
    // Key: Resource ID, Value: Queue of reservation IDs.
    static HashMap<String, Queue<String>> borrowQue = new HashMap<>();

    /**
     * Adds a reservation to the borrowing queue for a specific resource.
     * 
     * @param resourceId    The ID of the resource being reserved.
     * @param reservationId The ID of the reservation to be added to the queue.
     */
    public static void addToQue(String resourceId, String reservationId) {
        // Check if the resource already has a queue.
        if (borrowQue.containsKey(resourceId)) {
            // If a queue exists, add the reservation ID to it.
            borrowQue.get(resourceId).add(reservationId);
        } else {
            // If no queue exists, create a new queue and add the reservation ID.
            Queue<String> resourceQue = new LinkedList<>();
            resourceQue.add(reservationId);
            borrowQue.put(resourceId, resourceQue);
        }
    }

    /**
     * Retrieves and removes the first reservation in the queue for a specific resource.
     * 
     * @param resourceId The ID of the resource whose queue is being accessed.
     * @return The ID of the first reservation in the queue, or null if the queue is empty or doesn't exist.
     */
    public static String getFirstPerson(String resourceId) {
        // Check if the resource has an existing queue.
        if (!borrowQue.containsKey(resourceId)) {
            return null; // No queue exists for this resource.
        } else {
            // Retrieve the first element in the queue and remove it if it exists.
            return borrowQue.get(resourceId).peek() != null ? borrowQue.get(resourceId).remove() : null;
        }
    }
}
