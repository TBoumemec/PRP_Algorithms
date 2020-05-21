public class RelayLogicManager {

    private final double KOEF = 0.85; // коэффициент отстройки
    private final double KOEF_2 = 0.9; // коэффициент отстройки
    private final double LINE_LENGTH = 273 * 6; // km
    private final double ACT_RES = 0.048; // active resistance per km
    private final double IND_RES = 0.328; // inductive resistance per km
    private final double CAP_RES = 0.293; // capacitive resistance per km

    private double impedance = Math.sqrt(Math.pow(LINE_LENGTH * ACT_RES, 2)
                                        + Math.pow(LINE_LENGTH * (IND_RES - CAP_RES), 2));

    private double cutOffTrip = KOEF * impedance; // миниальный ток срабатывания защиты

    private double minTripPoint = KOEF * (impedance + KOEF_2 * cutOffTrip); // ток срабатывания ДТО

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

    boolean process() {

        if (rms.isTriggered(minTripPoint)) {

            setTrip(true);
//            System.out.println(timeWait);
            System.out.println(
                    "До отключения осталось: " + (timeSet - (timeWait = rms.getTime() - iniTime)));
        }
        else setTrip(false);

        if (timeWait >= timeSet) boo = true; // срабатывание защиты

        TimeDiagramChart.addAnalogData(2, 3, minTripPoint);

        return boo;
    }

    private void launchAuthority(){
        if (!key) { // однократная логика пуска защиты
            iniTime = rms.getTime();
            key = true;
            System.out.println("puk");

        if (rms.isTriggered(cutOffTrip)) timeSet = 10000; // время срабатывания токовой отсечки
            else timeSet = 50000; // время срабатывания ДТЗ
    }}

    private void delaunchAuthority(){
        System.out.println("отключение защиты");
        key = false;
        timeSet = Double.POSITIVE_INFINITY;
    }

    private void setTrip(boolean trip) {
        TimeDiagramChart.addDiscreteData(0, trip);
//        System.out.println(bl.isBlockedI(1000) + " " + bl.isBlockedU(30));
//        if (trip & !bl.isBlockedI(1000) & !bl.isBlockedU(30)) launchAuthority();
        if (trip) launchAuthority();
        else delaunchAuthority();
    }

}


