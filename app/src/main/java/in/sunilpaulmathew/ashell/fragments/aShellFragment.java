package in.sunilpaulmathew.ashell.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.sunilpaulmathew.ashell.BuildConfig;
import in.sunilpaulmathew.ashell.R;
import in.sunilpaulmathew.ashell.activities.ExamplesActivity;
import in.sunilpaulmathew.ashell.adapters.CommandsAdapter;
import in.sunilpaulmathew.ashell.utils.Commands;
import in.sunilpaulmathew.ashell.utils.Utils;
import rikka.shizuku.Shizuku;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on October 28, 2022
 */
public class aShellFragment extends Fragment {

    private AppCompatAutoCompleteTextView mCommand;
    private AppCompatImageButton mHistoryButton;
    private MaterialTextView mOutput, mTitle;

    private CommandsAdapter mRecycleViewAdapter;

    private List<String> mHistory = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_ashell, container, false);

        mCommand = mRootView.findViewById(R.id.shell_command);
        mOutput = mRootView.findViewById(R.id.shell_output);
        mTitle = mRootView.findViewById(R.id.shell_title);
        AppCompatImageButton mClearButton = mRootView.findViewById(R.id.clear);
        mHistoryButton = mRootView.findViewById(R.id.history);
        AppCompatImageButton mSettingsButton = mRootView.findViewById(R.id.settings);
        RecyclerView mRecyclerView = mRootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));

        mCommand.requestFocus();

        mCommand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains("\n")) {
                    if (!s.toString().endsWith("\n")) {
                        mCommand.setText(s.toString().replace("\n", ""));
                    }
                    runShellCommand(requireActivity());
                } else {
                    if (!s.toString().isEmpty()) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            mRecycleViewAdapter = new CommandsAdapter(Commands.getCommand(s.toString()));
                            mRecyclerView.setAdapter(mRecycleViewAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);

                            mRecycleViewAdapter.setOnItemClickListener((command, v) -> {
                                if (command.contains(" <")) {
                                    mCommand.setText(command.split("<")[0]);
                                } else {
                                    mCommand.setText(command);
                                }
                                mCommand.setSelection(mCommand.getText().length());
                            });
                        });
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                }
            }
        });

        mSettingsButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), mSettingsButton);
            Menu menu = popupMenu.getMenu();
            menu.add(Menu.NONE, 0, Menu.NONE, R.string.shizuku_about);
            menu.add(Menu.NONE, 1, Menu.NONE, R.string.change_logs);
            menu.add(Menu.NONE, 2, Menu.NONE, R.string.examples);
            menu.add(Menu.NONE, 3, Menu.NONE, R.string.about);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 0) {
                    Utils.loadShizukuWeb(requireActivity());
                } else if (item.getItemId() == 1) {
                    new MaterialAlertDialogBuilder(requireActivity())
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle(getString(R.string.change_logs))
                            .setMessage(getString(R.string.change_logs_summary, getString(
                                    R.string.app_name) + " " + BuildConfig.VERSION_NAME))
                            .setPositiveButton(getString(R.string.cancel), (dialogInterface, i) -> {
                            }).show();
                } else if (item.getItemId() == 2) {
                    Intent examples = new Intent(requireActivity(), ExamplesActivity.class);
                    startActivity(examples);
                } else if (item.getItemId() == 3) {
                    new MaterialAlertDialogBuilder(requireActivity())
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle(getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME)
                            .setMessage("Copyright: © 2022–2023, sunilpaulmathew\n\nCredits:\nRikkaApps: Shizuku")
                            .setPositiveButton(getString(R.string.cancel), (dialogInterface, i) -> {
                            }).show();
                }
                return false;
            });
            popupMenu.show();
        });

        mClearButton.setOnClickListener(v -> {
            if (mOutput.getText() == null || mOutput.getText().toString().isEmpty()) return;
            if (PreferenceManager.getDefaultSharedPreferences(requireActivity()).getBoolean("clearAllMessage", true)) {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.clear_all_message))
                        .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                        })
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            PreferenceManager.getDefaultSharedPreferences(requireActivity()).edit().putBoolean("clearAllMessage", false).apply();
                            clearAll();
                        }).show();
            } else {
                clearAll();
            }
        });

        mHistoryButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), mCommand);
            Menu menu = popupMenu.getMenu();
            for (int i = 0; i < getRecentCommands().size(); i++) {
                menu.add(Menu.NONE, i, Menu.NONE, getRecentCommands().get(i));
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                for (int i = 0; i < getRecentCommands().size(); i++) {
                    if (item.getItemId() == i) {
                        mCommand.setText(getRecentCommands().get(i));
                        mCommand.setSelection(mCommand.getText().length());
                    }
                }
                return false;
            });
            popupMenu.show();
        });

        return mRootView;
    }

    private List<String> getRecentCommands() {
        List<String> mRecentCommands = new ArrayList<>(mHistory);
        Collections.reverse(mRecentCommands);
        return mRecentCommands;
    }

    private void clearAll() {
        mOutput.setText(null);
        mTitle.setText(null);
        mCommand.setHint(getString(R.string.command_hint));
        mCommand.requestFocus();
    }

    private void runShellCommand(Activity activity) {
        if (mCommand.getText() == null || mCommand.getText().toString().isEmpty()) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        if (mHistory == null) mHistory = new ArrayList<>();

        ExecutorService mExecutors = Executors.newSingleThreadExecutor();
        mExecutors.execute(() -> {
            String finalCommand = mCommand.getText().toString().replace("\n", "");
            if (finalCommand.startsWith("adb shell ")) {
                finalCommand = finalCommand.replace("adb shell ", "");
            } else if (finalCommand.startsWith("adb -d shell ")) {
                finalCommand = finalCommand.replace("adb -d shell ", "");
            }
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                String mResult = Utils.runCommand(finalCommand);
                String mTitleText = "shell@" + Utils.runCommand("getprop ro.build.product")
                        .replace("\n", "") + "# " + finalCommand;
                mHistory.add(finalCommand);
                new Handler(Looper.getMainLooper()).post(() -> {
                    mTitle.setText(mTitleText);
                    mOutput.setText(mResult);
                    if (mHistory.size() > 0 && mHistoryButton.getVisibility() != View.VISIBLE) {
                        mHistoryButton.setVisibility(View.VISIBLE);
                    }
                    mCommand.setText(null);
                    mCommand.setHint(null);
                    mCommand.requestFocus();
                });
            } else {
                new Handler(Looper.getMainLooper()).post(() ->
                        new MaterialAlertDialogBuilder(requireActivity())
                                .setCancelable(false)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle(getString(R.string.app_name))
                                .setMessage(getString(R.string.shizuku_access_denied_message))
                                .setNegativeButton(getString(R.string.quit), (dialogInterface, i) -> requireActivity().finish())
                                .setPositiveButton(getString(R.string.request_permission), (dialogInterface, i) -> Shizuku.requestPermission(0)
                                ).show());
            }
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            if (!mExecutors.isShutdown()) mExecutors.shutdown();
        });
    }

}