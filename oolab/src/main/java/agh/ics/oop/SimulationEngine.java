package agh.ics.oop;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {

    private Map<UUID, Simulation> simulations;
    private Map<UUID, Thread> simulationsThreads;
    private ExecutorService executorService;

    public SimulationEngine(Map<UUID, Simulation> simulations){
        this.simulations=simulations;
    }

    public void runSync(){
        for(Simulation simulation: simulations.values()){
            simulation.run();
        }
    }

    public void runAsync(){
        simulationsThreads=new HashMap<>();
        for(UUID keyId: simulations.keySet()){
            simulationsThreads.put(keyId, new Thread(simulations.get(keyId)));
        }
        for (Thread simulationThread: simulationsThreads.values()){
            simulationThread.start();
        }
    }

    public void pauseSpecificSimulation(UUID keyId){
        Simulation simulation = simulations.get(keyId);
        if (simulation != null) {
            simulation.pause();
        }
    }


    public void resumeSpecificSimulation(UUID keyId) {
        Simulation simulation = simulations.get(keyId);
        if (simulation != null) {
            simulation.resume();
        }
    }

    public void runAsyncInThreadPool(){
        executorService = Executors.newFixedThreadPool(4);
        for(UUID keyId: simulations.keySet()){
            executorService.submit(simulations.get(keyId));
        }
        executorService.shutdown();
    }

    public void awaitSimulationsEnd() throws InterruptedException{
        if (simulationsThreads!=null && !simulationsThreads.isEmpty()){
            for (Thread simulationThread: simulationsThreads.values()){
                simulationThread.join();
            }
        }
        else if (executorService!=null){
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("10 s minęło. Zakończenie wątków");
                executorService.shutdownNow();
            }
        }
    }
}
