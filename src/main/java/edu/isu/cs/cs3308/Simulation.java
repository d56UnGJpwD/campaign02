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
    //added the timer, current time tracker, and the average wait time
    private int timer = 720;
    private int trackTime = 0;


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
    public void runSimulation()
    {

        //one loop
        //make a queue
        //add a random number of people to it
        //add 1 min
        //first group done

        //add another random number of people
        //if total amount in line is >= 2 remove 2 from queue for every person that got added
        //add 1 min
        //repeat

        //System.out.println("arrivalRate: " + arrivalRate);

        for(int lineCount = 1; lineCount <= maxNumQueues; lineCount++)
        {
            //creates a wait time for each line
            double aveWaitTime = 0;

            for(int run = 0; run < numIterations; run++)
            {
                int runTime = 0;
                double runWaitTimeAve = 0;
                int peopleProcessed = 0;

                LinkedQueue[] linesArr = new LinkedQueue[lineCount];

                for(int j = 0; j < lineCount; j++)
                {
                    linesArr[j] = new LinkedQueue<Double>();
                }

                while(trackTime < timer)
                {
                    int people = getRandomNumPeople(arrivalRate);

                    if(lineCount == 1)
                    {
                        for(int x = people; x > 0; x--)
                        {
                            linesArr[0].offer(0);
                        }
                    }
                    else
                    {
                        for(int x = people; x > 0; x--)
                        {
                            //how the person picks which line to go into
                            //shortest = index of shortest array position is current array
                            int shortest = 0;
                            int currPosition = 1;

                            while(currPosition < linesArr.length)
                            {
                                if(linesArr[shortest].size() > linesArr[currPosition].size())
                                {
                                    //set curret position to shortest
                                    shortest = currPosition;
                                }
                                else
                                {
                                    //move over 1 array
                                    currPosition++;
                                }
                            }
                            //add person to the shortest line
                            linesArr[shortest].offer(0);
                        }
                    }

                    for(int i = 0; i < linesArr.length; i++)
                    {
                        //remove the first two people if amount of people > 2
                        //if less < 2 then remove all remaining
                        if(linesArr[i].size() >= 2)
                        {

                            runTime += (int)linesArr[i].poll();
                            runTime += (int)linesArr[i].poll();
                            peopleProcessed++;
                        }
                        else
                        {
                            while(!linesArr[i].isEmpty())
                            {
                                runTime += (int)linesArr[i].poll();
                                peopleProcessed++;
                            }
                        }
                        //calculates wait time for this line
                        for(int k = 0; k < linesArr[i].size(); k++)
                        {
                            linesArr[i].offer((int)linesArr[i].poll() + 1);
                        }

                    }
                    runWaitTimeAve = runTime/peopleProcessed;
                    aveWaitTime += runWaitTimeAve;
                    timer--;
                }

            }

            aveWaitTime = aveWaitTime/numIterations;
            //System.out.println((int)aveWaitTime);
            System.out.println("Average wait time using " + lineCount + " queue(s): " + (int)aveWaitTime);
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
