package com.example.apphoctap.ultilities.loading

import android.app.Dialog
import android.content.Context
import com.example.apphoctap.R
import com.google.firebase.annotations.concurrent.UiThread

object LoadingDialogManager {
    private var loadingDialog: Dialog?=null

    @UiThread
    fun showDialog(context: Context) {
        if (loadingDialog == null) {
            loadingDialog = Dialog(context)
            loadingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            loadingDialog?.setContentView(R.layout.fragment_loading)
            loadingDialog?.show()
        }
    }

    @UiThread
    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}