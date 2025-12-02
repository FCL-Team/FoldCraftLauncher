package com.tungsten.fcl.control;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.card.MaterialCardView;
import com.tungsten.fcl.R;
import com.tungsten.fcl.terracotta.Terracotta;
import com.tungsten.fcl.terracotta.TerracottaState;
import com.tungsten.fcl.terracotta.profile.TerracottaProfile;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import net.burningtnt.terracotta.TerracottaAndroidAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class MultiplayerDialog extends FCLDialog implements View.OnClickListener {

    private final Activity activity;

    private final RelativeLayout stateUIContainer;
    private final FCLProgressBar progressBar;
    private final FCLButton negative;

    private final ArrayList<StateBindingUI> allUI;

    public MultiplayerDialog(@NonNull Context context, Activity activity, int width, int height) {
        super(context);
        this.activity = activity;
        setCancelable(false);
        Objects.requireNonNull(getWindow()).setLayout(width, height);
        setContentView(R.layout.dialog_multiplyer_menu);

        stateUIContainer = findViewById(R.id.state_ui_container);
        progressBar = findViewById(R.id.loading);
        FCLTextView metadataText = findViewById(R.id.metadata_text);
        negative = findViewById(R.id.cancel);
        Objects.requireNonNull(progressBar).setVisibility(View.VISIBLE);
        Objects.requireNonNull(negative).setOnClickListener(this);

        WaitingUI waitingUI = new WaitingUI(context, this, R.layout.view_multiplayer_waiting);
        HostScanningUI hostScanningUI = new HostScanningUI(context, this, R.layout.view_multiplayer_scanning);
        HostStartingUI hostStartingUI = new HostStartingUI(context, this, R.layout.view_multiplayer_starting);
        HostOkUI hostOkUI = new HostOkUI(context, this, R.layout.view_multiplayer_host_guest_ok);
        GuestStartingUI guestStartingUI = new GuestStartingUI(context, this, R.layout.view_multiplayer_starting);
        GuestOkUI guestOkUI = new GuestOkUI(context, this, R.layout.view_multiplayer_host_guest_ok);
        ExceptionUI exceptionUI = new ExceptionUI(context, this, R.layout.view_multiplayer_exception);

        allUI = new ArrayList<>();
        allUI.add(waitingUI);
        allUI.add(hostScanningUI);
        allUI.add(hostStartingUI);
        allUI.add(hostOkUI);
        allUI.add(guestStartingUI);
        allUI.add(guestOkUI);
        allUI.add(exceptionUI);

        Terracotta.initialize(activity);
        Terracotta.setWaiting(context, true);

        Objects.requireNonNull(metadataText).setText(String.format(getContext().getString(R.string.terracotta_metadata), Terracotta.getMetadata().getTerracottaVersion(), Terracotta.getMetadata().getEasyTierVersion()));

        Terracotta.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof TerracottaState.HostOK) {
                if (newValue.isForkOf(oldValue)) {
                    hostOkUI.refresh();
                    return;
                }
            }
            if (newValue instanceof TerracottaState.GuestOK) {
                if (newValue.isForkOf(oldValue)) {
                    guestOkUI.refresh();
                    return;
                }
            }
            switchUI(newValue);
        });
    }

    @Override
    public void onClick(View v) {
        if (v == negative) {
            dismiss();
        }
    }

    private void switchUI(TerracottaState.Ready state) {
        String stateString = state.toString();
        stateUIContainer.removeAllViews();
        for (StateBindingUI ui : allUI) {
            if (ui.getBindingState().equals(stateString)) {
                stateUIContainer.addView(ui.getLayout(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ui.show();
                break;
            }
        }
        Objects.requireNonNull(progressBar).setVisibility(View.GONE);
    }

    private abstract static class StateBindingUI {

        private final Context context;
        private final MultiplayerDialog parent;
        private final View layout;

        public StateBindingUI(Context context, MultiplayerDialog parent, int resId) {
            this.context = context;
            this.parent = parent;
            this.layout = LayoutInflater.from(context).inflate(resId, null);
        }

        public Context getContext() {
            return context;
        }

        public MultiplayerDialog getParent() {
            return parent;
        }

        public View getLayout() {
            return layout;
        }

        @NonNull
        public final <T extends View> T findViewById(int id) {
            return layout.findViewById(id);
        }

        public abstract void show();

        public abstract String getBindingState();
    }

    private static final class WaitingUI extends StateBindingUI {

        private final InviteCodeInputDialog inviteCodeInputDialog;

        private final MaterialCardView hostCard;
        private final MaterialCardView guestCard;

        public WaitingUI(Context context, MultiplayerDialog parent, int resId) {
            super(context, parent, resId);
            hostCard = findViewById(R.id.card_host);
            guestCard = findViewById(R.id.card_guest);

            String player = getParent().activity.getIntent().getStringExtra("TERRACOTTA_PLAYER");
            if (player == null)
                player = getContext().getString(R.string.terracotta_player_anonymous);
            final String finalPlayer = player;

            inviteCodeInputDialog = new InviteCodeInputDialog(getContext(), code -> {
                try {
                    boolean success = Terracotta.setGuesting(code, finalPlayer);
                    if (!success) {
                        Toast.makeText(getContext(), getContext().getString(R.string.terracotta_status_waiting_guest_prompt_invalid), Toast.LENGTH_SHORT).show();
                    } else {
                        guestCard.setEnabled(false);
                        Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Logging.LOG.log(Level.SEVERE, e.getMessage());
                    guestCard.setEnabled(true);
                    Objects.requireNonNull(getParent().progressBar).setVisibility(View.GONE);
                }
            });

            hostCard.setOnClickListener(v -> {
                try {
                    Terracotta.setScanning(null, finalPlayer);
                    hostCard.setEnabled(false);
                    Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Logging.LOG.log(Level.SEVERE, e.getMessage());
                    hostCard.setEnabled(true);
                    Objects.requireNonNull(getParent().progressBar).setVisibility(View.GONE);
                }
            });
            guestCard.setOnClickListener(v -> inviteCodeInputDialog.show());
        }

        @Override
        public void show() {
            hostCard.setEnabled(true);
            guestCard.setEnabled(true);
        }

        @Override
        public String getBindingState() {
            return "waiting";
        }

        private static class InviteCodeInputDialog extends FCLDialog {

            public interface Listener {
                void onPositive(String code);
            }

            private final FCLEditText editCode;
            private final FCLTextView validation;

            public InviteCodeInputDialog(Context context, Listener listener) {
                super(context);
                setCancelable(false);
                setContentView(R.layout.dialog_input_invite_code);

                editCode = findViewById(R.id.code);
                validation = findViewById(R.id.validation);
                Objects.requireNonNull(editCode).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Ignore
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        refreshTextBox();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        refreshTextBox();
                    }
                });
                Objects.requireNonNull(validation).setVisibility(View.GONE);

                FCLButton positive = findViewById(R.id.positive);
                Objects.requireNonNull(positive).setOnClickListener(v -> {
                    if (Terracotta.parseRoomCode(editCode.getText().toString()) != null) {
                        listener.onPositive(editCode.getText().toString());
                        InviteCodeInputDialog.this.dismiss();
                    } else
                        Toast.makeText(getContext(), getContext().getString(R.string.terracotta_status_waiting_guest_prompt_invalid), Toast.LENGTH_SHORT).show();
                });
                FCLButton negative = findViewById(R.id.negative);
                Objects.requireNonNull(negative).setOnClickListener(v -> dismiss());
            }

            @Override
            public void show() {
                super.show();
                editCode.setText("");
            }

            private void refreshTextBox() {
                TerracottaAndroidAPI.RoomType type = Terracotta.parseRoomCode(editCode.getText().toString());
                if (editCode.getText().toString().isEmpty()) {
                    Objects.requireNonNull(validation).setVisibility(View.GONE);
                } else if (type == TerracottaAndroidAPI.RoomType.TERRACOTTA_LEGACY) {
                    Objects.requireNonNull(validation).setVisibility(View.VISIBLE);
                    Objects.requireNonNull(validation).setText(getContext().getString(R.string.terracotta_status_waiting_guest_prompt_terracotta_legacy));
                    Objects.requireNonNull(validation).setTextColor(Color.YELLOW);
                } else if (type == TerracottaAndroidAPI.RoomType.PCL2CE) {
                    Objects.requireNonNull(validation).setVisibility(View.VISIBLE);
                    Objects.requireNonNull(validation).setText(getContext().getString(R.string.terracotta_status_waiting_guest_prompt_pcl2ce));
                    Objects.requireNonNull(validation).setTextColor(Color.YELLOW);
                } else if (type == TerracottaAndroidAPI.RoomType.SCAFFOLDING) {
                    Objects.requireNonNull(validation).setVisibility(View.VISIBLE);
                    Objects.requireNonNull(validation).setText(getContext().getString(R.string.terracotta_status_waiting_guest_prompt_scaffolding));
                    Objects.requireNonNull(validation).setTextColor(Color.GREEN);
                } else {
                    Objects.requireNonNull(validation).setVisibility(View.VISIBLE);
                    Objects.requireNonNull(validation).setText(getContext().getString(R.string.terracotta_status_waiting_guest_prompt_invalid));
                    Objects.requireNonNull(validation).setTextColor(Color.RED);
                }
            }
        }
    }

    private static final class HostScanningUI extends StateBindingUI {

        private final LinearLayoutCompat scanningBack;

        public HostScanningUI(Context context, MultiplayerDialog parent, int resId) {
            super(context, parent, resId);
            scanningBack = findViewById(R.id.scanning_back);
            scanningBack.setOnClickListener(v -> {
                Terracotta.setWaiting(getContext(), true);
                scanningBack.setEnabled(false);
                Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
            });
            findViewById(R.id.back_text_sub).setSelected(true);
        }

        @Override
        public void show() {
            scanningBack.setEnabled(true);
        }

        @Override
        public String getBindingState() {
            return "host_scanning";
        }
    }

    private static final class HostStartingUI extends StateBindingUI {

        private final LinearLayoutCompat startingBack;

        public HostStartingUI(Context context, MultiplayerDialog parent, int resId) {
            super(context, parent, resId);
            startingBack = findViewById(R.id.starting_back);
            startingBack.setOnClickListener(v -> {
                Terracotta.setWaiting(getContext(), true);
                startingBack.setEnabled(false);
                Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
            });
            ((FCLTextView) findViewById(R.id.starting_text)).setText(getContext().getString(R.string.terracotta_status_host_starting));
            ((FCLTextView) findViewById(R.id.exit_text)).setText(getContext().getString(R.string.terracotta_status_host_starting_back));
            findViewById(R.id.exit_text).setSelected(true);
        }

        @Override
        public void show() {
            startingBack.setEnabled(true);
        }

        @Override
        public String getBindingState() {
            return "host_starting";
        }
    }

    private static final class HostOkUI extends StateBindingUI {

        private final ListView listView;
        private final LinearLayoutCompat copy;
        private final LinearLayoutCompat back;

        private MultiPlayerProfileAdapter adapter;

        public HostOkUI(Context context, MultiplayerDialog parent, int resId) {
            super(context, parent, resId);
            listView = findViewById(R.id.profile_list);
            copy = findViewById(R.id.ok_copy);
            copy.setOnClickListener(v -> {
                if (Terracotta.stateProperty().get() instanceof TerracottaState.HostOK)
                    copyInviteCode(((TerracottaState.HostOK) Terracotta.stateProperty().get()).getCode());
            });
            back = findViewById(R.id.ok_back);
            back.setOnClickListener(v -> {
                Terracotta.setWaiting(getContext(), true);
                copy.setEnabled(false);
                back.setEnabled(false);
                Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
            });
            ((FCLTextView) findViewById(R.id.info_text)).stringProperty().bind(Bindings.createStringBinding(() -> {
                if (Terracotta.stateProperty().get() instanceof TerracottaState.HostOK)
                    return ((TerracottaState.HostOK) Terracotta.stateProperty().get()).getCode();
                return "";
            }, Terracotta.stateProperty()));
            ((FCLTextView) findViewById(R.id.ok_text)).setText(getContext().getString(R.string.terracotta_status_host_ok));
            ((FCLTextView) findViewById(R.id.info_title)).setText(getContext().getString(R.string.terracotta_status_host_ok_code));
            ((FCLTextView) findViewById(R.id.copy_text)).setText(getContext().getString(R.string.terracotta_status_host_ok_code_copy));
            ((FCLTextView) findViewById(R.id.copy_text_sub)).setText(getContext().getString(R.string.terracotta_status_host_ok_code_desc));
            ((FCLTextView) findViewById(R.id.back_text_sub)).setText(getContext().getString(R.string.terracotta_status_host_ok_back));
            findViewById(R.id.info_title).setSelected(true);
            findViewById(R.id.copy_text_sub).setSelected(true);
            findViewById(R.id.back_text_sub).setSelected(true);
        }

        @Override
        public void show() {
            refresh();
            copy.setEnabled(true);
            back.setEnabled(true);
            if (Terracotta.stateProperty().get() instanceof TerracottaState.HostOK)
                copyInviteCode(((TerracottaState.HostOK) Terracotta.stateProperty().get()).getCode());
        }

        @Override
        public String getBindingState() {
            return "host_ok";
        }

        public void refresh() {
            if (Terracotta.stateProperty().get() instanceof TerracottaState.HostOK) {
                TerracottaState.HostOK hostOK = (TerracottaState.HostOK) Terracotta.stateProperty().get();
                List<TerracottaProfile> profileList = hostOK.getProfiles();
                if (adapter == null) {
                    adapter = new MultiPlayerProfileAdapter(getContext(), profileList);
                    listView.setAdapter(adapter);
                } else
                    adapter.refreshList(profileList);
            }
        }

        private void copyInviteCode(String code) {
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("invite_code", code));
            Toast.makeText(getContext(), getContext().getString(R.string.terracotta_status_host_ok_code_copy_toast), Toast.LENGTH_SHORT).show();
        }
    }

    private static final class GuestStartingUI extends StateBindingUI {

        private final LinearLayoutCompat startingBack;

        public GuestStartingUI(Context context, MultiplayerDialog parent, int resId) {
            super(context, parent, resId);
            startingBack = findViewById(R.id.starting_back);
            startingBack.setOnClickListener(v -> {
                Terracotta.setWaiting(getContext(), true);
                startingBack.setEnabled(false);
                Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
            });
            ((FCLTextView) findViewById(R.id.starting_text)).setText(getContext().getString(R.string.terracotta_status_guest_starting));
            ((FCLTextView) findViewById(R.id.exit_text)).setText(getContext().getString(R.string.terracotta_status_guest_starting_back));
            findViewById(R.id.exit_text).setSelected(true);
        }

        @Override
        public void show() {
            startingBack.setEnabled(true);
        }

        @Override
        public String getBindingState() {
            return "guest_starting";
        }
    }

    private static final class GuestOkUI extends StateBindingUI {

        private final ListView listView;
        private final LinearLayoutCompat copy;
        private final LinearLayoutCompat back;

        private MultiPlayerProfileAdapter adapter;

        public GuestOkUI(Context context, MultiplayerDialog parent, int resId) {
            super(context, parent, resId);
            listView = findViewById(R.id.profile_list);
            copy = findViewById(R.id.ok_copy);
            copy.setOnClickListener(v -> {
                if (Terracotta.stateProperty().get() instanceof TerracottaState.GuestOK)
                    copyServerAddress(((TerracottaState.GuestOK) Terracotta.stateProperty().get()).getUrl());
            });
            back = findViewById(R.id.ok_back);
            back.setOnClickListener(v -> {
                Terracotta.setWaiting(getContext(), true);
                copy.setEnabled(false);
                back.setEnabled(false);
                Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
            });
            ((FCLTextView) findViewById(R.id.info_text)).stringProperty().bind(Bindings.createStringBinding(() -> {
                if (Terracotta.stateProperty().get() instanceof TerracottaState.GuestOK)
                    return ((TerracottaState.GuestOK) Terracotta.stateProperty().get()).getUrl();
                return "";
            }, Terracotta.stateProperty()));
            ((FCLTextView) findViewById(R.id.ok_text)).setText(getContext().getString(R.string.terracotta_status_guest_ok));
            ((FCLTextView) findViewById(R.id.info_title)).setText(getContext().getString(R.string.terracotta_status_guest_ok_address));
            ((FCLTextView) findViewById(R.id.copy_text)).setText(getContext().getString(R.string.terracotta_status_guest_ok_address_copy));
            ((FCLTextView) findViewById(R.id.copy_text_sub)).setText(getContext().getString(R.string.terracotta_status_guest_ok_address_desc));
            ((FCLTextView) findViewById(R.id.back_text_sub)).setText(getContext().getString(R.string.terracotta_status_guest_ok_back));
            findViewById(R.id.info_title).setSelected(true);
            findViewById(R.id.copy_text_sub).setSelected(true);
            findViewById(R.id.back_text_sub).setSelected(true);
        }

        @Override
        public void show() {
            refresh();
            copy.setEnabled(true);
            back.setEnabled(true);
        }

        @Override
        public String getBindingState() {
            return "guest_ok";
        }

        public void refresh() {
            if (Terracotta.stateProperty().get() instanceof TerracottaState.GuestOK) {
                TerracottaState.GuestOK guestOK = (TerracottaState.GuestOK) Terracotta.stateProperty().get();
                List<TerracottaProfile> profileList = guestOK.getProfiles();
                if (adapter == null) {
                    adapter = new MultiPlayerProfileAdapter(getContext(), profileList);
                    listView.setAdapter(adapter);
                } else
                    adapter.refreshList(profileList);
            }
        }

        private void copyServerAddress(String address) {
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("server_address", address));
            Toast.makeText(getContext(), getContext().getString(R.string.terracotta_status_guest_ok_address_copy_toast), Toast.LENGTH_SHORT).show();
        }
    }

    private static final class ExceptionUI extends StateBindingUI {

        private final LinearLayoutCompat exit;
        private final LinearLayoutCompat export;

        public ExceptionUI(Context context, MultiplayerDialog parent, int resId) {
            super(context, parent, resId);
            exit = findViewById(R.id.exception_back);
            export = findViewById(R.id.exception_export);
            exit.setOnClickListener(v -> {
                Terracotta.setWaiting(getContext(), true);
                exit.setEnabled(false);
                export.setEnabled(false);
                Objects.requireNonNull(getParent().progressBar).setVisibility(View.VISIBLE);
            });
            export.setOnClickListener(v -> {
                String logString = Terracotta.collectLogs();
                try {
                    FileUtils.writeText(new File(FCLPath.LOG_DIR, "terracotta.log"), logString);
                    Toast.makeText(getContext(), getContext().getString(R.string.terracotta_export_log_done), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getContext(), getContext().getString(R.string.terracotta_export_log_failed), Toast.LENGTH_SHORT).show();
                }
            });
            ((FCLTextView) findViewById(R.id.exception_text)).stringProperty().bind(Bindings.createStringBinding(() -> Terracotta.stateProperty().get() instanceof TerracottaState.Exception ? Terracotta.parseException(getContext(), (TerracottaState.Exception) Terracotta.stateProperty().get()) : "Unknown Error", Terracotta.stateProperty()));
            findViewById(R.id.export_text_sub).setSelected(true);
            findViewById(R.id.back_text_sub).setSelected(true);
        }

        @Override
        public void show() {
            exit.setEnabled(true);
            export.setEnabled(true);
        }

        @Override
        public String getBindingState() {
            return "exception";
        }
    }
}
