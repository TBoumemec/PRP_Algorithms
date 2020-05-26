import java.util.HashMap;

/**
 * Класс хранения и обработки векторов нулевой последовательности
 */
public class ZeroSeqSignal {

    private HashMap<String, Double> i30;
    private HashMap<String, Double> u30;
    private HashMap<String, Double> s30;

    private void setI30(Vector vecI){
        i30 = new HashMap<>();
        i30 = getZeroSeqVector(vecI);
    }

    public HashMap<String, Double> getI30() {
        return i30;
    }

    public double getI30RMS(){
        return i30.get("module");
    }

    public double getU30RMS() {
        return u30.get("module");
    }

    public double getS30RMS() {
        return s30.get("module")*Math.cos(s30.get("angle"));
    }

    public double getS30Angle(){
        return s30.get("angle");
    }

    private void setU30(Vector vecU){
        u30 = new HashMap<>();
        u30 = getZeroSeqVector(vecU);
    }

    public void setPower(){
        s30 = new HashMap<>();

        s30.put("module", u30.get("module") * i30.get("module"));
        s30.put("angle", u30.get("angle") - i30.get("angle"));
    }

    public void setPower(Vector vecI, Vector vecU) {

        setI30(vecI);
        setU30(vecU);

        s30 = new HashMap<>();

        s30.put("module", u30.get("module") * i30.get("module"));
        s30.put("angle",(2*Math.PI +  u30.get("angle") - i30.get("angle"))%(2*Math.PI));
    }

    private double time = 0;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public HashMap<String, Double> getZeroSeqVector(Vector vec) {

        double re = 0, im = 0;

        for (int i = 0; i < 3; i++) {
            re += vec.Vec_getReal(i);
            im += vec.Vec_getImag(i);
        }

        return Processor.createVectorForm(re * 3, im * 3);
    }
}
