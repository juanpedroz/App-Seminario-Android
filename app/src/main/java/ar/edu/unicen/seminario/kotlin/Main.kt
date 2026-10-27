package ar.edu.unicen.seminario.kotlin

fun main(){

    // val/var name: Type = value
    // Int Long Float Double Boolean Char String
    // ? = nullable

    // Any Unit Nothing

    // listOf / mutableListOf
    // setOf / mutableSetOf
    // mapOf / mutableMapOf

    var nombre: String? = "Juan"

    val persona: Persona = Persona("Juan", "Perez", 20)

    val conjunto = setOf(1, 2, 3, 4, 5)
    val promedio = conjunto.promedio() // se ejecuta como una función de conjunto pero no es propia

    println("Hello $nombre")
    println(suma(1, 2))

}

fun Set<Int>.promedio(): Double {
    var sum = 0.0
    for (element in this) { // donde digo this es el Set<Int> de la función
        sum += element
    }
    return sum.toDouble() / this.size
}

fun suma(a: Int, b: Int?): Int {
    if (b == null) {
        return a
    }
    return a + b
}