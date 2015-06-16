package info.rynkowski.hamsterclient.presentation.model.mapper;

import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@ActivityScope
public class FactModelDataMapper {

  @Inject
  FactModelDataMapper() {
    //empty
  }

  public Fact transform(FactModel factModel) {
    return new Fact.Builder()
        .activity(new Activity(factModel.getActivity(), factModel.getCategory()))
        .tags(factModel.getTags())
        .description(factModel.getDescription())
        .startTime(factModel.getStartTime())
        .endTime(factModel.getEndTime())
        .build();
  }

  public FactModel transform(Fact fact) {
    return new FactModel.Builder()
        .activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory())
        .tags(fact.getTags())
        .description(fact.getDescription())
        .startTime(fact.getStartTime())
        .endTime(fact.getEndTime())
        .build();
  }

  public List<FactModel> transform(List<Fact> factList) {
    List<FactModel> factModelList = new ArrayList<>(factList.size());
    FactModel factModel;
    for (Fact fact : factList) {
      factModel = transform(fact);
      factModelList.add(factModel);
    }
    return factModelList;
  }
}
