import Values.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


 /** Класс считывания, расчета и визуализации данных
         */
class TreatmentData {

    private File comtrCfg, comtrDat;
    private double[] k1;
    private double[] k2;
    private double shift = (-30d/360)*2*Math.PI;


     /*
    Name of unpacked comtrade file
    PhA/AB/ABC__20/60/80  //  kind of short circuit // discritisation scale
     */
    private String comtrName = "Trans2ObmVneshABC";
    // внимательно с путем файла
    private String path = "C:\\Users\\Alexander\\JavaProjects\\MicropocessorRealyAlgorithms\\Algorithms_Lab2\\src\\data\\";

    private Filter HWFilter = new Filter();
    private Filter LWFilter = new Filter();
    private Filter deltaFilter = new Filter();
    private Vector vecHW = new Vector();
    private Vector vecLW = new Vector();
    private Vector deltaVec = new Vector();
    private SV sv = new SV();
    private RMS rms = new RMS();
    private BlockLogic bl = new BlockLogic();
    private RelayLogic relayLogic = new RelayLogic();

    TreatmentData() {
        String cfgName = path + comtrName + ".cfg";
        comtrCfg = new File(cfgName);
        String datName = path + comtrName + ".dat";
        comtrDat = new File(datName);
    }

    void start() throws IOException {

        HWFilter.setVector(vecHW);
        LWFilter.setVector(vecLW);
        deltaFilter.setVector(deltaVec);

        relayLogic.setRms(rms);
        relayLogic.setBL(bl);

        parseScaleCoefficients(comtrCfg);

        makeDataProcessing(comtrDat);

    }

    private void parseScaleCoefficients(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));


        int lineNumber, count, numberData, flagNumber;
        lineNumber = count = numberData = flagNumber = 0;

        String line;

        while ((line = br.readLine()) != null) {
            lineNumber++;
            if (lineNumber == 2) {
                flagNumber = lineNumber;
                numberData = Integer.parseInt(line.split(",")[1].replaceAll("A", ""));
                System.out.println("Number Signals: " + numberData);
                k1 = new double[numberData];
                k2 = new double[numberData];
                System.out.println("Number Data: " + numberData);

            }
            else if (lineNumber > 2 && lineNumber <= numberData + flagNumber) {

                k1[count] = Double.parseDouble(line.split(",")[5]);
                k2[count] = Double.parseDouble(line.split(",")[6]);

                count++;

            }
        }
    }

    private void makeDataProcessing(File f) throws IOException {

        int count = 0;

        BufferedReader br = new BufferedReader(new FileReader(comtrDat));
        String line;

        outer:
        while ((line = br.readLine()) != null) { // outer - метка для выхода из внешнего цикла
            count++;

//            if (count > 100) continue; // позволяет срезать часть графика в норм режиме
            String[] lineData = line.split(",");
            rms.setTime(Double.parseDouble(lineData[1]));

            for (int phase = 0; phase < 3; phase++) {

                double curHW = Double.parseDouble(lineData[phase + 2]) * k1[phase] + k2[phase];
                double curLW = Double.parseDouble(lineData[phase + 5])* k1[phase + 3] + k2[phase + 3];

                makeFourierCalculation(curHW, curLW, phase);

                sv.setMean(phase, (deltaFilter.getDigitSV(count)));
                rms.setMean(phase, deltaFilter.get1stRMSMean());


                bl.setMean(phase, deltaFilter.get5thRMSMean()/deltaFilter.get1stRMSMean());
                System.out.println(bl.isBlocked());

                Charts.addAnalogData(phase, 0, sv.getMean(phase));
                Charts.addAnalogData(phase, 1, rms.getMean(phase));

                relayLogic.calcStopCurrent(HWFilter.get1stRMSMean(), LWFilter.get1stRMSMean(),
                        deltaFilter.getPhase(deltaVec.getAnyFx(phase), deltaVec.getAnyFy(phase),
                                deltaFilter.getAmplitude(deltaVec.getAnyFx(phase),deltaVec.getAnyFy(phase))));

                if (relayLogic.process(phase)) {
                    System.out.println("puk");
                    break outer; // при срабатывании защиты происходит выход
                }
            }
        }
    }

    private void makeFourierCalculation(double curHW, double curLW, int phase){
        HWFilter.filterFourier(curHW, phase, 0);
        LWFilter.filterFourier(curLW, phase, shift);

        deltaVec.setAllF1(phase, vecHW.getAnyFx(phase) + vecLW.getAnyFx(phase),
                vecHW.getAnyFy(phase) + vecLW.getAnyFy(phase));

        deltaVec.setAllF5(phase, vecHW.getAnyF5x(phase) + vecLW.getAnyF5x(phase),
                vecHW.getAnyF5y(phase) + vecLW.getAnyF5y(phase));
    }
}

