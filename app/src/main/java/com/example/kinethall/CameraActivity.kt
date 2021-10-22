package com.example.kinethall

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_camera.*
import java.nio.charset.CoderResult

class CameraActivity : AppCompatActivity() {

    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        supportActionBar?.hide()

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Posts Pictures")

        bChoosePicture.setOnClickListener {
            val intent = Intent(this, AddPost::class.java)
            startActivity(intent)
            finish()
        }

        bTakePicture.setOnClickListener {
            val intent = Intent(this, AddPostCamera::class.java)
            startActivity(intent)

        }
    }

    /*private fun uploadImage() {
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

                        val intent = Intent(this@CameraActivity, Activity2::class.java)
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

    }*/
}