package service;

import model.Measurement;
import model.MeasurementType;

import java.io.*;
import java.nio.file.*;
import java.time.LocalTime;
import java.util.*;

public class MeasurementParser {

    public static List<Measurement> parseMeasurements(Path folderPath, MeasurementType type) throws IOException {
        List<Measurement> measurements = new ArrayList<>();
        File[] files = folderPath.toFile().listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) return measurements;

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String header = reader.readLine();
                String[] parts = header.split(" ");
                String id = parts[0].split(":")[1];
                String yer = parts[3];
                String tarih = parts[5];

                Measurement m = new Measurement(id, yer, tarih, type);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    LocalTime time = LocalTime.parse(data[0]);
                    double value = Double.parseDouble(data[1]);
                    m.addData(time, value);
                }
                measurements.add(m);
            }
        }
        return measurements;
    }
}
