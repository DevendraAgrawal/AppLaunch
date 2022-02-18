package com.applaunch.db.daw

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.applaunch.db.entity.UserForm

@Dao
interface UserFormDao {

    @Insert
    fun insert(user: UserForm)

    @Query("select * from user_form")
    fun getAllRecords(): LiveData<List<UserForm>>

    @Delete
    fun deleteUser(user: UserForm)

    @Query("select * from user_form where email = :emailId")
    fun checkEmailExists(emailId: String): LiveData<UserForm>
}