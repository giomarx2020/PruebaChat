package com.giosoto.chatpruebaradar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.giosoto.chatpruebaradar.R
import com.giosoto.chatpruebaradar.model.HolderMensaje
import com.giosoto.chatpruebaradar.notifications.MensajeRecibir
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AdapterMensajes(private val context: Context) : RecyclerView.Adapter<HolderMensaje>() {

  private val listMensaje: MutableList<MensajeRecibir> = ArrayList()

    fun addMensaje(mensaje: MensajeRecibir) {
        listMensaje.add(mensaje)
        notifyItemInserted(listMensaje.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMensaje {
        val view = LayoutInflater.from(context).inflate(R.layout.card_view_mensajes, parent, false)
        return HolderMensaje(view)
    }

    override fun onBindViewHolder(holder: HolderMensaje, position: Int) {
        val mensaje = listMensaje[position]
        holder.nombre.text = mensaje.nombre
        holder.mensaje.text = mensaje.mensaje

        if (mensaje.type_mensaje == "2") {
            holder.fotoMensaje.visibility = View.VISIBLE
            holder.mensaje.visibility = View.VISIBLE
            Glide.with(context).load(mensaje.urlFoto).into(holder.fotoMensaje)
        } else if (mensaje.type_mensaje == "1") {
            holder.fotoMensaje.visibility = View.GONE
            holder.mensaje.visibility = View.VISIBLE
        }else if(mensaje.type_mensaje == "3"){
            holder.fotoMensaje.visibility = View.VISIBLE
            holder.mensaje.visibility = View.VISIBLE
            Glide.with(context).load(mensaje.urlFoto).into(holder.fotoMensaje)
        }

        if (mensaje.fotoPerfil.isNullOrEmpty()) {
            holder.fotoMensajePerfil.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(context).load(mensaje.fotoPerfil).into(holder.fotoMensajePerfil)
        }

        val codigoHora = mensaje.hora ?: 0L
        val date = Date(codigoHora)
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        holder.hora.text = sdf.format(date)
    }

    override fun getItemCount(): Int {
        return listMensaje.size
    }

}