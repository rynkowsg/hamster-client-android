package info.rynkowski.hamsterclient.presentation.view;

import info.rynkowski.hamsterclient.presentation.model.FactModel;
import java.util.Collection;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of {@link FactModel}.
 */
public interface FactListView extends LoadDataView {
  /**
   * Render a fact list in the UI.
   *
   * @param factModelCollection The collection of {@link FactModel} that will be shown.
   */
  void renderUserList(Collection<FactModel> factModelCollection);
}
