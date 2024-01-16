package com.example.posedetection.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posedetection.data.Camera
import com.example.posedetection.databinding.FragmentMainBinding
import com.google.android.material.color.utilities.Score.score
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var resList = arrayListOf<Float>()
    private lateinit var picList : ArrayList<Float>
    lateinit var res: Bitmap
    lateinit var picRes:Bitmap
    var showToast = true

    lateinit var pickedPic: Uri
    // gets the photo from the files when you click select photo in the ui
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            picList.clear()
            pickedPic=uri
            binding.refImage.setImageURI(pickedPic)
            picRes = viewModel.detectPose((activity as AppCompatActivity?)!!, binding.refImage.drawable.toBitmap(), picList)!!
            binding.refImage.setImageBitmap(picRes)
            showToast=true
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        picList = arrayListOf(0f, 0f, 0f, 0f, 0f, 0.34623873f, 0.71022284f, 0.42492044f, 0.2484994f, 0.4253208f, 0.82463264f, 0.6510935f, 0.18993132f, 0.6482881f)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (allPermissionsGranted()) {
            //startCamera()
        } else {
            ActivityCompat.requestPermissions(
                (activity as AppCompatActivity?)!!, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        binding.pickImg.setOnClickListener {
            getContent.launch("image/*")
        }


        binding.textureView.surfaceTextureListener= object:TextureView.SurfaceTextureListener
        {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                viewModel.openCamera((activity as AppCompatActivity?)!!,binding.textureView)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {


                if (binding.textureView.bitmap != null) {
                    res = viewModel.detectPose((activity as AppCompatActivity?)!!, binding.textureView.bitmap!!,resList)!!
                    binding.res.setImageBitmap(res)
                    if(viewModel.poseSimilarity(picList, resList)>0.80) {
                        println("similar")
                        if (showToast) {
                            Toast.makeText((activity as AppCompatActivity?)!!, "similar", Toast.LENGTH_SHORT).show()
                            showToast=false
                        }
                    }
                }
                resList.clear()
            }

        }

        //Camera().startCamera((activity as AppCompatActivity?)!!,binding.res)
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            (activity as AppCompatActivity?)!!, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                android.Manifest.permission.CAMERA,
                //Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    //add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                //startCamera()
            } else {
                Toast.makeText((activity as AppCompatActivity?)!!,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                //finish()
                ActivityCompat.requestPermissions(
                    (activity as AppCompatActivity?)!!, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}