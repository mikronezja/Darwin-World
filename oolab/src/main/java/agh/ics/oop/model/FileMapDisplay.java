package agh.ics.oop.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileMapDisplay implements MapChangeListener{

    @Override
    public void mapChanged(ProjectWorldMap worldMap, String message) {
        File file = new File("map_%s.log".formatted(worldMap.getID()));
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write("%s %s\n".formatted(new java.util.Date().toGMTString(),message));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
