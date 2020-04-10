import Values.RMS;
import Values.SV;

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

    /*
    Name of unpacked comtrade file
    PhA/AB/ABC__20/60/80  //  kind of short circuit // discritisation scale
     */
    private String comtrName = "PhA80";
    // внимательно с путем файла
    private String path = "C:\\Users\\Alexander\\JavaProjects\\MicropocessorRealyAlgorithms\\Algorithms_Lab1\\src\\data\\Начало линии\\";

    private SV sv = new SV();
    private RMS rms = new RMS();
    private Filter filter = new Filter();
    private RelayLogic relayLogic = new RelayLogic();

    TreatmentData() {
        String cfgName = path + comtrName + ".cfg";
        comtrCfg = new File(cfgName);
        String datName = path + comtrName + ".dat";
        comtrDat = new File(datName);
    }


    void start() throws IOException {

        filter.setSv(sv);
        filter.setRms(rms);
        relayLogic.setRms(rms);

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

            if (lineNumber > 2 && lineNumber <= numberData + flagNumber) {

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
            if (count < 3500 || count > 5000) continue; // позволяет срезать часть графика в норм режиме
            String[] lineData = line.split(",");
            sv.setTime(Double.parseDouble(lineData[1]));

            for (int i = 0; i < 3; i++) {
                sv.setAny(i, Double.parseDouble(lineData[i + 2]) * k1[i] + k2[i]);

                filter.calculate(i); // расчет фильтра


                Charts.addAnalogData(i, 0, sv.getAny(i));
                Charts.addAnalogData(i, 1, rms.getAny(i));

                if (relayLogic.process(i)) {
                    System.out.println("ТО сработала успешно");
                    break outer; // при срабатывании защиты происходит выход
                }


            }
        }
    }
}

