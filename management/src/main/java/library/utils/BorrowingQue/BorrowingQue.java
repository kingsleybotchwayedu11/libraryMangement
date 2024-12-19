package library.utils.BorrowingQue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BorrowingQue {


    public static HashMap<String, Queue<String>> borrowQue = new HashMap<>();

   
    public static boolean addToQue(String resourceId, String reservationId) {
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
        return true;
    }

   
    public static String getFirstPerson(String resourceId) {
        // Check if the resource has an existing queue.
        if (!borrowQue.containsKey(resourceId)) {
            return null; 
        } else {
            return borrowQue.get(resourceId).peek() != null ? borrowQue.get(resourceId).remove() : null;
        }
    }
}
