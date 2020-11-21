package com.example.realmakeup

import android.app.ActivityManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.ArCoreApk
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.AugmentedFaceNode
import kotlinx.android.synthetic.main.activity_makeup.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.security.AccessController.getContext
import java.util.*

class MakeupActivity : AppCompatActivity() {
    companion object {
        const val MIN_OPENGL_VERSION = 3.0
    }
    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var currentPhotoPath: String

    lateinit var arFragment: FaceArFragment
    private var faceMeshTexture: Texture? = null
    var faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish()) {
            return
        }

        val textureIntent = intent
        textureIntent.getIntExtra("textureid", R.drawable.makeup)

        var textureId: Int = textureIntent.getIntExtra("textureid", R.drawable.makeup);

        setContentView(R.layout.activity_makeup)



        arFragment = face_fragment as FaceArFragment
        Texture.builder()
                .setSource(this, textureId)
                .build()
                .thenAccept { texture -> faceMeshTexture = texture }

        val sceneView = arFragment.arSceneView
        sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
        val scene = sceneView.scene

        //카메라 촬영 기능
        val camera_btn = findViewById<Button>(R.id.camera)
        camera_btn.setOnClickListener() {
            val bitmap = Bitmap.createBitmap(sceneView.width, sceneView.height, Bitmap.Config.ARGB_8888)

            PixelCopy.request(sceneView, bitmap, { copyResult ->
                if (copyResult === PixelCopy.SUCCESS) {
                    bitmap.saveImage(this)
                } else {
                    // Error
                }
            }, Handler())

            val toast = Toast.makeText(this, "사진 저장이 완료 되었습니다", Toast.LENGTH_SHORT)
            toast.show()
        }
        //뒤로가기 버튼
        val back_btn = findViewById<Button>(R.id.Back)
        back_btn.setOnClickListener(){
            finish()
        }
        //갤러리 버튼 기능
        val gallery_btn = findViewById<Button>(R.id.gallery_btn)
        gallery_btn.setOnClickListener(){
            val intent = Intent(Intent.ACTION_VIEW,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1)
        }
        ////////////////

        scene.addOnUpdateListener {
            faceMeshTexture.let {
                sceneView.session
                        ?.getAllTrackables(AugmentedFace::class.java)?.let {
                            for (f in it) {
                                if (!faceNodeMap.containsKey(f)) {
                                    val faceNode = AugmentedFaceNode(f)
                                    faceNode.setParent(scene)
                                    faceNode.faceMeshTexture = faceMeshTexture
                                    faceNodeMap.put(f, faceNode)
                                }
                            }
                            // Remove any AugmentedFaceNodes associated with an AugmentedFace that stopped tracking.
                            val iter = faceNodeMap.entries.iterator()
                            while (iter.hasNext()) {
                                val entry = iter.next()
                                val face = entry.key
                                if (face.trackingState == TrackingState.STOPPED) {
                                    val faceNode = entry.value
                                    faceNode.setParent(null)
                                    iter.remove()
                                }
                            }
                        }
            }
        }
    }

    //갤러리에 이미지 저장
    fun Bitmap.saveImage(context: Context): Uri? {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ARMakeUp")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

            val uri: Uri? =
                    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(this, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
                return uri
            }
        } else {
            val directory =
                    File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + separator + "RealMakeUp")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName =  "img_${SystemClock.uptimeMillis()}"+ ".jpeg"
            val file = File(directory, fileName)
            saveImageToStream(this, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                return Uri.fromFile(file)
            }
        }
        return null
    }


    fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    /////////////

    fun checkIsSupportedDeviceOrFinish(): Boolean {
        if (ArCoreApk.getInstance().checkAvailability(this) == ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE) {
            Toast.makeText(this, "Augmented Faces requires ARCore", Toast.LENGTH_LONG).show()
            finish()
            return false
        }
        val openGlVersionString = (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
                ?.deviceConfigurationInfo
                ?.glEsVersion

        openGlVersionString?.let { s ->
            if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
                Toast.makeText(this, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                        .show()
                finish()
                return false
            }
        }
        return true
    }
}
