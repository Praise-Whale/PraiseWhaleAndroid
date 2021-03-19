package com.sopt27.praisewhale.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.textChangedListener(textChanged : (CharSequence?) -> Unit){
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textChanged(s)
        }
    })
}