package in.sunilpaulmathew.ashell.utils;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.sunilpaulmathew.ashell.R;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on October 28, 2022
 */
public class Utils {

    public static boolean isBookmarked(String command, Context context) {
        return new File(context.getExternalFilesDir("bookmarks"), command).exists();
    }

    public static boolean deleteFromBookmark(String command, Context context) {
        return new File(context.getExternalFilesDir("bookmarks"), command).delete();
    }

    public static Drawable getDrawable(int drawable, Context context) {
        return ContextCompat.getDrawable(context, drawable);
    }

    public static int getColor(int color, Context context) {
        return ContextCompat.getColor(context, color);
    }

    public static List<String> getBookmarks(Context context) {
        List<String> mBookmarks = new ArrayList<>();
        for (File file : Objects.requireNonNull(context.getExternalFilesDir("bookmarks").listFiles())) {
            mBookmarks.add(file.getName());
        }
        return mBookmarks;
    }

    public static Snackbar snackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss());
        return snackbar;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static void addToBookmark(String command, Context context) {
        create(command, new File(context.getExternalFilesDir("bookmarks"), command));
    }

    public static void copyToClipboard(String text, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied to clipboard", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    public static void create(String text, File path) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(text);
            writer.close();
        } catch (IOException ignored) {
        }
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