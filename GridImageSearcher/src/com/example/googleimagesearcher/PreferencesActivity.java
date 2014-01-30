package com.example.googleimagesearcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googleimagesearcher.util.Preferences;

public class PreferencesActivity extends Activity {

    Spinner imageSizeSpinner;
    Spinner imageColorSpinner;
    Spinner imageTypeSpinner;
    EditText etSearchSite;
    Button btnSave;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        setViews();
        addListeners();
        
        btnSave.setEnabled(false);
    }

    private void setViews() {
        imageSizeSpinner = (Spinner) findViewById(R.id.spImageSize);
        imageColorSpinner = (Spinner) findViewById(R.id.spImageColor);
        imageTypeSpinner = (Spinner) findViewById(R.id.spImageType);
        etSearchSite = (EditText) findViewById(R.id.etSearchSite);
        btnSave = (Button) findViewById(R.id.btnSave);
    }
    
    private void addListeners() {
        etSearchSite.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearchSite.getText().toString().trim().isEmpty()) {
                    btnSave.setEnabled(false);
                } else {
                    btnSave.setEnabled(true);
                }
            }
        });
    }
    
    
    public void onSave(View v) {

        // Set the values from the spinners and site filter into
        // preferences object and send it back
        String siteFilter = etSearchSite.getText().toString();
        if (!URLUtil.isValidUrl(siteFilter) && !URLUtil.isValidUrl("http://" + siteFilter)) {
            String siteFilterText = ((TextView) findViewById(R.id.lblSiteFilter)).getText().toString();
            Toast.makeText(this, getString(R.string.enter_valid_site_filter) + " \'" + siteFilterText + "'", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Preferences preferences = new Preferences(
            imageSizeSpinner.getSelectedItem().toString(),
            imageColorSpinner.getSelectedItem().toString(),
            imageTypeSpinner.getSelectedItem().toString(),
            etSearchSite.getText().toString()
        );
        
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Preferences.PREFERENCES_BUNDLE_PARAM, preferences);
        setResult(RESULT_OK, returnIntent);     
        finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preferences, menu);
        return true;
    }

}
