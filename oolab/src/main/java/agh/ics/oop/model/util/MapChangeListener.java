package agh.ics.oop.model.util;

import agh.ics.oop.model.ProjectWorldMap;

public interface MapChangeListener {

    void mapChanged(ProjectWorldMap worldMap, String message);

}
