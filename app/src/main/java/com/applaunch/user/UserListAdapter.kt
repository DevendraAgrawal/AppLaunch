package com.applaunch.user

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.applaunch.R
import java.io.FileDescriptor
import java.io.IOException

class UserListAdapter(val userList: ArrayList<User>, val callback: CallBack): RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    lateinit var context: Context
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val fullName = view.findViewById<TextView>(R.id.fullName)
        val emailId = view.findViewById<TextView>(R.id.emailId)
        val mainLayout = view.findViewById<ConstraintLayout>(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_list_adapter, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.fullName.text = "${user.firstName} ${user.lastName}"
        holder.profileImage.setImageBitmap(getRoundedShape(getBitmapFromUri(Uri.parse(user.image), context)))
        holder.emailId.text = user.email
        holder.mainLayout.setOnClickListener {
            callback.openWeather()
        }
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

    override fun getItemCount(): Int {
        return userList.size
    }

    interface CallBack{
        fun openWeather()
    }
}