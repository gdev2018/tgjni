/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.LocaleController;
///*import com.github.gdev2018.master.MessagesController;*/
import com.github.gdev2018.master.R;
import com.github.gdev2018.master.ui.ActionBar.ActionBar;
import com.github.gdev2018.master.ui.ActionBar.ActionBarMenu;
import com.github.gdev2018.master.ui.ActionBar.BaseFragment;
import com.github.gdev2018.master.ui.ActionBar.Theme;
import com.github.gdev2018.master.ui.ActionBar.ThemeDescription;
import com.github.gdev2018.master.ui.Components.EditTextBoldCursor;
import com.github.gdev2018.master.ui.Components.LayoutHelper;

import java.util.ArrayList;

public class ReportOtherActivity extends BaseFragment {

    private EditTextBoldCursor firstNameField;
    private View headerLabelView;
    private long dialog_id;
    private int message_id;
    private View doneButton;

    private final static int done_button = 1;

    public ReportOtherActivity(Bundle args) {
        super(args);
        dialog_id = getArguments().getLong("dialog_id", 0);
        message_id = getArguments().getInt("message_id", 0);
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("ReportChat", R.string.ReportChat));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == done_button) {
                    if (firstNameField.getText().length() != 0) {
///*                        TLObject req;
//                        TLRPC.InputPeer peer = MessagesController.getInstance(BaseUserConfig.selectedAccount).getInputPeer((int) dialog_id);
//                        if (message_id != 0) {
//                            TLRPC.TL_messages_report request = new TLRPC.TL_messages_report();
//                            request.peer = peer;
//                            request.id.add(message_id);
//                            request.reason = new TLRPC.TL_inputReportReasonOther();
//                            request.reason.text = firstNameField.getText().toString();
//                            req = request;
//                        } else {
//                            TLRPC.TL_account_reportPeer request = new TLRPC.TL_account_reportPeer();
//                            request.peer = MessagesController.getInstance(currentAccount).getInputPeer((int) dialog_id);
//                            request.reason = new TLRPC.TL_inputReportReasonOther();
//                            request.reason.text = firstNameField.getText().toString();
//                            req = request;
//                        }
//                        ConnectionsManager.getInstance(currentAccount).sendRequest(req, new RequestDelegate() {
//                            @Override
//                            public void run(TLObject response, TLRPC.TL_error error) {
//
//                            }
//                        });*/
                        if (getParentActivity() != null) {
                            Toast.makeText(getParentActivity(), LocaleController.getString("ReportChatSent", R.string.ReportChatSent), Toast.LENGTH_SHORT).show();
                        }
                        finishFragment();
                    }
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        doneButton = menu.addItemWithWidth(done_button, R.drawable.ic_done, AndroidUtilities.dp(56));

        LinearLayout linearLayout = new LinearLayout(context);
        fragmentView = linearLayout;
        fragmentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((LinearLayout) fragmentView).setOrientation(LinearLayout.VERTICAL);
        fragmentView.setOnTouchListener((v, event) -> true);

        firstNameField = new EditTextBoldCursor(context);
        firstNameField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        firstNameField.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        firstNameField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        firstNameField.setMaxLines(3);
        firstNameField.setPadding(0, 0, 0, 0);
        firstNameField.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        firstNameField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        firstNameField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        firstNameField.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        firstNameField.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        firstNameField.setCursorSize(AndroidUtilities.dp(20));
        firstNameField.setCursorWidth(1.5f);
        firstNameField.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE && doneButton != null) {
                doneButton.performClick();
                return true;
            }
            return false;
        });

        linearLayout.addView(firstNameField, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 36, 24, 24, 24, 0));
        firstNameField.setHint(LocaleController.getString("ReportChatDescription", R.string.ReportChatDescription));
        firstNameField.setSelection(firstNameField.length());

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
///*        SharedPreferences preferences = BaseApplication.getGlobalMainSettings();
//        boolean animations = preferences.getBoolean("view_animations", true);
//        if (!animations) {
//            firstNameField.requestFocus();
//            AndroidUtilities.showKeyboard(firstNameField);
//        }*/
    }

    @Override
    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen) {
            AndroidUtilities.runOnUIThread(() -> {
                if (firstNameField != null) {
                    firstNameField.requestFocus();
                    AndroidUtilities.showKeyboard(firstNameField);
                }
            }, 100);
        }
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();

        themeDescriptions.add(new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));

        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));

        themeDescriptions.add(new ThemeDescription(firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        themeDescriptions.add(new ThemeDescription(firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        themeDescriptions.add(new ThemeDescription(firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));

        return themeDescriptions;
    }
}
