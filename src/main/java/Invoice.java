import java.util.ArrayList;

public class Invoice {

    long time;
    String title;
    String seller;
    String buyer;
    ArrayList<Car> list;

    public Invoice(String seller, String buyer, ArrayList<Car> list){
        this.time = System.currentTimeMillis();
        this.title = "VAT"+this.time;
        this.seller = seller;
        this.buyer = buyer;
        this.list = list;
    }

    String getTitle(){
        return this.title;
    }

    String getSeller(){
        return this.seller;
    }

    String getBuyer(){
        return this.buyer;
    }

    ArrayList<Car> getList(){
        return this.list;
    }

    String createInvoiceId(){
        return "invoice" + Long.toString(this.time);
    }


}
