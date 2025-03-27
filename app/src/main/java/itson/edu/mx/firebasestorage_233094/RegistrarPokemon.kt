package itson.edu.mx.firebasestorage_233094

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class RegistrarPokemon : AppCompatActivity() {
    val REQUEST_IMAGE_GET = 1
    val CLOUD_NAME = "dvksecnll"
    val UPLOAD_PRESET = "pokemon_upload"
    var imageUri: Uri? = null
    var imagePublicUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initCloudinary()

        val name: EditText = findViewById(R.id.etName)
        val number: EditText = findViewById(R.id.etNumber)
        val upload: Button = findViewById(R.id.btnUpload)
        val save: Button = findViewById(R.id.btnSavePokemon)
        val thumbail: ImageView = findViewById(R.id.thumbnail)

        upload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        save.setOnClickListener {
            savePokemon()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val fullPhotoUrl: Uri? = data?.data
            if (fullPhotoUrl != null) {
                changeImage(fullPhotoUrl)
                imageUri = fullPhotoUrl
            }
        }
    }

    fun initCloudinary() {
        val config: MutableMap<String, String> = HashMap<String, String>()
        config["cloud_name"] = CLOUD_NAME
        MediaManager.init(this, config)
    }

    fun changeImage(uri: Uri) {
        val thumbnail: ImageView = findViewById(R.id.thumbnail)
        try {
            thumbnail.setImageURI(uri)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun savePokemon(): String {
        if (imageUri != null) {
            val requesId = MediaManager.get().upload(imageUri)
                .unsigned(UPLOAD_PRESET)
                .callback(object: UploadCallback {
                    override fun onStart(requestId: String?) {

                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                    }

                    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                        imagePublicUrl = resultData?.get("url") as String
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Log.e("onError", error.toString())
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                    }
                })
                .dispatch()

            return requesId
        }

        return ""
    }
}