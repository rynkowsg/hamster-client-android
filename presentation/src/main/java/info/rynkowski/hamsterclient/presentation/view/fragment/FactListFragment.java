package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;

public class FactListFragment extends BaseFragment {

  private OnAddFactClickedListener listener;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      listener = (OnAddFactClickedListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement OnAddFactClickedListener");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_fact_list, container, false);
    ButterKnife.inject(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }

  @Override public void onDetach() {
    super.onDetach();
    listener = null;
  }

  @OnClick(R.id.btn_add_fact)
  public void onAddFactClicked(View view) {
    listener.onAddFactClicked();
  }

  public interface OnAddFactClickedListener {
    public void onAddFactClicked();
  }
}
