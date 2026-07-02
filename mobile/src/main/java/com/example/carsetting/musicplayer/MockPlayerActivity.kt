package com.example.carsetting.musicplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carsetting.shared.MyMusicService

class MockPlayerActivity : ComponentActivity() {

    private lateinit var mediaBrowser: MediaBrowserCompat
    private var controller by mutableStateOf<MediaControllerCompat?>(null)
    private var metadata by mutableStateOf<MediaMetadataCompat?>(null)
    private var playbackState by mutableStateOf<PlaybackStateCompat?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {
                val mediaController = MediaControllerCompat(this@MockPlayerActivity as Context, mediaBrowser.sessionToken)
                controller = mediaController
                metadata = mediaController.metadata
                playbackState = mediaController.playbackState

                mediaController.registerCallback(object : MediaControllerCompat.Callback() {
                    override fun onMetadataChanged(newMetadata: MediaMetadataCompat?) {
                        metadata = newMetadata
                    }

                    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                        playbackState = state
                    }
                })
            }
        }

        mediaBrowser = MediaBrowserCompat(
            this as Context,
            ComponentName(this as Context, MyMusicService::class.java),
            connectionCallback,
            null
        )

        setContent {
            MaterialTheme {
                PlayerScreen(
                    metadata = metadata,
                    playbackState = playbackState,
                    onPlayPause = {
                        val state = playbackState?.state
                        if (state == PlaybackStateCompat.STATE_PLAYING) {
                            controller?.transportControls?.pause()
                        } else {
                            controller?.transportControls?.play()
                        }
                    },
                    onNext = { controller?.transportControls?.skipToNext() },
                    onPrevious = { controller?.transportControls?.skipToPrevious() }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onStop() {
        super.onStop()
        mediaBrowser.disconnect()
    }
}

@Composable
fun PlayerScreen(
    metadata: MediaMetadataCompat?,
    playbackState: PlaybackStateCompat?,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    val title = metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE) ?: "夜曲"
    val artist = metadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST) ?: "周杰伦"
    val isPlaying = playbackState?.state == PlaybackStateCompat.STATE_PLAYING

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Music Player",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )

            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF282828), Color(0xFF1DB954).copy(alpha = 0.3f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = Color(0xFF1DB954)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = artist,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPrevious) {
                    Icon(
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }

                FilledIconButton(
                    onClick = onPlayPause,
                    modifier = Modifier.size(88.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = Color.Black
                    )
                }

                IconButton(onClick = onNext) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}
