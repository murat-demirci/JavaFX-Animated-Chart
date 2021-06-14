package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class LineChart extends Chart {
    private ScheduledExecutorService scheduledExecutorService;
    Line line=new Line();
    private final NumberAxis yAxis = new NumberAxis();
    private final CategoryAxis xAxis = new CategoryAxis();
    final javafx.scene.chart.LineChart<String,Number> lineChart=new javafx.scene.chart.LineChart(xAxis,yAxis);
    private ArrayList<XYChart.Series>seriesList=new ArrayList<>();
    ArrayList<Integer> seriesNumber=new ArrayList<>();
    ArrayList<XYChart.Series> newSeries=new ArrayList<>();
    ArrayList<String> dateList=new ArrayList<>();


    public LineChart(String title, String xAxisLabel) {
        super(title, xAxisLabel);
        lineChart.setTitle(super.getTitle());
        yAxis.setLabel(super.getxAxisLabel());
        yAxis.setAnimated(false);
        xAxis.setAnimated(false);
    }
    final int[] i={0};
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ActionEvent->{
        lineChart.getData().clear();
        lineChart.getData().add(newSeries.get(i[0]++));
    }));

    public javafx.scene.chart.LineChart<String,Number> lineChartDraw(){
        setSeries();
        setData();
        System.out.println();
        //lineChart.getData().add(newSeries.get(0));
        timeline.setCycleCount(newSeries.size());
        timeline.setAutoReverse(false);
        timeline.play();


        lineChart.setMinSize(1000,800);
        lineChart.setCreateSymbols(false);
        //lineChart.autosize();

        return lineChart;
    }
    private AtomicBoolean isStop = new AtomicBoolean(false);
    public Button startStopButton(){
        Button button = new Button();
        button.setText("Stop");
        button.setOnAction(actionEvent -> {
            if (!isStop.get()){
                timeline.stop();
                button.setText("Start");
                isStop.set(true);
            }
            else {
                timeline.play();
                button.setText("Stop");
                isStop.set(false);
            }
        });
        return button;
    }

    private void  setSeries() {
        for(int j=0;j<line.byName.size();j++){
            XYChart.Series series=new XYChart.Series();//serileri isimlendirme ve Arrayliste ekleme.
            series.setName(line.byName.get(j).getName());
            seriesList.add(series);
        }
        //System.out.println(seriesList);

        boolean frag=false;
        while(seriesList.size()!=0){
            for (int i=0;i<seriesList.size();i++){
                if (seriesList.get(0).getName().equals(seriesList.get(i).getName())){
                    if (frag){
                        newSeries.add(seriesList.get(0));
                        frag=false;
                    }
                    if(seriesNumber.size()==1){ //olusturulan Arraylist<Series> de bulunan tekrar eden seri isimlerini ayiklama
                        frag=true;
                    }
                    seriesNumber.add(i);
                }
            }

            for (int i=seriesNumber.size()-1;i>=0;i--){
                int index=seriesNumber.get(i);           //silme islemi
                seriesList.remove(index);
            }
            seriesNumber.clear();
        }

        //System.out.println(newSeries)
    }
    private ArrayList<XYChart.Data> dataList=new ArrayList<>();
    private void setData(){
        for (int j=0;j<newSeries.size();j++) {
            for (int i = 0; i < line.byName.size(); i++) {                      //olusturulan serilere isimlerine uyumlu verilerin eklenmesi
                if (newSeries.get(j).getName().equals(line.byName.get(i).getName())) {
                    XYChart.Data data =new XYChart.Data<>(line.byName.get(i).getDate(), line.byName.get(i).getValue(),line.byName.get(i).getCategory());
                    dateList.add(line.byName.get(i).getDate());
                    dataList.add(data);
                    newSeries.get(j).getData().add(data);
                }
                //System.out.println(newSeries.get(i).getData());
            }
        }
        Set<String> s=new HashSet<>(dateList);
        dateList.clear();
        dateList.addAll(s);
        Collections.sort(dateList);
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

