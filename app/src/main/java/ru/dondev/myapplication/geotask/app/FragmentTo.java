package ru.dondev.myapplication.geotask.app;

/**
 * Created by artem on 24.06.14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentTo extends Fragment {

    private EditText etTo;
    private String sTo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_input, null);
        etTo = (EditText) v.findViewById(R.id.etInput);
        etTo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                sTo = etTo.getText().toString();
            }
        });
        return v;
    }

    public String getsTo() {
        return sTo;
    }
}
