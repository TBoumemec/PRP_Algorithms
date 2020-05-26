import java.util.HashMap;

/**
 * Класс отвечает за цифровую обработку сигнала и
 * выполнение различных математических операций
 */
class Processor {

    private int n = 80; // количество снятий сигнала за период

    private double[][] bufferU = new double[3][n]; // буфер памяти
    private double[][] bufferI = new double[3][n]; // буфер памяти

    private double delta_t = 2 * Math.PI / n; // шаг дискретизации
    private int count = 1;
    private Vector vecU; // класс вектора напряжения
    private Vector vecI; // класс вектора тока

    void setVector(String s, Vector vec){
        switch (s){
            case "I":
                vecI = vec;
                break;
            case "U":
                vecU = vec;
                break;
        }
    }
    Vector getVector(String s){
        switch (s){
            case "I":
                return vecI;
            case "U":
                return vecU;
            default:
                return null;
        }
    }

    /**
     * метод фильтрации и оцифровки сигнала с помощью
     * преобразования Фурье
     * @param meanU аналоговое значение напряжения
     * @param meanI аналоговое значение тока
     * @param phase фаза сигнала
     */
    void filtrateFourier(double meanU, double meanI, int phase) {

        bufferU[phase][count] = meanU; // заполнение буфера
        bufferI[phase][count] = meanI; // заполнение буфера

        // преобразование в векторную форму
        vecI.setMean(phase, createVectorForm(calcReal(bufferI[phase]), calcImag(bufferI[phase])));
        vecU.setMean(phase, createVectorForm(calcReal(bufferU[phase]), calcImag(bufferU[phase])));

        if (phase == 2) if (++count == n) count = 0;
    }

    /**
     * метод позволяет получить векторную форму комплексного числа
     * @param Fx действительное значение
     * @param Fy мнимое значение
     * @return словарь с ключами "module" & "angle"
     */
    public static HashMap<String, Double> createVectorForm(double Fx, double Fy){
        double mod = getAmplitude(Fx, Fy);
        double angle = getPhase(Fx, Fy, mod);
        HashMap<String, Double> map = new HashMap<>();

        map.put("module", mod);
        map.put("angle", angle);

        return map;
    }

    private double calcReal(double[] buffer) {
        double Harm = 0;
        for (int i = 0; i < buffer.length; ++i) {

            Harm += buffer[i] * Math.cos(delta_t * i) / (n);
        }
        return Harm;
    }

    private double calcImag(double[] buffer) {
        double Harm = 0;
        for (int i = 0; i < buffer.length; ++i) {

            Harm += buffer[i] * Math.sin(delta_t * i) / (n);
        }
        return Harm;
    }

    public static double getPhase(double Fx, double Fy, double z) {
        if (Fy > 0) {
            return Math.acos(Fx / z);
        } else if (Fx < 0) {
            return Math.PI - Math.asin(Fy / z);
        } else return Math.PI * 2 + Math.asin(Fy / z);
    }

    public static double getAmplitude(double Fx, double Fy) {
        return Math.sqrt(Math.pow(Fx, 2) + Math.pow(Fy, 2));
    }

}
