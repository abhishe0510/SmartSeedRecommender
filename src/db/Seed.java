package db;

public class Seed {
    public String name, soilType, season;
    public float temperature, humidity, rainfall, ph;
    public int profitPerAcre;

    public Seed(String name, String soilType, float temperature, float humidity, float rainfall, float ph, String season, int profitPerAcre) {
        this.name = name;
        this.soilType = soilType.toLowerCase();
        this.temperature = temperature;
        this.humidity = humidity;
        this.rainfall = rainfall;
        this.ph = ph;
        this.season = season.toLowerCase();
        this.profitPerAcre = profitPerAcre;
    }

    @Override
    public String toString() {
        return String.format("Seed: %s | Soil: %s | Season: %s | Profit: ₹%d", name, soilType, season, profitPerAcre);
    }
}

