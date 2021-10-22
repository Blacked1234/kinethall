package com.example.kinethall

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPost : AppCompatActivity() {

    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        supportActionBar?.hide()

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Posts Pictures")

        bAddPost.setOnClickListener {
            uploadImage()
        }


        //val intentcapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //startActivity(intentcapture)

        CropImage.activity()
            .setAspectRatio(1,1)
            .start(this@AddPost)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image_post.setImageURI(imageUri)
        }
    }

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

                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId!!
                        postMap["postimage"] = myUrl
                        postMap["description"] = descrption_post.text.toString()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

                        ref.child(postId).updateChildren(postMap)

                        Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@AddPost, Activity2::class.java)
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