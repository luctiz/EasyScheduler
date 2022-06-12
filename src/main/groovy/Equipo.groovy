import Excepciones.MiembroYaExistenteException

class Equipo {

    String nombre
    Usuario lider
    Usuario[] miembros = [] // contiene al lider
    Equipo(nombre, Usuario creador){
        this.nombre = nombre
        this.lider = creador
        miembros = miembros + creador
    }

    void agregarMiembro(Usuario nuevo_miembro){
        if (miembros.contains(nuevo_miembro)){
            throw new MiembroYaExistenteException()
        }
        miembros = miembros + nuevo_miembro
    }
}
