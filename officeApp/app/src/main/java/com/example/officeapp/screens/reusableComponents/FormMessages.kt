package com.example.officeapp.screens.reusableComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.officeapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormMessages(
    errorMessage: String?,
    successMessage: String?,
    modifier: Modifier = Modifier,
    onMessageShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    val message = if (!errorMessage.isNullOrBlank()) {
        errorMessage
    } else {
        successMessage
    }

    LaunchedEffect(message) {
        if (!message.isNullOrBlank()) {
           snackbarHostState.showSnackbar(
               message = message,
               duration = SnackbarDuration.Short
           )
            onMessageShown()
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { snackbarData ->

            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {value ->
                    if (value == SwipeToDismissBoxValue.StartToEnd ||
                        value == SwipeToDismissBoxValue.EndToStart
                        ) {
                        snackbarData.dismiss()
                        true
                    } else {
                        false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {},
                enableDismissFromEndToStart = true,
                enableDismissFromStartToEnd = true
            ) {
                Snackbar(
                    action = null,
                    dismissAction = null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                clipboardManager.setText(
                                    AnnotatedString(snackbarData.visuals.message)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = stringResource(R.string.description_copy_message)
                            )
                        }

                        Text(
                            text = snackbarData.visuals.message,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    )
}