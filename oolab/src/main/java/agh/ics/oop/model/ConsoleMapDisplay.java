package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {

    private int howManyActualisations = 0;

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        System.out.println(message);
        System.out.println(worldMap.toString());
        System.out.println("Id mapy: %s".formatted(worldMap.getID().toString()));
        howManyActualisations++;
        System.out.println("Liczba aktualizacji: %d\n".formatted(howManyActualisations));
    }
}