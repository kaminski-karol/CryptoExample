package com.example.cryptobiometricsexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.cryptobiometricsexample.datastore.userDetailsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val savedText = remember {
                    mutableStateOf("empty")
                }
                LaunchedEffect("savedData") {
                    lifecycleScope.launch {
                        context.userDetailsDataStore.data.map {
                            it.idToken
                        }.onEach {
                            savedText.value = it
                        }.launchIn(this)
                    }
                }

                Text("Saved data:")
                Text(text = savedText.value)

                val dataToSave = remember { mutableStateOf("") }
                TextField(
                    value = dataToSave.value,
                    onValueChange = { dataToSave.value = it},
                    label = { Text(text = "Data to save")}
                )

                Button(onClick = {
                    GlobalScope.launch(Dispatchers.IO) {
                        context.userDetailsDataStore.updateData {
                            Log.i("karkaminski", "updateData: ${dataToSave.value}")
                            it.toBuilder().setIdToken(dataToSave.value).build()
                        }
                    }
                }) {
                    Text(text = "Save data")
                }

                Button(onClick = {}) {
                    Text(text = "Encrypt")
                }

                Button(onClick = {}) {
                    Text(text = "Decrypt")
                }

            }
        }
    }
}