package in.sunilpaulmathew.ashell.utils;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on November 05, 2022
 */
public class Commands {

    private static List<CommandItems> commandList() {
        List<CommandItems> mCommands = new ArrayList<>();
        mCommands.add(new CommandItems("cat <file_path>", "Display the contents of a text file"));
        mCommands.add(new CommandItems("cp <from> <to>", "Copy a file"));
        mCommands.add(new CommandItems("cp -r <from> <to>", "Copy a file or directory"));
        mCommands.add(new CommandItems("dumpsys activity", "Print activity info"));
        mCommands.add(new CommandItems("dumpsys battery", "Primt battery stats"));
        mCommands.add(new CommandItems("dumpsys battery set level <n>", "Change the level from 0 to 100"));
        mCommands.add(new CommandItems("dumpsys battery set status <n>", "Change the level to unknown, charging, discharging, not charging or full"));
        mCommands.add(new CommandItems("dumpsys battery reset", "Reset battery"));
        mCommands.add(new CommandItems("dumpsys display", "Primt display stats"));
        mCommands.add(new CommandItems("dumpsys iphonesybinfo", "Get IMEI"));
        mCommands.add(new CommandItems("ls", "List contents of a directory"));
        mCommands.add(new CommandItems("ls -R", "List subdirectories recursively"));
        mCommands.add(new CommandItems("ls -s", "Print size of each file"));
        mCommands.add(new CommandItems("mv <from> <to>", "Move a file or directory"));
        mCommands.add(new CommandItems("netstat", "List TCP connectivity"));
        mCommands.add(new CommandItems("pm clear <package>", "Deletes all data associated with an app"));
        mCommands.add(new CommandItems("pm dump <package>", "List info of one app"));
        mCommands.add(new CommandItems("pm dump package packages", "List info of all apps"));
        mCommands.add(new CommandItems("pm grant <package> <Permission>", "Grant a permission to an app"));
        mCommands.add(new CommandItems("pm install <apk_path>", "Install an apk file"));
        mCommands.add(new CommandItems("pm install -r <apk_path>", "Update an existing apps with  new version"));
        mCommands.add(new CommandItems("pm list features", "List phone features"));
        mCommands.add(new CommandItems("pm list packages", "List package names"));
        mCommands.add(new CommandItems("pm list packages -r", "List package names along with their apk paths"));
        mCommands.add(new CommandItems("pm list packages -3", "List third party package names"));
        mCommands.add(new CommandItems("pm list packages -s", "List only system packages"));
        mCommands.add(new CommandItems("pm list packages -u", "List package names of all apps including the uninstalled ones"));
        mCommands.add(new CommandItems("pm path <package>", "Show apk file path of an app"));
        mCommands.add(new CommandItems("pm revoke <package> <Permission>", "Revoke a permission from an app"));
        mCommands.add(new CommandItems("pm reset-permissions -p <package>", "Reset permissions of an app"));
        mCommands.add(new CommandItems("pm uninstall <package>", "Uninstall an app"));
        mCommands.add(new CommandItems("pm uninstall -k --user 0 <package>", "Uninstall an app but keep the data and cache untouched"));
        mCommands.add(new CommandItems("ps", "Print process status"));
        mCommands.add(new CommandItems("pwd", "Print current working directory"));
        mCommands.add(new CommandItems("reboot", "Reboot device"));
        mCommands.add(new CommandItems("reboot recovery", "Reboot device into recovery mode"));
        mCommands.add(new CommandItems("reboot fastboot", "Reboot device into fastboot"));
        mCommands.add(new CommandItems("reboot bootloader", "Reboot device into bootloader"));
        mCommands.add(new CommandItems("rm <file_path>", "Delete a file"));
        mCommands.add(new CommandItems("rm -r <file_path>", "Delete a file or directory"));
        mCommands.add(new CommandItems("service list", "List all services"));
        mCommands.add(new CommandItems("wm density", "Displays current screen density"));
        mCommands.add(new CommandItems("wm density reset", "Reset screen density to default"));
        mCommands.add(new CommandItems("wm size", "Displays the current screen resolution"));
        mCommands.add(new CommandItems("wm size reset", "Reset screen resolution to default"));
        return mCommands;
    }

    public static List<CommandItems> getCommand(String command) {
        List<CommandItems> mCommands = new ArrayList<>();
        for (CommandItems commands: commandList()) {
            if (commands.getTitle().startsWith(command)) {
                mCommands.add(commands);
            }
        }
        return mCommands;
    }

}