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
public class Bus {
    
    public static int MAX_CAPACITY;
    String inRoute[]  = {"South P", "West", "SAC", "Chapin"}; // 0, 1, 2, 3
    String outRoute[] = {"South P", "PathMart", "Walmart", "Target"}; // 0, 1, 2, 3 for index, but stop numbers are 4, 5, 6, 7
    private int route; // 1 is in, -1 is out
    private int nextStop = 0; // stores name use the string array
    private int toNextStop = 20;// stores time to next stop
    private int timeToRest;
    private PassengerQueue passengerQueue;
    private String status;
    private boolean arrived = false;
    
    public Bus(int c, int r) {
        MAX_CAPACITY = c;
        route = r;
        passengerQueue = new PassengerQueue();
    }
    
    public int getRoute() {
        return route;
    }
    public int getCapacity() {
        return MAX_CAPACITY;
    }
    public int getNextStop() { // index of stop depending on route
        return nextStop;
    }
    public int getToNextStop() {
        return toNextStop;
    }
    public int getTimeToRest() {
        return timeToRest;
    }
    public void setTimeToRest(int i) {
        timeToRest = i;
    }
    public String getNextStopName() {
        if (getRoute() == 1) { // in route names
            return inRoute[getNextStop()];
        } else {
            return outRoute[getNextStop()];
        }
    }
    /*public String getStatus() {
        if (isOperating() == true && getToNextStop() == 0) { //ARRIVAL
            status = "arrived at ";
            arrived = true;
            if (getNextStop() == 3) { // arrived at this stop
                status += getNextStopName();
                if (getRoute() == 1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + unload(getNextStop()) + " passengers"; // unload
                } else { // outroute. unload parameters 4-7. just add 4
                    status += " Dropped off " + unload(getNextStop() + 4) + " passengers"; // unload
                }
                setNextStop(0);
                setToNextStop(20);
            } else if (getNextStop() == 0) { // arrived at last stop reset rest timer back to 30, non operational
                status += getNextStopName();
                if (getRoute() == 1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + unload(getNextStop()) + " passengers"; // unload
                } else { // outroute. unload parameters 4-7. just add 4
                    status += " Dropped off " + unload(getNextStop() + 4) + " passengers"; // unload
                }
                setNextStop(1);
                setTimeToRest(30);
            } else { // arrived at some other stop.
                status += getNextStopName();
                if (getRoute() == 1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + unload(getNextStop()) + " passengers"; // unload
                } else { // outroute. unload parameters 4-7. just add 4
                    status += " Dropped off " + unload(getNextStop() + 4) + " passengers"; // unload
                }
                setNextStop(getNextStop() + 1);
                setToNextStop(20);
            }
            return status;
        } else if (isOperating() == true && getToNextStop() > 0) { // operational and still traveling
            status = "moving towards " + getNextStopName() + " . Arriving in " + getToNextStop() + " minutes.";
            return status;
        } else { // operational is false
            status = "resting at South P. for " + getTimeToRest() + " minutes.";
            return status;
        }
    }
     */
    public void setNextStop(int i) {
        nextStop = i;
    }
    public void setToNextStop(int i) {
        toNextStop = i;
    }
    public boolean isOperating() {
        if (getTimeToRest() == 0) {
           return true; 
        } else {
            return false;
        }
    }
    public void setRoute(int i) {
        route = i;
    }
    public PassengerQueue getPassengerQueue() {
        return passengerQueue;
    }
    
    public int unload(int dest) {
        int counter = 0;
        for (int i = 0; i < getPassengerQueue().size(); i++) {
            if (getPassengerQueue().getPassenger(i).getDestination() == dest) {
                counter += getPassengerQueue().getPassenger(i).getSize(); // number of individual passengers removed. not group
                getPassengerQueue().remove(i);
            }
        }
        return counter;
    }
}
