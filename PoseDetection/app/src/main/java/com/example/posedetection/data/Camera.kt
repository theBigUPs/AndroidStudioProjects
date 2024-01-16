package com.example.posedetection.data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import org.checkerframework.checker.units.qual.degrees
import org.tensorflow.lite.gpu.CompatibilityList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Camera {


    private lateinit var cameraManager:CameraManager
    private lateinit var handler:Handler
    private lateinit var handlerThread: HandlerThread

    private val compatList = CompatibilityList()



    @SuppressLint("MissingPermission")//permission is already checked in main fragment its fine
    fun createCamera(context:Context, textureView: TextureView)
    {

        handlerThread= HandlerThread("cameraThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.openCamera(cameraManager.cameraIdList[1],object: CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                var captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                var surface=Surface(textureView.surfaceTexture)
                captureRequest.addTarget(surface)

                camera.createCaptureSession(listOf(surface),object :CameraCaptureSession.StateCallback(){
                    override fun onConfigured(session: CameraCaptureSession) {
                        session.setRepeatingRequest(captureRequest.build(),null,null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }
                },handler)

            }

            override fun onDisconnected(camera: CameraDevice) {

            }

            override fun onError(camera: CameraDevice, error: Int) {

            }
        },handler)
    }







    private lateinit var cameraExecutor: ExecutorService

    fun startCamera(context:Context ,imageView: ImageView )
    {
        var resList = arrayListOf<Float>()
        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        val imageAnalysis = ImageAnalysis.Builder()
            // enable the following line if RGBA output is needed.
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setTargetResolution(Size(1920, 1080))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()


        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                     //it.setSurfaceProvider(imageView.surfaceProvider)
                }


            imageAnalysis.setAnalyzer(Executors.newCachedThreadPool(), ImageAnalysis.Analyzer { imageProxy ->

                val bitmap = imageProxy.toBitmap()

                var res = PoseDetect().detectPose(context,bitmap,resList)
                // after done, release the ImageProxy object

                Handler(Looper.getMainLooper()).post(Runnable {
                    val matrix = Matrix()

                    matrix.preRotate(-90f)
                    val rotatedBitmap = res?.let {
                        Bitmap.createBitmap(
                            it,
                            0,
                            0,
                            res.getWidth(),
                            res.getHeight(),
                            matrix,
                            true
                        )
                    }
                    //original.recycle()

                    imageView.setImageBitmap(rotatedBitmap)
                })

                imageProxy.close()
            })


            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    (context as AppCompatActivity), cameraSelector, imageAnalysis, preview)

            } catch(exc: Exception) {
                Log.e("camera", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))

    }

}