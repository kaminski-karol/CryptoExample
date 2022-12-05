package com.example.cryptobiometricsexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.cryptobiometricsexample.crypto.CryptoManager
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var path = this.filesDir
        var file = File(path, "token.txt")
        var cryptoManager = CryptoManager()

        setContent {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val savedText = remember {
                    mutableStateOf("empty")
                }

                val dataToSave1 = remember { mutableStateOf("") }
                TextField(
                    value = dataToSave1.value,
                    onValueChange = { dataToSave1.value = it },
                    label = { Text(text = "Data to save") }
                )

                Button(onClick = {
                    var stream = FileOutputStream(file)
                    try {
                        cryptoManager.encrypt(
                            dataToSave1.value.toByteArray(),
                            stream
                        )
                    } finally {
                        stream.close()
                    }
                }) {
                    Text(text = "encrypt and save data")
                }

                Button(onClick = {
                    val length = file.length().toInt()

                    val fis = FileInputStream(file)
                    try {

                        var decrypted = cryptoManager.decrypt(fis)
                        val contents = String(decrypted)
                        savedText.value = contents

                    } finally {
                        fis.close()
                    }
                }) {
                    Text(text = "decrypt")
                }

                Text("Saved data (after decryption):")
                Text(text = savedText.value)

            }
        }
    }
}