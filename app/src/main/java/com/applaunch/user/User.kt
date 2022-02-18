package com.applaunch.user

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.applaunch.BR
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var firstName: String, var lastName: String, var email: String, var image: String) :
    BaseObservable(), Parcelable {

    var fName: String
        @Bindable get() = firstName
        set(value) {
            firstName = value
            notifyPropertyChanged(BR.fName)
        }

    var lName: String
        @Bindable get() = lastName
        set(value) {
            lastName = value
            notifyPropertyChanged(BR.lName)
        }

    var mail: String
        @Bindable get() = email
        set(value) {
            email = value
            notifyPropertyChanged(BR.mail)
        }
}