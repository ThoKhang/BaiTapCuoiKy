package com.example.apphoctapchotre.Api;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonProvider {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Gson getGson() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonDeserializer<LocalDateTime>)
                        (json, type, context) -> LocalDateTime.parse(json.getAsString(), formatter))
                .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>)
                        (src, typeOfSrc, context) -> context.serialize(src.format(formatter)))
                .create();
    }
}
