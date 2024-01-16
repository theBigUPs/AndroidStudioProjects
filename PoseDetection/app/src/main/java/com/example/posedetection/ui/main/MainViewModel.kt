package com.example.posedetection.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.view.TextureView
import androidx.lifecycle.ViewModel
import com.example.posedetection.data.Camera
import com.example.posedetection.data.PoseDetect

class MainViewModel : ViewModel() {
    private val poseInstance = PoseDetect()
    private val cameraInstance = Camera()

    fun detectPose(context: Context, myBitmap: Bitmap, list:ArrayList<Float>): Bitmap?
    {
        return poseInstance.detectPose(context, myBitmap, list)
    }

    fun openCamera(context:Context, textureView: TextureView)
    {
        cameraInstance.createCamera(context,textureView)
    }


    fun poseSimilarity(joints1: List<Float>, joints2: List<Float>): Float {

        return poseInstance.poseSimilarity(joints1,joints2)
    }


}