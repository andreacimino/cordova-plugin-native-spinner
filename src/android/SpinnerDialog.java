// cordova-plugin-native-spinner
package com.greybax.spinnerdialog;

import java.util.Stack;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.InsetDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

public class SpinnerDialog extends CordovaPlugin {

  public Stack<ProgressDialog> spinnerDialogStack = new Stack<ProgressDialog>();

  public SpinnerDialog() {
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (action.equals("show")) {

      final String title = "null".equals(args.getString(0)) ? null : args.getString(0);
      final String message = "null".equals(args.getString(1)) ? null : args.getString(1);
      final String inner_message = "null".equals(args.getString(2)) ? null : args.getString(2);
      final boolean isFixed = args.getBoolean(3);

      final CordovaInterface cordova = this.cordova;
      Runnable runnable = new Runnable() {
        public void run() {

          DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
              if (!isFixed) {
                while (!SpinnerDialog.this.spinnerDialogStack.empty()) {
                  SpinnerDialog.this.spinnerDialogStack.pop().dismiss();
                  callbackContext.success();
                }
              }
            }
          };

          ProgressDialog dialog;
          if (isFixed) {
            //If there is a progressDialog yet change the text
            if (!SpinnerDialog.this.spinnerDialogStack.empty()) {
              dialog = SpinnerDialog.this.spinnerDialogStack.peek();
              if (title != null) {
                dialog.setTitle(title);
              }
              if (message!=null) {
                dialog.setMessage(message);
              }
            }
            else{
              dialog = CallbackProgressDialog.show(cordova.getActivity(), title, message, true, false, null, callbackContext);
              SpinnerDialog.this.spinnerDialogStack.push(dialog);
            }
          } else {
            //If there is a progressDialog yet change the text
            if (!SpinnerDialog.this.spinnerDialogStack.empty()) {
              dialog = SpinnerDialog.this.spinnerDialogStack.peek();
              if (title != null) {
                dialog.setTitle(title);
              }
              if (message!=null) {
                dialog.setMessage(message);
              }
            }
            else{
              dialog = ProgressDialog.show(cordova.getActivity(), title, message, true, true, onCancelListener);
              SpinnerDialog.this.spinnerDialogStack.push(dialog);
            }
          }

          if (title == null && message == null) {

              ProgressBar  pb = new ProgressBar(cordova.getActivity());
            //  pb.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
          //  pb.setIndeterminateTintMode();
            pb.getIndeterminateDrawable().setColorFilter(
                    0xFFFFFFFF,
                    android.graphics.PorterDuff.Mode.SRC_IN);
            //  pb.setProgressTintList(ColorStateList.valueOf(Color.BLACK));
              LinearLayout   horizontalLayout=new LinearLayout(cordova.getActivity());
              horizontalLayout.setOrientation(LinearLayout.VERTICAL);
              horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);
              horizontalLayout.addView(pb);
              TextView  theButton= new TextView(cordova.getActivity());
              theButton.setText(inner_message);
              theButton.setGravity(Gravity.CENTER);
              horizontalLayout.addView(theButton);

              dialog.setContentView(horizontalLayout);
              horizontalLayout.setClipToOutline(true);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
              DisplayMetrics metrics = new DisplayMetrics();
              cordova.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
              //int width = display.getWidth();  // deprecated
              //int height = display.getHeight();  // deprecated
         //   lp.height = 240;
          //  lp.width = 500;
              lp.height  = (int) Math.round(metrics.heightPixels/7.5);
              lp.width = (int) Math.round(lp.height*2.5);
              //  dialog.setBa
             dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setDimAmount(0.0f);
            //dialog.getWindow().setClipToOutline(true);
            ColorDrawable colordrawable = new ColorDrawable(Color.BLACK);
            colordrawable.setAlpha(100);
           // DialogUtils.setMargins( dialog, 0, 150, 50, 75 );
        //    InsetDrawable inset = new InsetDrawable(colordrawable, 50);
            dialog.getWindow().setBackgroundDrawable(colordrawable);
          }
        }
      };
      this.cordova.getActivity().runOnUiThread(runnable);

    } else if (action.equals("hide")) {
      Runnable runnable = new Runnable() {
        public void run() {
          if (!SpinnerDialog.this.spinnerDialogStack.empty()) {
            SpinnerDialog.this.spinnerDialogStack.pop().dismiss();
          }
        }
      };
      this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    return true;
  }
}
