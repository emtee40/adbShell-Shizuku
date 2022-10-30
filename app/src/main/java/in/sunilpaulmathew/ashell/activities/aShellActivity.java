package in.sunilpaulmathew.ashell.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import in.sunilpaulmathew.ashell.R;
import in.sunilpaulmathew.ashell.fragments.aShellFragment;
import in.sunilpaulmathew.ashell.utils.Utils;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on October 28, 2022
 */
public class aShellActivity extends AppCompatActivity {

    private boolean mExit;
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ashell);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new aShellFragment()).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.destroyProcess();
    }

    @Override
    public void onBackPressed() {
        if (mExit) {
            mExit = false;
            Utils.destroyProcess();
            finish();
        } else {
            Utils.snackBar(findViewById(android.R.id.content), getString(R.string.press_back)).show();
            mExit = true;
            mHandler.postDelayed(() -> mExit = false, 2000);
        }
    }

}