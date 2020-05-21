import java.util.HashMap;

class ImpedanceManager {

    private int n = 80; // количество снятий сигнала за период
    private int k = 5;
    private int k1; // масштабные коэффициенты
    private double[][] bufferU = new double[3][n]; // буфер памяти
    private double[][] bufferI = new double[3][n]; // буфер памяти
    private HashMap<String, Double> impedance;
    private double delta_t = 2 * Math.PI / n; // шаг дискретизации
    private int count = 1;
    private int phase; // обрабатываемая фаза
    private double[] actTime = {0,0,0};
    private double[] actU = {0,0,0};
    private double[] actI = {0,0,0};
    private DigitSignal digitSignal;
    private BlockManager bl;

    void setBlockManager(BlockManager bl){
        this.bl = bl;
    }

    void setDS(DigitSignal digitSignal){
        this.digitSignal = digitSignal;
    }

    DigitSignal getDigitSignal(){return digitSignal;}

    void setImpedance(double meanU, double meanI, int phase) {
        k1 = 1000;

        this.phase = phase;

        bufferU[phase][count] = meanU;
        bufferI[phase][count] = meanI;

        HashMap<String, Double> mapU = filterFourier(bufferU);
        HashMap<String, Double> mapI = filterFourier(bufferI);
        bl.setMeanI(phase, (mapI.get("modul")-actI[phase])*k1/(digitSignal.getTime()-actTime[phase]));
        actU[phase] = meanU;
        actI[phase] = meanI;
        actTime[phase] = digitSignal.getTime();

        impedance = new HashMap<>();
        impedance.put("modul", mapU.get("modul")/mapI.get("modul"));
        impedance.put("angle", mapU.get("angle")-mapI.get("angle"));

        digitSignal.setMean(phase, impedance.get("modul")/Math.sqrt(2));

        if (phase == 2) if (++count == n) count = 0;
    }

    private HashMap<String, Double> filterFourier(double[][] buffer){
        double Fx = calcDiscreteReal(buffer[phase]);
        double Fy = calcDiscreteImag(buffer[phase]);

        double mod = getAmplitude(Fx, Fy);
        double angle = getPhase(Fx, Fy, mod);
        HashMap<String, Double> map = new HashMap<>();

        map.put("modul", mod);
        map.put("angle", angle);

        return map;
    }

    private double calcDiscreteReal(double[] buffer) {
        double Harm = 0;
        for (int i = 0; i < buffer.length; ++i) {

            Harm += buffer[i] * Math.cos(delta_t * i) / (n);
        }
        return Harm;
    }

    private double calcDiscreteImag(double[] buffer) {
        double Harm = 0;
        for (int i = 0; i < buffer.length; ++i) {

            Harm += buffer[i] * Math.sin(delta_t * i) / (n);
        }
        return Harm;
    }

    double getImpRealMean(){
        return impedance.get("modul")*Math.cos(impedance.get("angle")/360*Math.PI);
    }

    double getImpImagMean(){
        return impedance.get("modul")*Math.sin(impedance.get("angle")/360*Math.PI);
    }

    double getImpModul(){
        return impedance.get("modul");
    }

    double getImpAngle(){
        return impedance.get("angle");
    }

    double getPhase(double Fx, double Fy, double z) {
        if (Fy > 0) {
            return Math.acos(Fx / z);
        } else if (Fx < 0) {
            return Math.PI - Math.asin(Fy / z);
        } else return Math.PI * 2 + Math.asin(Fy / z);
    }

    double getAmplitude(double Fx, double Fy) {
        return Math.sqrt(Math.pow(Fx, 2) + Math.pow(Fy, 2));
    }

}
