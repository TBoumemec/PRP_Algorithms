import Values.RMS;

public class RelayLogicManager {

    private double minTripCurrent = 50; // миниальный ток срабатывания защиты

    private double cutOffCurrent = 50; // ток срабатывания ДТО

    private double tripPoint = minTripCurrent; // значение уставки

    private double timeWait, iniTime; // время до срабатывания защиты / время пуска защиты
    private double timeSet = Double.POSITIVE_INFINITY; // время срабатывания защиты
    private boolean boo, key = false;

    private RMS rms;
    private BlockManager bl;

    public RMS getRms() {
        return rms;
    }

    void setRms(RMS rms) {
        this.rms = rms;
    }

    void setBL(BlockManager bl) {
        this.bl = bl;
    }

    boolean process(int phase) {

        if (rms.getMean(phase) < tripPoint) {

            setTrip(true, phase);
            System.out.println(
                    "До отключения осталось: " + (timeSet - (timeWait = rms.getTime() - iniTime)));
        }
        else setTrip(false, phase);

        if (timeWait >= timeSet) boo = true; // срабатывание защиты

        TimeDiagramChart.addAnalogData(2, 3, tripPoint);

        return boo;
    }

    private void launchAuthority(int phase){
        System.out.println("puk");
        if (!key) { // однократная логика пуска защиты
            iniTime = rms.getTime();
            key = true;
        }
        if (rms.getMean(phase) < cutOffCurrent) timeSet = 1000; // время срабатывания токовой отсечки
            else timeSet = 5000; // время срабатывания ДТЗ
    }

    private void delaunchAuthority(){
        key = false;
        timeSet = Double.POSITIVE_INFINITY;
    }

    private void setTrip(boolean trip, int phase) {
        TimeDiagramChart.addDiscreteData(0, trip);
        if (trip & !bl.isBlockedU(10) & !bl.isBlockedI(100)) launchAuthority(phase);
        else delaunchAuthority();
    }

}


