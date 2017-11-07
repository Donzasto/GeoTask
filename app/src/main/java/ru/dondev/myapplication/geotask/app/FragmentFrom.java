package ru.dondev.myapplication.geotask.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentFrom extends Fragment {
    //Интерфейс для передачи события в активити
    public interface EventListener{
        public void event(DownloadJSON s);
    }

    EventListener eventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            eventListener = (EventListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement EventListener");
        }

    }

    private EditText etFrom;
    private String sFrom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_input, null);
        etFrom = (EditText) v.findViewById(R.id.etInput);
        etFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = etFrom.getText().toString();
                Log.d("myLogs", "onTextChanged " + str);
                DownloadJSON downloadJSON = new DownloadJSON(str);

                eventListener.event(downloadJSON);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                sFrom = etFrom.getText().toString();
            }
        });
        return v;
    }

    public String getsFrom() {
        return sFrom;
    }
}
