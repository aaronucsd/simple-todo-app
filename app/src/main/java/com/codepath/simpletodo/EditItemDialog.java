package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by long on 2/1/15.
 * Author: Long A. Huynh
 */
    public class EditItemDialog extends DialogFragment implements TextView.OnEditorActionListener {

        private EditText mEditText;
        private String textValue;

        public interface EditNameDialogListener {
            void onFinishEditDialog(String inputText);
        }

        public EditItemDialog() {
            // Empty constructor required for DialogFragment
        }

        public static EditItemDialog newInstance(String title) {
            EditItemDialog frag = new EditItemDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }

        public void setTextValue(String textValue){
            this.textValue = textValue;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_edit_item, container);


            mEditText = (EditText) view.findViewById(R.id.editText);
            String title = getArguments().getString("title", "Enter Name");
            getDialog().setTitle(title);
            //get the text from the input
            mEditText.setText(this.textValue);

            // Show soft keyboard automatically
            mEditText.requestFocus();
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            mEditText.setOnEditorActionListener(this);

            return view;
        }


        // Fires whenever the textfield has an action performed
        // In this case, when the "Done" button is pressed
        // REQUIRES a 'soft keyboard' (virtual keyboard)
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                // Return input text to activity
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog(mEditText.getText().toString());
                dismiss();
                return true;
            }
            return false;
        }

    }