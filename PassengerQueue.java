
import java.util.LinkedList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Colin Ruan
 * SBU ID # 111705515
 * Recitation 05
 */
public class PassengerQueue extends LinkedList {
    
    
    public PassengerQueue() {
        
    }
    
    public void enqueuePassenger(Passenger p) {
        this.add((Passenger)p);
    }
    public Passenger dequeue(int i) { // should only dequeue if criteria is met.
         return ((Passenger)this.remove(i));// first in first out. remove already removes the head    
    }
    
    @Override
    public Passenger peek() {
        return this.peek();
    }
    public Passenger getPassenger(int i) {
        return (Passenger)this.get(i);
    }
    
    public int getSize() { // size of individual passengers not groups
        int s = 0;
        for (int i = 0; i < this.size(); i++) {
            Passenger p = (Passenger)this.get(i);
            s += p.getSize();
        }
        return s;
    }
    
    
    @Override
    public String toString() {
        String s = "";
        if (!this.isEmpty()) {
        for (int i = 0; i < this.size(); i++) {
            s += "[" + this.getPassenger(i).getSize() + ", " + this.getPassenger(i).getDestination() + " (" + this.getPassenger(i).getDestinationName() + "), " + this.getPassenger(i).getTimeArrivedAtStop() + "]" + " "; 
        }
        }
        return s;
    }
    
        
}
