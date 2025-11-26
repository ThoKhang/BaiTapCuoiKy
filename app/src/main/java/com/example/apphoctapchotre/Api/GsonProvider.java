package com.example.apphoctapchotre.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonProvider {

    // Định nghĩa formatter một lần
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Tạo sẵn một instance Gson dùng chung
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (com.google.gson.JsonDeserializer<LocalDateTime>) (json, type, context) ->
                            LocalDateTime.parse(json.getAsString(), formatter))
            .registerTypeAdapter(LocalDateTime.class,
                    (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            context.serialize(src.format(formatter)))
            .create();

    // KHÔNG dùng @RequiresApi nữa
    public static Gson getGson() {
        return gson;
    }
}
