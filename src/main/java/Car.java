import com.itextpdf.text.BaseColor;

import java.util.ArrayList;
import java.util.UUID;

public class Car {
    private UUID uuid;
    private String model;
    private int year;
    private ArrayList<Airbag> airbag;
    private String color;
    private boolean invoice = false;

    private CustomDate customDate;
    private String date;
    private int price;
    private int vat;

    public Car(UUID uuid, String model, int year, ArrayList<Airbag> airbag, String color){
        this.uuid = uuid;
        this.model = model;
        this.year = year;
        this.airbag = airbag;
        this.color = color;
        this.customDate = new CustomDate(Helpers.randInt(30)+1990, Helpers.randInt(12)+1, Helpers.randInt(31)+1);
        this.date = this.customDate.toString();
        this.price = Helpers.randInt(100000)+5000;
        int temp = Helpers.randInt(3);
        if(temp == 0)
            this.vat = 0;
        else if (temp==1)
            this.vat = 7;
        else
            this.vat = 22;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid(){
        return this.uuid;
    }

    public String getModel(){
        return this.model;
    }

    public int getYear(){
        return this.year;
    }

    public ArrayList<Airbag> getAirbag(){
        return airbag;
    }

    public String getColor(){
        return color;
    }

    public void setModel(String model){
        this.model = model;
    }

    public void setYear(int year){
        this.year = year;
    }

    public void setInvoice(boolean invoice){
        this.invoice = invoice;
    }

    public int getPrice(){
        return this.price;
    }

    public int getVat(){
        return this.vat;
    }

    public CustomDate getCustomDate(){
        return this.customDate;
    }
}
