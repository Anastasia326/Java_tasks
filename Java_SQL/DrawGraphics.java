import java.awt.*;
import java.io.File;
import org.jfree.chart.ChartUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class DrawGraphics<T extends Format> {
    public void Draw(Pair<ArrayList<String>,ArrayList<T>> data, String nameOfFile, String nameOfPlot,  int size) throws IOException {
        JFreeChart freeChart;
        File file = new File(nameOfFile);
        FileOutputStream stream = new FileOutputStream(file);

        //обработка данных в нужный формат
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 0; i < size; ++i) {
            dataset.addValue(Integer.valueOf(data.getSecond().get(i).getData().get(1).toString()),
                    "", data.getSecond().get(i).getData().get(0).toString());
        }

        //"графическая обработка"
        String label = data.getFirst().get(1);
        freeChart = ChartFactory.createBarChart(nameOfPlot, data.getFirst().get(0), label, dataset,
                PlotOrientation.VERTICAL, true, true, false);
        Plot plot = freeChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);

        CategoryPlot categoryPlot = freeChart.getCategoryPlot();
        //добавим сетку
        categoryPlot.setDomainGridlinesVisible(true);
        categoryPlot.setDomainGridlinePaint(Color.black);
        categoryPlot.setRangeGridlinesVisible(true);
        categoryPlot.setRangeGridlinePaint(Color.black);

        ChartUtils.writeChartAsPNG(stream, freeChart, 1000, 1000);
    }

}
