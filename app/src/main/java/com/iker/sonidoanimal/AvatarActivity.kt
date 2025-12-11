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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        val avatarOso = findViewById<ImageButton>(R.id.btnOso)
        val avatarOsoPanda = findViewById<ImageButton>(R.id.btnOsoPanda)
        val avatarPingu = findViewById<ImageButton>(R.id.btnPinguino)
        val avatarZorro = findViewById<ImageButton>(R.id.btnZorro)
        val avatarTigre = findViewById<ImageButton>(R.id.btnTigre)
        val avatarLeon = findViewById<ImageButton>(R.id.btnLeon)

        // Funcion para devolver avatar
        fun devolverAvatar(animal: String) {
            val intent = Intent()
            intent.putExtra("avatar", animal)
            setResult(RESULT_OK, intent)
            finish()
        }

        avatarOso.setOnClickListener { devolverAvatar("oso") }
        avatarOsoPanda.setOnClickListener { devolverAvatar("oso_panda") }
        avatarPingu.setOnClickListener { devolverAvatar("pinguino") }
        avatarZorro.setOnClickListener { devolverAvatar("zorro") }
        avatarTigre.setOnClickListener { devolverAvatar("tigre") }
        avatarLeon.setOnClickListener { devolverAvatar("leon") }
    }
}