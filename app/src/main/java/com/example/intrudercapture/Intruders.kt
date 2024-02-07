package com.example.intrudercapture
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.intrudercapture.databinding.ActivityIntrudersSavedBinding

import java.io.File

class Intruders : AppCompatActivity() {
    private val binding:ActivityIntrudersSavedBinding by lazy {
        ActivityIntrudersSavedBinding.inflate(layoutInflater)
    }
    private lateinit var imageAdapter: ImageAdapter
    val fileList: ArrayList<File> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val fileList = imageReaderNew()
        if (fileList.isEmpty()) {
binding.rcv.visibility = View.GONE
            binding.noIntruder.visibility = View.VISIBLE


        } else {
            binding.noIntruder.visibility = View.GONE
            binding.rcv.visibility = View.VISIBLE
            imageAdapter = ImageAdapter(fileList) {

             //handle click listener here
            }
            binding.rcv.apply {
                layoutManager = GridLayoutManager(this@Intruders, 2)
                adapter = imageAdapter
            }

        }
      /* if(isNetworkConnected()){
           with(binding) {
               loadNativeAd(
                   frame.adContainer,
                   frame.ShimmerContainerSmall,
                   frame.FrameLayoutSmall,
                   R.layout.small_native_ad,
                   getString(R.string.native_showintruder)
               )
           }
       }
        else{
            binding.frame.adContainer.visibility = View.GONE
        }*/




    }
    fun imageReaderNew(): ArrayList<File> {
        fileList.clear()
        val gpath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val spath = "intruder"
        val fullpath = File(gpath + File.separator + spath)
        val listAllFiles = fullpath.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            val sortedFiles = listAllFiles.filter { it.name.endsWith(".png") }
                .sortedWith(compareByDescending { it.lastModified() }) // Sort files by last modified time (descending order)
            fileList.addAll(sortedFiles)
            Log.w("fileList", "" + fileList.size)
        }
        return fileList
    }

    private fun openImageInGallery(imageFile: File) {
        val authority = "${packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(this, authority, imageFile)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }
}