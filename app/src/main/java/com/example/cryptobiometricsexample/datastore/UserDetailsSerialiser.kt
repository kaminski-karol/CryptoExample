package com.example.cryptobiometricsexample.datastore

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import java.io.InputStream
import java.io.OutputStream

val Context.userDetailsDataStore by dataStore("user_details.pb", UserDetailsSerializer())

class UserDetailsSerializer : Serializer<UserProto> {

    override val defaultValue: UserProto = UserProto.getDefaultInstance()


    override suspend fun readFrom(input: InputStream): UserProto {
        return try {
            UserProto.parseFrom(input)
        } catch (exception: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserProto, output: OutputStream) = t.writeTo(output)
}
