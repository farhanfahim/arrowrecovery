package com.tekrevol.arrowrecovery.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxSearchObserver {

    companion object
    {

        fun fromView(text : EditText) : Observable<String> {

            val subject = PublishSubject.create<String>()

            text.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
                    if(s.length > 1){
                        subject.onNext(s.toString())
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {

                }
            })

            return subject
        }
    }
}