import java.util.HashMap;

/**
 * Класс представления данных в виде вектора
 */
public class Vector {

    private HashMap<String, Double> phA;
    private HashMap<String, Double> phB;
    private HashMap<String, Double> phC;

    public void setMean(int phase, HashMap<String, Double> mean) {
        switch (phase) {
            case (0):
                this.phA = mean;
                break;
            case (1):
                this.phB = mean;
                break;
            case (2):
                this.phC = mean;
                break;
            default:
                System.out.println("StupidSetError");
        }
    }

    public HashMap<String, Double> getMean(int phase) {
        switch (phase) {
            case (0):
                return phA;
            case (1):
                return phB;
            case (2):
                return phC;
            default:
                System.out.println("StupidGetError");
                return null;
        }
    }

    public double Vec_getReal(int phase){
        HashMap<String,Double> map = this.getMean(phase);
        return map.get("module")*Math.cos(map.get("angle"));
    }

    public double Vec_getImag(int phase){
        HashMap<String,Double> map = this.getMean(phase);
        return map.get("module")*Math.sin(map.get("angle"));
    }

}
