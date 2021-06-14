package sample;

public abstract class Chart {
    private String title;
    private String xAxisLabel;

    ReadData readData = new ReadData();

    public Chart(String title, String xAxisLabel){
        this.title = title;
        this.xAxisLabel = xAxisLabel;
    }

    public void setTitle() {
        this.title = readData.getTitle();
    }

    public String getTitle() {
        return title;
    }
    public String getxAxisLabel() {
        return xAxisLabel;
    }
}
