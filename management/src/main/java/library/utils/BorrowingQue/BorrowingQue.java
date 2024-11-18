
package library.utils.BorrowingQue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import library.model.Users.*;;;

public class BorrowingQue {

     static HashMap<String, Queue<Patron>> borrowQue = new HashMap<>(); // resource and que

    //check if resource is given
    public static void addToQue(String resourceId, Patron patron) {
        //check if the libraryry resource has poeple already queing
        if(borrowQue.containsKey(resourceId)) {
            borrowQue.get(resourceId).add(patron);
        } else {
            //form a que and add the patron
            Queue<Patron> resourceQue = new LinkedList<>();
            resourceQue.add(patron);
            borrowQue.put(resourceId, resourceQue);
        }
    }

    public static Patron getFirstPerson(String resourceId) {
        if(!borrowQue.containsKey(resourceId))
            return null;
        else {
            //check if there is an element remove the element and return else return null
            return borrowQue.get(resourceId).peek() != null ? borrowQue.get(resourceId).remove() : null;
        }
    }


}