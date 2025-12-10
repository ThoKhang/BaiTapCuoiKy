package com.example.apphoctapchotre.UI.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.Repository.BaoThucRepository;


public class ThongBaoViewModel extends AndroidViewModel {

    private final BaoThucRepository repo;

    private final MutableLiveData<String> timeMorning = new MutableLiveData<>();
    private final MutableLiveData<String> timeNoon = new MutableLiveData<>();
    private final MutableLiveData<String> timeEvening = new MutableLiveData<>();

    private final MutableLiveData<Boolean> alarmMorning = new MutableLiveData<>();
    private final MutableLiveData<Boolean> alarmNoon = new MutableLiveData<>();
    private final MutableLiveData<Boolean> alarmEvening = new MutableLiveData<>();

    public ThongBaoViewModel(@NonNull Application application) {
        super(application);
        repo = new BaoThucRepository(application);

        timeMorning.setValue(repo.getTime("time_morning", "06:00"));
        timeNoon.setValue(repo.getTime("time_noon", "12:00"));
        timeEvening.setValue(repo.getTime("time_evening", "20:00"));

        alarmMorning.setValue(repo.getAlarmEnabled("alarm_morning", false));
        alarmNoon.setValue(repo.getAlarmEnabled("alarm_noon", false));
        alarmEvening.setValue(repo.getAlarmEnabled("alarm_evening", false));
    }

    public LiveData<String> getTimeMorning() { return timeMorning; }
    public LiveData<String> getTimeNoon() { return timeNoon; }
    public LiveData<String> getTimeEvening() { return timeEvening; }

    public LiveData<Boolean> getAlarmMorning() { return alarmMorning; }
    public LiveData<Boolean> getAlarmNoon() { return alarmNoon; }
    public LiveData<Boolean> getAlarmEvening() { return alarmEvening; }

    // Người dùng thay đổi giờ (qua dialog)
    public void updateTime(String type, String time) {
        repo.saveTime("time_" + type, time);
        switch (type) {
            case "morning": timeMorning.setValue(time);
                // nếu đang bật -> cập nhật alarm
                if (Boolean.TRUE.equals(alarmMorning.getValue())) {
                    repo.setAlarm(type, time, "Báo thức sáng");
                }
                break;
            case "noon": timeNoon.setValue(time);
                if (Boolean.TRUE.equals(alarmNoon.getValue())) {
                    repo.setAlarm(type, time, "Báo thức trưa");
                }
                break;
            case "evening": timeEvening.setValue(time);
                if (Boolean.TRUE.equals(alarmEvening.getValue())) {
                    repo.setAlarm(type, time, "Báo thức tối");
                }
                break;
        }
    }

    // Người dùng bật/tắt switch
    public void toggleAlarm(String type, boolean enable) {
        repo.saveSwitch("alarm_" + type, enable);

        if (enable) {
            String time = "06:00";
            if ("morning".equals(type)) time = timeMorning.getValue() != null ? timeMorning.getValue() : "06:00";
            if ("noon".equals(type)) time = timeNoon.getValue() != null ? timeNoon.getValue() : "12:00";
            if ("evening".equals(type)) time = timeEvening.getValue() != null ? timeEvening.getValue() : "20:00";
            repo.setAlarm(type, time, titleFor(type));
        } else {
            repo.cancelAlarm(type);
        }

        switch (type) {
            case "morning": alarmMorning.setValue(enable); break;
            case "noon": alarmNoon.setValue(enable); break;
            case "evening": alarmEvening.setValue(enable); break;
        }
    }

    private String titleFor(String type) {
        switch (type) {
            case "morning": return "Báo thức sáng";
            case "noon": return "Báo thức trưa";
            default: return "Báo thức tối";
        }
    }
}
