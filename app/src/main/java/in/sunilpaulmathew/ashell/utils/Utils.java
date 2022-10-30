package in.sunilpaulmathew.ashell.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import in.sunilpaulmathew.ashell.R;
import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on October 28, 2022
 */
public class Utils {
    private static ShizukuRemoteProcess mProcess = null;

    public static Snackbar snackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss());
        return snackbar;
    }

    public static String runCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            mProcess = Shizuku.newProcess(new String[] {"sh", "-c", command}, null, null);
            BufferedReader mInput = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            BufferedReader mError = new BufferedReader(new InputStreamReader(mProcess.getErrorStream()));
            String line;
            while ((line = mInput.readLine()) != null) {
                output.append(line).append("\n");
            }
            while ((line = mError.readLine()) != null) {
                output.append(line).append("\n");
            }
            mProcess.waitFor();
        } catch (Exception e) {
            output.append(e.getMessage()).append("\n");
        }
        return output.toString();
    }

    public static void destroyProcess() {
        if (mProcess != null) mProcess.destroy();
    }

}