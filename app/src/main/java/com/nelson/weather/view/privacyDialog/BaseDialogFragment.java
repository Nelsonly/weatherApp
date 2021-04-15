package com.nelson.weather.view.privacyDialog;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;

public class BaseDialogFragment extends DialogFragment {

    public void setOnDismissOrCancelListener(OnDismissOrCancelListener onDismissOrCancelListener) {
        this.mOnDismissOrCancelListener = onDismissOrCancelListener;
    }

    public OnDismissOrCancelListener getOnDismissOrCancelListener() {
        return mOnDismissOrCancelListener;
    }

    protected OnDismissOrCancelListener mOnDismissOrCancelListener;

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.add(this, tag);
        return transaction.commitAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissOrCancelListener != null) {
            mOnDismissOrCancelListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnDismissOrCancelListener != null) {
            mOnDismissOrCancelListener.onCancel(dialog);
        }
    }

    public interface OnDismissOrCancelListener {
        /**
         * This method will be invoked when the dialog is dismissed.
         *
         * @param dialog the dialog that was dismissed will be passed into the
         *               method
         */
        void onDismiss(DialogInterface dialog);
        /**
         * This method will be invoked when the dialog is canceled.
         *
         * @param dialog the dialog that was canceled will be passed into the
         *               method
         */
        void onCancel(DialogInterface dialog);
    }
}
