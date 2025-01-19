package agh.ics.oop.model.WriteDaysToFile;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.ProjectWorldMap;
import agh.ics.oop.model.util.DailyDataCollector;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteDaysToCSV
{
    DailyDataCollector dailyDataCollector;

    public WriteDaysToCSV(ProjectWorldMap projectWorldMap, List<Animal> deadAnimals, int currentSimulationDay)
    {
        dailyDataCollector = new DailyDataCollector(projectWorldMap, deadAnimals,currentSimulationDay);
    }

    private static List<String[]> dataLines(int day, int numberOfAnimals,
                                           int numberOfPlants, List<Integer> mostPopularGenotype,
                                           int averageEnergyLevel,
                                           Optional<Integer> averageDaysAlive,
                                           int averageKidsNumber
    ) {
        return List.of(
                new String[]{"Day", String.valueOf(day)},
                new String[]{"Number of animals", String.valueOf(numberOfAnimals)},
                new String[]{"Number of plants", String.valueOf(numberOfPlants)},
                new String[]{"Most popular genotypes", mostPopularGenotype.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" "))},
                new String[]{"Average energy level", String.valueOf(averageEnergyLevel)},
                new String[]{"Average days alive for dead animals",
                        averageDaysAlive
                        .map(String::valueOf)
                        .orElse("No data available")},
                new String[]{"Average kids number", String.valueOf(averageKidsNumber)}
        );
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replace("\"", "\"\"");
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            escapedData = "\"" + escapedData + "\"";
        }
        return escapedData;
    }

    public void givenDataArray_whenConvertToCSV_thenOutputCreated() throws IOException {
        File csvOutputFile = new File("darwin_simulation.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile))
        {
            dataLines( dailyDataCollector.getCurrentSimulationDay(), dailyDataCollector.numberOfAliveAnimals(),
             dailyDataCollector.numberOfPlants(), dailyDataCollector.mostPopularGenotype(),
             dailyDataCollector.averageEnergyLevel(),
             dailyDataCollector.averageLifeSpan(),
             dailyDataCollector.averageKidsNumber())
                    .stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
}
