import Values.*;

class Vector {

    private double[] Fx = new double[3];
    private double[] Fy = new double[3];
    private double[] F5x = new double[3];
    private double[] F5y = new double[3];

    void setAnyFx(int phase, double mean) {
        Fx[phase] = mean;
    }

    double getAnyFx(int phase) {
        return Fx[phase];
    }

    void setAnyFy(int phase, double mean) {
        Fy[phase] = mean;
    }

    double getAnyFy(int phase) {
        return Fy[phase];
    }

    void setAnyF5x(int phase, double mean) {F5x[phase] = mean;}

    double getAnyF5x(int phase) {return F5x[phase];}

    void setAnyF5y(int phase, double mean) {F5y[phase] = mean;}

    double getAnyF5y(int phase) {return F5y[phase];}

    void setAllF1(int phase, double F1x, double F1y){
        setAnyFx(phase, F1x);
        setAnyFy(phase, F1y);
    }

    void setAllF5(int phase, double F5x, double F5y){
        setAnyF5x(phase, F5x);
        setAnyF5y(phase, F5y);
    }




}
