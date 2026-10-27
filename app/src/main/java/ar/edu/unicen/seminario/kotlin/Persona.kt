package ar.edu.unicen.seminario.kotlin

open class Persona(
    // data class genera automaticamente tostring y equals y hashcode
    // open class permite que se pueda extender
    // abstract class no se puede instanciar
    var nombre: String,
    var apellido: String,
    var edad: Int
) {

    fun getNombreCompleto(): String {
        return "$nombre $apellido"
    }

}
