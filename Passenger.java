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
public class Passenger {
    
    
    private int size;
    private int destination;
    private int timeArrivedAtStop;
    private String[] destinations = {"South P", "West", "SAC", "Chapin", "South P", "PathMart", "Walmart", "Target"};
    
    public Passenger(int s, int d, int t) {
        size = s;
        destination = d;
        timeArrivedAtStop = t;
    }
    
    public int getSize() {
        return size;
    }
    public int getDestination() {
        return destination;
    }
    public String getDestinationName() {
        return destinations[destination];
    }
    public int getTimeArrivedAtStop() {
        return timeArrivedAtStop;
    }
    public void setSize(int i) {
        size = i;
    }
    public void setDestination(int i) {
        destination = i;
    }
    public void setTime(int i) {
        timeArrivedAtStop = i;
    }
    
}
