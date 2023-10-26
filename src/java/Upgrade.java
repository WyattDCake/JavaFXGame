public class Upgrade {
    private String upgradeName;
    private int upgradeLevel;
    private double upgradeCost;
    private double upgradeModifier;

    public Upgrade(String n,int l, double c, double m){
        this.upgradeName = n;
        this.upgradeLevel = l;
        this.upgradeCost = c;
        this.upgradeModifier = m;
    }
    
    public String getName(){
        return this.upgradeName;
    }

    public int getLevel(){
        return this.upgradeLevel;
    }

    public double getCost(){
        return this.upgradeCost;
    }

    public double getModifier(){
        return this.upgradeModifier;
    }

    public void setName(String n){
        this.upgradeName = n;
    }

    public void setLevel(int l){
        this.upgradeLevel = l;
    }

    public void setCost(double c){
        this.upgradeCost = c;
    }

    public void setModifier(double m){
        this.upgradeModifier = m;
    }

    public void upgraded(){
        upgradeLevel++;
        upgradeCost*=1.25;
        upgradeModifier+=(upgradeModifier*2);
    }
    
    public String outputData(){
        return (this.upgradeName+"-"+this.upgradeLevel+"-"+this.upgradeCost+"-"+this.upgradeModifier);
    }

    public String toString(){
        return (this.upgradeName+" lvl."+this.upgradeLevel+"\n"+"Cost: "
            +String.format("%.2f",this.upgradeCost)+" Upgrade: +"
            +String.format("%.2f",this.upgradeModifier));
    }
    
}
