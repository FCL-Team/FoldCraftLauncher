package com.tungsten.fcl.ui;

import static com.tungsten.fclcore.util.Lang.tryCast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.task.TaskListener;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TaskListPane extends FCLAdapter {

    private final TaskExecutor executor;
    private final Map<Task<?>, ProgressListNode> nodes = new HashMap<>();
    private final List<StageNode> stageNodes = new ArrayList<>();
    private final ArrayList<View> listBox = new ArrayList<>();

    public TaskListPane(Context context, TaskExecutor taskExecutor) {
        super(context);
        this.executor = taskExecutor;
        setExecutor(taskExecutor);
    }

    @Override
    public int getCount() {
        return listBox.size();
    }

    @Override
    public Object getItem(int i) {
        return listBox.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return listBox.get(i);
    }

    private void setExecutor(TaskExecutor executor) {
        List<String> stages = Lang.removingDuplicates(executor.getStages());
        executor.addTaskListener(new TaskListener() {
            @Override
            public void onStart() {
                Schedulers.androidUIThread().execute(() -> {
                    stageNodes.clear();
                    stageNodes.addAll(stages.stream().map(it -> new StageNode(getContext(), it)).collect(Collectors.toList()));
                    for (StageNode stageNode : stageNodes) {
                        listBox.add(stageNode.getView());
                    }
                    notifyDataSetChanged();
                });
            }

            @Override
            public void onReady(Task<?> task) {
                if (task.getStage() != null) {
                    Schedulers.androidUIThread().execute(() -> stageNodes.stream().filter(x -> x.stage.equals(task.getStage())).findAny().ifPresent(StageNode::begin));
                }
            }

            @Override
            public void onRunning(Task<?> task) {
                if (!task.getSignificance().shouldShow() || task.getName() == null)
                    return;

                TaskLabels.resolveTaskName(getContext(), task);

                Schedulers.androidUIThread().execute(() -> {
                    StageNode stageNode = stageNodes.stream().filter(x -> x.stage.equals(task.getInheritedStage())).findAny().orElse(null);
                    ProgressListNode node = new ProgressListNode(getContext(), stageNode != null && stageNodes.contains(stageNode), task);
                    nodes.put(task, node);
                    listBox.add(stageNode == null || !stageNodes.contains(stageNode) ? 0 : listBox.indexOf(stageNode.getView()) + 1, node.getView());
                    notifyDataSetChanged();
                });
            }

            @Override
            public void onFinished(Task<?> task) {
                if (task.getStage() != null) {
                    Schedulers.androidUIThread().execute(() -> stageNodes.stream().filter(x -> x.stage.equals(task.getStage())).findAny().ifPresent(StageNode::succeed));
                }

                Schedulers.androidUIThread().execute(() -> {
                    ProgressListNode node = nodes.remove(task);
                    if (node == null)
                        return;
                    node.unbind();
                    listBox.remove(node.getView());
                    notifyDataSetChanged();
                });
            }

            @Override
            public void onFailed(Task<?> task, Throwable throwable) {
                if (task.getStage() != null) {
                    Schedulers.androidUIThread().execute(() -> stageNodes.stream().filter(x -> x.stage.equals(task.getStage())).findAny().ifPresent(StageNode::fail));
                }
                ProgressListNode node = nodes.remove(task);
                if (node == null)
                    return;
                Schedulers.androidUIThread().execute(() -> node.setThrowable(throwable));
            }

            @Override
            public void onPropertiesUpdate(Task<?> task) {
                if (task instanceof Task.CountTask) {
                    Schedulers.androidUIThread().execute(() -> stageNodes.stream()
                            .filter(x -> x.stage.equals(((Task<?>.CountTask) task).getCountStage()))
                            .findAny()
                            .ifPresent(StageNode::count));

                    return;
                }

                if (task.getStage() != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        int total = tryCast(task.getProperties().get("total"), Integer.class).orElse(0);
                        stageNodes.stream()
                                .filter(x -> x.stage.equals(task.getStage()))
                                .findAny()
                                .ifPresent(stageNode -> stageNode.setTotal(total));
                    });
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private static class StageNode {
        private final Context context;
        private final String stage;
        private final String message;
        private int count = 0;
        private int total = 0;
        private boolean started = false;

        private final View parent;
        private final FCLTextView title;
        private final FCLImageView icon;

        public StageNode(Context context, String stage) {
            this.context = context;
            this.stage = stage;

            parent = LayoutInflater.from(context).inflate(R.layout.item_task_stage, null);
            title = parent.findViewById(R.id.title);
            icon = parent.findViewById(R.id.icon);

            int[][] state = {
                    {

                    }
            };
            int[] color = {
                    ThemeEngine.getSystemAutoTint(context)
            };
            icon.setImageTintList(new ColorStateList(state, color));

            message = TaskLabels.resolveStageMessage(context, stage);
            title.setText(message);
            icon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_more_horiz_24));
        }

        public void begin() {
            if (started) return;
            started = true;
            icon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_forward_24));
        }

        public void fail() {
            icon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_close_24));
        }

        public void succeed() {
            icon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_done_24));
        }

        public void count() {
            updateCounter(++count, total);
        }

        public void setTotal(int total) {
            this.total = total;
            updateCounter(count, total);
        }

        @SuppressLint("DefaultLocale")
        public void updateCounter(int count, int total) {
            if (total > 0)
                title.setText(String.format("%s - %d/%d", message, count, total));
            else
                title.setText(message);
        }

        public View getView() {
            return parent;
        }
    }

    private static class ProgressListNode {
        private final View parent;

        private final FCLProgressBar bar;
        private final FCLTextView state;

        private final FlowSubscriptions.Subscription progressSubscription;
        private final FlowSubscriptions.Subscription messageSubscription;

        public ProgressListNode(Context context, boolean padding, Task<?> task) {
            parent = LayoutInflater.from(context).inflate(R.layout.item_task_progress, null);
            if (padding) {
                parent.setPadding(ConvertUtils.dip2px(context, 31), 0, 0, ConvertUtils.dip2px(context, 8));
            }

            bar = parent.findViewById(R.id.progress);
            FCLTextView title = parent.findViewById(R.id.name);
            state = parent.findViewById(R.id.state);

            // 对齐原 bind 语义：先同步当前值，再跟踪后续变化；unbind 时取消订阅
            progressSubscription = FlowSubscriptions.subscribeWithCurrent(task.progressFlow(), bar.percentProgressProperty()::set);
            title.setText(task.getName());
            messageSubscription = FlowSubscriptions.subscribeWithCurrent(task.messageFlow(), state.stringProperty()::set);
        }

        public void unbind() {
            progressSubscription.cancel();
            messageSubscription.cancel();
        }

        public void setThrowable(Throwable throwable) {
            unbind();
            state.setText(throwable.getLocalizedMessage());
            bar.setProgress(0);
        }

        public View getView() {
            return parent;
        }
    }
}