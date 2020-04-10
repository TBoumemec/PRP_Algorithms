package Values;

public interface ValuesInterface {

    double getTime();

    void setTime(double time);

    double getPhA();

    void setPhA(double phA);

    double getPhB();

    void setPhB(double phB);

    double getPhC();

    void setPhC(double phC);

    void setAny(int phase, double mean);

    double getAny(int phase);
}
