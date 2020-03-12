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

    public double getPhA() {
        return phA;
    }

    public void setPhA(double phA) {
        this.phA = phA;
    }

    public double getPhB() {
        return phB;
    }

    public void setPhB(double phB) {
        this.phB = phB;
    }

    public double getPhC() {
        return phC;
    }

    public void setPhC(double phC) {
        this.phC = phC;
    }

    public void setAny(int phase, double mean) {
        switch (phase) {
            case (0):
                setPhA(mean);
            case (1):
                setPhB(mean);
            case (2):
                setPhC(mean);
        }
    }

    public double getAny(int phase) {
        double returning = 0;
        switch (phase) {
            case (0):
                return returning = getPhA();
            case (1):
                return returning = getPhB();
            case (2):
                return returning = getPhC();
        }
        return returning;
    }

}
