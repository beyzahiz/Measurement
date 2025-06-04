package controller;

import model.Measurement;
import service.MeasurementService;
import service.MeasurementServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

public class MeasurementController {
    private final MeasurementService service;
    private List<Measurement> temperatureMeasurements;
    private List<Measurement> humidityMeasurements;
    private String selectedFolder;

    public MeasurementController() {
        this.service = new MeasurementServiceImpl();
        this.temperatureMeasurements = new ArrayList<>();
        this.humidityMeasurements = new ArrayList<>();
    }

    public void setSelectedFolder(String folderPath) {
        this.selectedFolder = folderPath;
        loadMeasurements();
    }

    private void loadMeasurements() {
        System.out.println("Loading measurements from: " + selectedFolder);
        temperatureMeasurements = service.readMeasurements(selectedFolder, "sicaklik");
        humidityMeasurements = service.readMeasurements(selectedFolder, "nem");
        System.out.println("Loaded " + temperatureMeasurements.size() + " temperature measurements");
        System.out.println("Loaded " + humidityMeasurements.size() + " humidity measurements");
    }

    public void calculate(boolean average, boolean maximum, boolean minimum, 
                        boolean standardDeviation, boolean frequency, boolean median) {
        if (selectedFolder == null) {
            throw new IllegalStateException("Lütfen önce bir klasör seçin!");
        }

        // Process temperature measurements
        if (!temperatureMeasurements.isEmpty()) {
            processCalculations("sicaklik", temperatureMeasurements, 
                              average, maximum, minimum, standardDeviation, frequency, median);
        }

        // Process humidity measurements
        if (!humidityMeasurements.isEmpty()) {
            processCalculations("nem", humidityMeasurements, 
                              average, maximum, minimum, standardDeviation, frequency, median);
        }
    }

    private void processCalculations(String type, List<Measurement> measurements,
                                   boolean average, boolean maximum, boolean minimum, 
                                   boolean standardDeviation, boolean frequency, boolean median) {
        if (measurements.isEmpty()) {
            System.out.println("No measurements found for type: " + type);
            return;
        }

        Map<Integer, List<Measurement>> measurementsByFile = measurements.stream()
                .collect(Collectors.groupingBy(Measurement::getId));

        System.out.println("Processing " + type + " measurements for " + measurementsByFile.size() + " files");

        if (average) {
            List<String> results = new ArrayList<>();
            measurementsByFile.forEach((id, mList) -> {
                double avg = service.calculateAverage(mList);
                String result = String.format("id:%d ölçüm: %s - yer: %s - tarih: %s , ortalama: %.2f",
                        id, type, mList.get(0).getLocation(), 
                        mList.get(0).getDate().toLocalDate(), avg);
                results.add(result);
                System.out.println("Average result: " + result);
            });
            service.saveResults(type, "ortalama", results);
        }

        if (maximum) {
            List<String> results = new ArrayList<>();
            measurementsByFile.forEach((id, mList) -> {
                double max = service.calculateMaximum(mList);
                results.add(String.format("id:%d ölçüm: %s - yer: %s - tarih: %s , max: %.2f",
                        id, type, mList.get(0).getLocation(), 
                        mList.get(0).getDate().toLocalDate(), max));
            });
            service.saveResults(type, "maximum", results);
        }

        if (minimum) {
            List<String> results = new ArrayList<>();
            measurementsByFile.forEach((id, mList) -> {
                double min = service.calculateMinimum(mList);
                results.add(String.format("id:%d ölçüm: %s - yer: %s - tarih: %s , min: %.2f",
                        id, type, mList.get(0).getLocation(), 
                        mList.get(0).getDate().toLocalDate(), min));
            });
            service.saveResults(type, "minimum", results);
        }

        if (standardDeviation) {
            List<String> results = new ArrayList<>();
            measurementsByFile.forEach((id, mList) -> {
                double std = service.calculateStandardDeviation(mList);
                results.add(String.format("id:%d ölçüm: %s - yer: %s - tarih: %s , standart sapma: %.2f",
                        id, type, mList.get(0).getLocation(), 
                        mList.get(0).getDate().toLocalDate(), std));
            });
            service.saveResults(type, "standartsapma", results);
        }

        if (frequency) {
            List<String> results = new ArrayList<>();
            measurementsByFile.forEach((id, mList) -> {
                Map<Double, Integer> freq = service.calculateFrequency(mList);
                results.add(String.format("id:%d ölçüm: %s - yer: %s - tarih: %s",
                        id, type, mList.get(0).getLocation(), 
                        mList.get(0).getDate().toLocalDate()));
                freq.forEach((value, count) -> 
                    results.add(String.format("%.1f derece %d defa ölçüldü", value, count)));
                results.add("---------------");
            });
            service.saveResults(type, "frekans", results);
        }

        if (median) {
            List<String> results = new ArrayList<>();
            measurementsByFile.forEach((id, mList) -> {
                double med = service.calculateMedian(mList);
                results.add(String.format("id:%d ölçüm: %s - yer: %s - tarih: %s , medyan: %.2f",
                        id, type, mList.get(0).getLocation(), 
                        mList.get(0).getDate().toLocalDate(), med));
            });
            service.saveResults(type, "medyan", results);
        }
    }
} 