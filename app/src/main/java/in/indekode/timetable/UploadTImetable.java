package in.indekode.timetable;

import java.util.ArrayList;

public class UploadTImetable {
    public String day;
    public ArrayList<String> time;
    public ArrayList<String> praTime;

    public ArrayList<String> subProf, labProf;

    public UploadTImetable() {

    }

    public UploadTImetable(String day, ArrayList<String> time, ArrayList<String> praTime, ArrayList<String> subProf, ArrayList<String> labProf) {
        this.day = day;
        this.time = time;
        this.praTime = praTime;
        this.subProf = subProf;
        this.labProf = labProf;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public void setTime(ArrayList<String> time) {
        this.time = time;
    }

    public ArrayList<String> getPraTime() {
        return praTime;
    }

    public void setPraTime(ArrayList<String> praTime) {
        this.praTime = praTime;
    }

    public ArrayList<String> getSubProf() {
        return subProf;
    }

    public void setSubProf(ArrayList<String> subProf) {
        this.subProf = subProf;
    }

    public ArrayList<String> getLabProf() {
        return labProf;
    }

    public void setLabProf(ArrayList<String> labProf) {
        this.labProf = labProf;
    }
}
