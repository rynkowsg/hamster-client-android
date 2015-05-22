package info.rynkowski.hamsterclient.presentation.presenter;

/**
 * Interface representing a Presenter in a model view presenter (MVP) pattern.
 */
public interface Presenter {

  /**
   * Called when the presenter is initialized, this method represents the start of the presenter
   * lifecycle.
   */
  void initialize();

  /**
   * Called when the presenter is resumed. After the initialization and when the presenter comes
   * from a pause state.
   */
  void resume();

  /**
   * Called when the presenter is paused.
   */
  void pause();

  /**
   * Called at the end of the presenter life cycle.
   */
  void destroy();
}
