import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Charts.createAnalogChart("Фаза А", 0);
        Charts.addSeries("Фаза A (sv)", 0 , 0);
        Charts.addSeries("Фаза A (rms)", 0 , 1);
        Charts.addSeries("Уставка", 0 , 2);
        Charts.createDiscreteChart("Flag", 0);

        InputData inD = new InputData();
        inD.start();
    }
}
