package info.rynkowski.hamsterclient.ui;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.model.Fact;

public class AddFactActivity extends BaseActivity {
    private static final String TAG = AddFactActivity.class.getName();

    @InjectView(R.id.start_time)
    EditText mStartTime;
    @InjectView(R.id.end_time)
    EditText mEndTime;
    @InjectView(R.id.activity)
    EditText mActivity;
    @InjectView(R.id.category)
    EditText mCategory;
    @InjectView(R.id.tags)
    EditText mTags;
    @InjectView(R.id.description)
    EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fact);
        ButterKnife.inject(this);

        // enabling action bar app icon and behaving it as toggle button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_fact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(), item.getItemId() = " + item.getItemId());
        switch (item.getItemId()) {
            case (R.id.action_settings):
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.start_time, R.id.end_time})
    public void onTimePickerClicked(final View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddFactActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                switch (view.getId()) {
                    case R.id.start_time:
                        mStartTime.setText(selectedHour + ":" + selectedMinute);
                        break;
                    case R.id.end_time:
                        mEndTime.setText(selectedHour + ":" + selectedMinute);
                        break;
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @OnClick(R.id.ok)
    public void onButtonClicked(View view) {
        Intent intent = getIntent();
        intent.putExtra("fact", readFactFields());
        AddFactActivity.this.setResult(RESULT_OK, intent);
        AddFactActivity.this.finish();
    }

    private Fact readFactFields() {
        Fact fact = new Fact.Builder()
                .activity(mActivity.getText().toString())
                .category(mCategory.getText().toString())
                .description(mDescription.getText().toString())
                .tags(getTags(mTags.getText().toString()))
                .startTime(getTimestamp(mStartTime.getText().toString()))
                .endTime(getTimestamp(mEndTime.getText().toString()))
                .build();
        Log.i(TAG, "fact = " + fact);
        Log.i(TAG, "fact.serialized_name() = " + fact.serialized_name());
        return fact;
    }

    private int getTimestamp(String pickedTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar parsedTime = Calendar.getInstance();
        try {
            Date parsedDate = dateFormat.parse(pickedTime);
            parsedTime.setTimeInMillis(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Provided time doesn't support format 'mm:ss', but it should.");
        }

        Calendar resCalendar = Calendar.getInstance();
        resCalendar.set(Calendar.MINUTE, parsedTime.get(Calendar.MINUTE));
        resCalendar.set(Calendar.HOUR, parsedTime.get(Calendar.HOUR));

        return (int) (resCalendar.getTimeInMillis() / 1000);
    }

    private List<String> getTags(String tags) {
        List<String> result = new ArrayList<>();
        for (String i : StringUtils.split(tags, ","))
            result.add(StringUtils.trim(i));
        return result;
    }
}
