package com.tekrevol.arrowrecovery.callbacks

import android.location.Address

/**
 * Created by hamza.ahmed on 3/7/2018.
 */
interface OnDataPass {
    fun onDataPass(title: String, firstName: String, lastName: String, rabioBtnCompanyOrIndividual: Int, companyName: String, kindOfCompany: String, phoneNo: String, comment: String, address: String, zipCode: String, city: String, state: String, country: String)
}