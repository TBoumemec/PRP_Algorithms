package Values;

public class BlockLogic implements ValuesInterface{
    private double phA = 0;
    private double phB = 0;
    private double phC = 0;

    public double getTime() {
        return 0;
    }

    public void setTime(double time) {

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
                this.phA = mean;
            case (1):
                this.phB = mean;
            case (2):
                this.phC = mean;
        }
    }

    public double getAny(int phase) {
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

    public boolean isBlocked(){
        double tripPoint = 0.08;

        return compare(phA, tripPoint) ||
                compare(phB, tripPoint) ||
                compare(phC, tripPoint);
    }

    private boolean compare(double b, double a){
        return (a > b);
    }

}
