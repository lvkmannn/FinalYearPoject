package model;

public class InfoHome {
    int id;

    double latitude, longitude, normal, alert, warning, danger, hourlyRainfall, todayRainfall, waterLevel;
    String stationId, districtName, stationName, lastUpdate;

    public InfoHome(){

    }

    public InfoHome(int id, double latitude, double longitude, double normal, double alert, double warning, double danger, double hourlyRainfall, double todayRainfall, double waterLevel, String stationId, String districtName, String stationName, String lastUpdate) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.normal = normal;
        this.alert = alert;
        this.warning = warning;
        this.danger = danger;
        this.hourlyRainfall = hourlyRainfall;
        this.todayRainfall = todayRainfall;
        this.waterLevel = waterLevel;
        this.stationId = stationId;
        this.districtName = districtName;
        this.stationName = stationName;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getNormal() {
        return normal;
    }

    public void setNormal(double normal) {
        this.normal = normal;
    }

    public double getAlert() {
        return alert;
    }

    public void setAlert(double alert) {
        this.alert = alert;
    }

    public double getWarning() {
        return warning;
    }

    public void setWarning(double warning) {
        this.warning = warning;
    }

    public double getDanger() {
        return danger;
    }

    public void setDanger(double danger) {
        this.danger = danger;
    }

    public double getHourlyRainfall() {
        return hourlyRainfall;
    }

    public void setHourlyRainfall(double hourlyRainfall) {
        this.hourlyRainfall = hourlyRainfall;
    }

    public double getTodayRainfall() {
        return todayRainfall;
    }

    public void setTodayRainfall(double todayRainfall) {
        this.todayRainfall = todayRainfall;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
