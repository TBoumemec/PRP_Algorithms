import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        TimeDiagramChart.createAnalogChart("U", 0, 3);
        TimeDiagramChart.createAnalogChart("I", 1, 3);
        TimeDiagramChart.createAnalogChart("3U0, 3SO", 2, 2);
        TimeDiagramChart.createAnalogChart("3I0", 3, 2);
        TimeDiagramChart.createDiscreteChart("Flag", 0);
        TimeDiagramChart.createDiscreteChart("Block", 1);

        DataManager inD = new DataManager();
        inD.start();

    }

}
