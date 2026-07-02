package com.example.carsetting.shared

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import java.util.ArrayList

class MyMusicService : MediaBrowserServiceCompat() {

    private lateinit var session: MediaSessionCompat
    private var currentIndex = 0

    private data class Song(val title: String, val artist: String)
    private val playlist = listOf(
        Song("夜曲", "周杰伦"),
        Song("晴天", "周杰伦"),
        Song("七里香", "周杰伦"),
        Song("简单爱", "周杰伦"),
        Song("稻香", "周杰伦")
    )

    private val callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING)
        }

        override fun onPause() {
            updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
        }

        override fun onStop() {
            updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
        }

        override fun onSkipToNext() {
            currentIndex = (currentIndex + 1) % playlist.size
            updateMetadata(playlist[currentIndex].title, playlist[currentIndex].artist)
        }

        override fun onSkipToPrevious() {
            currentIndex = (currentIndex - 1 + playlist.size) % playlist.size
            updateMetadata(playlist[currentIndex].title, playlist[currentIndex].artist)
        }
    }

    override fun onCreate() {
        super.onCreate()

        session = MediaSessionCompat(this, "MyMusicService")
        session.setCallback(callback)
        
        // 将 Session 的 Token 设置给 MediaBrowserService
        sessionToken = session.sessionToken

        // 初始化模拟数据
        updateMetadata(playlist[currentIndex].title, playlist[currentIndex].artist)
        updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
    }

    private fun updateMetadata(title: String, artist: String) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
            .build()
        session.setMetadata(metadata)
    }

    private fun updatePlaybackState(state: Int) {
        val stateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_STOP
            )
            .setState(state, 0, 1.0f)
        session.setPlaybackState(stateBuilder.build())
    }

    override fun onDestroy() {
        session.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        val mediaItems = ArrayList<MediaItem>()
        playlist.forEachIndexed { index, song ->
            val description = MediaDescriptionCompat.Builder()
                .setMediaId("song_$index")
                .setTitle(song.title)
                .setSubtitle(song.artist)
                .build()
            mediaItems.add(MediaItem(description, MediaItem.FLAG_PLAYABLE))
        }
        result.sendResult(mediaItems)
    }
}
