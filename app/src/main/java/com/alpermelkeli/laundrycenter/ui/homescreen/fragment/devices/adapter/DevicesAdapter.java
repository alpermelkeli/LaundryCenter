package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.devices.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.model.Device;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {

    private List<Device> deviceList;

    public DevicesAdapter(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        long durationInMillis = device.getTime() - (System.currentTimeMillis() - device.getStart());
        // Device ID'sini ayarla
        holder.deviceIdTextView.setText(device.getId());

        if (durationInMillis > 0) {
            holder.statusTextView.setText("Online");
            startCountDownTimer(holder.remainingTimeTextView, durationInMillis);
        } else {
            holder.statusTextView.setText("Offline");
            holder.remainingTimeTextView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceIdTextView;
        TextView statusTextView;
        TextView remainingTimeTextView;

        DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceIdTextView = itemView.findViewById(R.id.device_id_text_view);
            statusTextView = itemView.findViewById(R.id.status_text_view);
            remainingTimeTextView = itemView.findViewById(R.id.remaining_time_text_view);
        }
    }

    // Geri sayım zamanlayıcısını başlat
    private void startCountDownTimer(final TextView textView, long durationInMillis) {
        CountDownTimer countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                textView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                textView.setText("00:00:00");
            }
        };
        countDownTimer.start();
    }
    public void updateDeviceList(List<Device> deviceList){
        this.deviceList = deviceList;

    }
}
