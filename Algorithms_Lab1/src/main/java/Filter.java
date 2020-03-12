import Values.RMS;
import Values.SV;

public class Filter {


    private double[][] buffer = new double[80][3];
    private double[] sum = new double[]{0, 0, 0};
    private double k = 0.0125;
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
     *  универсальный метод расчета фильтра для всех фаз
     */
    @Deprecated
    void calculate() {

        for (int i = 0; i <= 2; i++) {
        sum[i] += Math.abs(sv.getAny(i)) - buffer[count][i];
        buffer[count][i] = Math.abs(sv.getAny(i));
        rms.setAny(i, sum[i] * k);
        rms.setTime(sv.getTime());

    }

        if (++count >= 80) count = 0;

}

    /**
     * метод расчета фильтра для указанной фазы
     * @param phase рассчитываемая фаза
     */
    void calculate(int phase) {

        sum[phase] += Math.abs(sv.getAny(phase)) - buffer[count][phase];
        buffer[count][phase] = Math.abs(sv.getAny(phase));
        rms.setAny(phase, sum[phase] * k);
        rms.setTime(sv.getTime());

        if (phase == 2) if (++count >= 80) count = 0;

    }
}
