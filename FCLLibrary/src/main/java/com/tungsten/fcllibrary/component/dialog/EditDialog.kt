package com.tungsten.fcllibrary.component.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tungsten.fcllibrary.component.view.FCLEditText
import com.tungsten.fcllibrary.databinding.DialogEditBinding
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

class EditDialog @JvmOverloads constructor(
    context: Context,
    private val defaultText: String = "",
    private val callback: Consumer<String>
) : FCLDialog(context),
    View.OnClickListener {
    var binding: DialogEditBinding
    var onCancelListener: (() -> Unit)? = null

    init {
        setCancelable(false)
        window?.setLayout(ConvertUtils.dip2px(context, 400f), ViewGroup.LayoutParams.WRAP_CONTENT)
        binding = DialogEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            positive.setOnClickListener(this@EditDialog)
            negative.setOnClickListener(this@EditDialog)
            editText.setText(defaultText)
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            if (v === positive) {
                val s = editText.getText().toString()
                if (!s.trim { it <= ' ' }.isEmpty()) {
                    callback.accept(s)
                    dismiss()
                }
            }
            if (v === negative) {
                onCancelListener?.invoke()
                dismiss()
            }
        }
    }

    override fun setTitle(titleId: Int) {
        binding.title.setText(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        binding.title.text = title
    }

    @SuppressLint("SetTextI18n")
    fun appendTitle(title: String) {
        binding.title.text = "${binding.title.text} $title"
    }

    fun getEditText(): FCLEditText {
        return binding.editText
    }
}
