
import java.util.Scanner;




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
public class Simulator {
    
    static int numInBusses;
    static int numOutBusses;
    static int minGroupSize;
    static int maxGroupSize;
    static int capacity;
    static double arrivalProb;
    static int groupsServed;
    static int totalTimeWaited;
    static PassengerQueue[] busStops = new PassengerQueue[8]; // 0, 1, 2, 3, 4, 5, 6, 7
    static String inRoute[]  = {"South P", "West", "SAC", "Chapin"};
    static String outRoute[] = {"South P", "PathMart", "Walmart", "Target"};
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of In Route busses: ");
        numInBusses = input.nextInt();
        System.out.print("Enter the number of Out Route busses: ");
        numOutBusses = input.nextInt();
        System.out.print("Enter minimum group size of passengers: ");
        minGroupSize = input.nextInt();
        System.out.print("Enter maximum group size of passengers: ");
        maxGroupSize = input.nextInt();
        System.out.print("Enter the capacity of a bus: ");
        capacity = input.nextInt();
        System.out.print("Enter the arrival probability: ");
        arrivalProb = input.nextDouble();
        System.out.print("Enter the duration of the simulation: ");
        int duration = input.nextInt();
        System.out.println("Avg Wait Time: " + simulate(duration) + " Groups Served: " + groupsServed);
        
    }
    public static double simulate(int duration) { // return avg wait time and # groups served. use totalTimeWaited divided by groupsServed.
        groupsServed = 0; // reset
        totalTimeWaited = 0; // reset
        int currentTime = 1;
        //creating busses
        Bus[] inBusses = new Bus[numInBusses];
        Bus[] outBusses = new Bus[numOutBusses];
        for(int i = 0; i < numInBusses; i++) {
            inBusses[i] = new Bus(capacity, 1); // in route is 1
            inBusses[i].setTimeToRest((30 * i));
        }
        inBusses[0].setToNextStop(0);
        
        for (int i = 0; i < numOutBusses; i++) {
            outBusses[i] = new Bus(capacity, -1);
            outBusses[i].setTimeToRest(30 * i);
        }
        outBusses[0].setToNextStop(0);
        //generate bus stops
        for (int i = 0; i < 8; i++) {
            busStops[i] = new PassengerQueue();
        }
        while (currentTime <= duration) {
            System.out.println("Minute: " + currentTime);
            System.out.println();
            
            for (int i = 0; i < 8; i++) { // generate passengers for all bus stops if successful
                if (arrivalProb(arrivalProb) == true) { // group is generated successfully and arrives at the stop.
                    int groupSize = randInt(minGroupSize, maxGroupSize);
                    Passenger group = new Passenger(groupSize, 0, currentTime); // set destination and time arrived later. default is 0. 
                    if (i == 0 || i == 4) {
                        int randomDest = randInt(i + 1, i + 3);
                        group.setDestination(randomDest);
                        busStops[i].enqueuePassenger(group);
                    } else if (i < 4) {
                        int[] nums = {2, 3, 0}; // 0, 1, 2
                        int randomDest = 1;
                        if (i == 1) {
                            randomDest = nums[randInt(0, 2)];
                        } else if (i == 2) {
                            randomDest = nums[randInt(1, 2)];
                        } else if (i == 3) {
                            randomDest = 0;
                        }
                        group.setDestination(randomDest);
                        busStops[i].enqueuePassenger(group);
                    } else if (i > 4 && i < 8) {
                        int[] nums = {6, 7, 4}; // 0, 1, 2
                        int randomDest = 5;
                        if (i == 5) {
                            randomDest = nums[randInt(0, 2)];
                        } else if (i == 6) {
                            randomDest = nums[randInt(1, 2)];
                        } else if (i == 7) {
                            randomDest = 4;
                        }
                        group.setDestination(randomDest);
                        busStops[i].enqueuePassenger(group);
                    }
                    if (i < 4) {
                        System.out.println("A group of " + group.getSize() + " passengers arrived at " + inRoute[i] + " heading to " + group.getDestinationName());
                    } else {
                        System.out.println("A group of " + group.getSize() + " passengers arrived at " + outRoute[i - 4] + " heading to " + group.getDestinationName());
                    }
                }
            }
            //print bus statuses
            for (int i = 0; i < numInBusses; i++) { // getstatus method includes unloading. implement pick up here
                String status = "";
                if (inBusses[i].isOperating() == true && inBusses[i].getToNextStop() == 0) { //ARRIVAL
                status = "In Route Bus " + (i + 1) + " arrived at ";
            if (inBusses[i].getNextStop() == 3) { // arrived at this stop. busstop 3
                status += inBusses[i].getNextStopName();
                if (inBusses[i].getRoute() == 1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + inBusses[i].unload(inBusses[i].getNextStop()) + " passengers"; // unload
                }
                int s = 0;
                for (int j = 0; j < busStops[3].size(); j++) {
                    if (busStops[3].getPassenger(j).getDestination() == 0) {
                        Passenger temp = busStops[3].getPassenger(j);
                        if (inBusses[i].getPassengerQueue().getSize() + temp.getSize() <= inBusses[i].getCapacity()) {
                        s += temp.getSize();
                        inBusses[i].getPassengerQueue().enqueuePassenger(busStops[3].dequeue(j));
                        groupsServed++;
                        totalTimeWaited += currentTime - temp.getTimeArrivedAtStop();
                        }
                    }
                }
                status += " Picked up " + s + " passengers.";
                inBusses[i].setNextStop(0);
                inBusses[i].setToNextStop(19);
            } else if (inBusses[i].getNextStop() == 0) { // arrived at last stop reset rest timer back to 30, non operational or the beginning
                status += inBusses[i].getNextStopName();
                if (inBusses[i].getRoute() == 1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + inBusses[i].unload(inBusses[i].getNextStop()) + " passengers"; // unload
                }
                int s = 0;
                for (int j = 0; j < busStops[0].size(); j++) {
                    if (busStops[0].getPassenger(j).getDestination() > 0) {
                        Passenger temp = busStops[0].getPassenger(j);
                        if (inBusses[i].getPassengerQueue().getSize() + temp.getSize() <= inBusses[i].getCapacity()) {
                            s += temp.getSize();
                            inBusses[i].getPassengerQueue().enqueuePassenger(busStops[0].dequeue(j));
                            groupsServed++;
                            totalTimeWaited += currentTime - temp.getTimeArrivedAtStop();
                        }
                    }
                }
                // last stop unload all passengers
                status += " Picked up " + s + " passengers.";
                inBusses[i].setNextStop(1);
                inBusses[i].setToNextStop(19);
            } else { // arrived at some other stop.
                status += inBusses[i].getNextStopName();
                if (inBusses[i].getRoute() == 1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + inBusses[i].unload(inBusses[i].getNextStop()) + " passengers"; // unload
                }
                int s = 0;
                for (int j = 0; j < busStops[inBusses[i].getNextStop()].size(); j++) {
                    if (busStops[inBusses[i].getNextStop()].getPassenger(j).getDestination() >= 0) {
                        Passenger temp = busStops[inBusses[i].getNextStop()].getPassenger(j);
                        if (inBusses[i].getPassengerQueue().getSize() + temp.getSize() <= inBusses[i].getCapacity()) {
                        s += temp.getSize();
                        inBusses[i].getPassengerQueue().enqueuePassenger(busStops[inBusses[i].getNextStop()].dequeue(j));
                        groupsServed++;
                        totalTimeWaited += currentTime - temp.getTimeArrivedAtStop();
                        }
                    }
                }
                status += " Picked up " + s + " passengers.";
                inBusses[i].setNextStop(inBusses[i].getNextStop() + 1);
                inBusses[i].setToNextStop(19);
                
            }
            
        } else if (inBusses[i].isOperating() == true && inBusses[i].getToNextStop() > 0) { // operational and still traveling
            status = "In Route Bus " + (i + 1) +" moving towards " + inBusses[i].getNextStopName() + " . Arriving in " + inBusses[i].getToNextStop() + " minutes.";
        } else { // operational is false
            status = "In Route Bus " + (i + 1) + " resting at South P. for " + inBusses[i].getTimeToRest() + " minutes.";
        }
           System.out.println(status);     
            }
            
            for (int i = 0; i < numOutBusses; i++) {
                String status = "";
                if (outBusses[i].isOperating() == true && outBusses[i].getToNextStop() == 0) { //ARRIVAL
                status = "Out Route Bus " + (i + 1) + " arrived at ";
            if (outBusses[i].getNextStop() == 3) { // arrived at this stop
                status += outBusses[i].getNextStopName();
                if (outBusses[i].getRoute() == -1) { // outroute. unload parameters 0-3
                    status += " Dropped off " + outBusses[i].unload(outBusses[i].getNextStop()) + " passengers"; // unload
                }
                int s = 0;
                for (int j = 0; j < busStops[7].size(); j++) {
                    if (busStops[7].getPassenger(j).getDestination() == 4) {
                        Passenger temp = busStops[7].getPassenger(j);
                        if (outBusses[i].getPassengerQueue().getSize() + temp.getSize() <= outBusses[i].getCapacity()) {
                        s += temp.getSize();
                        outBusses[i].getPassengerQueue().enqueuePassenger(busStops[7].dequeue(j));
                        groupsServed++;
                        totalTimeWaited += currentTime - temp.getTimeArrivedAtStop();
                        }
                    }
                }
                status += " Picked up " + s + " passengers.";
                outBusses[i].setNextStop(0);
                outBusses[i].setToNextStop(20);
            } else if (outBusses[i].getNextStop() == 0) { // arrived at last stop reset rest timer back to 30, non operational
                status += outBusses[i].getNextStopName();
                if (outBusses[i].getRoute() == -1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + outBusses[i].unload(outBusses[i].getNextStop()) + " passengers"; // unload
                }
                int s = 0;
                for (int j = 0; j < busStops[4].size(); j++) {
                    if (busStops[4].getPassenger(j).getDestination() > 4) {
                        Passenger temp = busStops[4].getPassenger(j);
                        if (outBusses[i].getPassengerQueue().getSize() + temp.getSize() <= outBusses[i].getCapacity()) {
                            s += temp.getSize();
                            outBusses[i].getPassengerQueue().enqueuePassenger(busStops[4].dequeue(j));
                            groupsServed++;
                            totalTimeWaited += currentTime - temp.getTimeArrivedAtStop();
                        }
                    }
                }
                // last stop unload all passengers
                status += " Picked up " + s + " passengers.";
                outBusses[i].setNextStop(1);
                outBusses[i].setToNextStop(19);
            } else { // arrived at some other stop.
                status += outBusses[i].getNextStopName();
                if (outBusses[i].getRoute() == -1) { // inroute. unload parameters 0-3
                    status += " Dropped off " + outBusses[i].unload(outBusses[i].getNextStop()) + " passengers"; // unload
                }
                int s = 0;
                for (int j = 0; j < busStops[outBusses[i].getNextStop() + 4].size(); j++) {
                    if (busStops[outBusses[i].getNextStop() + 4].getPassenger(j).getDestination() >= 4) {
                        Passenger temp = busStops[outBusses[i].getNextStop() + 4].getPassenger(j);
                        if (outBusses[i].getPassengerQueue().getSize() + temp.getSize() <= outBusses[i].getCapacity()) {
                        s += temp.getSize();
                        outBusses[i].getPassengerQueue().enqueuePassenger(busStops[outBusses[i].getNextStop() + 4].dequeue(j));
                        groupsServed++;
                        totalTimeWaited += currentTime - temp.getTimeArrivedAtStop();
                        }
                    }
                }
                status += " Picked up " + s + " passengers.";
                outBusses[i].setNextStop(outBusses[i].getNextStop() + 1);
                outBusses[i].setToNextStop(19);
            }
            
        } else if (outBusses[i].isOperating() == true && outBusses[i].getToNextStop() > 0) { // operational and still traveling
            status = "Out Route Bus " + (i + 1) + " moving towards " + outBusses[i].getNextStopName() + " . Arriving in " + outBusses[i].getToNextStop() + " minutes.";
        } else { // operational is false
            status = "Out Route Bus " + (i + 1) + " resting at South P. for " + outBusses[i].getTimeToRest() + " minutes.";
        }
           System.out.println(status);
            }
            
            for (int i = 0; i < 4; i++) { // print stops 0-32
                System.out.println(i + " (" + inRoute[i] + ") " + busStops[i].toString());
            }
            for (int i = 4; i < 8; i++) { // print stops 4-7
                System.out.println(i + " (" + outRoute[i - 4] + ") " + busStops[i].toString());
            }
            for (int k = 0; k < inBusses.length; k++) {
                if (inBusses[k].getTimeToRest() > 0) { // non operational
                    inBusses[k].setTimeToRest(inBusses[k].getTimeToRest() - 1);
                } else { // operational
                    inBusses[k].setToNextStop(inBusses[k].getToNextStop() - 1);
                }
                
            }
            for (int k = 0; k < outBusses.length; k++) {
                if (outBusses[k].getTimeToRest() > 0) { // non operational
                    outBusses[k].setTimeToRest(outBusses[k].getTimeToRest() - 1);
                } else { // operational
                    outBusses[k].setToNextStop(outBusses[k].getToNextStop() - 1);
                }
                
            }
            currentTime++;
            System.out.println();
        }
        return ((double)totalTimeWaited/(double)groupsServed);
        
    }
    private static boolean arrivalProb(double d) { // use user input for probability
        double i = Math.random();
        if (i >= 0 && i <= d) { // true range
            return true;
        } else { // false range  
            return false;
        }
    }
    private static int randInt(int min, int max) { // used to generate random # of passengers depending on min and max group size
        int i = (int)((Math.random() * (max - min + 1)) + min);
        return i;
    }
    
    
    
}
