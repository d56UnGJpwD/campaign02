package edu.isu.cs.cs3308;

import edu.isu.cs.cs3308.structures.Queue;
import edu.isu.cs.cs3308.structures.impl.LinkedQueue;
import java.util.Random;

/**
 * Class representing a wait time simulation program.
 *
 * @author Isaac Griffith
 * @author
 */
public class Simulation {

    private int arrivalRate;
    private int maxNumQueues;
    private Random r;
    private int numIterations = 50;
    // You will probably need more fields

    /**
     * Constructs a new simulation with the given arrival rate and maximum number of queues. The Random
     * number generator is seeded with the current time. This defaults to using 50 iterations.
     *
     * @param arrivalRate the integer rate representing the maximum number of new people to arrive each minute
     * @param maxNumQueues the maximum number of lines that are open
     */
    public Simulation(int arrivalRate, int maxNumQueues) {
        this.arrivalRate = arrivalRate;

        this.maxNumQueues = maxNumQueues;
        r = new Random();
    }

    /**
     * Constructs a new simulation with the given arrival rate and maximum number of queues. The Random
     * number generator is seeded with the provided seed value, and the number of iterations is set to
     * the provided value.
     *
     * @param arrivalRate the integer rate representing the maximum number of new people to arrive each minute
     * @param maxNumQueues the maximum number of lines that are open
     * @param numIterations the number of iterations used to improve data
     * @param seed the initial seed value for the random number generator
     */
    public Simulation(int arrivalRate, int maxNumQueues, int numIterations, int seed) {
        this(arrivalRate, maxNumQueues);
        r = new Random(seed);
        this.numIterations = numIterations;
    }

    /**
     * Executes the Simulation
     */
    public void runSimulation() {

        //runs the simulation for increasing lines up to the max number of lines

        for(int linesOpen = 1; linesOpen <= maxNumQueues; linesOpen++)
        {
            //average wait time total for each sim needs to be reset between sim
            double aveTotalWaitTime = 0.0;

            //runs the simulation a set number of times
            for(int runs = 0; runs < numIterations; runs++)
            {
                //because I am going to be finding averages, I need to use doubles and then just
                //have it truncate when I convert them to ints

                //total people processed
                Double peopleProcessed = 0.0;
                //time taken for all those people
                Double totalTimeTaken = 0.0;
                //average wait time of each run of the sim
                Double aveRunWaitTime;

                //I used a linked queue array to hold all of my individual lines
                LinkedQueue[] arrLines = new LinkedQueue[linesOpen];

                //creates a new line to represent every open line
                for(int count = 0; count < linesOpen; count++)
                {
                    arrLines[count] = new LinkedQueue<Double>();
                }

                //for loop for keeping track of time
                int timer = 0;
                while(timer < 720)
                {
                    //people arriving each minute
                    int arriving = getRandomNumPeople(arrivalRate);

                    //if only 1 line
                    if(linesOpen == 1){

                        //offer each person to our linked queue, the value is their time spent waiting
                        for(int peopleToAdd = arriving; peopleToAdd > 0; peopleToAdd--)
                        {
                            arrLines[0].offer(0.0);
                        }
                    }
                    //if more than 1 line, check for shortest line
                    else
                    {
                        for(int peopleToAdd = arriving; peopleToAdd > 0; peopleToAdd--)
                        {
                            //variables for tracking the shortest line and our current position
                            int currentPos = 0;
                            int shortestLine = 1;

                            while(shortestLine < arrLines.length)
                            {
                                //set our position to the shortest line
                                if (arrLines[currentPos].size() > arrLines[shortestLine].size())
                                {
                                    currentPos = shortestLine;
                                }
                                //check the next line
                                else
                                {
                                    shortestLine++;
                                }
                            }
                            //after finding the shortest line, we add our person to that line
                            arrLines[currentPos].offer(0.0);
                        }
                    }

                    //this removes our people every minute
                    for (int i = 0; i < arrLines.length; i++)
                    {
                        //removes the first two people from a line as long as the line has people in it.  Adds to the totalPeople and totalWaitTime
                        for (int removal = 0; removal < 2; removal++)
                        {
                            //this handles all removes, even if the amount of people in the line are < 2
                            if (arrLines[i].peek() != null)
                            {
                                //increase time taken, and people people processed tracker
                                totalTimeTaken += (Double)arrLines[i].poll();
                                peopleProcessed += 1.0;
                            }
                        }
                        //adds 1 minute to every polled person in the tracker
                        int j = 0;
                        while(j < arrLines[i].size())
                        {
                            arrLines[i].offer((Double)arrLines[i].poll() + 1.0);
                            j++;
                        }
                    }
                    timer++;
                }
                //calculates the average run time and then adds it to the total wait time
                aveRunWaitTime = totalTimeTaken/peopleProcessed;
                aveTotalWaitTime += aveRunWaitTime;
            }
            //finds the average of all the runs;
            aveTotalWaitTime = aveTotalWaitTime/numIterations;
            System.out.println("Average wait time using " + linesOpen + " queue(s): " + (int)aveTotalWaitTime);
        }
    }

    /**
     * returns a number of people based on the provided average
     *
     * @param avg The average number of people to generate
     * @return An integer representing the number of people generated this minute
     */
    //Don't change this method.
    private static int getRandomNumPeople(double avg) {
        Random r = new Random();
        double L = Math.exp(-avg);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }
}