import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NLP {

    public static List<String> splitText() {
        InputStreamReader reader = null;
        BufferedReader br = null;
        List<String> tokens;

        try {
            reader = new InputStreamReader(new FileInputStream("/home/libby/Dev/AP/resources/Q4/warandpeaceTEST.txt"));
            br = new BufferedReader(reader);
            String line = br.readLine();
            tokens = new ArrayList<>();
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    String[] result = line.split("\\s+");
                    tokens.addAll(Arrays.asList(result));
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return splitText();
    }


    public static void main(String[] args) {
        new NLP();
        try {
            splitText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
