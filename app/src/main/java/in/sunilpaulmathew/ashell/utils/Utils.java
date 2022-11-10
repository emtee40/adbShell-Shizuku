package in.sunilpaulmathew.ashell.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> runCommand(String command) {
        List<String> output = new ArrayList<>();
        try {
            mProcess = Shizuku.newProcess(new String[] {"sh", "-c", command}, null, null);
            BufferedReader mInput = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            BufferedReader mError = new BufferedReader(new InputStreamReader(mProcess.getErrorStream()));
            String line;
            while ((line = mInput.readLine()) != null) {
                output.add(line);
            }
            while ((line = mError.readLine()) != null) {
                output.add(line);
            }
            mProcess.waitFor();
        } catch (Exception e) {
            output.add(e.getMessage());
        }
        return output;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static void destroyProcess() {
        if (mProcess != null) mProcess.destroy();
    }

    public static void loadShizukuWeb(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://shizuku.rikka.app/"));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
        }
    }

}