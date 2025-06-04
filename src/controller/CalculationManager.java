public class CalculationManager {
    private List<CalculationStrategy> strategies;  // Seçilen hesaplama türleri
    private List<Measurement> measurements;        // Okunan ölçüm verileri
    
    public CalculationManager(List<Measurement> measurements) {
        this.measurements = measurements;
        this.strategies = new ArrayList<>();
    }

    public void addStrategy(CalculationStrategy strategy) {
        strategies.add(strategy);
    }
    
    // Hesaplamaları çalıştıracak metot
    public void executeCalculations(String outputFolder) {
        // Her strateji için ayrı hesaplama yap ve dosyaya yaz
        for (CalculationStrategy strategy : strategies) {
            strategy.calculateAndWrite(measurements, outputFolder);
        }
    }
}
