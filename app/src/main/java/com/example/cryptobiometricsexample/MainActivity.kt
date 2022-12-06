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
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
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

                val dataToSave1 = remember { mutableStateOf("") }
                TextField(
                    value = dataToSave1.value,
                    onValueChange = { dataToSave1.value = it },
                    label = { Text(text = "Data to save") }
                )

                Button(onClick = {
                    GlobalScope.launch(Dispatchers.IO) {
                        context.userDetailsDataStore.updateData {
                            it.toBuilder().setIdToken(dataToSave1.value).build()
                        }
                    }
                }) {
                    Text(text = "encrypt and save data")
                }

                Text("Saved data (after decryption):")
                Text(text = savedText.value)

            }
        }
    }
}