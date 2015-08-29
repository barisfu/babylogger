package com.rorlig.babyapp.ui.fragment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.auth.ForgotBtnClickedEvent;
import com.rorlig.babyapp.otto.auth.LoginSkippedEvent;
import com.rorlig.babyapp.otto.auth.LoginSuccessEvent;
import com.rorlig.babyapp.otto.auth.SignupBtnClickedEvent;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author gaurav gupta
 */
public class LoginFragment extends InjectableFragment {

    @InjectView(R.id.btn_forgot_password)
    Button forgotPasswordBtn;

    @InjectView(R.id.btn_signup)
    Button signupBtn;
    private String TAG = "LoginFragment";


    @InjectView(R.id.skip_message)
    TextView skipMessageTextView;

    @InjectView(R.id.email)
    TextView emailTextView;

    @InjectView(R.id.email_text_input_layout)
    TextInputLayout emailTextInputLayout;

    @InjectView(R.id.password_text_input_layout)
    TextInputLayout passwordTextInputLayout;

    @InjectView(R.id.password)
    TextView passwordTextView;

    private ProgressDialog dialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailTextInputLayout.setErrorEnabled(true);
        passwordTextInputLayout.setErrorEnabled(true);
//        skipMessageTextView.setText(AppUtils.getSpannable(skipMessageTextView.getText().toString()));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_forgot_password)
    public void forgotBtnClicked(){
        Log.d(TAG, "forgotBtnClicked");
        scopedBus.post(new ForgotBtnClickedEvent());
    }

    @OnClick(R.id.btn_signup)
    public void btnSignUpClicked(){
        Log.d(TAG, "btnSignUpClicked");

        scopedBus.post(new SignupBtnClickedEvent());


    }

    @OnClick(R.id.btn_login)
    public void btnLoginClicked(){
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (email.length() == 0) {
            emailTextInputLayout.setError(getString(R.string.no_email));
            return;
        }
        if (!isValidEmail(email)){
            emailTextInputLayout.setError(getString(R.string.not_valid_email));
            return;
        }

        if (password.length()==0) {
            passwordTextInputLayout.setError(getString(R.string.no_password));
            return;
        }


        // Set up a progress dialog
//        dialog = new ProgressDialog(getActivity(), getString(R.string.title_login));
////        dialog.se(getString(R.string.progress_login));
//        dialog.show();


        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
//                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    scopedBus.post(new LoginSuccessEvent());
//                    Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.btn_skip)
    public void btnSkipClicked(){
        scopedBus.post(new LoginSkippedEvent());

    }

    protected void showToast(int id) {
        Toast.makeText(getActivity(), getString(id), Toast.LENGTH_SHORT).show();

    }


}
