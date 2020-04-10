import Values.RMS;
import Values.SV;

class Filter {


    private double[][] buffer = new double[80][3];
    private double[] sum = new double[]{0, 0, 0};
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
     * на выходе из фильтра - средневыпрямленное значение
     * @param phase рассчитываемая фаза
     */
    void calculate(int phase) {

        sum[phase] += Math.abs(sv.getAny(phase)) - buffer[count][phase];
        buffer[count][phase] = Math.abs(sv.getAny(phase));
        double k = 0.0125;
        rms.setAny(phase, sum[phase] * k);
        rms.setTime(sv.getTime());

        if (phase == 2) if (++count >= 80) count = 0;

    }
}
