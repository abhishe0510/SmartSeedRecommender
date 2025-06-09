# 🌾 Smart Seed Recommender 🌱

A **Java-based intelligent farming assistant** that recommends the most suitable crops (seeds) based on environmental conditions, soil properties, and economic potential.

---

## 🚀 Features

### ✅ Smart Crop Recommendations
Recommendations are based on:
- 🌡️ **Temperature**
- 💧 **Humidity**
- 🌧️ **Rainfall**
- 🧪 **Soil pH**
- 🌱 **Soil Type**
- 🌤️ **Season**

### 💸 Economic Evaluation
Each recommended seed includes:
- Production Cost (₹/acre)
- Market Price (₹/quintal)
- Yield Estimate (quintals/acre)
- **Profit Calculation**: Automatically computed as `(yield × price) - cost`

### 🧠 Personalized User Profiles
- Saves user-specific input history and past recommendations to disk.
- Supports multiple sessions per user.
- Recommendations can be revisited any time.

---

## 🖥️ How It Works (Example)

```plaintext
=== Smart Seed Recommender ===  

👤 Enter your username: raj_farm  

📜 Welcome back, raj_farm!  
Session 1:  
  🔍 Input: Temp: 25.0°C, Humidity: 80.0%, Rainfall: 1200.0 mm, pH: 6.5, Soil: clay, Season: kharif  
  🌱 Recommended Seeds: Rice, Cotton, Soybean  

🌡️ Temperature (°C): 28  
💧 Humidity (%): 70  
🌧️ Rainfall (mm/year): 1000  
🧪 Soil pH: 6.8  
🌱 Soil Type: loamy  
🌤️ Season: rabi  

🎯 Recommended Seeds:  
✅ Wheat — ₹2000/quintal, 30 quintals/acre  
✅ Gram — ₹4500/quintal, 20 quintals/acre


---

⚠️ Input Requirement Notice

Please ensure all inputs are valid and correctly spelled to avoid errors or empty recommendations.

The application expects:

Input Parameter	Example	Valid Range / Notes

🌡️ Temperature	28.0	0°C to 60°C
💧 Humidity	70.0	0% to 100%
🌧️ Rainfall	1000.0	0 mm/year to 5000 mm/year
🧪 Soil pH	6.8	3.5 to 9.0
🌱 Soil Type	clay	loamy, clay, sandy, black, silty, peaty, saline, laterite, red
🌤️ Season	kharif	kharif, rabi, zaid


⚠️ If any input is misspelled, missing, or outside the valid range, the system will show a warning or may not return any recommendations.


---

📂 Supported Crops

Includes over 20+ crops spanning:

Kharif crops (e.g., Rice, Maize, Cotton)

Rabi crops (e.g., Wheat, Barley, Gram)

Zaid crops (e.g., Watermelon, Cucumber)

Perennial crops (e.g., Tea, Coffee, Sugarcane)


Each crop has detailed suitability parameters and economic data.


---

💾 Data Persistence

All user interactions and seed recommendations are saved to a .txt file named as {username}_history.txt

Session history is reloaded each time the user logs in



---

📈 Profit Formula

profit = (yieldPerAcre * marketPrice) - productionCost

Each crop’s profitability is calculated based on user’s area input and shown alongside suitability.


---

🛠️ Technologies Used

Java

JavaFX (for ObservableList support; GUI integration ready)

File I/O (for persistence)

OOP principles with nested classes for seed data, user profile, and area
