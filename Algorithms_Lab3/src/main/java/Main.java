import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        TimeDiagramChart.createAnalogChart("U", 0);
        TimeDiagramChart.createAnalogChart("I", 1);
        TimeDiagramChart.createAnalogChart("Impedance", 2);
        TimeDiagramChart.createDiscreteChart("Flag", 0);
        ChartsXY.createAnalogChart("PUK", 0);

        ProcessDataManager inD = new ProcessDataManager();
        inD.start();

    }
}
