public class MiddleValues implements Filter {

    private double buffer[] = new double[80];
    private double sum = 0;
    private double k = 1.11/8;
    private SimpleValues sv;
    private RMSValues rms;
    private int count = 0;

    public SimpleValues getSv() {
        return sv;
    }

    public void setSv(SimpleValues sv) {
        this.sv = sv;
    }

    public RMSValues getRms() {
        return rms;
    }

    public void setRms(RMSValues rms) {
        this.rms = rms;
    }

    public void calculate () {
        sum += Math.abs(sv.getPhA()) - buffer[count];
        buffer[count] = Math.abs(sv.getPhA());
        rms.setPhA(sum*k);

        if(++count >= 80) count = 0;
        rms.setTime(sv.getTime());
    }

}
