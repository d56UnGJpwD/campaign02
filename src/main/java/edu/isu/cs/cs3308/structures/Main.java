package edu.isu.cs.cs3308.structures;

import edu.isu.cs.cs3308.Simulation;

public class Main
{
    public static void main(String[] args)
    {
        Simulation test = new Simulation(18, 10, 50, 1024);
        test.runSimulation();
    }
}
