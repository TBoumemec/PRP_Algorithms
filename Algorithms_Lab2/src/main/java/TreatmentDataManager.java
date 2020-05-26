import Charts.TimeDiagramChart;
import Values.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


 /** Класс считывания, расчета и визуализации данных
         */
 class TreatmentDataManager {

     private File comtrCfg, comtrDat; // конфигурационный/файл данных
     private double[] k1, k2; // масштабные коэффициенты

     private FourierFilter HWFilter = new FourierFilter(); // фильтр ВН
     private FourierFilter LWFilter = new FourierFilter(); // фильтр НН
     private FourierFilter deltaFilter = new FourierFilter(); // фильтр ДЗ
     private Vector vecHW = new Vector(); // вектор токов ВН
     private Vector vecLW = new Vector(); // вектор токов НН
     private Vector deltaVec = new Vector(); // вектор токов в ДЗ

     private RMS rms = new RMS();
     private Blocking bl = new Blocking();
     private RelayLogicManager relayManager = new RelayLogicManager();

     TreatmentDataManager() {
         // внимательно с путем файла
         String path = "C:\\Users\\Alexander\\JavaProjects\\MicropocessorRealyAlgorithms\\Algorithms_Lab2\\src\\data\\";
        /*
    Name of unpacked comtrade file
    PhA/AB/ABC__20/60/80  //  kind of short circuit // discritisation scale
     */
         String comtrName = "Trans2ObmVneshABC";
         String cfgName = path + comtrName + ".cfg"; // имя конфигурационного файла
         comtrCfg = new File(cfgName); // конфигурационный файл
         String datName = path + comtrName + ".dat"; // имя файла данных
         comtrDat = new File(datName); // файл данных
     }

     void start() throws IOException {

         // задание фильтрам соответствующих векторов
         HWFilter.setVector(vecHW);
         LWFilter.setVector(vecLW);
         deltaFilter.setVector(deltaVec);

         // задание логике РЗ соответствующих классов сигналов и блокировки
         relayManager.setRms(rms);
         relayManager.setBL(bl);

         // парсинг конфигурации файла
         parseConfigFile(comtrCfg);
         // обработка значений
         makeDataProcessing(comtrDat);
     }

     /**
      * обработка конфигурационного Комтрейд-файла
      *
      * @param file файл конфигурации
      * @throws IOException
      */
     private void parseConfigFile(File file) throws IOException {
         BufferedReader br = new BufferedReader(new FileReader(file));


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

             } else if (lineNumber > 2 && lineNumber <= numberData + flagNumber) {

                 k1[count] = Double.parseDouble(line.split(",")[5]);
                 k2[count] = Double.parseDouble(line.split(",")[6]);

                 count++;

             }
         }
     }

     /**
      * обработка данных Комтрейд-файла
      *
      * @param file файл, из которого производится чтение
      * @throws IOException
      */
     private void makeDataProcessing(File file) throws IOException {

         int count = 0;
         BufferedReader br = new BufferedReader(new FileReader(file));
         String line;

         outer:
         while ((line = br.readLine()) != null) { // outer - метка для выхода из внешнего цикла
             count++;

             String[] lineData = line.split(",");
             rms.setTime(Double.parseDouble(lineData[1]));

             for (int phase = 0; phase < 3; phase++) {

                 double curHW = Double.parseDouble(lineData[phase + 2]) * k1[phase] + k2[phase];
                 double curLW = Double.parseDouble(lineData[phase + 5]) * k1[phase + 3] + k2[phase + 3];

                 makeFourierCalculation(curHW, curLW, phase);

                 rms.setMean(phase, deltaFilter.get1stRMSMean());


                 bl.setMean(phase, deltaFilter.get5thRMSMean() / deltaFilter.get1stRMSMean());
                 System.out.println("Включена ли блокировка: " + bl.isBlocked());

                 TimeDiagramChart.addAnalogData(phase, 0, deltaFilter.getDigitSV(count));
                 TimeDiagramChart.addAnalogData(phase, 1, rms.getMean(phase));
                 Charts.VectorChart.addVector(0, phase, vecLW.getAnyFx(phase), vecLW.getAnyFy(phase));
                 Charts.VectorChart.addVector(0, phase + 3, vecHW.getAnyFx(phase), vecHW.getAnyFy(phase));
                 Charts.VectorChart.addVector(0, phase + 6, deltaVec.getAnyFx(phase), deltaVec.getAnyFy(phase));

                 relayManager.calcStopCurrent(HWFilter.get1stRMSMean(), LWFilter.get1stRMSMean(),
                         deltaFilter.getPhase(deltaVec.getAnyFx(phase), deltaVec.getAnyFy(phase),
                                 deltaFilter.getAmplitude(deltaVec.getAnyFx(phase), deltaVec.getAnyFy(phase))));

                 if (relayManager.process(phase)) {
                     System.out.println("Защита сработала");
                     break outer; // при срабатывании защиты происходит выход
                 }
             }
         }
     }


     /**
      * проведение снимаемых значений через фильтр Фурье
      *
      * @param curHW I_ВН
      * @param curLW I_НН
      * @param phase рассматриваемая фаза
      */
     private void makeFourierCalculation(double curHW, double curLW, int phase) {
         HWFilter.filterFourier(curHW, phase, 0);
         //
         double shift = (-30d / 360) * 2 * Math.PI;
         LWFilter.filterFourier(curLW, phase, shift);

         deltaVec.setAllF1(phase, vecHW.getAnyFx(phase) + vecLW.getAnyFx(phase),
                 vecHW.getAnyFy(phase) + vecLW.getAnyFy(phase));

         deltaVec.setAllF5(phase, vecHW.getAnyF5x(phase) + vecLW.getAnyF5x(phase),
                 vecHW.getAnyF5y(phase) + vecLW.getAnyF5y(phase));
     }
 }

