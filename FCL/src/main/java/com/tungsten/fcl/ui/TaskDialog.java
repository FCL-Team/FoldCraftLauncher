package com.tungsten.fcl.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.task.TaskListener;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class TaskDialog extends FCLDialog implements View.OnClickListener {

    private FCLTextView titleView;
    private FCLTextView speedView;
    private FCLButton cancelButton;
    private ScrollView logScroll;
    private FCLTextView logView;

    private final Map<Task<?>, ChangeListener<String>> messageListeners = new HashMap<>();

    private TaskExecutor executor;
    private TaskListener autoCloseListener;
    private TaskListener messageUpdateListener;
    private TaskCancellationAction onCancel;
    private final Consumer<FileDownloadTask.SpeedEvent> speedEventHandler;

    /** 当前正在写日志的任务，用于在它结束时清空并隐藏日志区 */
    private Task<?> logTask;

    private TaskListPane taskListPane;
    private ListView taskListView;

    @SuppressLint("DefaultLocale")
    public TaskDialog(@NonNull Context context, @NotNull TaskCancellationAction cancel) {
        super(context);
        setContentView(R.layout.dialog_task);
        setCancelable(false);

        titleView = findViewById(R.id.title);
        taskListView = findViewById(R.id.list);
        speedView = findViewById(R.id.speed);
        cancelButton = findViewById(R.id.cancel);
        logScroll = findViewById(R.id.logScroll);
        logView = findViewById(R.id.logView);

        setCancel(cancel);

        cancelButton.setOnClickListener(this);

        // 对话框关闭时解除所有外部持有（executor 的任务监听、任务消息监听、任务列表 View 树），
        // 避免任务完成后整棵 View 树随页面持有的 executor 泄漏
        setOnDismissListener(dialog -> {
            messageListeners.forEach((task, listener) -> task.messageProperty().removeListener(listener));
            messageListeners.clear();
            if (executor != null) {
                if (autoCloseListener != null) executor.removeTaskListener(autoCloseListener);
                if (messageUpdateListener != null) executor.removeTaskListener(messageUpdateListener);
            }
            if (taskListPane != null) taskListPane.release();
        });

        speedEventHandler = speedEvent -> {
            String unit = "B/s";
            double speed = speedEvent.getSpeed();
            if (speed > 1024) {
                speed /= 1024;
                unit = "KB/s";
            }
            if (speed > 1024) {
                speed /= 1024;
                unit = "MB/s";
            }
            double finalSpeed = speed;
            String finalUnit = unit;
            Schedulers.androidUIThread().execute(() -> {
                speedView.setText(String.format("%.1f %s", finalSpeed, finalUnit));
            });
        };
        FileDownloadTask.speedEvent.channel(FileDownloadTask.SpeedEvent.class).registerWeak(speedEventHandler);
    }

    public void setExecutor(TaskExecutor executor) {
        setExecutor(executor, true);
    }

    public void setExecutor(TaskExecutor executor, boolean autoClose) {
        this.executor = executor;

        if (executor != null) {
            if (autoClose) {
                autoCloseListener = new TaskListener() {
                    @Override
                    public void onStop(boolean success, TaskExecutor executor) {
                        Schedulers.androidUIThread().execute(() -> dismiss());
                    }
                };
                executor.addTaskListener(autoCloseListener);
            }

            // 监听任务消息，实时显示安装器日志
            messageUpdateListener = new TaskListener() {
                @Override
                public void onRunning(Task<?> task) {
                    Schedulers.androidUIThread().execute(() -> {
                        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
                            logTask = task;
                            onInstallerLog(newValue);
                        };
                        task.messageProperty().addListener(listener);
                        messageListeners.put(task, listener);
                    });
                }

                @Override
                public void onFinished(Task<?> task) {
                    Schedulers.androidUIThread().execute(() -> {
                        removeMessageListener(task);
                        // 写日志的任务结束后清空并隐藏日志区，避免最后一条信息一直占据界面
                        if (logTask == task) {
                            logTask = null;
                            clearLog();
                        }
                    });
                }
            };
            executor.addTaskListener(messageUpdateListener);

            taskListPane = new TaskListPane(getContext(), executor);
            taskListView.setAdapter(taskListPane);
        }
    }

    /** 更新日志面板内容并滚动到底部 */
    private void onInstallerLog(String message) {
        if (message == null || message.isEmpty())
            return;
        logScroll.setVisibility(View.VISIBLE);
        logView.setText(message);
        logScroll.post(() -> logScroll.fullScroll(View.FOCUS_DOWN));
    }

    /** 清空日志内容并恢复隐藏 */
    private void clearLog() {
        logView.setText("");
        logScroll.setVisibility(View.GONE);
    }

    /** 手动追加一条实时进度文字，供无法接入任务体系的流程使用 */
    public void appendLog(String message) {
        onInstallerLog(message);
    }

    private void removeMessageListener(Task<?> task) {
        ChangeListener<String> listener = messageListeners.remove(task);
        if (listener != null) {
            task.messageProperty().removeListener(listener);
        }
    }

    public StringProperty titleProperty() {
        return titleView.stringProperty();
    }

    public String getTitle() {
        return titleView.getText().toString();
    }

    public void setTitle(String currentState) {
        titleView.setString(currentState);
    }

    public void setCancel(TaskCancellationAction onCancel) {
        this.onCancel = onCancel;

        cancelButton.setEnabled(onCancel != null);
    }

    @Override
    public void onClick(View view) {
        if (view == cancelButton) {
            Optional.ofNullable(executor).ifPresent(TaskExecutor::cancel);
            onCancel.getCancellationAction().accept(this);
            dismiss();
        }
    }
}
