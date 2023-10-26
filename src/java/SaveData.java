import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveData {

    String filePath = "src/resources/UserData.txt";
    private BufferedReader reader;
    private FileWriter writer;


    public SaveData(){
        try{
            reader = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public UserData inputData(){
        if(reader == null){
            System.out.println("Reader was not initialized");
            return new UserData();
        }
        try{
            String numberLine;
            while((numberLine = reader.readLine())!= null){
            String[] parts = numberLine.split(";");
            ArrayList<Upgrade> upgradeList = new ArrayList<Upgrade>();
            String upgradeLine = reader.readLine();
            String[] upgradeLineParts = upgradeLine.split(";");
            for(String upgrades : upgradeLineParts){
                String[] upgradeParts = upgrades.split("-");
                upgradeList.add(new Upgrade(upgradeParts[0],Integer.parseInt(upgradeParts[1]),
                        Double.parseDouble(upgradeParts[2]),Double.parseDouble(upgradeParts[3])));
            }
            UserData data = new UserData(Double.parseDouble(parts[0]),Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]), upgradeList);
            return data;
            }
            return new UserData();
        } catch(IOException e){
            e.printStackTrace();
            return new UserData();
        }
    
    }
    public void outputData(UserData data){
        try{
            writer = new FileWriter(filePath);
        } catch(IOException e){
            e.printStackTrace();
        }
        try{
            writer.write(data.outputData());
        } catch(IOException e){
            e.printStackTrace();
        }
        close();
    }
    public void close(){
        try{
            if(reader != null){
                reader.close();
            }
            if(writer != null){
                writer.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}