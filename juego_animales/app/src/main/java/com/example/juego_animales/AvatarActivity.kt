package com.example.juego_animales

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AvatarActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_avatar)

        val avatarOso = findViewById<ImageButton>(R.id.btnOso)
        val avatarRata = findViewById<ImageButton>(R.id.btnRata)
        val avatarPingu = findViewById<ImageButton>(R.id.btnPinguino)
        val avatarZorro = findViewById<ImageButton>(R.id.btnZorro)
        val avatarConejo = findViewById<ImageButton>(R.id.btnConejo)
        val avatarJirafa = findViewById<ImageButton>(R.id.btnJirafa)

        avatarOso.setOnClickListener{
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("avatar", "oso")
            startActivity(intent)
        }

        avatarRata.setOnClickListener{
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("avatar", "rata")
            startActivity(intent)
        }

        avatarPingu.setOnClickListener{
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("avatar", "pinguino")
            startActivity(intent)
        }

        avatarZorro.setOnClickListener{
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("avatar", "zorro")
            startActivity(intent)
        }

        avatarConejo.setOnClickListener{
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("avatar", "conejo")
            startActivity(intent)
        }

        avatarJirafa.setOnClickListener{
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("avatar", "jirafa")
            startActivity(intent)
        }

        val rootView = findViewById<android.view.View>(R.id.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }
}