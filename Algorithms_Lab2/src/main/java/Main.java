import Charts.TimeDiagramChart;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Charts.VectorChart.createVectorChart("VectorDiagram");
        TimeDiagramChart.createAnalogChart("Phase A", 0);
        TimeDiagramChart.createAnalogChart("Phase B", 1);
        TimeDiagramChart.createAnalogChart("Phase C", 2);
        TimeDiagramChart.createDiscreteChart("Flag", 0);

        TreatmentDataManager inD = new TreatmentDataManager();
        inD.start();
    }
}
