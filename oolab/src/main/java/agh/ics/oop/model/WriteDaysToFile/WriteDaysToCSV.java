package agh.ics.oop.model.WriteDaysToFile;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.ProjectWorldMap;
import agh.ics.oop.model.util.DailyDataCollector;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteDaysToCSV {
    DailyDataCollector dailyDataCollector;

    public WriteDaysToCSV(ProjectWorldMap projectWorldMap, List<Animal> deadAnimals, int currentSimulationDay) {
        dailyDataCollector = new DailyDataCollector(projectWorldMap, deadAnimals, currentSimulationDay);
    }

    private static List<String> dataLines(int day, int numberOfAnimals,
                                          int numberOfPlants, Set<List<Integer>> mostPopularGenotype,
                                          int averageEnergyLevel,
                                          Optional<Integer> averageDaysAlive,
                                          int averageKidsNumber
    ) {
        return List.of(
                new String[]{String.valueOf(day),
                        String.valueOf(numberOfAnimals), String.valueOf(numberOfPlants),

                        mostPopularGenotype.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" ")),

                        String.valueOf(averageEnergyLevel),
                        averageDaysAlive
                        .map(String::valueOf)
                        .orElse("No data available"),
                        String.valueOf(averageKidsNumber)

                });
    }

    private List<String> beginningOfFile() {
        return Arrays.asList
                (
                        "Day",
                        "Number of animals",
                        "Number of plants",
                        "Most popular genotypes",
                        "Average energy level",
                        "Average days alive for dead animals",
                        "Average kids number"
                );
    }

    private String convertToCSV(List<String> data) {
        return data.stream()
                .collect(Collectors.joining(";"));
    }

    public void givenDataArray_whenConvertToCSV_thenOutputCreated() throws IOException {
        File csvOutputFile = new File("darwin_simulation_%s.csv".formatted(dailyDataCollector.getCurrentMapID()));

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(csvOutputFile, true))) {

            if (dailyDataCollector.getCurrentSimulationDay() == 0) {
                String tableNames = convertToCSV(beginningOfFile());
                pw.println(tableNames);
            }
            String csvLine = convertToCSV(dataLines(
                    dailyDataCollector.getCurrentSimulationDay(),
                    dailyDataCollector.numberOfAliveAnimals(),
                    dailyDataCollector.numberOfPlants(),
                    dailyDataCollector.mostPopularGenotype(),
                    dailyDataCollector.averageEnergyLevel(),
                    dailyDataCollector.averageLifeSpan(),
                    dailyDataCollector.averageKidsNumber()
            ));
            
            pw.println(csvLine);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}