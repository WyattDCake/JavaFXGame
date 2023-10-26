import java.util.ArrayList;

public class UserData {
    private double clickCount;
    private double chickens;
    private double idle;
    private ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();
    private Upgrade idleChickens = new Upgrade("Idle Chickens", 1, 5, 0.1);
    private Upgrade increase = new Upgrade("Increase", 1, 20, 1.05);
    private Upgrade reset = new Upgrade("Reset", 1, 0, 0);

    public UserData(){
        this.clickCount = 1;
        this.chickens = 0;
        this.idle = 0;
        upgrades = new ArrayList<Upgrade>();
        upgrades.add(idleChickens);
        upgrades.add(increase);
        upgrades.add(reset);
    }
    
    public UserData(double cc, double c, double i, ArrayList<Upgrade> u){
        this.clickCount = cc;
        this.chickens = c;
        this.idle = i;
        upgrades = u;
    }

    public String outputData(){
        String output = (clickCount+";"+chickens+";"+idle+"\n");
        for(Upgrade u : upgrades){
            output+=(u.outputData()+";");
        }
        return output;
    }

    public double getClickCount(){
        return this.clickCount;
    }
    public double getChickens(){
        return this.chickens;
    }
    public double getIdle(){
        return this.idle;
    }
    public ArrayList<Upgrade> getUpgrades(){
        return this.upgrades;
    }
}
