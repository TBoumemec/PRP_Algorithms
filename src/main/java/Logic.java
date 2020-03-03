public class Logic {

    private RMSValues rms;
    private OutputData od = new OutputData();

    public void process() {
        double tripPoint = 5;
        if(rms.getPhA() > tripPoint) od.trip(true); else od.trip(false);
        Charts.addAnalogData(0, 2, tripPoint);
    }

    public RMSValues getRms() {
        return rms;
    }

    public void setRms(RMSValues rms) {
        this.rms = rms;
    }
}
