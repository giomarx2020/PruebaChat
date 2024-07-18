package com.giosoto.chatpruebaradar.views.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.giosoto.chatpruebaradar.R
import com.giosoto.chatpruebaradar.adapter.AdapterMensajes
import com.giosoto.chatpruebaradar.databinding.FragmentHomeBinding
import com.giosoto.chatpruebaradar.notifications.MensajeEnviar
import com.giosoto.chatpruebaradar.notifications.MensajeRecibir
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var fotoPerfil: CircleImageView
    private lateinit var nombre: TextView
    private lateinit var rvMensajes: RecyclerView
    private lateinit var txtMensaje: TextInputLayout
    private lateinit var btnEnviar: Button
    private lateinit var adapter: AdapterMensajes
    private lateinit var btnEnviarFoto: ImageButton
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var PHOTO_SEND = 1
    private var PHOTO_PERFIL = 2
    private var LOCATION = 3
    private lateinit var fotoPerfilCadena: String
    var sharedPreferences: SharedPreferences? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        sharedPreferences = requireContext().getSharedPreferences("chat", Context.MODE_PRIVATE)

        fotoPerfil = binding.fotoPerfil
        nombre = binding.nombre
        rvMensajes = binding.rvMensajes
        txtMensaje = binding.txtMensaje
        btnEnviar = binding.btnEnviar
        btnEnviarFoto = binding.btnEnviarFoto
        fotoPerfilCadena = ""

        var nombreUsuario = sharedPreferences?.getString("nombre", "")
        binding.nombre.text = nombreUsuario

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("chat") //Sala de chat (nombre)

        storage = FirebaseStorage.getInstance()

        adapter = AdapterMensajes(requireContext())
        val l = LinearLayoutManager(requireContext())
        rvMensajes.layoutManager = l
        rvMensajes.adapter = adapter

        initListener()

        return binding.root
    }

    fun initListener() {

        btnEnviar.setOnClickListener {
            databaseReference.push().setValue(
                MensajeEnviar(
                    txtMensaje.editText?.text.toString(),
                    nombre.text.toString(),
                    fotoPerfilCadena,
                    "1",
                    ServerValue.TIMESTAMP
                )
            )
            txtMensaje.editText?.setText("")
        }

        btnEnviarFoto.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/jpeg"
            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND)
        }

        fotoPerfil.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/jpeg"
            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL)
        }

        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        })

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val m = dataSnapshot.getValue(MensajeRecibir::class.java)
                adapter.addMensaje(m!!)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        binding.btnUbicacion.setOnClickListener {
            onClickRequestPermission(binding.btnUbicacion)
        }

    }

    private fun setScrollbar() {
        rvMensajes.scrollToPosition(adapter.itemCount - 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var nombreUsuario = sharedPreferences?.getString("nombre", "")
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val storageReference = storage.reference.child("imagenes_chat")
                val fotoReferencia = storageReference.child(uri.lastPathSegment!!)
                fotoReferencia.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                        val mensajeEnviar = MensajeEnviar(
                            "$nombreUsuario te ha enviado una foto",
                            downloadUri.toString(),
                            nombre.text.toString(),
                            fotoPerfilCadena,
                            "2",
                            ServerValue.TIMESTAMP
                        )
                        databaseReference.push().setValue(mensajeEnviar)
                    }
                }
            }
        } else if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {
            val uri = data?.data
            var nombreUsuario = sharedPreferences?.getString("nombre", "")
            if (uri != null) {
                val storageReference = storage.reference.child("foto_perfil")
                val fotoReferencia = storageReference.child(uri.lastPathSegment!!)
                fotoReferencia.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                        fotoPerfilCadena = downloadUri.toString()
                        val mensajeEnviar = MensajeEnviar(
                            "$nombreUsuario ha actualizado su foto de perfil",
                            downloadUri.toString(),
                            nombre.text.toString(),
                            fotoPerfilCadena,
                            "2",
                            ServerValue.TIMESTAMP
                        )
                        databaseReference.push().setValue(mensajeEnviar)
                        Glide.with(this@HomeFragment).load(downloadUri.toString()).into(fotoPerfil)
                    }
                }
            }
        }else if (requestCode == LOCATION && resultCode == RESULT_OK) {
            val uri = data?.data
            var nombreUsuario = sharedPreferences?.getString("nombre", "")
            if (uri != null) {
                val storageReference = storage.reference.child("urlFoto")
                val fotoReferencia = storageReference.child(uri.lastPathSegment!!)
                fotoReferencia.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                        fotoPerfilCadena = downloadUri.toString()
                        val mensajeEnviar = MensajeEnviar(
                            "$nombreUsuario ha enviado su ubicacion",
                            downloadUri.toString(),
                            nombre.text.toString(),
                            fotoPerfilCadena,
                            "2",
                            ServerValue.TIMESTAMP
                        )
                        databaseReference.push().setValue(mensajeEnviar)
                        Glide.with(this@HomeFragment).load(downloadUri.toString()).into(fotoPerfil)
                    }
                }
            }
        }
    }

    private fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.mapFragment)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                view.showSnackbar(
                    view,
                    "Se requiere el permiso de ubicacion",
                    Snackbar.LENGTH_INDEFINITE,
                    "ok"
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }
}

fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(view, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackbar.show()
    }
}