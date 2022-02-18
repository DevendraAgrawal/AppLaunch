package com.applaunch.userForm

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.applaunch.databinding.UserFormFragmentBinding
import com.applaunch.db.entity.UserForm
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

class UserFormFragment : Fragment() {

    private var _binding: UserFormFragmentBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: UserFormViewModel
    lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserFormFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(UserFormViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.profilePic.setOnClickListener {
            if (isPermissionsAllowed(arrayOf(Manifest.permission.CAMERA), true, 300)) {
                getPickImageIntent()
            }
        }

        someActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                handleImageRequest(data)
            }
        }

        binding.saveButton.setOnClickListener {
            val data = UserForm(binding.firstName.text.toString(), binding.lastname.text.toString(), binding.email.text.toString(), imageUri)
            viewModel.insertDb(requireContext(), data)
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            300 -> {
                if (isAllPermissionsGranted(grantResults)) {
                    getPickImageIntent()
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 300
                    )
                }
            }
        }
    }

    fun isAllPermissionsGranted(grantResults: IntArray): Boolean {
        var isGranted = true
        for (grantResult in grantResults) {
            isGranted = grantResult.equals(PackageManager.PERMISSION_GRANTED)
            if (!isGranted)
                break
        }
        return isGranted
    }

    private fun handleImageRequest(data: Intent?) {
        if (data?.data != null) {
            imageUri = data.data!!.toString()
            val imageBitmap = getBitmapFromUri(data.data!!, requireContext())
            binding.profilePic.setImageBitmap(getRoundedShape(imageBitmap))
        }
    }

    fun getRoundedShape(scaleBitmapImage: Bitmap): Bitmap? {
        val targetWidth = 50
        val targetHeight = 50
        val targetBitmap = Bitmap.createBitmap(
            targetWidth,
            targetHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(targetBitmap)
        val path = Path()
        path.addCircle(
            (targetWidth.toFloat() - 1) / 2,
            (targetHeight.toFloat() - 1) / 2,
            Math.min(
                targetWidth.toFloat(),
                targetHeight.toFloat()
            ) / 2,
            Path.Direction.CCW
        )
        canvas.clipPath(path)
        canvas.drawBitmap(
            scaleBitmapImage,
            Rect(
                0, 0, scaleBitmapImage.width,
                scaleBitmapImage.height
            ),
            Rect(
                0, 0, targetWidth,
                targetHeight
            ), null
        )
        return targetBitmap
    }

    @Throws(IOException::class)
    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor =
            context.contentResolver.openFileDescriptor(uri, "r")!!
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    private fun getPickImageIntent() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        setImageUriAndPath()
        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        someActivityResultLauncher.launch(chooserIntent)

//        startActivityForResult(chooserIntent, REQ_CAPTURE)
    }

    private fun setImageUriAndPath() {
        val folder = File("${requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        folder.mkdirs()

        val file = File(folder, "Image_Tmp.JPEG")
        if (file.exists())
            file.delete()
        file.createNewFile()
    }

    fun isPermissionsAllowed(
        permissions: Array<String>,
        shouldRequestIfNotAllowed: Boolean = false,
        requestCode: Int = -1
    ): Boolean {
        var isGranted = true

        for (permission in permissions) {
            isGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
            if (!isGranted)
                break
        }
        if (!isGranted && shouldRequestIfNotAllowed) {
            if (requestCode.equals(-1))
                throw RuntimeException("Send request code in third parameter")
            requestRequiredPermissions(permissions, requestCode)
        }

        return isGranted
    }

    fun requestRequiredPermissions(permissions: Array<String>, requestCode: Int) {
        val pendingPermissions: ArrayList<String> = ArrayList()
        permissions.forEachIndexed { index, permission ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_DENIED
            )
                pendingPermissions.add(permission)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val array = arrayOfNulls<String>(pendingPermissions.size)
            pendingPermissions.toArray(array)
            requestPermissions(array, requestCode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}