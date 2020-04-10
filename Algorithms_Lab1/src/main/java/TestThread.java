import Values.RMS;
import Values.SV;

class TestThread extends Thread {

    private Filter filter;
    private SV sv;
    private RMS rms;
    private RelayLogic rl;
    double k1, k2;
    int phase;
    double lineData;

    void setAll(Filter filter, SV sv, RMS rms, RelayLogic rl, int phase)
    {
        setFiler(filter);
        setRele(rl);
        setRMS(rms);
        setSV(sv);
        setPhase(phase);
    }


    void setFiler(Filter filer){
        this.filter = filer;
    }

    void setSV(SV sv){
        this.sv = sv;
    }

    void setRMS(RMS rms){
        this.rms = rms;
    }

    void setRele(RelayLogic rl){
        this.rl = rl;
    }

    void setPhase(int phase){
        this.phase = phase;
    }

    TestThread(double k1, double k2, String lineData){
        this.k1 = k1;
        this.k2 = k2;
        this.lineData = Double.parseDouble(lineData);
    }

    @Override
    public void run() {

        sv.setAny(phase, lineData * k1 + k2);

        filter.calculate(phase); // расчет фильтра


        Charts.addAnalogData(phase, 0, sv.getAny(phase));
        Charts.addAnalogData(phase, 1, rms.getAny(phase));

        rl.process(phase);
    }
}
