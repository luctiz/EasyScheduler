class Usuario {
    String nombreUsuario
    String contraseña

    static constraints = {
        nombreUsuario blank: false, nullable: false
        contraseña blank: false, nullable: false
    }
}
