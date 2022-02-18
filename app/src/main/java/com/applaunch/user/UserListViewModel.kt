package com.applaunch.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applaunch.db.database.UserFormDatabase
import com.applaunch.db.entity.UserForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListViewModel: ViewModel() {

    fun getAllUser(context: Context): LiveData<List<UserForm>>{
        val userDao = UserFormDatabase.getInstance(context).userFormDao()
        val allusers = userDao.getAllRecords()
        return  allusers
    }

    fun deleteUser(context: Context, user: UserForm){
        viewModelScope.launch(Dispatchers.IO) {
            val userDao = UserFormDatabase.getInstance(context).userFormDao()
            userDao.deleteUser(user)
        }
    }

}