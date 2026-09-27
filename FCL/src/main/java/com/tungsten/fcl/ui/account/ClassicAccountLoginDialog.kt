package com.tungsten.fcl.ui.account

import android.content.Context
import android.view.inputmethod.EditorInfo
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogReloginPasswordBinding
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fclcore.auth.AuthInfo
import com.tungsten.fclcore.auth.ClassicAccount
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import java.util.function.Consumer
import java.util.logging.Level

/** Classic 账户（Yggdrasil / authlib-injector）凭据过期后的密码重登弹窗 */
class ClassicAccountLoginDialog(
    context: Context,
    private val account: ClassicAccount,
    private val success: Consumer<AuthInfo>,
    private val failed: Runnable,
) : FCLDialog(context) {

    private val binding = DialogReloginPasswordBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCancelable(false)
        binding.username.text = account.username
        binding.login.setOnClickListener { logIn() }
        binding.cancel.setOnClickListener {
            failed.run()
            dismiss()
        }
        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                logIn()
                true
            } else {
                false
            }
        }
    }

    private fun logIn() {
        val password = binding.password.text?.toString().orEmpty()
        if (password.isEmpty()) {
            binding.password.error = context.getString(R.string.input_hint_not_empty)
            return
        }
        binding.login.isEnabled = false
        binding.cancel.isEnabled = false
        Task.supplyAsync { account.logInWithPassword(password) }
            .whenComplete(Schedulers.androidUIThread()) { authInfo, exception ->
                if (exception == null) {
                    success.accept(authInfo)
                    dismiss()
                } else {
                    LOG.log(Level.INFO, "Failed to login with password: $account", exception)
                    FCLAlertDialog.Builder(context).apply {
                        setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                        setMessage(Accounts.localizeErrorMessage(context, exception))
                        setCancelable(false)
                        setNegativeButton(context.getString(R.string.dialog_positive), null)
                    }.create().show()
                    binding.login.isEnabled = true
                    binding.cancel.isEnabled = true
                }
            }.start()
    }
}
