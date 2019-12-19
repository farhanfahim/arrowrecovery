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

    fun carMakeSelector(): ArrayList<DummyModel> {
        val array: ArrayList<DummyModel> = ArrayList()
        array.add(DummyModel("Acura", true))
        array.add(DummyModel("Alfa Romeo", false))
        array.add(DummyModel("Aston Martin", false))
        array.add(DummyModel("Audi", false))
        array.add(DummyModel("BMW", false))
        return array
    }
    fun carModelSelector(): ArrayList<DummyModel> {
        val array: ArrayList<DummyModel> = ArrayList()
        array.add(DummyModel("Microcar", true))
        array.add(DummyModel("Mitsubishi Mirage G4", false))
        array.add(DummyModel("Economy hatchback", false))
        array.add(DummyModel("SUVs", false))
        array.add(DummyModel("Convertibles", false))
        return array
    }

    fun carYearSelector(): ArrayList<DummyModel> {
        val array: ArrayList<DummyModel> = ArrayList()
        array.add(DummyModel("2001", true))
        array.add(DummyModel("2002", false))
        array.add(DummyModel("2003", false))
        array.add(DummyModel("2004", false))
        array.add(DummyModel("2005", false))
        array.add(DummyModel("2006", false))
        array.add(DummyModel("2007", false))
        array.add(DummyModel("2008", false))
        array.add(DummyModel("2009", false))
        array.add(DummyModel("2011", false))
        array.add(DummyModel("2012", false))
        array.add(DummyModel("2013", false))
        array.add(DummyModel("2014", false))
        array.add(DummyModel("2015", false))
        array.add(DummyModel("2016", false))
        array.add(DummyModel("2017", false))
        array.add(DummyModel("2018", false))
        array.add(DummyModel("2019", false))
        return array
    }

    var sampleConverterBanners = intArrayOf(R.drawable.banner0, R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)

    var qualities = arrayOf("25%", "50%", "75%", "100%")
    var title = arrayOf("Mr", "Mrs", "Ms", "Miss")


}