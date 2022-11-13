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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import in.sunilpaulmathew.ashell.BuildConfig;
import in.sunilpaulmathew.ashell.R;
import in.sunilpaulmathew.ashell.activities.ExamplesActivity;
import in.sunilpaulmathew.ashell.adapters.CommandsAdapter;
import in.sunilpaulmathew.ashell.adapters.ShellOutputAdapter;
import in.sunilpaulmathew.ashell.utils.Commands;
import in.sunilpaulmathew.ashell.utils.ShizukuShell;
import in.sunilpaulmathew.ashell.utils.Utils;
import rikka.shizuku.Shizuku;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on October 28, 2022
 */
public class aShellFragment extends Fragment {

    private AppCompatAutoCompleteTextView mCommand;
    private AppCompatEditText mSearchWord;
    private AppCompatImageButton mClearButton, mHistoryButton, mSearchButton, mSettingsButton;
    private RecyclerView mRecyclerViewOutput;
    private ShizukuShell mShizukuShell = null;
    private Thread mRefreshThread = null;
    private boolean mExit;
    private final Handler mHandler = new Handler();
    private int mPosition = 1;
    private List<String> mHistory = null, mResult = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_ashell, container, false);

        mCommand = mRootView.findViewById(R.id.shell_command);
        mSearchWord = mRootView.findViewById(R.id.search_word);
        mClearButton = mRootView.findViewById(R.id.clear);
        mHistoryButton = mRootView.findViewById(R.id.history);
        mSettingsButton = mRootView.findViewById(R.id.settings);
        mSearchButton = mRootView.findViewById(R.id.search);
        mRecyclerViewOutput = mRootView.findViewById(R.id.recycler_view_output);
        mRecyclerViewOutput.setLayoutManager(new LinearLayoutManager(requireActivity()));

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
                    initializeShell(requireActivity());
                } else {
                    RecyclerView mRecyclerViewCommands = mRootView.findViewById(R.id.recycler_view_commands);
                    if (!s.toString().isEmpty()) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            CommandsAdapter mCommandsAdapter = new CommandsAdapter(Commands.getCommand(s.toString()));
                            mRecyclerViewCommands.setLayoutManager(new LinearLayoutManager(requireActivity()));
                            mRecyclerViewCommands.setAdapter(mCommandsAdapter);
                            mRecyclerViewCommands.setVisibility(View.VISIBLE);
                            mCommandsAdapter.setOnItemClickListener((command, v) -> {
                                if (command.contains(" <")) {
                                    mCommand.setText(command.split("<")[0]);
                                } else {
                                    mCommand.setText(command);
                                }
                                mCommand.setSelection(mCommand.getText().length());
                            });
                        });
                    } else {
                        mRecyclerViewCommands.setVisibility(View.GONE);
                    }
                }
            }
        });

        mSettingsButton.setOnClickListener(v -> {
            if (mShizukuShell != null && mShizukuShell.isBusy()) {
                Utils.snackBar(mRootView, getString(R.string.app_busy_message)).show();
                return;
            }
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
            if (mResult == null) return;
            if (mShizukuShell != null && mShizukuShell.isBusy()) {
                Utils.snackBar(mRootView, getString(R.string.app_busy_message)).show();
                return;
            }
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

        mSearchButton.setOnClickListener(v -> {
            if (mShizukuShell != null && mShizukuShell.isBusy()) {
                Utils.snackBar(mRootView, getString(R.string.app_busy_message)).show();
                return;
            }
            if (mSearchWord.getVisibility() == View.GONE) {
                mSearchWord.setVisibility(View.VISIBLE);
                mHistoryButton.setVisibility(View.GONE);
                mClearButton.setVisibility(View.GONE);
                mSettingsButton.setVisibility(View.GONE);
                mSearchButton.setVisibility(View.GONE);
                mCommand.setText(null);
                mCommand.setHint(null);
                mSearchWord.requestFocus();
            }
        });

        mSearchWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().trim().isEmpty()) {
                    updateUI(mResult);
                } else {
                    List<String> mResultSorted = new ArrayList<>();
                    for (int i = mPosition; i < mResult.size(); i++) {
                        if (mResult.get(i).toLowerCase().contains(s.toString().toLowerCase())) {
                            mResultSorted.add(mResult.get(i));
                        }
                    }
                    updateUI(mResultSorted);
                }
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

        mRefreshThread = new RefreshThread();
        mRefreshThread.start();

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mSearchWord.getVisibility() == View.VISIBLE) {
                    hideSearchBar();
                } else if (mShizukuShell.isBusy()) {
                    new MaterialAlertDialogBuilder(requireActivity())
                            .setCancelable(false)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle(getString(R.string.app_name))
                            .setMessage(getString(R.string.process_destroy_message))
                            .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                            })
                            .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> mShizukuShell.destroy()
                            ).show();
                } else if (mExit) {
                    mExit = false;
                    requireActivity().finish();
                } else {
                    Utils.snackBar(mRootView, getString(R.string.press_back)).show();
                    mExit = true;
                    mHandler.postDelayed(() -> mExit = false, 2000);
                }
            }
        });

        return mRootView;
    }

    private List<String> getRecentCommands() {
        List<String> mRecentCommands = new ArrayList<>(mHistory);
        Collections.reverse(mRecentCommands);
        return mRecentCommands;
    }

    private void clearAll() {
        if (mShizukuShell != null) mShizukuShell.destroy();
        mResult = null;
        mRecyclerViewOutput.setAdapter(null);
        mSearchButton.setVisibility(View.GONE);
        mCommand.setHint(getString(R.string.command_hint));
        mCommand.requestFocus();
    }

    private void hideSearchBar() {
        mSearchWord.setText(null);
        mCommand.requestFocus();
        mSearchWord.setVisibility(View.GONE);
        mSettingsButton.setVisibility(View.VISIBLE);
        mHistoryButton.setVisibility(View.VISIBLE);
        mClearButton.setVisibility(View.VISIBLE);
        mSearchButton.setVisibility(View.VISIBLE);
    }

    private void initializeShell(Activity activity) {
        if (mCommand.getText() == null || mCommand.getText().toString().trim().isEmpty()) {
            return;
        }
        if (mShizukuShell != null) mShizukuShell.destroy();
        runShellCommand(mCommand.getText().toString().replace("\n", ""), activity);
    }

    private void runShellCommand(String command, Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        if (mHistory == null) mHistory = new ArrayList<>();
        mCommand.setText(null);
        mCommand.setHint(null);
        if (mSearchWord.getVisibility() == View.VISIBLE) {
            hideSearchBar();
        }

        String finalCommand;
        if (command.startsWith("adb shell ")) {
            finalCommand = command.replace("adb shell ", "");
        } else if (command.startsWith("adb -d shell ")) {
            finalCommand = command.replace("adb -d shell ", "");
        } else {
            finalCommand = command;
        }
        mHistory.add(finalCommand);
        String mTitleText = "<font color=\"" + Utils.getColor(R.color.colorBlue, activity) + "\">shell@" + Utils.getDeviceName() + "</font># <i>" + finalCommand + "</i>";
        if (mResult == null) {
            mResult = new ArrayList<>();
        }
        mResult.add(mTitleText);

        ExecutorService mExecutors = Executors.newSingleThreadExecutor();
        mExecutors.execute(() -> {
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                mPosition = mResult.size();
                mShizukuShell = new ShizukuShell(mResult, finalCommand);
                mShizukuShell.exec();
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException ignored) {}
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (mHistoryButton.getVisibility() != View.VISIBLE) {
                        mHistoryButton.setVisibility(View.VISIBLE);
                    }
                    if (mResult != null && mResult.size() > 0) {
                        mSearchButton.setVisibility(View.VISIBLE);
                        mResult.add("aShell: Finish");
                    }
                    mCommand.requestFocus();
                });
            } else {
                new Handler(Looper.getMainLooper()).post(() ->
                        new MaterialAlertDialogBuilder(activity)
                                .setCancelable(false)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle(getString(R.string.app_name))
                                .setMessage(getString(R.string.shizuku_access_denied_message))
                                .setNegativeButton(getString(R.string.quit), (dialogInterface, i) -> activity.finish())
                                .setPositiveButton(getString(R.string.request_permission), (dialogInterface, i) -> Shizuku.requestPermission(0)
                                ).show());
            }
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            if (!mExecutors.isShutdown()) mExecutors.shutdown();
        });
    }

    private void updateUI(List<String> data) {
        List<String> mData = new ArrayList<>();
        try {
            for (String result : data) {
                if (!result.trim().isEmpty() && !result.equals("aShell: Finish")) {
                    mData.add(result);
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
        ExecutorService mExecutors = Executors.newSingleThreadExecutor();
        mExecutors.execute(() -> {
            ShellOutputAdapter mShellOutputAdapter = new ShellOutputAdapter(mData);
            new Handler(Looper.getMainLooper()).post(() -> {
                mRecyclerViewOutput.setAdapter(mShellOutputAdapter);
                mRecyclerViewOutput.scrollToPosition(mData.size() - 1);
            });
            if (!mExecutors.isShutdown()) mExecutors.shutdown();
        });
    }

    private class RefreshThread extends Thread {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(250);
                    requireActivity().runOnUiThread(() -> {
                        if (mResult != null && mResult.size() > 0 && !mResult.get(mResult.size() - 1).equals("aShell: Finish")) {
                            updateUI(mResult);
                        }
                    });
                }
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRefreshThread != null) {
            try {
                mRefreshThread.interrupt();
            } catch(Exception ignored) {}
        }
        if (mShizukuShell != null) mShizukuShell.destroy();
    }

}