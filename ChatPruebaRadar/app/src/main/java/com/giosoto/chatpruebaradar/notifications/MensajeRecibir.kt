package com.giosoto.chatpruebaradar.notifications

import com.giosoto.chatpruebaradar.model.Mensaje

class MensajeRecibir : Mensaje{
    var hora: Long? = null

    constructor() : super()
    constructor(hora: Long?) : super() {
        this.hora = hora
    }

    constructor(
        mensaje: String?,
        urlFoto: String?,
        nombre: String?,
        fotoPerfil: String?,
        type_mensaje: String?,
        hora: Long?
    ) : super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje) {
        this.hora = hora
    }
}