package com.example.posedetection.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.posedetection.ml.LiteModelMovenetSingleposeLightningTfliteFloat164
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.model.Model
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.sqrt


class PoseDetect {

    private val compatList = CompatibilityList()

    private val isSupported = compatList.isDelegateSupportedOnThisDevice

    // If device does not support gpu, then it will be done on 4 CPU threads

    private val options: Model.Options = Model.Options.Builder().apply {

        if(isSupported) setDevice(Model.Device.GPU)

        else setNumThreads(4)

    }.build()



    private var imageProcessor: ImageProcessor =ImageProcessor.Builder().add(
    ResizeOp(192,192,
    ResizeOp.ResizeMethod.BILINEAR)
    ).build()

    fun detectPose(context: Context, myBitmap: Bitmap, list:ArrayList<Float>): Bitmap? {

        val model = LiteModelMovenetSingleposeLightningTfliteFloat164.newInstance(context,options)



        var tensorImage = TensorImage(DataType.UINT8)
        tensorImage.load(myBitmap)
        tensorImage = imageProcessor.process(tensorImage)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 192, 192, 3), DataType.UINT8)
        inputFeature0.loadBuffer(tensorImage.buffer)


        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray



        var paint= Paint()
        paint.color = Color.YELLOW

        var mutable = myBitmap.copy(Bitmap.Config.ARGB_8888,true)
        var canvas= Canvas(mutable)
        var height = myBitmap.height
        var width = myBitmap.width
        var x=0

        //list.addAll(outputFeature0.asList())
        while (x<=49)
        {
            if (outputFeature0[x+2] > 0.15)
            {
                canvas.drawCircle(outputFeature0[x+1]*width,outputFeature0[x]*height,10f,paint)
                //canvas.drawLine(outputFeature0[x+1]*width,outputFeature0[x]*height,outputFeature0[x+4]*width,outputFeature0[x+3]*height,paint)
                list.add(outputFeature0[x+1])
                list.add(outputFeature0[x])
            }
            x+=3
        }
        return mutable

    }


    fun poseSimilarity(joints1: List<Float>, joints2: List<Float>): Float {
        //computes euclidian distance
        if(joints1.size != joints2.size) {return -1f }

        var distance = 0f
        for (i in joints1.indices step 2) {
            val dx = joints1[i] - joints2[i]
            val dy = joints1[i+1] - joints2[i+1]
            distance += dx * dx + dy * dy
        }
        return 1 / (1 + sqrt(distance))
    }



}