package com.example.carsetting.manager

import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConnectivityManagerImpl(private val context: Context) : ConnectivityManager {

    private val _musicInfo = MutableStateFlow("打开内置音乐播放器")
    override val musicInfo: StateFlow<String> = _musicInfo.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    init {
        initializeMediaController()
    }

    private fun initializeMediaController() {
        val componentName = ComponentName(
            "com.example.carsetting.musicplayer",
            "com.example.carsetting.shared.MyMusicService"
        )
        try {
            val sessionToken = SessionToken(context, componentName)
            controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
            controllerFuture?.addListener({
                try {
                    val mediaController = controllerFuture?.get()
                    controller = mediaController
                    updateMusicInfoFromMetadata(mediaController?.mediaMetadata)

                    mediaController?.addListener(object : Player.Listener {
                        override fun onMediaMetadataChanged(metadata: MediaMetadata) {
                            updateMusicInfoFromMetadata(metadata)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, MoreExecutors.directExecutor())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateMusicInfoFromMetadata(metadata: MediaMetadata?) {
        val info = if (metadata?.title != null) {
            "${metadata.title} - ${metadata.artist ?: "未知艺术家"}"
        } else {
            "未在播放"
        }
        _musicInfo.value = info
    }

    override fun openBluetoothSettings() {
        val MEDIA_CENTER_PACKAGE = "com.android.car.media"
        val ACTION_MEDIA_TEMPLATE = "android.car.intent.action.MEDIA_TEMPLATE"
        val EXTRA_MEDIA_COMPONENT = "android.car.intent.extra.MEDIA_COMPONENT"
        
        // Standard Bluetooth Audio service component name for Android Automotive
        val bluetoothComponent = "com.android.bluetooth/com.android.bluetooth.avrcpcontroller.BluetoothMediaBrowserService"

        val intent = Intent(ACTION_MEDIA_TEMPLATE).apply {
            `package` = MEDIA_CENTER_PACKAGE
            putExtra(EXTRA_MEDIA_COMPONENT, bluetoothComponent)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to system Bluetooth settings if navigation to specific media source fails
            val fallbackIntent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        }
    }

    override fun openMusicPlayer() {
        val options = ActivityOptions.makeBasic()
        if (Build.VERSION.SDK_INT >= 36) {
            options.pendingIntentBackgroundActivityStartMode = ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOW_ALWAYS
        } else {
            @Suppress("DEPRECATION")
            options.pendingIntentBackgroundActivityStartMode = ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
        }

        try {
            controller?.sessionActivity?.send(context, 0, null, null, null, null, options.toBundle())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun openProjectionSettings() {
        // Use string constant to ensure compatibility as some API levels might not reference this constant directly
        val ACTION_CONNECTED_DEVICE = "android.settings.CONNECTED_DEVICE_SETTINGS"
        val intent = Intent(ACTION_CONNECTED_DEVICE).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to system Bluetooth settings if navigation to "Connected Devices" fails
            val fallbackIntent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        }
    }

    override fun release() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controller = null
    }
}
