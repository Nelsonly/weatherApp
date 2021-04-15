package com.nelson.weather.view.privacyDialog;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nelson.weather.R;
import com.nelson.weather.WeatherApplication;
import com.nelson.weather.utils.Constant;

public class PrivacyProtocolDialog extends BaseFullScreenDialogFragment {

    private static final String PREF_FILE_ENTER_ACTIVITY = "optimizer_enter_app";
    private static final String PREF_KEY_IS_FIRST_ENTER = "PREF_KEY_IS_FIRST_ENTER";
    private ViewGroup mRootView;
    private PrivacyProtocolDialog _thisDialog = this;

    public static final int DELAY = 1000;
    private static long lastClickTime = 0;

    public PrivacyProtocolDialog() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.dialog_privacy_protocol, container, false);

        TextView privacyContentText = mRootView.findViewById(R.id.privacy_content);

        Button privacyAgree = mRootView.findViewById(R.id.agree_privacy);
        privacyAgree.setOnClickListener(view -> {

//            Intent intent = new Intent(IRGApplication.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent.putExtra(MainActivity.EXTRA_STARTED_FROM_ENTER_ACTIVITY, true);
//
//            startActivity(intent);
            dismissAllowingStateLoss();
        });

        Button privacyRefuse = mRootView.findViewById(R.id.refuse_privacy);
        privacyRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mOnDismissOrCancelListener.onDismiss(null);
                System.exit(0);
            }
        });

        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(getResources().getString(R.string.privacy_dialog_content));
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")),44,54,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime > DELAY) {
                    lastClickTime = currentTime;
                    jumpToPolicyDetail();
                }
            }
        }, 54, 60, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new UnderlineSpan(), 54, 60, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#3885f5")), 54, 60, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 60, 61, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        style.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime > DELAY) {
                    lastClickTime = currentTime;
                    jumpToPrivacyDetail();
                }
            }
        }, 61, 67, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new UnderlineSpan(), 61, 67, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#3885f5")), 61, 67, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 67, 85, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        privacyContentText.setText(style);
        privacyContentText.setMovementMethod(LinkMovementMethod.getInstance());

        return mRootView;
    }

    private void jumpToPrivacyDetail() {
        String defaultUrl = "https://cdn.irigel.com/durian/Durian_privacy.html";
        String privacyUrl = null;
//        if (TextUtils.equals(ChannelInfoUtil.getCurrentApkStore(WeatherApplication.getContext()), "HW")) {
//            privacyUrl = IRGConfig.optString(defaultUrl, "Application", "ChannelInfo", "Store", "HW", "PrivacyURL");
//        } else {
//            privacyUrl = IRGConfig.optString(defaultUrl, "Application", "Modules", "PrivacyURL");
//        }
        if (TextUtils.isEmpty(privacyUrl)) {
            privacyUrl = defaultUrl;
        }
    }

    private void jumpToPolicyDetail() {
        String defaultUrl = "https://cdn.irigel.com/durian/Durian_TermsofService.html";
        String policyUrl = null;
//        if (TextUtils.equals(ChannelInfoUtil.getCurrentApkStore(WeatherApplication.getContext()), "HW")) {
//            policyUrl = IRGConfig.optString(defaultUrl, "Application", "ChannelInfo", "Store", "HW", "ServiceURL");
//        } else {
//            policyUrl = IRGConfig.optString(defaultUrl, "Application", "Modules", "ServiceURL");
//        }

    }
}
