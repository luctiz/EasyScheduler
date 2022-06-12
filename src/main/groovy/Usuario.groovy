import Excepciones.TareaNoAsignadaException

class Usuario {
    String nombreUsuario
    String contraseña


    Equipo EquipoPrivado = new Equipo("Privado", this)


    Equipo[] equipos = []


    static constraints = {
        nombreUsuario blank: false, nullable: false
        contraseña blank: false, nullable: false
    }
    Equipo crearNuevoEquipo(nombre){
        def equipo = new Equipo(nombre,this)
        equipos += equipo
        return equipo
    }
    
    void completarTarea(Tarea tarea){
        if (tarea.getAsignado() != this){
            throw new TareaNoAsignadaException()
        }
        tarea.setEstado(Estado.Completado)
    }

}
