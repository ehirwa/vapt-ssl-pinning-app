package com.example.sslpinningtester

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sslpinningtester.ui.theme.SSLpinningTesterTheme
import okhttp3.*
import java.io.IOException

class MainActivity : ComponentActivity() {
    private lateinit var urlEditText: EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        urlEditText = findViewById(R.id.urlEditText)
        resultTextView = findViewById(R.id.resultTextView)
        val testButton: Button = findViewById(R.id.testButton)

        testButton.setOnClickListener {
            performSSLTest()
        }

//        setContent {
//            SSLpinningTesterTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
//                }
//            }
//        }
    }

    private fun performSSLTest() {
        val url = urlEditText.text.toString()

        // Certificate pinning setup
        val hostname = "yourdomain.com" // replace with your hostname
        val certificatePinningSha256 = "sha256/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX=" // replace with your SHA-256 fingerprint

        val client = OkHttpClient.Builder()
            .certificatePinner(
                CertificatePinner.Builder()
                    .add(hostname, certificatePinningSha256)
                    .build()
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error
                runOnUiThread {
                    resultTextView.text = "Failed: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle the response
                runOnUiThread {
                    if (response.isSuccessful) {
                        resultTextView.text = "Success: $response"
                    } else {
                        resultTextView.text = "Failed: $response"
                    }
                }
            }
        })
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//            text = "Hello $name!",
//            modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SSLpinningTesterTheme {
//        Greeting("Android")
//    }
//}