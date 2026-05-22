package com.example.officeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.officeapp.appRoutes.AppNavHost
import com.example.officeapp.ui.theme.OfficeAppTheme
import com.example.officeapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by sessionManager.isDarkTheme.collectAsState(
                initial = true
            )

            OfficeAppRoot(
                isDarkTheme = isDarkTheme
            ) {
                AppNavHost(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { newThemeValue ->
                        lifecycleScope.launch {
                            sessionManager.saveDarkTheme(newThemeValue)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun OfficeAppRoot(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    OfficeAppTheme(
        darkTheme = isDarkTheme
    ){
        Surface{
            content()
        }
    }
}