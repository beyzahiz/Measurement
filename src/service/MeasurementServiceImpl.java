package service;

import model.Measurement;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MeasurementServiceImpl implements MeasurementService {
    
    @Override
    public double calculateAverage(List<Measurement> measurements) {
        return measurements.stream()
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public double calculateMaximum(List<Measurement> measurements) {
        return measurements.stream()
                .mapToDouble(Measurement::getValue)
                .max()
                .orElse(0.0);
    }

    @Override
    public double calculateMinimum(List<Measurement> measurements) {
        return measurements.stream()
                .mapToDouble(Measurement::getValue)
                .min()
                .orElse(0.0);
    }

    @Override
    public double calculateStandardDeviation(List<Measurement> measurements) {
        double mean = calculateAverage(measurements);
        double sum = measurements.stream()
                .mapToDouble(m -> Math.pow(m.getValue() - mean, 2))
                .sum();
        return Math.sqrt(sum / measurements.size());
    }

    @Override
    public Map<Double, Integer> calculateFrequency(List<Measurement> measurements) {
        return measurements.stream()
                .collect(Collectors.groupingBy(
                        Measurement::getValue,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    @Override
    public double calculateMedian(List<Measurement> measurements) {
        List<Double> sortedValues = measurements.stream()
                .map(Measurement::getValue)
                .sorted()
                .collect(Collectors.toList());
        
        int size = sortedValues.size();
        if (size == 0) return 0.0;
        
        if (size % 2 == 0) {
            return (sortedValues.get(size/2 - 1) + sortedValues.get(size/2)) / 2.0;
        } else {
            return sortedValues.get(size/2);
        }
    }

    @Override
    public void saveResults(String type, String operation, List<String> results) {
        try {
            // Create absolute path for sonuc directory
            Path basePath = Paths.get(System.getProperty("user.dir"));
            Path resultPath = basePath.resolve("sonuc").resolve(type.toLowerCase());
            
            // Create directories if they don't exist
            Files.createDirectories(resultPath);
            
            // Create the result file
            Path filePath = resultPath.resolve(operation.toLowerCase() + ".txt");
            
            // Write results to file with UTF-8 encoding
            Files.write(filePath, results, StandardCharsets.UTF_8, 
                       StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            System.out.println("Results saved to: " + filePath.toString());
            System.out.println("Number of results: " + results.size());
            System.out.println("First result: " + (results.isEmpty() ? "none" : results.get(0)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sonuçlar kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public List<Measurement> readMeasurements(String folderPath, String type) {
        List<Measurement> measurements = new ArrayList<>();
        try {
            Path typePath = Paths.get(folderPath, type.toLowerCase());
            if (!Files.exists(typePath)) {
                System.out.println("Directory not found: " + typePath);
                return measurements;
            }

            System.out.println("Reading from directory: " + typePath);
            Files.walk(typePath)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".txt"))
                .forEach(file -> {
                    try {
                        System.out.println("Reading file: " + file);
                        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                        if (lines.isEmpty()) {
                            System.out.println("Empty file: " + file);
                            return;
                        }

                        // Parse header
                        String header = lines.get(0);
                        System.out.println("Header: " + header);
                        String[] headerParts = header.split(" - ");
                        int id = Integer.parseInt(headerParts[0].split(":")[1].trim());
                        String location = headerParts[2].split(": ")[1];
                        String dateStr = headerParts[3].split(": ")[1];
                        
                        // Parse the date using the correct format
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        LocalDateTime date = LocalDateTime.of(LocalDate.parse(dateStr, formatter), LocalTime.MIDNIGHT);
                        
                        System.out.println("Parsed date: " + date);

                        // Parse measurements
                        for (int i = 1; i < lines.size(); i++) {
                            String line = lines.get(i).trim();
                            if (line.isEmpty()) continue;
                            
                            String[] parts = line.split(",");
                            if (parts.length != 2) {
                                System.out.println("Invalid line format: " + line);
                                continue;
                            }
                            
                            try {
                                // Parse time and combine with date
                                String[] timeParts = parts[0].trim().split(":");
                                LocalTime time = LocalTime.of(
                                    Integer.parseInt(timeParts[0]),
                                    Integer.parseInt(timeParts[1]),
                                    timeParts.length > 2 ? Integer.parseInt(timeParts[2]) : 0
                                );
                                LocalDateTime measurementTime = LocalDateTime.of(date.toLocalDate(), time);
                                
                                double value = Double.parseDouble(parts[1].trim());
                                
                                measurements.add(new Measurement(id, type, location, date, value, measurementTime));
                                System.out.println("Added measurement: time=" + measurementTime + ", value=" + value);
                            } catch (Exception e) {
                                System.out.println("Error parsing line: " + line);
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error reading file: " + file);
                        e.printStackTrace();
                    }
                });
            
            System.out.println("Total measurements read: " + measurements.size());
        } catch (IOException e) {
            System.out.println("Error accessing directory: " + typePath);
            e.printStackTrace();
        }
        return measurements;
    }
} 