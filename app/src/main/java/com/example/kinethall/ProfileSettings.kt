package com.example.kinethall

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.kinethall.Model.User
import com.example.kinethall.ProfileSettings
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_change_username.*
import kotlinx.android.synthetic.main.activity_image_change.*
import kotlinx.android.synthetic.main.activity_profile_settings.*
import kotlinx.android.synthetic.main.activity_selected_user.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileSettings : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var profileId: String
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")

        supportActionBar?.hide()

        val buttonChangingUsername = findViewById<Button>(R.id.bChangeUsername)
        buttonChangingUsername.setOnClickListener {
            val intent = Intent(this, ChangeUsername::class.java)
            startActivity(intent)
        }

        //val buttonChangingImage = findViewById<Button>(R.id.bChangeAvatar)
        //buttonChangingImage.setOnClickListener {
        //    val intent = Intent(Intent.ACTION_PICK)
        //    intent.type = "image/*"
        //    startActivityForResult(intent, 0)
        //}

        bChangeAvatar.setOnClickListener {
            val intent = Intent(this@ProfileSettings, ImageChange::class.java)
            startActivity(intent)
        }



    }

    private fun updateImage()
    {
        when
        {
            imageUri == null -> Toast.makeText(this, "Please select image first!", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Profile Settings")
                progressDialog.setMessage("Please waitm we are updating your profile image...")
                progressDialog.show()

                val fileRef = storageProfilePicRef!!.child(firebaseUser!!.uid + ".png")

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

                        val ref = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Users")

                        val userMap = HashMap<String, Any>()
                        userMap["image"] = myUrl

                        ref.child(firebaseUser.uid).updateChildren(userMap)

                        Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@ProfileSettings, Activity2::class.java)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_image.setImageURI(imageUri)
        }
    }

    private fun userInfo()
    {
        val usersRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(firebaseUser.uid)

        usersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.ic_dogprofile).into(profile_image)
                    etChangeUsername1.setText(user!!.getUsername())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}