package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {

    private List<Simulation> simulations;
    private List<Thread> simulationsThreads;
    private ExecutorService executorService;

    public SimulationEngine(List<Simulation> simulations){
        this.simulations=simulations;
    }

    public void runSync(){
        for(Simulation simulation: simulations){
            simulation.run();
        }
    }

    public void runAsync(){
        simulationsThreads=new ArrayList<>();
        for(Simulation simulation: simulations){
            simulationsThreads.add(new Thread(simulation));
        }
        for (Thread simulationThread: simulationsThreads){
            simulationThread.start();
        }
    }

    public void runAsyncInThreadPool(){
        executorService = Executors.newFixedThreadPool(4);
        for(Simulation simulation: simulations){
            executorService.submit(simulation);
        }
        executorService.shutdown();
    }

    public void awaitSimulationsEnd(){
        if (simulationsThreads!=null && !simulationsThreads.isEmpty()){
            for (Thread simulationThread: simulationsThreads){
                try{
                    simulationThread.join();
                }
                catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    System.err.println("Wątek przerwany");
                }
            }
        }
        else if (executorService!=null){
            try{
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("10 s minęło. Zakończenie wątków");
                    executorService.shutdownNow();
                }
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Czekanie zostało przerwane.");
                executorService.shutdownNow();
            }
        }
    }
}
