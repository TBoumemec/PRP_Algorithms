import Values.RMS;
import Values.SV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OldFartTreatmentData {
    private File comtrCfg, comtrDat;
    private String line;
    private String[] lineData;
    private double k1[], k2[], time;

    /*
    Name of unpacked comtrade file
    PhA/AB/ABC__20/60/80  //  kind of short circuit // discritisation scale
     */
    private String comtrName = "PhA80";
    private String path = "C:\\Users\\Alexander\\IdeaProjects\\ForGit\\PRP_Algorithms\\Algorithms_Lab1\\src\\data\\Начало линии\\";
    private String cfgName = path + comtrName + ".cfg";
    private String datName = path + comtrName + ".dat";

    private SV sv = new SV();
    private RMS rms = new RMS();
    private Filter filter = new Filter();
    private RelayLogic relayLogic = new RelayLogic();

    public OldFartTreatmentData() {
        comtrCfg = new File(cfgName);
        comtrDat = new File(datName);
    }

    public void start() throws IOException {

        filter.setSv(sv);
        filter.setRms(rms);
        relayLogic.setRms(rms);
        BufferedReader br = new BufferedReader(new FileReader(comtrCfg));

        int lineNumber = 0;
        int count = 0;
        int numberData = 0;
        int flagNumber = 0;

//        RTDS Simulation, 0,1999 // system type, ..., standard format
//        4,3A,1D // 4 signals 3 analog.sign., 1 discrete format
//        1,IaLine,,,KA,1.49589E-4,-3.66569,0,0,65535,1.0,1.0,P //  \
//        2,IbLine,,,KA,1.21193E-4,-4.17412,0,0,65535,1.0,1.0,P //  - coefficients
//        3,IcLine,,,KA,1.31696E-4,-4.96902,0,0,65535,1.0,1.0,P //  /
//        1,LG_FLT,0,,0
//        50.0 // industry frequency
//        1
//        1000.0,2000
//        04/02/2020,03:31:44.019000
//        04/02/2020,03:31:45.019000
//        ASCII
//        1

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

            if (lineNumber > 2 && lineNumber <= numberData + flagNumber) {

                k1[count] = Double.parseDouble(line.split(",")[5]);
                k2[count] = Double.parseDouble(line.split(",")[6]);
                count++;

            }
        }

        count = 0;

        br = new BufferedReader(new FileReader(comtrDat));
        while ((line = br.readLine()) != null) {
            count++;
            if (!(count > 3000 && count < 5000)) continue;
            lineData = line.split(",");
            sv.setTime(Double.parseDouble(lineData[1]));
            for (int i = 0 ; i < 3; i++) {
                sv.setAny(i, Double.parseDouble(lineData[i+2]) * k1[i] + k2[i]);
//                sv.setPhA(Double.parseDouble(lineData[2]) * k1[0] + k2[0]);
//                sv.setPhB(Double.parseDouble(lineData[3]) * k1[1] + k2[1]);
//                sv.setPhC(Double.parseDouble(lineData[4]) * k1[2] + k2[2]);

                filter.calculate(i);
                relayLogic.process(i);
//                filter.calculate();
//                logic.process();

                Charts.addAnalogData(i, 0, sv.getAny(i));
                Charts.addAnalogData(i, 1, rms.getAny(i));

//                Charts.addAnalogData(0, 0, sv.getPhA());
//                Charts.addAnalogData(0, 1, rms.getPhA());
//                Charts.addAnalogData(1, 0, sv.getPhB());
//                Charts.addAnalogData(1, 1, rms.getPhB());
//                Charts.addAnalogData(2, 0, sv.getPhC());
//                Charts.addAnalogData(2, 1, rms.getPhC());
            }


        }

    }

}
