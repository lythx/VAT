import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.eclipse.jetty.http.HttpParser;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static spark.Spark.*;

public class App {

    static ArrayList<Car> cars = new ArrayList<>();
    static ArrayList<String> models = new ArrayList<>();
    static ArrayList<String> airbagTypes = new ArrayList<>();

    static ArrayList<String> colors = new ArrayList<>();

    static String allCarsInvoicePath;

    static String yearInvoicePath;

    static String priceInvoicePath;

    static HashMap<String, BaseColor> baseColors = new HashMap<>() {{
        put("red", BaseColor.RED);
        put("blue", BaseColor.BLUE);
        put("yellow", BaseColor.YELLOW);
        put("black", BaseColor.WHITE);
        put("orange", BaseColor.ORANGE);
        put("green", BaseColor.GREEN);
        put("magenta", BaseColor.MAGENTA);
        put("pink", BaseColor.PINK);
        put("white", BaseColor.WHITE);
    }};

    public static void main(String[] args) {
//        port(5000);
        port(getHerokuPort());
        staticFiles.location("/public");

        colors.add("red");
        colors.add("blue");
        colors.add("yellow");
        colors.add("black");
        colors.add("orange");
        colors.add("green");
        colors.add("magenta");
        colors.add("pink");
        colors.add("white");

        models.add("Fiat");
        models.add("Renault");
        models.add("Porsche");
        models.add("Opel");
        models.add("Kia");
        models.add("Nissan");
        models.add("Hyundai");
        models.add("Honda");

        airbagTypes.add("kier");
        airbagTypes.add("pas");
        airbagTypes.add("tyl");
        airbagTypes.add("bok");

        post("/add", (req,res) -> {
            Gson gson = new Gson();
            Car car = gson.fromJson(req.body(), Car.class);
            UUID uuid = Generators.randomBasedGenerator().generate();
            car.setUuid(uuid);
            cars.add(car);
            return gson.toJson(car);
        });


        post("/json",  (req,res)->{
            Gson gson = new Gson();
            return gson.toJson(cars);
        });

        post("/delete",  (req,res)->{
            Gson gson = new Gson();
            for(int i =0;i<cars.size();i++){
                if(cars.get(i).getUuid().toString().equals(req.body())) {
                    cars.remove(i);
                    System.out.println(cars);
                    return gson.toJson("auto usuniete");
                }
            }
            return gson.toJson("błąd");
        });

        post("/update",  (req,res)->{
            Gson gson = new Gson();
            System.out.println(req.body());
            Car car = gson.fromJson(req.body(), Car.class);
            System.out.println(car.toString());
            UUID uuid = car.getUuid();
            System.out.println(car.getUuid());
            for(int i =0;i<cars.size();i++){
                System.out.println(uuid);
                if(cars.get(i).getUuid().toString().equals(uuid.toString())) {
                    cars.get(i).setModel(car.getModel());
                    cars.get(i).setYear(car.getYear());
                    System.out.println("================");
                    return gson.toJson("auto zmienione");
                }
            }
            return gson.toJson("błąd");
        });

        post("/generatecars",  (req,res)->{
            Gson gson = new Gson();
            Random rand = new Random();
            for(int i =0; i<10;i++){
                UUID uuid = Generators.randomBasedGenerator().generate();
                String model = models.get(rand.nextInt(models.size()));
                int year = rand.nextInt(20) + 2000;
                ArrayList<Airbag> airbag = new ArrayList<>();
                for(int j =0; j <4; j++){
                    Airbag a = new Airbag(airbagTypes.get(j), rand.nextBoolean());
                    airbag.add(a);
                    System.out.println(airbag);
                }
                String color = colors.get(rand.nextInt(colors.size()));
                Car car = new Car(uuid, model, year, airbag, color);
                cars.add(car);
            }
            return gson.toJson("auta wygenerowane");
        });

        post("/invoice", (req, res) ->{
            Gson gson = new Gson();
            Document document = new Document();
            Car car = null;

            for(int i =0;i<cars.size();i++){
                if(cars.get(i).getUuid().toString().equals(req.body())) {
                    car = cars.get(i);
                }
            }

            String path = "invoices/"+ car.getUuid() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font headerFont = FontFactory.getFont(FontFactory.COURIER, 15, BaseColor.BLACK);
            Paragraph chunk = new Paragraph("FAKTURA dla: " + car.getUuid(), headerFont); // akapit
            document.add(chunk);
            Font bigFont = FontFactory.getFont(FontFactory.COURIER, 30, BaseColor.BLACK);
            Paragraph chunk1 = new Paragraph("Model: " + car.getModel(), bigFont); // akapit
            document.add(chunk1);
            BaseColor baseColor = baseColors.get(car.getColor());
            Font coloredFont = FontFactory.getFont(FontFactory.COURIER, 15, baseColor);
            Paragraph chunk2 = new Paragraph("Kolor: " + car.getColor(), coloredFont); // akapit
            document.add(chunk2);
            Font font =  FontFactory.getFont(FontFactory.COURIER, 15, BaseColor.BLACK);
            Paragraph chunk3 = new Paragraph("rok: " + car.getYear(), font);
            document.add(chunk3);
            ArrayList<Airbag> airbags = car.getAirbag();
            Paragraph chunk4 = new Paragraph("poduszka: " + airbags.get(0).getName() + " - " + car.getAirbag().get(0).getValue());
            document.add(chunk4);
            Paragraph chunk5 = new Paragraph("poduszka: " + airbags.get(1).getName() + " - " + car.getAirbag().get(1).getValue());
            document.add(chunk5);
            Paragraph chunk6 = new Paragraph("poduszka: " + airbags.get(2).getName() + " - " + car.getAirbag().get(2).getValue());
            document.add(chunk6);
            Paragraph chunk7 = new Paragraph("poduszka: " + airbags.get(3).getName() + " - " + car.getAirbag().get(3).getValue());
            document.add(chunk7);
//            Image img = Image.getInstance("path_to_image.jpg");
//            document.add(img);
            document.close();
            car.setInvoice(true);
            return gson.toJson("faktura wygenerowana");
        });

        get("/invoices", (req,res) ->{
            Gson gson = new Gson();
            res.type("application/octet-stream"); //
            res.header("Content-Disposition", "attachment; filename=plik.pdf"); // nagłówek
            OutputStream outputStream = res.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of("invoices/"+req.queryParams("uuid")+".pdf"))); // response pliku do przeglądarki
            return gson.toJson("faktura pobrana");
        });

        post("/generateinvoice", (req,res) -> {
            Gson gson = new Gson();
            String seller = "Sprzedawca";
            String buyer = "Kupujacy";
            Invoices invoices = new Invoices(seller, buyer, cars, "Faktura za wszystkie auta");
            allCarsInvoicePath = invoices.generateInvoice();
            return gson.toJson("faktura wygenerowana");
        });

        post("/generateyearinvoice", (req,res) -> {
            Gson gson = new Gson();
            for(int i =0; i < cars.size(); i++){
                System.out.println(cars.get(i).getYear());
            }
            String seller = "Sprzedawca";
            String buyer = "Kupujacy";
            ReqHandler reqHandler = gson.fromJson(req.body(), ReqHandler.class);
            ArrayList<Car> yearCars = new ArrayList<>();
            System.out.println(reqHandler.getYear());
            for(int i = 0; i < cars.size(); i++)
                if(cars.get(i).getCustomDate().getYear() == reqHandler.getYear())
                    yearCars.add(cars.get(i));
            Invoices invoices = new Invoices(seller, buyer, yearCars, "Faktura za auta z roku: " + reqHandler.getYear());
            yearInvoicePath =  invoices.generateInvoice();
            return gson.toJson("faktura za rok wygenerowana");
        });

        post("/generatepriceinvoice", (req,res) -> {
            Gson gson = new Gson();
            String seller = "Sprzedawca";
            String buyer = "Kupujacy";
            ReqHandler reqHandler = gson.fromJson(req.body(), ReqHandler.class);
            ArrayList<Car> priceCars = new ArrayList<>();
            System.out.println(reqHandler.getBottom());
            for(int i = 0; i < cars.size(); i++) {
                int price = cars.get(i).getPrice();
                if (price >= reqHandler.getBottom() && price <= reqHandler.getTop())
                    priceCars.add(cars.get(i));
            }
            Invoices invoices = new Invoices(seller, buyer, priceCars, "Faktura za auta w cenach: " + reqHandler.getBottom() + "-" + reqHandler.getTop());
            priceInvoicePath = invoices.generateInvoice();
            return gson.toJson("faktura za cenę wygenerowana");
        });

        get("/getallcarsinvoice", (req,res) -> {
            Gson gson = new Gson();
            res.type("application/octet-stream"); //
            res.header("Content-Disposition", "attachment; filename=plik.pdf"); // nagłówek
            OutputStream outputStream = res.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of(allCarsInvoicePath))); // response pliku do przeglądarki
            return gson.toJson("faktura pobrana");
        });

        get("/getyearinvoice", (req,res) -> {
            Gson gson = new Gson();
            res.type("application/octet-stream"); //
            res.header("Content-Disposition", "attachment; filename=plik.pdf"); // nagłówek
            OutputStream outputStream = res.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of(yearInvoicePath))); // response pliku do przeglądarki
            return gson.toJson("faktura pobrana");
        });

        get("/getpriceinvoice", (req,res) -> {
            Gson gson = new Gson();
            res.type("application/octet-stream"); //
            res.header("Content-Disposition", "attachment; filename=plik.pdf"); // nagłówek
            OutputStream outputStream = res.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of(priceInvoicePath))); // response pliku do przeglądarki
            return gson.toJson("faktura pobrana");
        });

        get("/test", (req, res) -> "test");

    }

    static int getHerokuPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}
