package Values;

public class RMS implements ValuesInterface {
    private double phA = 0;
    private double phB = 0;
    private double phC = 0;
    private double time = 0;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
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

}
