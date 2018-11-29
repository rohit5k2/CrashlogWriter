package rohit5k2.logwriter;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private String url;
    private final Context _context;
    private final boolean shouldWriteCrashLogToFile = true;
    private final boolean shouldWriteCrashLogToServer = true;

    /**
     * if any of the parameters is null, the respective functionality
     * will not be used
     * @param context
     * @param url
     */
    public CustomExceptionHandler(Context context, String url) {
        this.url = url;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this._context = context;
    }

    /**
     *
     * @param t
     * @param e
     */
    public void uncaughtException(Thread t, Throwable e) {
        final String timestamp = new Date().getTime() + "";
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();

        if (shouldWriteCrashLogToFile) {
            writeToFile(timestamp, stacktrace);
        }

        if (url != null && shouldWriteCrashLogToServer) {
            sendToServer(timestamp, stacktrace);
        }

        defaultUEH.uncaughtException(t, e);
    }

    /**
     * Writes crash log to a file named with current timestamp in a folder /data/data/<packageName>/crashes
     * @param stacktrace
     * @param timestamp
     */
    private void writeToFile(String timestamp, String stacktrace) {
        try {
            File folder = new File(_context.getFilesDir() + "/crashes");
            boolean success = true;
            if(!folder.exists())
                success = folder.mkdir();

            if(success) {
                File logFile = new File(folder, timestamp + ".crash");
                if(!logFile.exists()) {
                    logFile.createNewFile();
                    BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
                    buf.write(stacktrace);
                    buf.close();
                }
                else
                    Log.e("CustomExceptionHandler", "How did file already exist? Something is wrong here.");
            }
            else
                Log.e("CustomExceptionHandler", "Couldn't create the directory.");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void sendToServer(String timestamp, String stacktrace) {
        String deviceId = Settings.Secure.getString(_context.getContentResolver(), Settings.Secure.ANDROID_ID);
        new WriteLogToServer().execute(deviceId, timestamp, stacktrace);
    }
}