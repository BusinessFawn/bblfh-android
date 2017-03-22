package a4tay.xyz.brokebandslookingforhome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import a4tay.xyz.brokebandslookingforhome.Util.LoaderManagers.LoginCreate;
import a4tay.xyz.brokebandslookingforhome.Util.LoaderManagers.LoginStatus;

import static a4tay.xyz.brokebandslookingforhome.EventList.loggedIn;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by johnkonderla on 3/22/17.
 */

public class LoginFrag extends Fragment {

    private EditText email;
    private EditText password;
    private Button submitLogin;
    private static final String MY_PREFS = "harbor-preferences";
    private static final String NAME_KEY = "nameKey";
    private static final String PASS_KEY = "passKey";
    private String submittedEM;
    private String submittedPW1;
    private JSONObject userInfo;
    private static final String LOG_TAG = LoginFrag.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.login_activity, container, false);

        email = (EditText) rootView.findViewById(R.id.et_login_email);
        password = (EditText) rootView.findViewById(R.id.et_login_password);

        submitLogin = (Button) rootView.findViewById(R.id.bt_submit_login);


        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submittedEM = getText(email);
                submittedPW1 = getText(password);
                String paramEM = "fanEmail";
                String paramPW = "fanPass";
                if (!submittedPW1.equals("") && !submittedEM.equals("")) {
                    userInfo = new JSONObject();
                    try {
                        userInfo.put(paramEM, submittedEM);
                        userInfo.put(paramPW, submittedPW1);

                        String loginUrl = "http://dev.4tay.xyz:4567/fanLogin/";

                        loginUrl = loginUrl + submittedEM + "/" + submittedPW1;

                        //url = QueryUtils.newURL(userInfo,url);
                        new LoginStatus(getActivity()).execute(new String[]{loginUrl});


                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(NAME_KEY, submittedEM);
                        editor.putString(PASS_KEY, submittedPW1);

                        // Commit the edits!
                        editor.apply();

                        Intent intent = new Intent();
                        intent.setClass(getContext(), TabActivity.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        System.out.println(e.getMessage());
                    }

                    //getLoaderManager().initLoader(3, null, this).forceLoad();
                } else if (submittedEM.equals("")) {
                    email.setError("Name is required");
                } else {
                    email.setError("Email is required");
                }
            }
        });

        return rootView;
    }

    private String getText(EditText getFromIt) {

        if(getFromIt != null) {


            return getFromIt.getText().toString().trim();

        }
        return "";
    }
}