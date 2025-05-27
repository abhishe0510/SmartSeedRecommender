package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeedDAO {

    public static List<Seed> getAllSeeds() {
        List<Seed> seeds = new ArrayList<>();
        String query = "SELECT * FROM seeds";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String soilType = rs.getString("soil_type");
                float temperature = rs.getFloat("temperature");
                float humidity = rs.getFloat("humidity");
                float rainfall = rs.getFloat("rainfall");
                float ph = rs.getFloat("ph");
                String season = rs.getString("season");
                int profit = rs.getInt("profit_per_acre");

                Seed seed = new Seed(name, soilType, temperature, humidity, rainfall, ph, season, profit);
                seeds.add(seed);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seeds;
    }
}
