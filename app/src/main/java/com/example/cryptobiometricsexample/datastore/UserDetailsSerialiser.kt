package com.example.cryptobiometricsexample.datastore

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

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
