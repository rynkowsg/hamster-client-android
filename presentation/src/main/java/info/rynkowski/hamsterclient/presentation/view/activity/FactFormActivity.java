package info.rynkowski.hamsterclient.presentation.view.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Activity provides a new fact.
 */
public class FactFormActivity extends BaseActivity {

  @InjectView(R.id.et_activity) EditText activity;
  @InjectView(R.id.et_category) EditText category;
  @InjectView(R.id.et_tags) EditText tags;
  @InjectView(R.id.et_start_time) EditText startTime;
  @InjectView(R.id.et_end_time) EditText endTime;
  @InjectView(R.id.et_description) EditText description;

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactFormActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_form);
    ButterKnife.inject(this);
    // TODO: Rewrite below lines.
    startTime.setEnabled(true);
    startTime.setClickable(true);
    startTime.setFocusable(false);
    startTime.setText(04 + ":" + 02);
    endTime.setEnabled(true);
    endTime.setClickable(true);
    endTime.setFocusable(false);
    endTime.setText(04 + ":" + 02);
  }

  @OnClick(R.id.cb_ongoing) public void onCheckBoxClicked(View view) {
    boolean isChecked = ((CheckBox) view).isChecked();
    endTime.setEnabled(!isChecked);
  }

  @OnClick({ R.id.et_start_time, R.id.et_end_time }) public void onTimeClicked(final View view) {

    Calendar currentTime = Calendar.getInstance();
    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
    int minute = currentTime.get(Calendar.MINUTE);

    TimePickerDialog timePicker = new TimePickerDialog(FactFormActivity.this,
        (timePicker1, selectedHour, selectedMinutes) -> {
          switch (view.getId()) {
            case R.id.et_start_time:
              startTime.setText(selectedHour + ":" + selectedMinutes);
              break;
            case R.id.et_end_time:
              endTime.setText(selectedHour + ":" + selectedMinutes);
              break;
          }
        }, hour, minute, true);

    timePicker.setTitle("Select Time");
    timePicker.show();
  }

  @OnClick(R.id.btn_ok) public void onSubmitButtonClicked(View view) {
    // TODO: Rewrite below lines.
    Intent intent = FactFormActivity.this.getIntent();
    intent.putExtra("fact", readFact());
    setResult(Activity.RESULT_OK, intent);
    finish();
  }

  private FactModel readFact() {
    return new FactModel.Builder().activity(activity.getText().toString())
        .category(category.getText().toString())
        .tags(getTags(tags.getText().toString()))
        .description(description.getText().toString())
        .build();
  }

  private List<String> getTags(String tags) {
    List<String> result = new ArrayList<>();
    for (String i : StringUtils.split(tags, ","))
      result.add(StringUtils.trim(i));
    return result;
  }
}
