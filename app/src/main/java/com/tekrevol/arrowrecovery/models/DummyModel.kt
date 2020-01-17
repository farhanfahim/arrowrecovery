package com.tekrevol.arrowrecovery.models

import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory

data class DummyModel(var text: String) {

    var isSelected = false

    constructor(text: String, isSelected: Boolean) : this(text) {
        this.isSelected = isSelected
     }

    override fun toString(): String {
        return GsonFactory.getSimpleGson().toJson(this)
    }

}
