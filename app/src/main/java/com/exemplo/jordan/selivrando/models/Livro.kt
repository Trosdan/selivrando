package com.exemplo.jordan.selivrando.models

class Livro (
    var id_livro: String,
    var titulo: String,
    var autor: String,
    var ano: String,
    var edicao: String,
    var descricao: String,
    var genero: Genero,
    var paginas: String,
    var isbn: String,
    var foto: String,
    var proprietario: String
) {}