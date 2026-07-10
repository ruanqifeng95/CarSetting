package com.example.carsetting.shared

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class MyMusicService : MediaLibraryService() {

    private var mediaLibrarySession: MediaLibrarySession? = null
    private lateinit var player: Player

    private data class Song(val title: String, val artist: String)
    private val playlist = listOf(
        Song("Nocturne", "Jay Chou"),
        Song("Sunny Day", "Jay Chou"),
        Song("Common Jasmine Orange", "Jay Chou"),
        Song("Simple Love", "Jay Chou"),
        Song("Fragrance of Rice", "Jay Chou")
    )

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()

        val intent = Intent().apply {
            component = ComponentName("com.example.carsetting.musicplayer", "com.example.carsetting.musicplayer.MockPlayerActivity")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // Initialize MediaLibrarySession
        mediaLibrarySession = MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {            override fun onGetLibraryRoot(
                session: MediaLibrarySession,
                browser: MediaSession.ControllerInfo,
                params: LibraryParams?
            ): ListenableFuture<LibraryResult<MediaItem>> {
                val rootItem = MediaItem.Builder()
                    .setMediaId("root")
                    .setMediaMetadata(MediaMetadata.Builder().setIsBrowsable(true).setIsPlayable(false).build())
                    .build()
                return Futures.immediateFuture(LibraryResult.ofItem(rootItem, params))
            }

            override fun onGetChildren(
                session: MediaLibrarySession,
                browser: MediaSession.ControllerInfo,
                parentId: String,
                page: Int,
                pageSize: Int,
                params: LibraryParams?
            ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
                val mediaItems = playlist.mapIndexed { index, song ->
                    MediaItem.Builder()
                        .setMediaId("song_$index")
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle(song.title)
                                .setArtist(song.artist)
                                .setIsBrowsable(false)
                                .setIsPlayable(true)
                                .build()
                        )
                        .build()
                }
                return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), params))
            }
        })
            .setSessionActivity(pendingIntent)
            .build()

        // Set mock data to player
        val mediaItems = playlist.map { song ->
            MediaItem.Builder()
                .setMediaId(song.title)
                .setUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3") // Use a valid HTTPS sample URI
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .build()
                )
                .build()
        }
        player.setMediaItems(mediaItems)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.prepare()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaLibrarySession
    }

    override fun onDestroy() {
        mediaLibrarySession?.run {
            player.release()
            release()
            mediaLibrarySession = null
        }
        super.onDestroy()
    }
}
