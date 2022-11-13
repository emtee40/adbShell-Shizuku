package in.sunilpaulmathew.ashell.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on November 12, 2022
 */
public class ShizukuShell {

    private static List<String> mOutput;
    private static ShizukuRemoteProcess mProcess = null;
    private static String mCommand;

    public ShizukuShell(List<String> output, String command) {
        mOutput = output;
        mCommand = command;
    }

    public boolean isBusy() {
        return !mOutput.get(mOutput.size() - 1).equals("aShell: Finish");
    }

    public void exec() {
        try {
            mProcess = Shizuku.newProcess(new String[] {"sh", "-c", mCommand}, null, null);
            BufferedReader mInput = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            BufferedReader mError = new BufferedReader(new InputStreamReader(mProcess.getErrorStream()));
            String line;
            while ((line = mInput.readLine()) != null) {
                mOutput.add(line);
            }
            while ((line = mError.readLine()) != null) {
                mOutput.add(line);
            }
            mProcess.waitFor();
        } catch (Exception ignored) {
        }
    }

    public void destroy() {
        if (mProcess != null) mProcess.destroy();
    }

}