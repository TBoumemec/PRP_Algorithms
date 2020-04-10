import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Charts.createAnalogChart("Phase A", 0);
        Charts.createAnalogChart("Phase B", 1);
        Charts.createAnalogChart("Phase C", 2);

        Charts.createDiscreteChart("Flag", 0);

        TreatmentData inD = new TreatmentData();
        inD.start();
    }
}
