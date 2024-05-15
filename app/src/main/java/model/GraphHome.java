package model;

public class GraphHome {
    String lastUpdate;
    double waterLevel;

    public GraphHome(String lastUpdate, double waterLevel) {
        this.lastUpdate = lastUpdate;
        this.waterLevel = waterLevel;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }
}
