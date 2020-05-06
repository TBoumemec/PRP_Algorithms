import Charts.TimeDiagramChart;
import Values.Blocking;
import Values.RMS;

public class RelayLogicManager {

    private final double I_TO = 0.01; // ток начала торможения
    private final double K_UNIFORM = 1; // коэффициент однотипности
    private final double K_TP = 2.5; // коэффициент переходного процесса
    private final double E = 0.1; // относительная погрешность ТТ
    private final double U_ONLOAD = 12; // относительная погрешность РПН
    private final double F_LEV = 0.02; // относительная погрешность выравнивания токов плеч
    private final double F_PTT = 0.05;// относительная погрешность выравнивания автотрансформатора
    private final double AT_K_DETUNE = 1.2; // коэффициент отстройки

    private double ratedCurrent = I_TO *(K_UNIFORM * K_TP * E + U_ONLOAD + F_LEV + F_PTT);
    private double minTripCurrent = AT_K_DETUNE * ratedCurrent; // миниальный ток срабатывания защиты

    private final double S_TR = 500; // мощность автотрансформатора в МВА
    private final double U_LW = 10; // напряжение на НН
    private final double basicCurrent = S_TR / (U_LW * Math.sqrt(3) * 8); // базисный ток
    private final double I_SC = 4; // ток короткого замыкания

    private double cutOffCurrent = (1.5 * I_SC / basicCurrent *
            (K_UNIFORM * 3 * E + U_ONLOAD + F_LEV + F_PTT)); // ток срабатывания ДТО

    private final double BL_K_DETUNE = 1.1; // коэффициент отстройки
    private final double K_ULTIM_LOAD = 1.5; // коэффициент предельной нагрузки
    private final double relativeCurrent = 0.4; // ????

    private double blockCurrent = BL_K_DETUNE * K_ULTIM_LOAD * relativeCurrent; // ток блокировки

    private double stopCoefficent = 3; // коэффициент торможения

    private double stopCurrent;

    private double tripPoint; // значение уставки

    private double timeWait, iniTime; // время до срабатывания защиты / время пуска защиты
    private double timeSet = Double.POSITIVE_INFINITY; // время срабатывания защиты
    private boolean boo, key = false;

    private RMS rms;
    private Blocking bl;

    public RMS getRms() {
        return rms;
    }

    void setRms(RMS rms) {
        this.rms = rms;
    }

    void setBL(Blocking bl) {
        this.bl = bl;
    }

    void calcStopCurrent(double IHW, double ILW, double angle){
        if (Math.cos(angle) < 0) stopCurrent = Math.sqrt(IHW * ILW * Math.cos(Math.PI - angle));
        else stopCurrent = 0;
    }

    double setTripPoint(){
        if (stopCurrent > blockCurrent){
        return minTripCurrent + stopCoefficent * (stopCurrent - blockCurrent);}
        return minTripCurrent;
    }

    boolean process(int phase) {

        tripPoint = setTripPoint();
//        System.out.println("puk " + tripPoint + " " + rms.getMean(phase));

        if (rms.getMean(phase) > tripPoint) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setTrip(true, phase);
            System.out.println(
                    "До отключения осталось: " + (timeSet - (timeWait = rms.getTime() - iniTime)));
        }
        else setTrip(false, phase);


        if (timeWait >= timeSet) boo = true; // срабатывание защиты

        TimeDiagramChart.addAnalogData(phase, 2, tripPoint);

        return boo;
    }

    private void launchingAuthority(int phase){
        if (!key) { // однократная логика пуска защиты
            iniTime = rms.getTime();
            key = true;
        }
        if (rms.getMean(phase) > cutOffCurrent) timeSet = 1000; // время срабатывания токовой отсечки
            else timeSet = 5000; // время срабатывания ДТЗ
    }

    private void delaunchingAuthority(){
        key = false;
        timeSet = Double.POSITIVE_INFINITY;
    }

    private void setTrip(boolean trip, int phase) {
        TimeDiagramChart.addDiscreteData(0, trip);
        if (trip & !bl.isBlocked()) launchingAuthority(phase);
        else delaunchingAuthority();
    }
}


