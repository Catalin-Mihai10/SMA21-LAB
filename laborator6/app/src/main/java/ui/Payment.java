package ui;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Payment implements Serializable {

    public String timestamp;
    private double cost;
    private String name;
    private String type;

    public Payment() { }

    public Payment(String timestamp, double cost, String name, String type) {
        this.timestamp = timestamp;
        this.cost = cost;
        this.name = name;
        this.type = type;
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp(){ return timestamp;}

    public Payment copy(){
        Payment copyOfPayment = new Payment(this.timestamp, this.cost, this.name, this.type);
        return copyOfPayment;
    }
}