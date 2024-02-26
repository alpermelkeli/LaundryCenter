package com.alpermelkeli.laundrycenter.TimeAPI;

import android.util.Log;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TimeApi {

    public interface OnTimeReceivedListener {
        void onTimeReceived(long timeMillis);
        void onError(Exception e);
    }

    public void getCurrentTimeMillis(OnTimeReceivedListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Long> future = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                NTPUDPClient client = new NTPUDPClient();
                try {
                    client.open();
                    TimeInfo info = client.getTime(InetAddress.getByName("pool.ntp.org"));
                    info.computeDetails();
                    long offset = info.getOffset();
                    return System.currentTimeMillis() + offset;
                } catch (IOException e) {
                    Log.e("NTPTime", "Error fetching time from NTP server", e);
                    throw e;
                } finally {
                    client.close();
                }
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long timeMillis = future.get();
                    listener.onTimeReceived(timeMillis);
                } catch (Exception e) {
                    listener.onError(e);
                }
            }
        });
    }

}
