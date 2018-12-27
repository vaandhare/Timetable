package in.indekode.timetable.login;

public class UserProfile {
    public String position;
    public String username;
    public String name;
    public String year;
    public String batch;
    public String rollNo;

    public UserProfile(){
    }

    public UserProfile(String position, String username, String name, String year, String batch, String rollNo) {
        this.position = position;
        this.username = username;
        this.name = name;
        this.year = year;
        this.batch = batch;
        this.rollNo = rollNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
}