package com.example.appbansach.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appbansach.R;


public class DialogUtils {
    public static void showSuccessDialog(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null);

        Button btnSuccess = view.findViewById(R.id.successDone);
        TextView tvMessage = view.findViewById(R.id.successMessage);
        tvMessage.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }

        alertDialog.show();
    }

    public static void showSuccessDialog(Context context, String message, DialogCallBack callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null);

        Button btnSuccess = view.findViewById(R.id.successDone);
        TextView tvMessage = view.findViewById(R.id.successMessage);
        tvMessage.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (callback != null) {
                    callback.onConfirm();
                }
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }

        alertDialog.show();
    }

    public static void showErrorDialog(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_error, null);

        Button btnError = view.findViewById(R.id.errorDone);
        TextView tvMessage = view.findViewById(R.id.errorMessage);
        tvMessage.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }

        alertDialog.show();
    }

    public static void showWarningDialog(Context context, String message, DialogCallBack callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_warning, null);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        TextView tvMessage = view.findViewById(R.id.warningMessage);
        tvMessage.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (callback != null) {
                    callback.onConfirm(); // Gọi hàm khi nhấn "Ok".
                }
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }

        alertDialog.show();
    }



}
