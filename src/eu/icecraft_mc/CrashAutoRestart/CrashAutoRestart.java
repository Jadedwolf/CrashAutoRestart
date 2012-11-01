package eu.icecraft_mc.CrashAutoRestart;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import com.drdanick.McRKit.*;
import com.drdanick.McRKit.module.*;

public class CrashAutoRestart extends Module {

    public CrashAutoRestart(ModuleMetadata meta, ModuleLoader loader, ClassLoader cl) {
        super(meta, loader, cl, ToolkitEvent.ON_TOOLKIT_START, ToolkitEvent.NULL_EVENT);
    }

    @Override
    public void onStderrString(String raw) {
        String err = raw.replaceFirst(".*?\\s.*?\\s", "");
        if (!err.startsWith("[SEVERE] ")) return;
        err = err.replaceFirst("[SEVERE]\\s", "");
        if (err.startsWith("Encountered an unexpected exception")) {
            System.out.println("Restarting due to server crash");
            try {
                List<String> linesToExecute = readAllLines("toolkit/crash_lines.txt");

                if (linesToExecute != null) {
                    Field consoleField = Wrapper.class.getDeclaredField("console");
                    consoleField.setAccessible(true);
                    OutputStream console = (OutputStream) consoleField.get(Wrapper.getInstance());
                    for (String line : linesToExecute)
                        if (line.startsWith(".")) Wrapper.getInstance().parseConsoleInput(line.substring(1));
                        else {
                            console.write(line.getBytes());
                            console.flush();
                        }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Wrapper.getInstance().rescheduleRestart("1s");
        }
    }

    @Override
    protected void onDisable() {}

    @Override
    protected void onEnable() {}

    private List<String> readAllLines(String path) {
        try {
            List<String> result = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new FileReader(path));
            for (;;) {
                String line = reader.readLine();
                if (line == null) break;
                result.add(line);
            }
            reader.close();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
