import Values.RMS;

public class RelayLogic {

    private RMS rms;
    private double tripPoint = 3.4;
    private boolean boo, key = false;
    private double timeWait, iniTime;

    @Deprecated
    void process() {
        if ((rms.getPhA() > tripPoint) | (rms.getPhB() > tripPoint) |
                (rms.getPhC() > tripPoint)) trip(true);
        Charts.addAnalogData(0, 2, tripPoint);
    }

    /**
     * Логика защиты. Реализует пуск и срабатывание защиты
     * @param phase рассматриваемая фаза
     * @return логическое срабатывание защиты
     */
    boolean process(int phase) {

        if (rms.getAny(phase) > tripPoint) {
            trip(true);

            if (!key) { // однократная логика пуска защиты
                iniTime = rms.getTime();
                key = true;
            }

            timeWait = rms.getTime() - iniTime;
            System.out.println("До отключения осталось: " + (1000 - timeWait));

        }
        else trip(false);
        if (timeWait >= 1000) boo = true; // срабатывание защиты

        Charts.addAnalogData(phase, 2, tripPoint);
        return boo;
    }

    public RMS getRms() {
        return rms;
    }

    void setRms(RMS rms) {
        this.rms = rms;
    }

    void trip(boolean trip) {
        Charts.addDiscreteData(0, trip);
    }
}
