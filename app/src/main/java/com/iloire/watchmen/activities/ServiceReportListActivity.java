package com.iloire.watchmen.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iloire.watchmen.R;
import com.iloire.watchmen.Settings;
import com.iloire.watchmen.adapters.ServiceReportListAdapter;
import com.iloire.watchmen.models.ServiceReport;
import com.iloire.watchmen.service.WatchmenService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ServiceReportListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        final Context c = this;

        // TODO: spinner

        WatchmenService service = new WatchmenService();
        service.getServices(new Callback<List<ServiceReport>>() {

            @Override
            public void success(final List<ServiceReport> serviceReports, Response response) {
                ServiceReportListAdapter adapter = new ServiceReportListAdapter(c, serviceReports);
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(c, ServiceReportDetailsActivity.class);
                        intent.putExtra("id", serviceReports.get(position).getService().getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                new AlertDialog.Builder(c)
                        .setTitle("Network error")
                        .setMessage("Error connecting to " + Settings.getServiceBaseUrl())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                System.err.println(error);
            }
        });
    }
}
