package ru.dondev.myapplication.geotask.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, FragmentFrom.EventListener{

    private FragmentFrom fFrom;
    private FragmentTo fTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fFrom = new FragmentFrom();
        fTo = new FragmentTo();
        // Создание табов
        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = bar.newTab();
        tab.setText("From");
        tab.setTabListener(this);
        bar.addTab(tab);

        tab = bar.newTab();
        tab.setText("To");
        tab.setTabListener(this);
        bar.addTab(tab);

//        String[] from = new String[] {"description"};
//        int[] to = new int[]{R.id.tvText};
//        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, place, R.layout.item, from, to);
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("from", fFrom.getsFrom());
        intent.putExtra("to", fTo.getsTo());
        startActivity(intent);
    }

    @Override
    public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // Смена фрагментов
        if (tab.getPosition() == 0) {
            ft.replace(R.id.fragment, fFrom);
        }else{
            ft.replace(R.id.fragment, fTo);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void event(DownloadJSON s) {
    //   передача объекта из фрамента в фрагментлист
        String input = "";
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment);
        Fragment fragL = getSupportFragmentManager().findFragmentById(R.id.fragmentList);
        input = ((EditText)frag.getView().findViewById(R.id.etInput)).getText().toString();
        ((TextView)fragL.getView().findViewById(R.id.tv)).setText(input);

    }
}
