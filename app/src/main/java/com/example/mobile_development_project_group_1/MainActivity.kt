package com.example.mobile_development_project_group_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.example.mobile_development_project_group_1.ui.theme.MobileDevelopmentProjectGroup1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileDevelopmentProjectGroup1Theme {
                // A surface container using the 'background' color from the theme
                Surface {
                    MainScaffoldView()
                }
            }
        }
    }
}