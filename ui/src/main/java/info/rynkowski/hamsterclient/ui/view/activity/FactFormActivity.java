/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.ui.view.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.ui.R;
import info.rynkowski.hamsterclient.ui.utils.TimeConverter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/**
 * Activity provides a new fact.
 */
public class FactFormActivity extends BaseActivity {

  public static final String EXTRAS_KEY_FACT = "EXTRAS_KEY_FACT";

  @Bind(R.id.et_activity) EditText editTextActivity;
  @Bind(R.id.et_category) EditText editTextCategory;
  @Bind(R.id.et_tags) EditText editTextTags;
  @Bind(R.id.et_start_time) EditText editTextStartTime;
  @Bind(R.id.et_end_time) EditText editTextEndTime;
  @Bind(R.id.et_description) EditText editTextDescription;
  @Bind(R.id.cb_ongoing) CheckBox checkBoxIsInProgress;

  private boolean isEndTimeDisabled;

  private @NonNull Calendar selectedStartTime = GregorianCalendar.getInstance(Locale.getDefault());
  private @NonNull Calendar selectedEndTime = GregorianCalendar.getInstance(Locale.getDefault());

  public static @NonNull Intent getCallingIntent(@NonNull Context context) {
    return new Intent(context, FactFormActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_form);
    ButterKnife.bind(this);
    initiateTimeFields();
  }

  @OnClick({ R.id.et_start_time, R.id.et_end_time }) public void onTimeClicked(final View view) {
    int hour, minutes;

    switch (view.getId()) {
      case R.id.et_start_time:
        hour = selectedStartTime.get(Calendar.HOUR_OF_DAY);
        minutes = selectedStartTime.get(Calendar.MINUTE);
        break;
      case R.id.et_end_time:
        hour = selectedEndTime.get(Calendar.HOUR_OF_DAY);
        minutes = selectedEndTime.get(Calendar.MINUTE);
        break;
      default:
        throw new RuntimeException("Unknown view id");
    }

    TimePickerDialog timePicker = new TimePickerDialog(FactFormActivity.this,
        (timePickerView, selectedHour, selectedMinutes) -> {
          switch (view.getId()) {
            case R.id.et_start_time:
              selectedStartTime.set(Calendar.HOUR_OF_DAY, selectedHour);
              selectedStartTime.set(Calendar.MINUTE, selectedMinutes);
              editTextStartTime.setText(TimeConverter.toString(selectedStartTime, "HH:mm"));
              break;
            case R.id.et_end_time:
              selectedEndTime.set(Calendar.HOUR_OF_DAY, selectedHour);
              selectedEndTime.set(Calendar.MINUTE, selectedMinutes);
              editTextEndTime.setText(TimeConverter.toString(selectedEndTime, "HH:mm"));
              break;
          }
        }, hour, minutes, true);

    timePicker.setTitle("Select Time");
    timePicker.show();
  }

  @OnClick(R.id.cb_ongoing) public void onCheckBoxClicked(CheckBox checkBox) {
    isEndTimeDisabled = checkBox.isChecked();
    editTextEndTime.setEnabled(!isEndTimeDisabled);
  }

  @OnClick(R.id.btn_apply) public void onApplyClicked(View view) {
    FactModel fact = readFact();
    Intent intent = FactFormActivity.this.getIntent();
    intent.putExtra(EXTRAS_KEY_FACT, fact);
    setResult(Activity.RESULT_OK, intent);
    finish();
  }

  private @NonNull FactModel readFact() {
    return new FactModel.Builder()
        .activity(editTextActivity.getText().toString())
        .category(editTextCategory.getText().toString())
        .tags(splitTags(editTextTags.getText().toString()))
        .description(editTextDescription.getText().toString())
        .startTime(selectedStartTime)
        .endTime(isEndTimeDisabled ? Optional.absent() : Optional.of(selectedEndTime))
        .build();
  }

  private void initiateTimeFields() {
    editTextStartTime.setClickable(true);
    editTextStartTime.setFocusable(false);
    editTextStartTime.setEnabled(true);
    editTextStartTime.setText(TimeConverter.toString(selectedStartTime, "HH:mm"));

    editTextEndTime.setClickable(true);
    editTextEndTime.setFocusable(false);
    editTextEndTime.setEnabled(false);
    editTextEndTime.setText(TimeConverter.toString(selectedEndTime, "HH:mm"));

    isEndTimeDisabled = true;
    checkBoxIsInProgress.setChecked(true);
  }

  private @NonNull List<String> splitTags(@NonNull String tags) {
    List<String> result = new ArrayList<>();
    for (String i : StringUtils.split(tags, ",")) {
      result.add(StringUtils.trim(i));
    }
    return result;
  }
}
