package service;

import model.Measurement;

import java.util.*;

public class MeasurementAnalyzer {

    public static double getAverage(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public static double getMax(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).max().orElse(Double.MIN_VALUE);
    }

    public static double getMin(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).min().orElse(Double.MAX_VALUE);
    }

    public static double getStdDev(List<Double> values) {
        double avg = getAverage(values);
        return Math.sqrt(values.stream()
                .mapToDouble(v -> Math.pow(v - avg, 2))
                .average().orElse(0));
    }

    public static double getMedian(List<Double> values) {
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int size = sorted.size();
        return (size % 2 == 0)
                ? (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2
                : sorted.get(size / 2);
    }

    public static Map<Double, Integer> getFrequency(List<Double> values) {
        Map<Double, Integer> freq = new TreeMap<>();
        for (double val : values) {
            freq.put(val, freq.getOrDefault(val, 0) + 1);
        }
        return freq;
    }
}
