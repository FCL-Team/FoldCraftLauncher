package com.tungsten.fcl.ui.download

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Function
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogTranslationBinding
import com.tungsten.fcl.databinding.ItemTranslationBinding
import com.tungsten.fcl.util.ModTranslations
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLEditText
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslationDialog(
    context: Context,
    repository: RemoteModRepository,
    callback: (String) -> Unit
) : FCLDialog(context) {
    private var text: FCLEditText
    private var recyclerView: RecyclerView
    private var adapter: TranslationAdapter

    init {
        window?.setLayout(ConvertUtils.dip2px(context, 500f), ViewGroup.LayoutParams.MATCH_PARENT)
        val binding = DialogTranslationBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        text = binding.text
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val newCallback: (String) -> Unit = {
            dismiss()
            callback(it)
        }
        adapter = TranslationAdapter(context, mutableListOf(), newCallback)
        recyclerView.adapter = adapter
        binding.cancel.setOnClickListener { dismiss() }
        text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch(Dispatchers.Default) {
                    val s = text.text?.toString() ?: return@launch
                    val mods = ModTranslations.getTranslationsByRepositoryType(repository.type)
                        .searchMod(s)
                    withContext(Dispatchers.Main) {
                        adapter.update(mods)
                    }
                }
            }
        })
    }

    inner class TranslationAdapter(
        val context: Context,
        private val translationList: MutableList<ModTranslations.Mod>,
        val callback: (String) -> Unit
    ) :
        RecyclerView.Adapter<TranslationAdapter.TranslationViewHolder>() {

        inner class TranslationViewHolder(view: View) :
            RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
            return TranslationViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_translation, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return translationList.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
            val binding = ItemTranslationBinding.bind(holder.itemView)
            val mod = translationList[position]
            binding.text.text = "${mod.name} ${mod.subname} ${mod.abbr}"
            binding.root.setOnClickListener {
                callback(mod.subname.ifEmpty { mod.abbr })
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun update(translationList: List<ModTranslations.Mod>) {
            this.translationList.clear()
            this.translationList.addAll(translationList)
            notifyDataSetChanged()
        }
    }
}