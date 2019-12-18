package com.tekrevol.arrowrecovery.models

data class DummyModel(var text: String) {

    var isSelected = false

    constructor(text: String, isSelected: Boolean) : this(text) {
        this.isSelected = isSelected
     }




}
