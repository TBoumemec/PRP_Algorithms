import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/** Класс считывания, расчета и визуализации данных
        */
class DataManager {

    private File comtrCfg, comtrDat; // конфигурационный/файл данных
    private double[] k1, k2; // масштабные коэффициенты

    private Processor processor = new Processor();
    private ZeroSeqSignal zeroSeqSignal = new ZeroSeqSignal();
    private RelayLogicManager termRight = new RelayLogicManager("right");
    private Vector vecI = new Vector();
    private Vector vecU = new Vector();

    DataManager() {
        // внимательно с путем файла
        String path = "C:\\Users\\Alexander\\JavaProjects\\MicropocessorRealyAlgorithms\\Algorithms_Lab3\\src\\data\\Опыты\\";

        /*
        1ph: KZ4 = KZ7
        2ph: KZ1 > KZ3 = KZ5 = KZ6
        3ph: KZ2
         */
        String comtrName = "KZ7";
        String cfgName = path + comtrName + ".cfg"; // имя конфигурационного файла
        comtrCfg = new File(cfgName); // конфигурационный файл
        String datName = path + comtrName + ".dat"; // имя файла данных
        comtrDat = new File(datName); // файл данных
    }

    void start() throws IOException {

        // задание фильтрам соответствующих векторов

        // задание логике РЗ соответствующих классов сигналов и блокировки
        termRight.setZeroSeqSignal(zeroSeqSignal);

        processor.setVector("I", vecI);
        processor.setVector("U", vecU);

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

        while ((line = br.readLine()) != null) { // outer - метка для выхода из внешнего цикла
            count++;

            if (count < 2000) continue; // позволяет срезать часть графика в норм режиме

            String[] lineData = line.split(",");
            zeroSeqSignal.setTime(Double.parseDouble(lineData[1]));

            for (int phase = 0; phase < 3; phase++) {

                // разархивирование данных
                double U = Double.parseDouble(lineData[phase + 2]) * k1[phase] + k2[phase];
                double I = Double.parseDouble(lineData[phase + 5]) * k1[phase + 3] + k2[phase + 3];

                // оцифровка сигнала и преобразование в векторную форму
                processor.filtrateFourier(U, I, phase);

                TimeDiagramChart.addAnalogData(0, phase, U);
                TimeDiagramChart.addAnalogData(1, phase, I);

            }

            // расчет нулевых составляющих сигнала U/I/S
            zeroSeqSignal.setPower(vecI, vecU);

            TimeDiagramChart.addAnalogData(2, 0, zeroSeqSignal.getU30RMS());
            TimeDiagramChart.addAnalogData(2, 1, zeroSeqSignal.getS30RMS());
            TimeDiagramChart.addAnalogData(3, 0, zeroSeqSignal.getI30RMS());

            // логика защиты
            if (termRight.process()) {
                System.out.println(zeroSeqSignal.getTime());
                System.out.println("Защита сработала");
                break; // при срабатывании защиты происходит выход
            }
        }
    }

}

