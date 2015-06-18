package info.rynkowski.hamsterclient.presentation.presenter;

public interface SignalsListener {

  void onActivitiesChanged();

  void onFactsChanged();

  void onTagsChanged();

  void onToggleCalled();
}
