package com.tungsten.fcllibrary.component.view

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.withStyledAttributes
import com.tungsten.fcl.R
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase
import com.tungsten.fclcore.fakefx.beans.property.StringProperty
import com.tungsten.fclcore.fakefx.beans.property.StringPropertyBase
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.util.regex.Pattern

class FCLEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs) {

    private var autoTint = false

    @JvmField
    var fromUserOrSystem = false

    private var visibilityProperty: BooleanProperty? = null
    private var disableProperty: BooleanProperty? = null
    private var stringProperty: StringProperty? = null

    init {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.FCLEditText) {
                autoTint = getBoolean(R.styleable.FCLEditText_auto_edit_tint, false)
            }
        }
        addTextWatcher()
        ThemeEngine.registerEvent(this) { refreshTheme() }
    }

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private fun refreshTheme() {
        val state = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf()
        )
        val color = intArrayOf(
            ThemeEngine.getTheme().getColor(),
            ThemeEngine.getTheme().dkColor
        )
        setBackgroundTintList(ColorStateList(state, color))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textCursorDrawable?.setTint(ThemeEngine.getTheme().getColor())
        }
        if (autoTint) {
            setTextColor(ThemeEngine.getTheme().autoTint)
            setHintTextColor(ThemeEngine.getTheme().autoHintTint)
        }
    }

    fun addTextWatcher() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                fromUserOrSystem = true
                stringProperty().set(text.toString())
                fromUserOrSystem = false
            }
        })
    }

    private class SignedIntegerFilter(min: Int) : InputFilter {
        private val pattern = Pattern.compile(if (min < 0) "^-?[0-9]*$" else "^[0-9]*$")

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence {
            val builder = StringBuilder(dest)
            builder.insert(dstart, source)
            return if (pattern.matcher(builder).matches()) source else ""
        }
    }

    fun setIntegerFilter(min: Int) {
        setFilters(arrayOf(SignedIntegerFilter(min)))
    }

    fun setAutoTint(autoTint: Boolean) {
        this.autoTint = autoTint
    }

    fun isAutoTint(): Boolean {
        return autoTint
    }

    fun setVisibilityValue(visibility: Boolean) {
        visibilityProperty().set(visibility)
    }

    fun getVisibilityValue(): Boolean {
        return visibilityProperty == null || visibilityProperty!!.get()
    }

    fun visibilityProperty(): BooleanProperty {
        if (visibilityProperty == null) {
            visibilityProperty = object : BooleanPropertyBase() {

                override fun invalidated() {
                    Schedulers.androidUIThread().execute {
                        val visible = get()
                        visibility = if (visible) VISIBLE else GONE
                    }
                }

                override fun getBean(): Any = this

                override fun getName(): String = "visibility"
            }
        }

        return visibilityProperty!!
    }

    fun setDisableValue(disableValue: Boolean) {
        disableProperty().set(disableValue)
    }

    fun getDisableValue(): Boolean {
        return disableProperty == null || disableProperty!!.get()
    }

    fun disableProperty(): BooleanProperty {
        if (disableProperty == null) {
            disableProperty = object : BooleanPropertyBase() {

                override fun invalidated() {
                    Schedulers.androidUIThread().execute {
                        val disable = get()
                        isEnabled = !disable
                    }
                }

                override fun getBean(): Any = this

                override fun getName(): String = "disable"
            }
        }

        return disableProperty!!
    }

    fun setStringValue(string: String) {
        stringProperty().set(string)
    }

    fun getStringValue(): String? {
        return stringProperty?.get()
    }

    fun stringProperty(): StringProperty {
        if (stringProperty == null) {
            stringProperty = object : StringPropertyBase() {

                override fun invalidated() {

                }

                override fun getBean(): Any = this

                override fun getName(): String = "string"
            }
        }

        return stringProperty!!
    }
}