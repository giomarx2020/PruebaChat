package com.giosoto.chatpruebaradar.views.activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.giosoto.chatpruebaradar.R


class Splash : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        iniciarSplash()
    }

    fun iniciarSplash(){
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 milisegundos = 3 segundos
    }


}