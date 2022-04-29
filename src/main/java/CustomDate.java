import java.util.Random;

public class CustomDate {

    private int day;
    private int month;
    private int year;

    public CustomDate(int year, int month, int day){
        this.year = year;
        this.month =  month;
        this.day = day;
    }

    public int getYear() {
        return this.year;
    }

    public String getDateString(){
        return this.year + "/" +  this.month + "/" + this.day;
    }

    @Override
    public String toString() {
        return this.year + "/" +  this.month + "/" + this.day;
    }
}
