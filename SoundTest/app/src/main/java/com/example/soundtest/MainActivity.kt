package com.example.soundtest

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioDeviceInfo
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.soundtest.databinding.ActivityMainBinding
import java.io.IOException
import kotlin.math.log10
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {

    private val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1001
    private lateinit var binding: ActivityMainBinding

    private lateinit var poller: Runnable
    private val handler = Handler()

    private var leftChannel = false
    private var rightChannel = false
    private var prevLeft = 0.0
    private var prevRight = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
        }


        binding.leftBtn.setOnClickListener {

            leftChannel = true
            prevLeft = 0.0
            binding.leftlvlLbl.text = ""
            playAudioFromLeftChannel(R.raw.bleep)

        }

        binding.rightBtn.setOnClickListener {

            rightChannel = true
            prevRight = 0.0
            binding.rightlvlLbl.text = ""
            playAudioFromRightChannel(R.raw.bleep)

        }




        binding.testBtn.setOnClickListener {
           if (!isRecording)
           {
               startRecording()
               poller = Runnable {
                   val decibel = getDecibel()

                   if (leftChannel && decibel>prevLeft)
                   {
                       prevLeft=decibel
                       binding.leftlvlLbl.text = decibel.toString()
                   }

                   if (rightChannel && decibel>prevRight)
                   {
                       prevRight=decibel
                       binding.rightlvlLbl.text = decibel.toString()
                   }

                   binding.dblevelLbl.text = decibel.toString()

                   // Schedule the next update
                   handler.postDelayed(poller, 100)
               }
               handler.post(poller)


           }
           else
           {
               stopRecording()
               handler.removeCallbacks(poller)
               binding.dblevelLbl.text = "NaN"

           }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else
            {
                // The permission is denied

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RECORD_AUDIO_PERMISSION_REQUEST_CODE
                )


            }
        }
    }



    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private var audioRecord: AudioRecord? = null
    private var isRecording = false

    private fun startRecording() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        )
        {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
            return
        }
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        audioRecord?.startRecording()
        isRecording = true
    }

    private fun stopRecording() {
        audioRecord?.apply {
            stop()
            release()
            isRecording = false
        }
        audioRecord = null
    }

    private fun getDecibel(): Double {
        if (isRecording) {
            val buffer = ShortArray(bufferSize)
            audioRecord?.read(buffer, 0, bufferSize)
            var sum = 0.0
            for (i in 0 until bufferSize) {
                sum += buffer[i] * buffer[i]
            }
            if (bufferSize > 0) {
                val amplitude = sqrt(sum / bufferSize.toDouble())
                return 20 * log10(amplitude)
            }
        }
        return 0.0
    }



    private fun playAudioFromLeftChannel(audioResourceId: Int) {

        val mediaPlayer = MediaPlayer()
        // Set the audio channel configuration to left only (mono) by adjusting the volume
        mediaPlayer.setVolume(1.0f, 0.0f)

        // Set the data source to the audio file in res/raw folder
        mediaPlayer.setDataSource(resources.openRawResourceFd(audioResourceId))

        mediaPlayer.setOnCompletionListener {
            // Release the MediaPlayer when audio playback is completed
            mediaPlayer.release()
            leftChannel=false
        }

        // Prepare and start playing the audio
        mediaPlayer.prepare()
        mediaPlayer.start()

    }

    private fun playAudioFromRightChannel(audioResourceId: Int) {

        val mediaPlayer = MediaPlayer()
        // Set the audio channel configuration to left only (mono) by adjusting the volume
        mediaPlayer.setVolume(0.0f, 1.0f)

        // Set the data source to the audio file in res/raw folder
        mediaPlayer.setDataSource(resources.openRawResourceFd(audioResourceId))

        mediaPlayer.setOnCompletionListener {
            // Release the MediaPlayer when audio playback is completed
            mediaPlayer.release()
            rightChannel = false
        }

        // Prepare and start playing the audio
        mediaPlayer.prepare()
        mediaPlayer.start()

    }




}