package com.giosoto.chatpruebaradar.views.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.giosoto.chatpruebaradar.R
import com.giosoto.chatpruebaradar.databinding.FragmentInicioBinding
import com.giosoto.chatpruebaradar.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class InicioFragment : Fragment() {

    private lateinit var binding : FragmentInicioBinding
    var firebaseUser :FirebaseUser ?= null
    private lateinit var auth : FirebaseAuth
    var sharedPreferences: SharedPreferences? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("chat", Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        initlistener()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        ComprobarSesion()
    }

    fun initlistener(){

        val btnRegistrarse = binding.btnRegistrarse
        binding.apply {
            btnRegistrarse.setOnClickListener {
                NavHostFragment.findNavController(this@InicioFragment).navigate(R.id.registroFragment)
            }

            btnSign.setOnClickListener {
                ValidarDatos()
            }
        }
    }

    private fun ComprobarSesion(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if(firebaseUser != null){
            var nom = firebaseUser!!.email
            sharedPreferences?.edit()?.putString("nombre", nom)?.apply()
            NavHostFragment.findNavController(this@InicioFragment).navigate(R.id.homeFragment)
            Toast.makeText(context,"Sesion Activa", Toast.LENGTH_LONG).show()

        }

    }

    private fun ValidarDatos(){

        val email : String = binding.tfCorreo.editText?.text.toString()
        val pass : String = binding.tfPass.editText?.text.toString()

        if(email.isEmpty()){
            Toast.makeText(context,"Ingrese su Correo", Toast.LENGTH_LONG).show()
        }
        if(pass.isEmpty()){
            Toast.makeText(context,"Ingrese su contraseña", Toast.LENGTH_LONG).show()
        }else{

            LoginUsuario(email,pass)
        }
    }

    private fun LoginUsuario(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    NavHostFragment.findNavController(this@InicioFragment).navigate(R.id.homeFragment)
                    Toast.makeText(context,"Inicio de Session", Toast.LENGTH_SHORT).show()

                }else {
                    Toast.makeText(context,"Ocurrio un error", Toast.LENGTH_SHORT).show()

                }
            }.addOnFailureListener {e ->
                Toast.makeText(context,"${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

}