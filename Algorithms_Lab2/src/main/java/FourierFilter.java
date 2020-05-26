class FourierFilter {

    private int n = 40; // количество снятий сигнала за период
    private int k = 5; // гармоника блокировки
    private double[][] buffer = new double[3][n]; // буфер памяти
    private double delta_t = 2 * Math.PI / n; // шаг дискретизации
    private int count = 0;
    private int phase; // обрабатываемая фаза
    private Vector vector; //


    void setVector(Vector vector) {
        this.vector = vector;
    }

    void filterFourier(double actualMean, int phase, double delta) {

        this.phase = phase;

        buffer[phase][count] = actualMean;

        double[] Fx = discreteReal();
        double[] Fy = discreteImag();

        set1stHarm(Fx[0], Fy[0], delta);

        set5thHarm(Fx[4], Fy[4], delta);

        if (phase == 2) if (++count == n) count = 0;

    }

    private double[] discreteReal() {
        double[] Harm = new double[k];
        double[] x = buffer[phase];
        for (int f = 1; f <= Harm.length; ++f) {
            for (int i = 0; i < x.length; ++i) {

                Harm[f - 1] += x[i] * Math.cos(delta_t * i * f) / (n);
            }
        }
        return Harm;
    }

    private double[] discreteImag() {
        double[] Harm = new double[k];
        double[] x = buffer[phase];
        for (int f = 1; f <= Harm.length; ++f) {
            for (int i = 0; i < x.length; ++i) {

                Harm[f - 1] += x[i] * Math.sin(delta_t * i * f) / (n);
            }
        }
        return Harm;
    }

    private void set1stHarm(double Fx, double Fy, double delta) {
        double module = getAmplitude(Fx, Fy);
        double argument = getPhase(Fx, Fy, module);

        vector.setAllF1(phase,
                module * Math.cos(argument + delta), module * Math.sin(argument + delta));
    }

    private void set5thHarm(double Fx, double Fy, double delta) {
        double module = getAmplitude(Fx, Fy);
        double argument = getPhase(Fx, Fy, module);

        vector.setAllF5(phase,
                module * Math.cos(argument + delta), module * Math.sin(argument + delta));
    }

    double get1stRMSMean() {
        return getAmplitude(vector.getAnyFx(phase), vector.getAnyFy(phase)) / Math.sqrt(2);
    }

    double get5thRMSMean() {
        return getAmplitude(vector.getAnyF5x(phase), vector.getAnyF5y(phase)) / Math.sqrt(2);
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

    double getDigitSV(int count) {
        double a = vector.getAnyFx(phase);
        double b = vector.getAnyFy(phase);

        return (Math.cos(delta_t * count) * a + Math.sin(delta_t * count) * b);
    }
}
