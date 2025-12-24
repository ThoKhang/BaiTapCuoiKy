package com.example.apphoctapchotre.UI.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.apphoctapchotre.DATA.model.Media;

import java.util.ArrayList;

public class MusicService extends Service {

    private ExoPlayer player;

    // playlist chỉ set 1 lần
    private boolean hasPlaylist = false;
    private final ArrayList<Media> playlist = new ArrayList<>();

    private final IBinder binder = new LocalBinder();

    // ================= BINDER =================

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // ================= LIFECYCLE =================

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this).build();

        // phát vòng lặp playlist
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hasPlaylist = false;
        playlist.clear();
        player.release();
    }

    // ================= PLAYLIST =================

    /**
     * Set playlist DUY NHẤT 1 LẦN – KHÔNG AUTO PLAY
     */
    public void setPlaylistOnce(ArrayList<Media> list, int startIndex) {
        if (hasPlaylist || list == null || list.isEmpty()) return;

        playlist.clear();
        playlist.addAll(list);

        player.clearMediaItems();

        for (Media m : playlist) {
            String url = "http://10.0.2.2:8080/" + m.getDuongDanFile();
            player.addMediaItem(MediaItem.fromUri(url));
        }

        player.prepare();
        player.seekTo(startIndex, 0);

        hasPlaylist = true;
    }

    // ================= CORE CONTROLS =================

    /**
     * 🔥 DÙNG KHI CLICK TRONG LIST
     * - Cùng bài → toggle play/pause
     * - Khác bài → PLAY NGAY
     */
    public void playOrToggleAt(int index) {
        int current = getCurrentIndex();

        if (index == current) {
            toggle();
        } else {
            playAt(index);
        }
    }

    /**
     * 🔥 DÙNG KHI MỞ DETAIL / CHỌN BÀI MỚI
     * → LUÔN PLAY
     */
    public void playAt(int index) {
        if (index < 0 || index >= player.getMediaItemCount()) return;
        player.seekTo(index, 0);
        player.play();
    }

    /**
     * Toggle play / pause
     */
    public void toggle() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.play();
        }
    }

    public void play() {
        player.play();
    }

    public void pause() {
        player.pause();
    }

    public void next() {
        if (player.hasNextMediaItem()) {
            player.seekToNext();
            player.play();
        }
    }

    public void previous() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPrevious();
            player.play();
        }
    }

    // ================= GETTERS =================

    public ExoPlayer getPlayer() {
        return player;
    }

    public boolean hasPlaylist() {
        return hasPlaylist;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public int getCurrentIndex() {
        return player.getCurrentMediaItemIndex();
    }

    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public Media getCurrentMedia() {
        int index = getCurrentIndex();
        if (index >= 0 && index < playlist.size()) {
            return playlist.get(index);
        }
        return null;
    }
}
