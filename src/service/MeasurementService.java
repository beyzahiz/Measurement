package service;

import model.Measurement;
import java.util.List;
import java.util.Map;

public interface MeasurementService {
    double calculateAverage(List<Measurement> measurements);
    double calculateMaximum(List<Measurement> measurements);
    double calculateMinimum(List<Measurement> measurements);
    double calculateStandardDeviation(List<Measurement> measurements);
    Map<Double, Integer> calculateFrequency(List<Measurement> measurements);
    double calculateMedian(List<Measurement> measurements);
    void saveResults(String type, String operation, List<String> results);
    List<Measurement> readMeasurements(String folderPath, String type);
} 