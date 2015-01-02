package nu.aing.loadmorerecycleview.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import nu.aing.loadmorerecycleview.R;

/**
 * @author Fanny Irawan Sutawanir (fannyirawans@gmail.com)
 */
public class LoadTask extends AsyncTask<String, Void, List<String>> {

    private final Context mContext;
    private final int mStart;
    private final ProgressDialog mDialog;
    private boolean error = false;

    public LoadTask(
            final Context context,
            final int start) {
        mContext = context;
        mStart = start;
        mDialog = new ProgressDialog(context);
    }

    @Override
    protected List<String> doInBackground(String... params) {
        List<String> contents = new ArrayList<>();
        InputStream is = mContext.getResources().openRawResource(R.raw.data);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr, 8192); // 2nd arg is buffer
        int index = 0;

        try {
            String test;
            while(true) {
                test = br.readLine();
                // readLine() returns null if no more lines in the file
                if(test == null || contents.size() == 10) {
                    break;
                }
                if(mStart != 0 && index++ <= mStart) {
                    continue;
                }

                contents.add(test);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            error = true;
        }
        finally {
            try {
                isr.close();
                is.close();
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return contents;
    }

    @Override
    protected void onPreExecute() {
        mDialog.setMessage("Getting your data... Please wait...");
        mDialog.show();
    }

    @Override
    protected void onCancelled() {
        mDialog.dismiss();
        Toast toast = Toast.makeText(mContext, "Error connecting to Server", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();

    }

    @Override
    protected void onPostExecute(final List<String> contents) {
        Toast toast;
        if(error) {
            toast = Toast.makeText(mContext, "Error", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
            mDialog.dismiss();
        }
        else {
            EventBus.getDefault().post(new IEvent() {
                @Override
                public List<String> getData() {
                    return contents;
                }

                @Override
                public void closeDialog() {
                    mDialog.dismiss();
                }
            });
        }
    }
}
