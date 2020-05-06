package Values;

public class Blocking implements ValuesInterface {
    private double phA = 0;
    private double phB = 0;
    private double phC = 0;

    public double getTime() {
        return 0;
    }

    public void setTime(double time) {

    }

    public void setMean(int phase, double mean) {
        switch (phase) {
            case (0):
                this.phA = mean;
            case (1):
                this.phB = mean;
            case (2):
                this.phC = mean;
        }
    }

    public double getMean(int phase) {
        switch (phase) {
            case (0):
                return phA;
            case (1):
                return phB;
            case (2):
                return phC;
        }
        return -1;
    }

    /**
     * если значение выше уставки, то блокировка срабатывает
     * @return True/False
     */
    public boolean isBlocked() {
        double tripPoint = 0.01; // уставка блокировки

        return compare(phA, tripPoint) ||
                compare(phB, tripPoint) ||
                compare(phC, tripPoint);
    }

    private boolean compare(double a, double b) {
        return (a > b);
    }
}
