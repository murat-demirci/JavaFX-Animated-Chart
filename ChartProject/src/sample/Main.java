package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Main extends Application {

    static Scene firstScene;
    private static File file;
    private static String selectedType;
    private boolean doesFileSelected = false;
    private TextField url_field;
    private String url;

    @Override
    public void start(Stage stage) {

        Label label = new Label("Select a file by pressing button");
        //label.getStyleClass().add("select_file_label");
        label.setStyle("-fx-text-fill: white;" +
                "    -fx-font-size: 24px;" +
                "    -fx-translate-x: 50;" +
                "    -fx-translate-y: 50;");

        Label print_file_path = new Label("No File Selected Yet!");
        print_file_path.setStyle("-fx-text-fill: white;" +
                "    -fx-font-size: 12px;" +
                "    -fx-translate-x: 160;" +
                "    -fx-translate-y: 140;");
        //print_file_path.getStyleClass().add("print_file_path");

        Button btn_select_file = new Button("Select a file");
        //btn_select_file.getStyleClass().add("select_file_button");
        btn_select_file.setStyle("-fx-background-color: #cde3d6;" +
                "-fx-translate-x: 175;"+"-fx-translate-y: 110;");

        url_field = new TextField();
        url_field.setPromptText("Enter a URL");
        url_field.setTranslateX(140);
        url_field.setTranslateY(165);
        //url_field.getStyleClass().add("url_field");

        Button btn_bar = new Button("Bar Chart");
        //btn_bar.getStyleClass().add("btn_chart_type");
        btn_bar.setStyle("-fx-background-color: #cde3d6");
        btn_bar.setTranslateY(200);
        btn_bar.setTranslateX(140);

        Button btn_line = new Button("Line Chart");
        //btn_line.getStyleClass().add("btn_chart_type");
        btn_line.setStyle("-fx-background-color: #cde3d6");
        btn_line.setTranslateY(200);
        btn_line.setTranslateX(220);

        btn_select_file.setOnAction(actionEvent -> chooseFile(stage, print_file_path));
        btn_bar.setOnAction(actionEvent -> {
            selectedType = "Bar";
            chartButtonActionEvents(btn_bar, stage);

        });
        btn_line.setOnAction(actionEvent -> {
            selectedType = "Line";
            chartButtonActionEvents(btn_line, stage);

        });

        Pane root = new Pane();
        root.getChildren().addAll(btn_select_file, label, btn_bar, btn_line, print_file_path, url_field);

        root.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #db0eba, #7994f6); -fx-min-height: 400; -fx-max-width: 400; -fx-max-height: 400; -fx-min-width: 400");

        firstScene = new Scene(root,400,300);
        //firstScene.getStylesheets().add("sample/myStylesheets/myStyles.css");
        stage.setScene(firstScene);
        stage.setTitle("File Chooser");
        stage.show();
    }

    private void chooseFile(Stage stage, Label print_file_path){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        String fileName = selectedFile.getName();
        String fileExtension = fileName.substring(fileName.length()-3);
        if((!fileExtension.equals("xml")) && (!fileExtension.equals("txt"))){
            System.out.println("Yanlış dosya seçimi");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Select .xml or .txt File", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        else {
            file = new File(selectedFile.getPath());
            print_file_path.setText("File has been selected!");
            doesFileSelected = true;
            url_field.setVisible(false);
            url_field.clear();
        }
    }

    private void chartButtonActionEvents(Button button,  Stage stage){
        url = url_field.getText();

        try {
            file = new File(new URL(url).toURI());
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
        if (!doesFileSelected && url.length()<=0){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Select a File or Enter an URL Before Clicking Chart Type Button!", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        else if (doesFileSelected && url.length() > 0){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Just Select a File or Enter an URL", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        else if (url.length() > 0 && !isValidUrl()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Enter a Valid URL", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        else{
            stage.close();
            updateScene(stage, button);
        }
    }

    private void updateScene(Stage stage, Button button){
        ReadData readData = new ReadData();
        if (selectedType.equals("Bar")){
            BarChart barChart = new BarChart(readData.getTitle(), readData.getXlabel());
            barChart.drawStaticLineChart().setTranslateX(20);
            System.out.println("Bar secildi");
            Label label = barChart.printDates();
            label.setTranslateY(500);
            Button stop_button = barChart.startStopButton();
            stop_button.setTranslateY(350);
            HBox root = new HBox();
            //Label label = new Label("Bar sayfasi");
            root.getChildren().addAll(barChart.drawStaticLineChart(), label, selectNewFileButton(stage), stop_button);
            stage.setScene(new Scene(root, 900,800));
        }
        else {
            LineChart lineChart = new LineChart(readData.getTitle(), readData.getXlabel());
            HBox root = new HBox();
            Button stop_button = lineChart.startStopButton();
            stop_button.setTranslateY(350);
            //Label label = new Label("Line Secildi");
            root.getChildren().addAll(lineChart.lineChartDraw() ,selectNewFileButton(stage),stop_button);
            stage.setScene(new Scene(root, 1200,1000));
        }
        stage.setTitle("next page "+button.getText());
        stage.show();
    }

    private Button selectNewFileButton(Stage stage){
        //buraya önceden eklenen dosyanın boş bir dosya şeklinde
        //tanımlanması eklenecek
        //dosya seçilmeden direkt url girildiğinde hatalar var
        Button select_new_file_btn = new Button("Select New File");
        select_new_file_btn.setTranslateY(390);
        select_new_file_btn.setTranslateX(50);
        select_new_file_btn.setOnAction(actionEvent -> stage.setScene(firstScene));
        url_field.setVisible(true);
        return select_new_file_btn;
    }

    private boolean isValidUrl(){
        try {
            new URL(url).openStream().close();
            return true;
        } catch (Exception ignored) { }
        return false;
    }

    public String getUrl() {
        return url;
    }
    public String getChartType(){
        return selectedType;
    }
    public File getFile() {
        return file;
    }
    public String getFileFormat(){
        return url != null ? "xml" : file.getName().substring(file.getName().length()-3);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

