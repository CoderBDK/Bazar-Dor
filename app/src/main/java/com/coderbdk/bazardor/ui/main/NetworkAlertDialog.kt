package com.coderbdk.bazardor.ui.main


import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class NetworkAlertDialog(context: Context) {
    private val dialog: AlertDialog

    interface NetworkDialogListener {
        fun onPositiveEvent()
        fun onNegativeEvent()
    }

    private var listener: NetworkDialogListener? = null

    init {
        dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Connection failed")
            .setMessage("Please check your internet connection.")
            .setCancelable(false)
            .setNegativeButton("Check connection") { _, _ ->
                listener?.let {
                    it.onNegativeEvent()
                }
            }
            .setPositiveButton("Retry again") { _, _ ->
                listener?.let {
                    it.onPositiveEvent()
                }
            }
            .create()

        dialog.setOnDismissListener {
            dialog.show()
        }
    }

    fun addNetworkDialogListener(listener: NetworkDialogListener) {
        this.listener = listener
    }

    fun show(message: String) {
        dialog.setMessage(message)
        if (!dialog.isShowing) dialog.show()
    }

    fun hide() {
        if (dialog.isShowing) dialog.hide()
    }
}