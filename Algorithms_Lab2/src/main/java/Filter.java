import Values.RMS;
import Values.SV;

public class Filter {

    private int n = 20;
    private double[][] buffer = new double[3][n];
    private double X1 = 0;
    private double X5 = 0;
    private double delta_t = 2 * Math.PI / n;

    private SV sv;
    private RMS rms;
    private int count = 0;

    SV getSv() {
        return sv;
    }

    void setSv(SV sv) {
        this.sv = sv;
    }

    RMS getRms() {
        return rms;
    }

    void setRms(RMS rms) {
        this.rms = rms;
    }

    /**
     * метод расчета фильтра для указанной фазы
     * @param phase рассчитываемая фаза
     */
    void calculate(int phase) {

        buffer[phase][count] = Math.abs(sv.getAny(phase));
        X1 = summary(buffer[phase], 1);
        X5 = summary(buffer[phase], 5);


        rms.setAny(phase, X1 / Math.sqrt(2));
        rms.setTime(sv.getTime());
        rms.setHar(X5);

        if (phase == 2) if (++count >= n) count = 0; // чтобы счетчик сменялся только в один момент

    }

    double summary(double[] a, int k) {
        double Fx = 0;
        double Fy = 0;

        for (int i = 0; i < a.length; ++i) {
            Fx += a[i] * Math.cos(delta_t * i * k);
            Fy += a[i] * Math.sin(delta_t * i * k);

        }

        return Math.sqrt(Math.pow(Fx, 2) + Math.pow(Fy, 2));
    }
}
