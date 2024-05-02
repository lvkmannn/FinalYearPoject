package model;

public class FireDepartment {
    private String name, district, state, phoneNum;

    public FireDepartment(){

    }

    public FireDepartment(String name, String district, String state, String phoneNum) {
        this.name = name;
        this.district = district;
        this.state = state;
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
