package com.tekrevol.arrowrecovery.constatnts

import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.models.DummyModel


object Constants {



    fun categorySelector(): ArrayList<DummyModel> {
        val array: ArrayList<DummyModel> = ArrayList()
        array.add(DummyModel("All Brands", true))
        array.add(DummyModel("Toyota", false))
        array.add(DummyModel("Honda", false))
        array.add(DummyModel("Audi", false))
        array.add(DummyModel("Ford", false))
        array.add(DummyModel("Porsche", false))
        array.add(DummyModel("BMW", false))
        return array
    }

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

    var sampleConverterBanners = intArrayOf(R.drawable.banner0, R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)




}