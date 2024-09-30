package com.tungsten.fcl.control

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tungsten.fcl.R
import com.tungsten.fcl.control.view.KeycodeView
import com.tungsten.fcl.control.view.KeycodeView.OnKeycodeChangeListener
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty
import com.tungsten.fclcore.fakefx.collections.ObservableList
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLButton
import com.tungsten.fcllibrary.component.view.FCLLinearLayout
import java.util.Objects
import java.util.function.Consumer

class SelectKeycodeDialog(
    context: Context,
    private val list: ObservableList<Int>,
    private val singleSelection: Boolean,
    mouse: Boolean
) : FCLDialog(context) {
    private val selectionProperty = SimpleIntegerProperty(this, "selection", -1)

    private val positive: FCLButton

    private val container: FCLLinearLayout

    private var onConfirm: (SelectKeycodeDialog)->Unit = {}

    fun selectionProperty(): SimpleIntegerProperty {
        return selectionProperty
    }

    init {
        setCancelable(false)
        setContentView(R.layout.dialog_select_keycode)

        if (singleSelection) {
            selectionProperty.set(list[0])
        }

        container = findViewById(R.id.parent_layout)!!
        initializeAllButtons(container)
        checkSelection(container)

        val mouseLayout = checkNotNull(findViewById<FCLLinearLayout>(R.id.mouse))
        mouseLayout.visibility = if (mouse) View.VISIBLE else View.GONE

        positive = findViewById(R.id.positive)!!
        positive.setOnClickListener{
            onConfirm(this)
            dismiss()
        }
    }

    constructor(
        context: Context,
        list: ObservableList<Int>,
        singleSelection: Boolean,
        mouse: Boolean,
        onConfirm: (SelectKeycodeDialog)->Unit
    ) : this(context, list, singleSelection, mouse) {
        this.onConfirm = onConfirm
    }

    private fun checkSelection(container: ViewGroup) {
        for (i in 0 until container.childCount) {
            if (container.getChildAt(i) is KeycodeView) {
                val l = ArrayList<Int>()
                if (singleSelection) {
                    l.add(selectionProperty.get())
                } else {
                    l.addAll(list)
                }
                (container.getChildAt(i) as KeycodeView).checkSelection(l)
            } else if (container.getChildAt(i) is ViewGroup) {
                checkSelection(container.getChildAt(i) as ViewGroup)
            }
        }
    }

    private fun initializeAllButtons(container: ViewGroup) {
        for (i in 0 until container.childCount) {
            if (container.getChildAt(i) is KeycodeView) {
                (container.getChildAt(i) as KeycodeView).setOnKeycodeChangeListener(object :
                    OnKeycodeChangeListener {
                    override fun onKeycodeAdd(view: KeycodeView, keycode: Int) {
                        if (singleSelection) {
                            selectionProperty.set(keycode)
                            checkSelection(this@SelectKeycodeDialog.container)
                        } else {
                            list.add(keycode)
                        }
                    }

                    override fun onKeycodeRemove(view: KeycodeView, keycode: Int) {
                        if (singleSelection) {
                            view.setSelectedWithoutCallback(true)
                        } else {
                            for (j in list.indices) {
                                if (list[j] == keycode) {
                                    list.removeAt(j)
                                    break
                                }
                            }
                        }
                    }
                })
            } else if (container.getChildAt(i) is ViewGroup) {
                initializeAllButtons(container.getChildAt(i) as ViewGroup)
            }
        }
    }
}
