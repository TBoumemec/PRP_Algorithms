package Logic;

public class TimeRelay {

    private boolean key = false; // сигнал включения реле времени
    private double timeWait; // время до срабатывания защиты
    private double timeSet = 0; // время включенного состояния защиты

    /**
     * пуск реле времени
     * @param cutBool
     */
    public void launchAuthority(boolean cutBool){
        if (!key) { // однократная логика пуска защиты
            key = true;

            if (cutBool) timeSet = 1000; // отсечка
            else timeSet = 50000; // ДЗ время срабатывания

        }
        setTick();
    }

    /**
     * откат реле времени
     */
    public void delaunchAuthority() {
            key = false;
            timeSet = timeWait = 0;

    }

    public void setTick(){
        timeWait+=1000;
        System.out.println("До срабатывания: " + (timeSet+1000-timeWait));
    }


    public boolean isTripped(){
        return timeWait > timeSet;
    }
}
