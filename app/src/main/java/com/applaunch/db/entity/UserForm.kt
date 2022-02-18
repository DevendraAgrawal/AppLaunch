package com.applaunch.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_form")
data class UserForm(
    var firstName: String,
    var lastName: String,
    @PrimaryKey var email: String,
    var image: String)