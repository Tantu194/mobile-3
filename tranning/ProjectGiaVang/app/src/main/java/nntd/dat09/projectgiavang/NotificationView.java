package nntd.dat09.projectgiavang;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class NotificationView extends Activity {

    ListView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        Intent i = getIntent();
        String[] items = i.getStringArrayExtra("events");

        tv = (ListView) findViewById(R.id.tvContent);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                items);

        tv.setAdapter(adp);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(i.getIntExtra("id", 100));

    }
}
