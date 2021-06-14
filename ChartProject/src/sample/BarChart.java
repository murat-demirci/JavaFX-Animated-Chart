package sample;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BarChart extends Chart{

    public BarChart(String title, String xAxisLabel) {
        super(title, xAxisLabel);
        System.out.println("Bar chart cons. calisti");
        xAxis.setTickLabelRotation(90);
        xAxis.setLabel(xAxisLabel);
        barChart.setTitle(title);
        setSeriesArrayList();
        setAllCategories();
        setHexColors();
    }

    private final NumberAxis xAxis = new NumberAxis();
    private final CategoryAxis yAxis = new CategoryAxis();

    private final javafx.scene.chart.BarChart<Number, String> barChart = new javafx.scene.chart.BarChart<>(xAxis, yAxis);

    Bar bar = new Bar();
    private final List<List<Bar>> barArray = bar.sublistedArray();

    XYChart.Series<Number, String> series = new XYChart.Series<>();

    final int[] i = {0};

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
        barChart.getData().clear();
        setSeries(i[0]++);
        barChart.getData().add(series);
    }));

    Label label = new Label();
    final int[] j = {0};

    Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
        label.setText("Date: "+getDate(j[0]++));
    }));

    private AtomicBoolean isStop = new AtomicBoolean(false);

    public Button startStopButton(){
        Button button = new Button();
        button.setText("Stop");
        button.setOnAction(actionEvent -> {
            if (!isStop.get()){
                timeline.stop();
                timeline2.stop();
                button.setText("Start");
                isStop.set(true);
            }
            else {
                timeline.play();
                timeline2.play();
                button.setText("Stop");
                isStop.set(false);
            }
        });
        return button;
    }

    public javafx.scene.chart.BarChart<Number, String> drawStaticLineChart(){


        timeline.setCycleCount(seriesArrayList.size());
        timeline.setAutoReverse(false);
        timeline.play();

        //barChart.getData().add(seriesArrayList.get(0));
        barChart.setLegendVisible(false);
        barChart.setAnimated(true);
        return barChart;
    }

    public Label printDates(){

        timeline2.setCycleCount(seriesArrayList.size());
        timeline2.setAutoReverse(false);
        timeline2.play();

        label.setFont(Font.font(18));
        return label;
    }

    private String getDate(int i){
        return barArray.get(i).get(0).getDate();
    }

    private void setSeries(int i){
        series = seriesArrayList.get(i);
    }

    private final ArrayList<XYChart.Series<Number, String>> seriesArrayList = new ArrayList<>();


    private void setSeriesArrayList(){
        for (List<Bar> bars : barArray) {
            XYChart.Series<Number, String> series = new XYChart.Series<>();
            for (int j = bars.size() - 1; j >= 0; j--) {
                Bar bar = bars.get(j);
                XYChart.Data<Number, String> data = new XYChart.Data<>(bar.getValue(), bar.getName());
                data.nodeProperty().addListener((observableValue, node, t1) -> {
                    if (t1 != null){
                        int index = allCategories.indexOf(bar.getCategory());
                        String color = hexColors.get(index);
                        t1.setStyle("-fx-bar-fill: " + color);
                    }
                });
                series.getData().add(data);
            }
            seriesArrayList.add(series);
        }
    }

    private final ArrayList<String> allCategories = new ArrayList<>();

    private void setAllCategories(){
        for (List<Bar> bars : barArray){
            for (Bar bar : bars){
                if (!allCategories.contains(bar.getCategory()))
                    allCategories.add(bar.getCategory());
            }
        }
    }

    private final List<String> hexColors = new ArrayList<>();

    private void setHexColors(){
        hexColors.addAll(Arrays.asList(
                "#80b1d3", "#fdb462", "#b3de69", "#fccde5", "#8dd3c7",
                "#ffffb3", "#bebada", "#fb8072", "#d9d9d9", "#bc80bd",
                "#ccebc5", "#ffed6f", "#aec7e8", "#c5b0d5", "#c49c94",
                "#dbdb8d", "#17becf","#9edae5", "#f7b6d2", "#c7c7c7",
                "#1f77b4", "#ff7f0e","#ffbb78", "#98df8a", "#d64c4c",
                "#2ca02c", "#9467bd","#8c564b", "#ff9896", "#e377c2",
                "#7f7f7f", "#bcbd22"));
    }
}
