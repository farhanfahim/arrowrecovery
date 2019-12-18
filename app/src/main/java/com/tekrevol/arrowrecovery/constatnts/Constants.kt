package com.tekrevol.arrowrecovery.constatnts

import com.tekrevol.arrowrecovery.models.DummyModel

object Constants {

    fun daysSelector(): ArrayList<DummyModel> {
        val array: ArrayList<DummyModel> = ArrayList()
        array.add(DummyModel("1d", true))
        array.add(DummyModel("1w", false))
        array.add(DummyModel("1m", false))
        array.add(DummyModel("6m", false))
        array.add(DummyModel("1y", false))
        array.add(DummyModel("all", false))
        return array
    }
}