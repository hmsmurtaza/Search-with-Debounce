package com.android.example.searchwithdebounce.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.example.searchwithdebounce.ui.viewmodel.QuoteViewModel

@Composable
fun QuoteScreen(viewModel: QuoteViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var text by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = { it ->
                text = it
                viewModel.onQueryChanged(text)
            },
            label = { Text("Search quotes") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            text = ""
                            viewModel.onQueryChanged("")
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        uiState.error?.let { errorMessage ->
            Text(text = "Error $errorMessage", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(12.dp))
        }

        LazyColumn {
            /*items(uiState.result) {

            }*/
            items(uiState.result) { quote ->
                Text(text = ". $quote",
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Divider()
            }
        }
        /*OutlinedTextField(
            value = text,
            onValueChange = { it ->
                text = it
                viewModel.onQueryChanged(text)
            },
            label = "Search quotes",
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            text = ""
                            viewModel.onQueryChanged("")    // clear query
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }
        )*/
    }
}