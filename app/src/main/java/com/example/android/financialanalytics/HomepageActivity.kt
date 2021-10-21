package com.example.android.financialanalytics

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_homepage.*

class HomepageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        website_button.setOnClickListener{
            val uri: Uri = Uri.parse("https://fintrackk.netlify.app/") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        app_button.setOnClickListener {
            val intent = Intent(this, BudgetTrackerActivity::class.java)
            startActivity(intent)
        }
    }
}