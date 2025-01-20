package agh.ics.oop.model;

import agh.ics.oop.model.util.MapChangeListener;

public class ConsoleMapDisplay implements MapChangeListener {

    private int howManyActualisations = 0;

    @Override
    public void mapChanged(ProjectWorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.println(message);
            System.out.println(worldMap.toString());
            System.out.println("Id mapy: %s".formatted(worldMap.getID().toString()));
            howManyActualisations++;
            System.out.println("Liczba aktualizacji: %d\n".formatted(howManyActualisations));
        }
    }
}