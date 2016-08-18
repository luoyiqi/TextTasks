package de.lenidh.texttasks.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

public final class Utils {

    private Utils() {
    }

    public static File getTodoTxtFile(Context context) {
        String key = context.getString(R.string.pref_key_todo_file);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String path = prefs.getString(key, "");

        File file;
        if(path.startsWith("/")) {
            file = new File(path);
        } else {
            File storage = Environment.getExternalStorageDirectory();
            file = new File(storage, path);
        }
        return file;
    }
}
