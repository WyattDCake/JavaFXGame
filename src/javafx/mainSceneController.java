import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.Random;

public class mainSceneController implements Initializable{

    private double clickCount;
    private double chickens;
    private double idle;
    private ArrayList<Upgrade> upgrades;
    private boolean upgradeChange;
    private Upgrade hundred = new Upgrade("Hundred Woo Hoo",100,100,100);
    private Upgrade gameWin = new Upgrade("Beat the game",1000,1000,1000);
    private SaveData savedData = new SaveData();
    private int numberOfChickens;
    private UserData data;
    private List<ImageView> chickenImages = new ArrayList<>();
    private Image backImage = new Image("background.png");
    private Image featherImage = new Image("feather.png");
    private Image nestsImage = new Image("nests.png");
    private Image roosterImage = new Image("rooster.png");
    @FXML
    private Label jokeLabel;
    @FXML
    private Label counterLabel;
    @FXML
    private Label clickLabel;
    @FXML
    private Label idleLabel;
    @FXML
    private Button counterButton;
    @FXML
    private Button upgradeButton;
    @FXML
    private ListView<Upgrade> upgradeListView = new ListView<>();
    @FXML
    private Button devButton;
    @FXML
    private TextField devField;
    @FXML
    private Pane mainPane;
    @FXML
    private ImageView backdrop;
    @FXML
    private ImageView feather;
    @FXML
    private ImageView nests;
    @FXML
    private ImageView rooster;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //Basic Set Up
        savedData = new SaveData();
        data = savedData.inputData();
        chickens = data.getChickens();
        counterLabel.setText(String.format("%.1f",chickens));
        clickCount = data.getClickCount();
        upgrades = data.getUpgrades();
        upgradeChange = true;
        idle = data.getIdle();
        numberOfChickens = 0;
        backdrop.setImage(backImage);
        feather.setImage(featherImage);
        nests.setImage(nestsImage);
        rooster.setImage(roosterImage);
        mainPane.getStylesheets().add(this.getClass().getResource("mainScene.css").toExternalForm());
        //by default disable all
        upgradeListView.setDisable(true);
        upgradeListView.setOpacity(0);
        upgradeButton.setDisable(true);
        upgradeButton.setOpacity(0);
        devButton.setOpacity(0);
        devButton.setDisable(true);
        devField.setOpacity(0);
        devField.setDisable(true);
        jokeLabel.setDisable(true);

        //if no upgrades have been bought dont show click count or idle count
        //if upgrade has been bought, show upgrade button even if under 15
        if(clickCount == 1.0){
            clickLabel.setOpacity(0);
        }else{
            clickLabel.setText(String.format("Click Amount: %.1f", clickCount));
            upgradeButton.setDisable(false);
            upgradeButton.setOpacity(1);
        }
        if(idle == 0.0){
            idleLabel.setOpacity(0);
        }else{
            idleLabel.setText(String.format("Chickens Per Second: %.2f",idle));
            upgradeButton.setDisable(false);
            upgradeButton.setOpacity(1);
        }
        //Background Upgrade Button Task
        ScheduledService<Double> unlockButtons = new ScheduledService<Double>() {
            @Override
            protected Task<Double> createTask() {
                Task<Double> task = new Task<Double>() {
                    @Override
                    protected Double call() throws Exception{
                        if(chickens >= 15.0 && upgradeButton.getOpacity() == 0){
                            Platform.runLater(() -> showButton());
                        }
                        if(chickens >= 100.0 && upgrades.size()==3){
                            Platform.runLater(() -> addUpgrade());
                        }
                        if(chickens >= 500.0 && upgrades.size()==4){
                            Platform.runLater(() -> addUpgrade());
                        }
                        return null;
                }
                };
                return task;
            }
            
        };
        unlockButtons.setPeriod(Duration.seconds(1));
        unlockButtons.start();
        //Input Upgrade List
        ScheduledService<Double> setUpgrades = new ScheduledService<Double>() {
            @Override
            protected Task<Double> createTask() {
                Task<Double> task = new Task<Double>() {
                    @Override
                    protected Double call() throws Exception{
                        if(upgradeChange == true){
                        Platform.runLater(() -> {
                            upgradeListView.getItems().setAll(upgrades);
                            upgradeChange = false;
                        
                        });}
                        return null;
                }
                };
                return task;
            }
            
        };
        setUpgrades.setPeriod(Duration.millis(100));
        setUpgrades.start();
        //Take Upgrade Inputs
        upgradeListView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                int index = upgradeListView.getSelectionModel().getSelectedIndex();
            if(index != -1){
                index++;
                switch(index){
                    case 1 :
                        upgradeIdleChickens();
                        break;
                    case 2 :
                        upgradeClickCount();
                        break;
                    case 3 :
                        reset();
                        break;
                    case 4 :
                        hundred();
                        break;
                    case 5 :
                        winGame();
                        break;
                        
                    default:
                        break;
                }
            }
        }
        });
        //Idle Chicken System
         ScheduledService<Double> idleSystem = new ScheduledService<Double>() {
            @Override
            protected Task<Double> createTask() {
                Task<Double> task = new Task<Double>() {
                    @Override
                    protected Double call() throws Exception{
                        Platform.runLater(() -> {
                        chickens+=idle;
                        counterLabel.setText(String.format("%.1f",chickens));
                        });
                        return null;
                }
                };
                return task;
            }
            
        };
        idleSystem.setPeriod(Duration.seconds(1));
        idleSystem.start();
        //Display Chicken System
        ScheduledService<Double> chickenSystem = new ScheduledService<Double>() {
            @Override
            protected Task<Double> createTask() {
                Task<Double> task = new Task<Double>(){
                @Override
                protected Double call() throws Exception{
                    int currentChickens = (int)chickens / 10;
                    while(currentChickens > numberOfChickens){
                        Image image = getRandomChicken();
                        ImageView imageView = new ImageView(image);
                        Random random = new Random();
                        int xPlacement = random.nextInt(1720);
                        int yPlacement = random.nextInt(967);
                        imageView.setX(xPlacement);
                        imageView.setY(yPlacement);
                        numberOfChickens++;
                        chickenImages.add(imageView);
                        Platform.runLater(() ->{
                            mainPane.getChildren().add(imageView);
                            imageView.toBack();
                            backdrop.toBack();
                        });
                    }
                    while(currentChickens < numberOfChickens){
                        numberOfChickens--;
                        ImageView removedImage = chickenImages.remove(0);
                        Platform.runLater(() -> mainPane.getChildren().remove(removedImage));
                    }
                    return null;
                }
                };
                return task;
            }
            
        };
        chickenSystem.setPeriod(Duration.seconds(1));
        chickenSystem.start();
    }

    @FXML
    public void increaseCount(ActionEvent event){
        chickens+=clickCount;
        counterLabel.setText(String.format("%.1f",chickens));
    }

    @FXML 
    public void showButton(){
        upgradeButton.setDisable(false);
        upgradeButton.setOpacity(1);
    }
    @FXML
    public void toggleUpgradeScreen(ActionEvent event){
        if(upgradeListView.getOpacity()==0){
            upgradeListView.setDisable(false);
            upgradeListView.setOpacity(1);
            Platform.runLater(() -> upgradeButton.setText("Close Upgrades"));
        } else if (upgradeListView.getOpacity()==1){
            upgradeListView.setOpacity(0);
            upgradeListView.setDisable(true);
            Platform.runLater(() -> upgradeButton.setText("Upgrades"));
        }
    }
    @FXML
    public void upgradeClickCount(){
        if(chickens >= upgrades.get(1).getCost()){
        chickens-=upgrades.get(1).getCost();
        clickCount*=upgrades.get(1).getModifier();
        upgrades.get(1).upgraded();
        upgradeChange = true;
        Platform.runLater(() -> {
            counterLabel.setText(String.format("%.1f",chickens));
            clickLabel.setText(String.format("Click Amount: %.1f", clickCount));
            if(clickLabel.getOpacity() == 0){
                clickLabel.setOpacity(1);
            }
        });

        }
    }
    @FXML
    public void upgradeIdleChickens(){
        if(chickens >= upgrades.get(0).getCost() && upgrades.get(0).getLevel() < 10){
            chickens-=upgrades.get(0).getCost();
            idle+=upgrades.get(0).getModifier();
            upgrades.get(0).upgraded();
            upgradeChange = true;
            Platform.runLater(()->{
                counterLabel.setText(String.format("%.1f",chickens));
                idleLabel.setText(String.format("Chickens Per Second: %.2f",idle));
                if(idleLabel.getOpacity() == 0){
                    idleLabel.setOpacity(1);
                }
            });
        }
    }
    
    @FXML
    public void exitAndSave(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        data = new UserData(clickCount,chickens, idle, upgrades);
        savedData.outputData(data);
    }
    @FXML
    public void reset(){
        //Reset Data
        data = new UserData();
        chickens = data.getChickens();
        Platform.runLater(()->counterLabel.setText(String.format("%.1f",chickens)));
        clickCount = data.getClickCount();
        upgrades = data.getUpgrades();
        upgradeChange = true;
        idle = data.getIdle();
        //Reset Game
        upgradeListView.setDisable(true);
        upgradeListView.setOpacity(0);
        upgradeButton.setDisable(true);
        upgradeButton.setOpacity(0);
        Platform.runLater(() -> upgradeButton.setText("Upgrades"));
        clickLabel.setOpacity(0);
        idleLabel.setOpacity(0);
        if(upgrades.size() > 3){
        for(int i = upgrades.size(); i > 2;i--){
            upgrades.remove(i);
        }
        }   
    }

    public void addUpgrade(){
        if(upgrades.size()==3){
            upgrades.add(hundred);
        }else if(upgrades.size()==4){
            upgrades.add(gameWin);
        }
        upgradeChange = true;
    }
    @FXML
    public void hundred(){
        chickens-=upgrades.get(3).getCost();
        Platform.runLater(()-> {
            jokeLabel.setText("That Was a Waste!");
            jokeLabel.setOpacity(1);
        });
            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> scheduledFuture = service.schedule(() -> {
                Platform.runLater(() -> jokeLabel.setOpacity(0));}
                ,10,TimeUnit.SECONDS);
            service.shutdown();
            scheduledFuture.isCancelled();
    }
    @FXML
    public void winGame(){
        if(chickens >= upgrades.get(4).getCost()){
        chickens-=upgrades.get(4).getCost();
        Alert winGameAlert = new Alert(AlertType.INFORMATION);
        winGameAlert.getDialogPane().getStylesheets().add(this.getClass().getResource("mainScene.css").toExternalForm());
        winGameAlert.setHeaderText("You Win!");

        winGameAlert.showAndWait().ifPresent(response -> {
            savedData.outputData(new UserData());
            Platform.exit();
        });
       }
    }

    @FXML
    public void toggleDevControls(ActionEvent event){
        Platform.runLater(()->{
        if(devButton.getOpacity()==0){
            devButton.setDisable(false);
            devButton.setOpacity(1);
            devField.setDisable(false);
            devField.setOpacity(1);
        } else if (devButton.getOpacity()==1){
            devButton.setOpacity(0);
            devButton.setDisable(true);
            devField.setOpacity(0);
            devField.setDisable(true);
        }});
    }
    
    @FXML
    public void addDevChickens(ActionEvent event){
        double devChickens = Double.parseDouble(devField.getText());
        chickens+=devChickens;
        Platform.runLater(()->counterLabel.setText(String.format("%.1f",chickens)));
    }
    public Image getRandomChicken(){
        Random random = new Random();
        int index = random.nextInt(5);
        switch(index){
            case 0:
                return new Image("grace.png");
            case 1:
                return new Image("fluffy.png");
            case 2:
                return new Image("nora.png");
            case 3:
                return new Image("mildred.png");
            case 4:
                return new Image("barbara.png");
            default:
                return new Image("barbara.png");
        }
    }
}   
