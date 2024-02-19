package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.devices.adapter;

import android.content.Context;
import android.graphics.Color;
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
        holder.deviceIdTextView.setText("No: " + device.getName());

        if (durationInMillis > 0) {
            holder.statusTextView.setText("Çalışıyor");
            holder.statusTextView.setTextColor(Color.parseColor("#D23C1B"));
            startCountDownTimer(holder.remainingTimeTextView,holder.statusTextView,durationInMillis);
        } else {
            holder.statusTextView.setText("Uygun");
            holder.statusTextView.setTextColor(Color.parseColor("#08831C"));
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

    private void startCountDownTimer(final TextView textView, final TextView statusText ,long durationInMillis) {
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
                textView.setText("");
                statusText.setTextColor(Color.parseColor("#08831C"));
                statusText.setText("Uygun");
            }
        };
        countDownTimer.start();
    }
    public void updateDeviceList(List<Device> deviceList){
        this.deviceList = deviceList;

    }
}
