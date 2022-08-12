package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var runnable: Runnable

    private lateinit var handler: Handler

    private lateinit var tvPlayed : TextView

    private  lateinit var tvDue : TextView

    //lateinitvar cannot be used in nullable references
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("MY_TAG","onCreate")

        seekBar = findViewById(R.id.sbClapping)
        tvPlayed  = findViewById<TextView>(R.id.tvPlayed)
        tvDue = findViewById<TextView>(R.id.tvDue)
        handler = Handler(Looper.getMainLooper())

        //mediaPlayer = MediaPlayer.create(this,R.raw.clap)


        val playButton = findViewById<FloatingActionButton>(R.id.fabPlay)
        val pauseButton = findViewById<FloatingActionButton>(R.id.fabPause)
        val stopButton = findViewById<FloatingActionButton>(R.id.fabStop)

        playButton.setOnClickListener {
            if (mediaPlayer==null) {
                mediaPlayer = MediaPlayer.create(this,R.raw.clap)
                initializeSeekBar()
                //Log.i("MY_TAG","START")

            }
            mediaPlayer?.start()
        }

        pauseButton.setOnClickListener {
            mediaPlayer?.pause()
        }

        stopButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
            tvPlayed.text = ""
            tvDue.text = ""

        }

    }

    private fun initializeSeekBar(){

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer?.seekTo(progress)
                    Log.i("MY_TAG","Progress changed")

                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })



        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            val playedTime = mediaPlayer!!.currentPosition/1000
            Log.i("MY_TAG","$playedTime")
            tvPlayed.text = "$playedTime sec"
            val duration = mediaPlayer!!.duration/1000
            val dueTime = duration - playedTime
            tvDue.text = "$dueTime sec"

            handler.postDelayed(runnable,1000)

        }
        handler.postDelayed(runnable,1000)

    }
}