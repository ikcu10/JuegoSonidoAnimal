package com.iker.sonidoanimal

import android.content.Context
import android.media.MediaPlayer

object SoundManager {

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, soundRes: Int) {
        stop() // detener sonido anterior si estaba sonando

        mediaPlayer = MediaPlayer.create(context, soundRes)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            stop()
        }
    }

    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }
}
