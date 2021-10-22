package com.example.kinethall

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_add_post_camera.*
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class AddPostCamera : AppCompatActivity() {

    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null
    private lateinit var currentPhotoPath: String
    private val REQUEST_CAMERA_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_camera)
        supportActionBar?.hide()

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Posts Pictures")


        bTakePic.setOnClickListener {
            takePicture()
        }

        bAddPostCam.setOnClickListener {
            uploadImage()
        }

    }

    fun takePicture() {
        val root =
            File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID + File.separator)
        root.mkdirs()
        val fname = "img_" + System.currentTimeMillis() + ".jpg"
        val sdImageMainDirectory = File(root, fname)
        imageUri = FileProvider.getUriForFile(this, applicationContext?.packageName + ".provider", sdImageMainDirectory)
        takePicture.launch(imageUri)

    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            // The image was saved into the given Uri -> do something with it
           image_post_cam.setImageURI(imageUri)
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

        }
    }*/


    private fun uploadImage() {
        when{
            imageUri == null -> Toast.makeText(this, "Please select image first!", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(descrption_post.text.toString()) -> {
                Toast.makeText(this, "Please write description!", Toast.LENGTH_LONG).show()
            }
            else->{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding post")
                progressDialog.setMessage("Please wait we are uploading your post...")
                progressDialog.show()

                val fileRef = storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg") //todo: można zmienić rozszerzenie pliku

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                Log.d("bad1", "Jestem1")
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Posts")
                        val postId = ref.push().key
                        Log.d("bad2", "Jestem2")
                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId!!
                        postMap["postimage"] = myUrl
                        postMap["description"] = descrption_post.text.toString()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

                        ref.child(postId).updateChildren(postMap)

                        Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@AddPostCamera, Activity2::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                    else
                    {
                        progressDialog.dismiss()
                    }
                } )

            }
        }
    }
}