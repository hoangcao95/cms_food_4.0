package vn.vano.cms.automl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class test {
    public static void main(String args[]) {

        String s = null;

        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            //Process p = Runtime.getRuntime().exec(new String[]{"C:/WINDOWS/system32/cmd.exe","/c","start","cd C:/Users/hoangcao/AppData/Local/Google/Cloud SDK/"});
            //C:\WINDOWS\system32\cmd.exe /k ""C:\Users\hoangcao\AppData\Local\Google\Cloud SDK\cloud_env.bat""
//            Process p = Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\cmd.exe /c start cd C:\\Users\\hoangcao\\AppData\\Local\\Google\\Cloud SDK\\ & gsutil -m cp -r image_food gs://test-auto-ml-233110-vcm");
            Process p = Runtime.getRuntime().exec("cmd /c cd C:\\Users\\hoangcao\\AppData\\Local\\Google\\Cloud SDK\\ & gsutil -m cp -r FOOD_V02 gs://test-auto-ml-233110-vcm");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
