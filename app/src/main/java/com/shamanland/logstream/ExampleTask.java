package com.shamanland.logstream;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.shamanland.common.debug.AndroidLogPrintStream;
import com.shamanland.common.debug.AndroidLogStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class ExampleTask extends AsyncTask<Void, Void, Void> {
    private final Resources mRes;
    private final OutputStream mRawStream;
    private final PrintStream mPrintStream;

    public ExampleTask(Resources res) {
        mRes = res;
        mRawStream = new AndroidLogStream(Log.VERBOSE, "RawTag");
        mPrintStream = new AndroidLogPrintStream(Log.DEBUG, "PrintTag");
    }

    @Override
    protected Void doInBackground(Void... params) {
        InputStream in = mRes.openRawResource(R.raw.example);
        byte[] buffer = new byte[1024];

        try {
            int read;
            while ((read = in.read(buffer)) > 0) {
                mRawStream.write(buffer, 0, read);
            }

            throw new IOException("example exception");
        } catch (IOException ex) {
            ex.printStackTrace(mPrintStream);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace(mPrintStream);
            }
        }

        return null;
    }
}
