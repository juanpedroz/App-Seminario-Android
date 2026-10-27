package ar.edu.unicen.seminario.kotlin

class Trabajador(
    nombre: String,
    apellido: String,
    edad: Int,
    var sueldo: Double
): Persona(nombre, apellido, edad) {
}