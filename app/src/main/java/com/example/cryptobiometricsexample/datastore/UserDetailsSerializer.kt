package com.example.cryptobiometricsexample.datastore

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.cryptobiometricsexample.crypto.CryptoManager
import java.io.InputStream
import java.io.OutputStream

val Context.userDetailsDataStore by dataStore("user_details.pb", UserDetailsSerializer(CryptoManager()))

class UserDetailsSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<UserProto> {

    override val defaultValue: UserProto = UserProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProto {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            UserProto.parseFrom(decryptedBytes)
        } catch (exception: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserProto, output: OutputStream) {
        cryptoManager.encrypt(
            t.toByteArray(),
            output
            )
    }
}