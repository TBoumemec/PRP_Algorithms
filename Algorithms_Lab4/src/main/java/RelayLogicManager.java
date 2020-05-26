import Logic.PowerRelay;
import Logic.TimeRelay;

public class RelayLogicManager {

    private double minTripPoint = 1.1; // ток срабатывания второй ступени
    private double cutOffTrip = 2; // ток отсечки

    private ZeroSeqSignal zeroSeqSignal; // сигнал НП
    private TimeRelay timeRelay; // реле времени
    private String direction; // направление защиты

    RelayLogicManager(String dir){
        direction = dir;
        timeRelay = new TimeRelay();
    }

    public ZeroSeqSignal getZeroSeqSignal() {
        return zeroSeqSignal;
    }

    void setZeroSeqSignal(ZeroSeqSignal zeroSeqSignal) {
        this.zeroSeqSignal = zeroSeqSignal;
    }

    /**
     * метод сравнения сигнала с уставкой, пуска и срабатывания защиты
     * @return
     */
    boolean process() {
        // превышение минимальной уставки
        if (isTriggered(zeroSeqSignal.getI30RMS(), minTripPoint)) {

            setTrip(true); // сигнал на пуск защиты
        }
        else setTrip(false);

        boolean boo = timeRelay.isTripped(); // флаг срабатывания защиты

        TimeDiagramChart.addAnalogData(3, 1, minTripPoint);


        return boo;
    }

    /**
     * метод логики пуска защиты по сигналу превышения сигнала
     * @param trip сигнал превышения уставки
     */
    private void setTrip(boolean trip) {
        TimeDiagramChart.addDiscreteData(0, trip); // превышение уставки
        boolean block = PowerRelay.isBlocked(zeroSeqSignal.getS30Angle(), direction);
        TimeDiagramChart.addDiscreteData(1, block); // блокировка

        if (trip & !block) { // условие отключения блокировки

            timeRelay.launchAuthority(zeroSeqSignal.getI30RMS() > cutOffTrip); // пуск
        }
//        else if (!isTriggered(zeroSeqSignal.getI30RMS(), 0.95 * minTripPoint)){
        else if (zeroSeqSignal.getI30RMS()<0.95*minTripPoint | block){
            timeRelay.delaunchAuthority(); // отключение
        }
    }

    private boolean isTriggered(double signal, double minTripPoint) {
        return (signal > minTripPoint);
    }




}


