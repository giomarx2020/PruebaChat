package com.giosoto.chatpruebaradar.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.giosoto.chatpruebaradar.R
import com.giosoto.chatpruebaradar.databinding.FragmentRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegistroFragment : Fragment() {

    private lateinit var binding : FragmentRegistroBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var reference : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistroBinding.inflate(inflater, container, false)

        initlistener()
        return binding.root
    }

    fun initlistener(){

        auth = FirebaseAuth.getInstance()
        binding.apply {

                btnSign.setOnClickListener {
                    validarDatos()

            }
        }
    }

    fun validarDatos(){

        val nombreUsuario : String = binding.tfNombre.editText?.text.toString()
        val correoUsuario : String = binding.tfCorreo.editText?.text.toString()
        val pass1 : String = binding.tfPass.editText?.text.toString()
        val pass2 : String = binding.tfPass2.editText?.text.toString()

        if(nombreUsuario.isEmpty()){
            Toast.makeText(context,"Ingrese el nombre del usuario", Toast.LENGTH_LONG).show()
        }else if(correoUsuario.isEmpty()){
            Toast.makeText(context,"Ingrese su correo", Toast.LENGTH_LONG).show()
        }else if(pass1.isEmpty()){
            Toast.makeText(context,"Ingrese su contraseña", Toast.LENGTH_LONG).show()
        }else if(pass2.isEmpty()){
            Toast.makeText(context,"Repita su contraseña", Toast.LENGTH_LONG).show()
        }else if(!pass1.equals(pass2)){
            Toast.makeText(context,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
        }else{

            RegistroUser(correoUsuario,pass1)
        }

    }

    fun RegistroUser(correo : String, pass : String){
        auth.createUserWithEmailAndPassword(correo,pass)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    var uid : String =""
                    uid = auth.currentUser!!.uid
                    reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(uid)

                    var hashmap = HashMap<String, Any >()
                    var hashmap_nUsuario : String = binding.tfNombre.editText?.text.toString()
                    var hashmap_nCorreo : String = binding.tfCorreo.editText?.text.toString()

                    hashmap["uid"] = uid
                    hashmap["nombreUsuario"] = hashmap_nUsuario
                    hashmap["correo"] = hashmap_nCorreo
                    hashmap["imagen"] = ""
                    hashmap["buscar"] = hashmap_nUsuario.lowercase()

                    reference.updateChildren(hashmap).addOnCompleteListener { task2 ->
                        if(task2.isSuccessful){
                            NavHostFragment.findNavController(this@RegistroFragment).navigate(R.id.homeFragment);
                            Toast.makeText(context,"Registro exitoso", Toast.LENGTH_LONG).show()

                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(context,"${e.message}", Toast.LENGTH_LONG).show()

                    }
                }else{
                    Toast.makeText(context,"Ocurrio un error", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context,"${e.message}", Toast.LENGTH_LONG).show()
            }

    }


}