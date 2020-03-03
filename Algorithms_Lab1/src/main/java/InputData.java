import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InputData {
    private File comtrCfg, comtrDat;
    private String line;
    private String[] lineData;
    private double k1[], k2[], time;

    /*
    Name of unpacked comtrade file
     */
    private String comtrName = "PhA80";
    private String path = "C:\\Users\\Alexander\\IdeaProjects\\laba1_holodov\\src\\data\\Начало линии\\";
    private String cfgName = path + comtrName + ".cfg";
    private String datName = path + comtrName + ".dat";

    private SimpleValues sv = new SimpleValues();
    private RMSValues rms = new RMSValues();
    private Filter filter = new MiddleValues();
    private Logic logic = new Logic();

    public InputData() {
        comtrCfg = new File(cfgName);
        comtrDat = new File(datName);
    }

    public void start() throws IOException {

        filter.setSv(sv);
        filter.setRms(rms);
        logic.setRms(rms);
        BufferedReader br = new BufferedReader(new FileReader(comtrCfg));

        int lineNumber = 0;
        int count = 0;
        int numberData = 100;

        while ((line = br.readLine()) != null) {
            lineNumber++;
            if (lineNumber == 2) {
                numberData = Integer.parseInt(line.split(",")[1].replaceAll("A", ""));
                System.out.println("Number Signals: " + numberData);
                k1 = new double[numberData];
                k2 = new double[numberData];
                System.out.println("Number Data: " + numberData);

            }

            if (lineNumber > 2 && lineNumber < numberData + 3) {
//                String sp1[] = line.split(",");

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
            sv.setPhA(Double.parseDouble(lineData[2]) * k1[0] + k2[0]);
            sv.setPhB(Double.parseDouble(lineData[2]) * k1[1] + k2[1]);
            sv.setPhC(Double.parseDouble(lineData[2]) * k1[2] + k2[2]);

            filter.calculate();
            logic.process();

            Charts.addAnalogData(0, 0, sv.getPhA());
            Charts.addAnalogData(0, 1, rms.getPhA());


        }

    }

}
