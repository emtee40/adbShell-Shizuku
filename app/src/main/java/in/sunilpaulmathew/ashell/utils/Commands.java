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
        mCommands.add(new CommandItems("pm clear <package>", "Delete all data associated with an app"));
        mCommands.add(new CommandItems("pm clear --cache-only <package>", "Only clear cache data associated with an app"));
        mCommands.add(new CommandItems("pm clear --user <user_id> <package>", "Only delete the data associated with a given user"));
        mCommands.add(new CommandItems("pm disable <package/component>", "Disable a given package or component (written as package/class"));
        mCommands.add(new CommandItems("pm dump <package>", "List info of one app"));
        mCommands.add(new CommandItems("pm dump package packages", "List info of all apps"));
        mCommands.add(new CommandItems("pm enable <package/component>", "Enable a given package or component (written as package/class"));
        mCommands.add(new CommandItems("pm grant <package> <Permission>", "Grant a permission to an app"));
        mCommands.add(new CommandItems("pm install <apk_path>", "Install an apk file"));
        mCommands.add(new CommandItems("pm install -d <apk_path>", "Allow version code downgrade (debuggable packages only)"));
        mCommands.add(new CommandItems("pm install -f <apk_path>", "Install application on internal flash"));
        mCommands.add(new CommandItems("pm install -g <apk_path>", "Grant all runtime permissions"));
        mCommands.add(new CommandItems("pm install -i <installer> <apk_path>", "Specify package name of installer owning the app"));
        mCommands.add(new CommandItems("pm install -p <split_apk_path>", "Partial application install (new split on top of existing pkg)"));
        mCommands.add(new CommandItems("pm install -R <apk_path>", "Update an existing apps, but disallow replacement of existing one"));
        mCommands.add(new CommandItems("pm install -t <apk_path>", "Allow installing test packages"));
        mCommands.add(new CommandItems("pm install --abi <apk_path>", "Override the default ABI of the platform"));
        mCommands.add(new CommandItems("pm install --dont-kill <apk_path>", "Install a new feature split without killing the running app"));
        mCommands.add(new CommandItems("pm install --full <apk_path>", "Cause the app to be installed as a non-ephemeral full app"));
        mCommands.add(new CommandItems("pm install --install-location <location> <apk_path>", "Force the install location (Options, 0=auto, 1=internal only, 2=prefer external)"));
        mCommands.add(new CommandItems("pm install --install-reason <reason> <apk_path>", "Indicates why the app is being installed (Options, 0=unknown, 1=admin policy, 2=device restore,3=device setup, 4=user request)"));
        mCommands.add(new CommandItems("pm install --instant <apk_path>", "Cause the app to be installed as an ephemeral install app"));
        mCommands.add(new CommandItems("pm install --restrict-permissions <apk_path>", "Don't whitelist restricted permissions at install"));
        mCommands.add(new CommandItems("pm install --pkg <package> <apk_path>", "Specify expected package name of app being installed"));
        mCommands.add(new CommandItems("pm install --user <user_id> <apk_path>", "Install for a given user"));
        mCommands.add(new CommandItems("pm install-abandon <session_id>", "Delete the given active install session"));
        mCommands.add(new CommandItems("pm install-commit <session_id>", "Commit the given active install session, installing the app"));
        mCommands.add(new CommandItems("pm install-create", "Create an install session"));
        mCommands.add(new CommandItems("pm install-existing", "Installs an existing application for a new user"));
        mCommands.add(new CommandItems("pm install-existing --full <package>", "Install as a full app"));
        mCommands.add(new CommandItems("pm install-existing --instant <package>", "Install as an instant app"));
        mCommands.add(new CommandItems("pm install-existing --restrict-permissions <package>", "Don't whitelist restricted permissions"));
        mCommands.add(new CommandItems("pm install-existing --user <user_id> <package>", "Install for a given user"));
        mCommands.add(new CommandItems("pm install-existing --wait <package>", "Wait until the package is installed"));
        mCommands.add(new CommandItems("pm install-remove <session_id> <split_name>", "Mark SPLIT(s) as removed in the given install session"));
        mCommands.add(new CommandItems("pm install-write <size> <session_id> <split_name> <split_path>", "Write an apk into the given install session"));
        mCommands.add(new CommandItems("pm list features", "List phone features"));
        mCommands.add(new CommandItems("pm list libraries", "List all system libraries"));
        mCommands.add(new CommandItems("pm list packages", "List package names"));
        mCommands.add(new CommandItems("pm list packages -3", "List only third party packages"));
        mCommands.add(new CommandItems("pm list packages -a", "List all known packages, but excluding APEXes"));
        mCommands.add(new CommandItems("pm list packages -d", "List all disabled packages"));
        mCommands.add(new CommandItems("pm list packages -e", "List all enabled packages"));
        mCommands.add(new CommandItems("pm list packages -f", "List package names along with their associated file"));
        mCommands.add(new CommandItems("pm list packages -i", "List package names along with their installer"));
        mCommands.add(new CommandItems("pm list packages -s", "List only system packages"));
        mCommands.add(new CommandItems("pm list packages --show-versioncode", "List package names along with their version code"));
        mCommands.add(new CommandItems("pm list packages -u", "List package names of all apps including the uninstalled ones"));
        mCommands.add(new CommandItems("pm list packages -U", "List package names along with their package UID"));
        mCommands.add(new CommandItems("pm list permissions", "Print all known permission groups"));
        mCommands.add(new CommandItems("pm list permissions -d", "Print only dangerous permissions"));
        mCommands.add(new CommandItems("pm list permissions -f", "Print all information about the known permissions"));
        mCommands.add(new CommandItems("pm list permissions -g", "Organize all known permission by group"));
        mCommands.add(new CommandItems("pm list permissions -s", "Print a short summary about the known permissions"));
        mCommands.add(new CommandItems("pm list permissions -u", "Print only permissions that users will see"));
        mCommands.add(new CommandItems("pm list users", "List all user names"));
        mCommands.add(new CommandItems("pm path <package>", "Show apk file path of an app"));
        mCommands.add(new CommandItems("pm revoke <package> <Permission>", "Revoke a permission from an app"));
        mCommands.add(new CommandItems("pm reset-permissions -p <package>", "Reset permissions of an app"));
        mCommands.add(new CommandItems("pm uninstall <package>", "Uninstall an app"));
        mCommands.add(new CommandItems("pm uninstall -k <package>", "Uninstall an app but keep the data and cache untouched"));
        mCommands.add(new CommandItems("pm uninstall --user <user_id> <package>", "Uninstall an app from a given user"));
        mCommands.add(new CommandItems("pm uninstall --versionCode <version_code> <package>", "Only uninstall if the app has the given version code"));
        mCommands.add(new CommandItems("pm uninstall-system-updates", "Removes updates to all system applications and falls back to its system version"));
        mCommands.add(new CommandItems("pm uninstall-system-updates <package>", "Removes updates to a given system application and falls back to its system version"));
        mCommands.add(new CommandItems("ps", "Print process status"));
        mCommands.add(new CommandItems("pwd", "Print current working directory"));
        mCommands.add(new CommandItems("reboot", "Reboot device"));
        mCommands.add(new CommandItems("reboot -p", "Shutdown device"));
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