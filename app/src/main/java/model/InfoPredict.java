package model;

public class InfoPredict {
    String date, district, location;
    Double testLoss, MAE, MSE, RMSE, day1, day2, day3;


    public InfoPredict(String date, String district, String location, Double testLoss, Double MAE, Double MSE, Double RMSE, Double day1, Double day2, Double day3) {
        this.date = date;
        this.district = district;
        this.location = location;
        this.testLoss = testLoss;
        this.MAE = MAE;
        this.MSE = MSE;
        this.RMSE = RMSE;
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getTestLoss() {
        return testLoss;
    }

    public void setTestLoss(Double testLoss) {
        this.testLoss = testLoss;
    }

    public Double getMAE() {
        return MAE;
    }

    public void setMAE(Double MAE) {
        this.MAE = MAE;
    }

    public Double getMSE() {
        return MSE;
    }

    public void setMSE(Double MSE) {
        this.MSE = MSE;
    }

    public Double getRMSE() {
        return RMSE;
    }

    public void setRMSE(Double RMSE) {
        this.RMSE = RMSE;
    }

    public Double getDay1() {
        return day1;
    }

    public void setDay1(Double day1) {
        this.day1 = day1;
    }

    public Double getDay2() {
        return day2;
    }

    public void setDay2(Double day2) {
        this.day2 = day2;
    }

    public Double getDay3() {
        return day3;
    }

    public void setDay3(Double day3) {
        this.day3 = day3;
    }
}
