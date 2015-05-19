package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import butterknife.InjectView;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.view.FactFormView;

/**
 * Fragment that allows to provide new fact attributes.
 */
public class FactFormFragment extends Fragment implements FactFormView {

  /**
   * Interface for listening user list events.
   */
  public interface FactFormListener {
    void onFactPrepared(/*final Fact fact*/);
  }

  @InjectView(R.id.et_activity) EditText etActivity;
  @InjectView(R.id.et_category) EditText etCategory;
  @InjectView(R.id.et_tags) EditText etTags;
  @InjectView(R.id.et_start_time) EditText etStartTime;
  @InjectView(R.id.et_end_time) EditText etEndTime;
  @InjectView(R.id.cb_ongoing) EditText cbOngoing;
  @InjectView(R.id.et_description) EditText etDescription;
  @InjectView(R.id.btn_ok) EditText btnOk;

  private FactFormListener factFormListener;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof FactFormListener) {
      this.factFormListener = (FactFormListener) activity;
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_fact_form, container, false);
  }
}
