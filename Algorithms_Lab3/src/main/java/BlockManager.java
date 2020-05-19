import Values.ValuesInterface;

public class BlockManager {
    private double uA = 0;
    private double uB = 0;
    private double uC = 0;
    private double iA = 0;
    private double iB = 0;
    private double iC = 0;

    public double getTime() {
        return 0;
    }

    public void setTime(double time) {

    }

    public void setMeanU(int phase, double mean) {
        switch (phase) {
            case (0):
                this.uA = mean;
            case (1):
                this.uB = mean;
            case (2):
                this.uC = mean;
        }
    }

    public double getMeanU(int phase) {
        switch (phase) {
            case (0):
                return uA;
            case (1):
                return uB;
            case (2):
                return uC;
        }
        return -1;
    }

    public void setMeanI(int phase, double mean) {
        switch (phase) {
            case (0):
                this.iA = mean;
            case (1):
                this.iB = mean;
            case (2):
                this.iC = mean;
        }
    }

    public double getMeanI(int phase) {
        switch (phase) {
            case (0):
                return iA;
            case (1):
                return iB;
            case (2):
                return iC;
        }
        return -1;
    }

    /**
     * если значение выше уставки, то блокировка срабатывает
     * @return True/False
     */
    public boolean isBlockedI(double tripPoint) {

        return compare(iA, tripPoint) ||
                compare(iB, tripPoint) ||
                compare(iC, tripPoint);
    }

    public boolean isBlockedU(double tripPoint) {

        return compare(uA, tripPoint) ||
                compare(uB, tripPoint) ||
                compare(uC, tripPoint);
    }

    private boolean compare(double a, double b) {
        return (a > b);
    }
}
