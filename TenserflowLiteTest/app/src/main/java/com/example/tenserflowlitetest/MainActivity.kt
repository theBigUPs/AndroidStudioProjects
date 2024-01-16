package com.example.tenserflowlitetest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.tenserflowlitetest.ml.LiteModelMovenetSingleposeLightningTfliteFloat164

import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.model.Model
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt


lateinit var myBitmap: Bitmap
lateinit var myBitmap2: Bitmap
//lateinit var imageProcessor: ImageProcessor
private var imageUri: Uri? = null
lateinit var imv:ImageView
lateinit var imv2:ImageView
val comp1 = arrayListOf<Float>()
val comp2 = arrayListOf<Float>()
val resComp = arrayListOf<Float>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imv = findViewById<ImageView>(R.id.ivtest)
        imv2= findViewById<ImageView>(R.id.ivtest2)
        val analyze = findViewById<Button>(R.id.analyze)
        val pick = findViewById<Button>(R.id.pickimage)
        val pick2 = findViewById<Button>(R.id.pickImage2)



        pick.setOnClickListener {

            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)

        }

        pick2.setOnClickListener {

            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 101)

        }




        analyze.setOnClickListener {

            comp1.clear()
            comp2.clear()
            resComp.clear()
            val res1 = detectPose(this,imv.drawable.toBitmap(), comp1)
            imv.setImageBitmap(res1)
            println("pose1 : $comp1")

            val res2 = detectPose(this,imv2.drawable.toBitmap(), comp2)
            imv2.setImageBitmap(res2)
            println("pose2 : $comp2")


            findDif(comp1,comp2, resComp)
            println("diff avg : $resComp")


            val averageDiff = resComp.sum()/ resComp.size
            //if
            println("avg: $averageDiff")

            println("pose similarity: ${poseSimilarity(comp1, comp2)}")

            println("compute joint similarity: ${computeJointSimilarity(comp1, comp2)}")

            println("pose similarity cosine: ${poseSimilarityCosine(comp1,comp2)}")

            println("pose similarity dynamic time warping: ${poseSimilarityDTW(comp1,comp2)}")

            println("pose similarity chamfer: ${poseSimilarityChamfer(comp1,comp2)}")

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            imv.setImageURI(imageUri)
        }
        if(resultCode == RESULT_OK && requestCode == 101) {
            imageUri = data?.data
            imv2.setImageURI(imageUri)
        }
    }

    var imageProcessor: ImageProcessor =ImageProcessor.Builder().add(
        ResizeOp(192,192,
            ResizeOp.ResizeMethod.BILINEAR)
    ).build()
    private fun detectPose(context: Context, myBitmap: Bitmap, list:ArrayList<Float>): Bitmap? {

        val model = LiteModelMovenetSingleposeLightningTfliteFloat164.newInstance(context)



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


    private fun findDif(list1:ArrayList<Float>, list2:ArrayList<Float>, listRes:ArrayList<Float>)
    {
        val len = list1.size
        if(list1.size == list2.size)
        {
            for (i in 0 until len)
            {
                listRes.add((list1.elementAt(i)-list2.elementAt(i)).absoluteValue)
            }
        }
        else
        {
            Log.d("main activity","diff size")
        }
    }







    fun computeJointSimilarity(pose1:ArrayList<Float>, pose2:ArrayList<Float>): Double {
        if(pose1.size != pose2.size) {return -1.0 }

        var distance = 0.0
        var count = 0

        var len=(pose1.size)-1
        for (i in 0 until len step 2) {

                val dx = pose1.elementAt(i) - pose2.elementAt(i)
                val dy = pose1.elementAt(i+1) - pose2.elementAt(i+1)
                distance += sqrt(dx.pow(2) + dy.pow(2))
                count++

        }

        return if (count > 0) distance / count else Double.MAX_VALUE
    }






    fun poseSimilarity(joints1: List<Float>, joints2: List<Float>): Float {
        //computes euclidian distance
        if(joints1.size != joints2.size) return -1f

        var distance = 0f
        for (i in 0 until joints1.size step 2) {
            val dx = joints1[i] - joints2[i]
            val dy = joints1[i+1] - joints2[i+1]
            distance += dx * dx + dy * dy
        }
        return 1 / (1 + sqrt(distance))
    }


    fun poseSimilarityCosine(joints1: List<Float>, joints2: List<Float>): Float {
        if(joints1.size != joints2.size) return -1f
        var dotProduct = 0f
        var mag1 = 0f
        var mag2 = 0f
        for (i in 0 until joints1.size step 2) {
            dotProduct += joints1[i] * joints2[i] + joints1[i+1] * joints2[i+1]
            mag1 += joints1[i] * joints1[i] + joints1[i+1] * joints1[i+1]
            mag2 += joints2[i] * joints2[i] + joints2[i+1] * joints2[i+1]
        }
        return dotProduct / (sqrt(mag1) * sqrt(mag2))
    }



    fun poseSimilarityDTW(joints1: List<Float>, joints2: List<Float>): Float {
        if(joints1.size != joints2.size) return -1f
        val n = joints1.size / 2
        val m = joints2.size / 2
        val dist = Array(n+1) { FloatArray(m+1) }
        for (i in 1..n) {
            for (j in 1..m) {
                val dx = joints1[(i-1)*2] - joints2[(j-1)*2]
                val dy = joints1[(i-1)*2+1] - joints2[(j-1)*2+1]
                dist[i][j] = sqrt(dx * dx + dy * dy)
            }
        }
        val dtw = Array(n+1) { FloatArray(m+1) }
        for (i in 0..n) {
            for (j in 0..m) {
                dtw[i][j] = Float.POSITIVE_INFINITY
            }
        }
        dtw[0][0] = 0f
        for (i in 1..n) {
            for (j in 1..m) {
                val cost = dist[i][j]
                dtw[i][j] = cost + minOf(dtw[i-1][j], dtw[i][j-1], dtw[i-1][j-1])
            }
        }
        return 1 / (1 + dtw[n][m])
    }




    fun poseSimilarityChamfer(joints1: List<Float>, joints2: List<Float>): Float {
        if(joints1.size != joints2.size) return -1f
        var distance = 0f
        for (i in 0 until joints1.size step 2) {
            val dx = joints1[i] - joints2[i]
            val dy = joints1[i+1] - joints2[i+1]
            val d = sqrt(dx*dx + dy*dy)
            val minDist1 = (0 until joints2.size step 2).map { j ->
                val dx = joints1[i] - joints2[j]
                val dy = joints1[i+1] - joints2[j+1]
                sqrt(dx*dx + dy*dy)
            }.min() ?: 0f
            val minDist2 = (0 until joints1.size step 2).map { j ->
                val dx = joints1[j] - joints2[i]
                val dy = joints1[j+1] - joints2[i+1]
                sqrt(dx*dx + dy*dy)
            }.min() ?: 0f
            distance += minDist1 + minDist2
        }
        return 1 / (1 + distance)
    }


}