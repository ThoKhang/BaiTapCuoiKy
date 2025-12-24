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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MusicService extends Service {

    private ExoPlayer player;

    // playlist giữ lâu trong service
    private final ArrayList<Media> playlist = new ArrayList<>();
    private boolean hasPlaylist = false;

    private final IBinder binder = new LocalBinder();

    // ================= CALLBACK LISTENERS =================

    public interface PlaybackListener {
        void onPlaybackChanged(int index, boolean isPlaying);
        void onTrackChanged(int index);
    }

    private final List<PlaybackListener> listeners = new CopyOnWriteArrayList<>();

    public void addPlaybackListener(PlaybackListener l) {
        if (l != null && !listeners.contains(l)) listeners.add(l);
    }

    public void removePlaybackListener(PlaybackListener l) {
        listeners.remove(l);
    }

    private void notifyPlaybackChanged() {
        int idx = getCurrentIndex();
        boolean playing = isPlaying();
        for (PlaybackListener l : listeners) {
            l.onPlaybackChanged(idx, playing);
        }
    }

    private void notifyTrackChanged() {
        int idx = getCurrentIndex();
        for (PlaybackListener l : listeners) {
            l.onTrackChanged(idx);
        }
    }

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
        player.setRepeatMode(Player.REPEAT_MODE_ALL);

        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                notifyPlaybackChanged();
            }

            @Override
            public void onMediaItemTransition(MediaItem mediaItem, int reason) {
                notifyTrackChanged();
                notifyPlaybackChanged();
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                // READY/BUFFERING cũng cập nhật để UI đồng bộ
                notifyPlaybackChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hasPlaylist = false;
        playlist.clear();
        if (player != null) player.release();
    }

    // ================= PLAYLIST HELPERS =================

    private boolean isSamePlaylist(ArrayList<Media> list) {
        if (list == null) return false;
        if (playlist.size() != list.size()) return false;

        for (int i = 0; i < list.size(); i++) {
            Media a = playlist.get(i);
            Media b = list.get(i);

            // ưu tiên so theo maMedia nếu có
            Long ida = a.getMaMedia();
            Long idb = b.getMaMedia();
            if (ida != null && idb != null) {
                if (!ida.equals(idb)) return false;
            } else {
                // fallback theo đường dẫn
                String pa = a.getDuongDanFile();
                String pb = b.getDuongDanFile();
                if (pa == null || pb == null) return false;
                if (!pa.equals(pb)) return false;
            }
        }
        return true;
    }

    /**
     * Set playlist nếu cần (khác playlist hiện tại).
     * Không seek/reset nếu playlist trùng.
     */
    public void ensurePlaylist(ArrayList<Media> list) {
        if (list == null || list.isEmpty()) return;

        if (hasPlaylist && isSamePlaylist(list)) {
            return; // ✅ trùng playlist → không reset
        }

        playlist.clear();
        playlist.addAll(list);

        player.clearMediaItems();

        for (Media m : playlist) {
            String url = "http://10.0.2.2:8080/" + m.getDuongDanFile();
            player.addMediaItem(MediaItem.fromUri(url));
        }

        player.prepare();
        hasPlaylist = true;
    }

    /**
     * Dùng khi click từ LIST:
     * - đảm bảo playlist đúng
     * - nếu click đúng bài đang phát → toggle play/pause (không reset)
     * - nếu click bài khác → play bài mới
     */
    public void onUserClickFromList(ArrayList<Media> list, int index) {
        ensurePlaylist(list);

        int current = getCurrentIndex();
        if (index == current) {
            toggle();
        } else {
            playAt(index);
        }
    }

    // ================= CORE CONTROLS =================

    public void playAt(int index) {
        if (index < 0 || index >= player.getMediaItemCount()) return;
        player.seekTo(index, 0);
        player.play();
    }

    public void toggle() {
        if (player.isPlaying()) player.pause();
        else player.play();
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
        return player != null && player.isPlaying();
    }

    public int getCurrentIndex() {
        if (player == null) return -1;
        return player.getCurrentMediaItemIndex();
    }

    public long getCurrentPosition() {
        if (player == null) return 0;
        return player.getCurrentPosition();
    }

    public Media getCurrentMedia() {
        int index = getCurrentIndex();
        if (index >= 0 && index < playlist.size()) return playlist.get(index);
        return null;
    }
}
