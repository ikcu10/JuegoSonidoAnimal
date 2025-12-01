package com.iker.sonidoanimal

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
        // Se puede borrar
        enableEdgeToEdge()
        setContentView(R.layout.activity_avatar)

        val avatarOso = findViewById<ImageButton>(R.id.btnOso)
        val avatarOsoPanda = findViewById<ImageButton>(R.id.btnOsoPanda)
        val avatarPingu = findViewById<ImageButton>(R.id.btnPinguino)
        val avatarZorro = findViewById<ImageButton>(R.id.btnZorro)
        val avatarTigre = findViewById<ImageButton>(R.id.btnTigre)
        val avatarLeon = findViewById<ImageButton>(R.id.btnLeon)

        // Esta función devuelve el animal elegido a la pantalla anterior
        fun devolverAvatar(animal: String) {
            val intent = Intent()
            intent.putExtra("avatar", animal)
            setResult(RESULT_OK, intent)
            finish() // Cierra esta pantalla y vuelve al Login
        }

        avatarOso.setOnClickListener { devolverAvatar("oso") }
        avatarOsoPanda.setOnClickListener { devolverAvatar("oso_panda") }
        avatarPingu.setOnClickListener { devolverAvatar("pinguino") }
        avatarZorro.setOnClickListener { devolverAvatar("zorro") }
        avatarTigre.setOnClickListener { devolverAvatar("tigre") }
        avatarLeon.setOnClickListener { devolverAvatar("leon") }

        // Pone más padding por eso es posible que lo borre
        val rootView = findViewById<android.view.View>(R.id.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }
}