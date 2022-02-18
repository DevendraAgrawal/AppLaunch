package com.applaunch.userForm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applaunch.db.database.UserFormDatabase
import com.applaunch.db.entity.UserForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserFormViewModel(): ViewModel() {

    fun insertDb(context: Context, data: UserForm){
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                val dao = UserFormDatabase.getInstance(context).userFormDao()
                data?.let { dao.insert(
                    it ) }
            }
        }
    }
}