package info.rynkowski.hamsterclient.data.entity.mapper;

import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.Date;

public class FactEntityMapper {

  public FactEntityMapper() {}

  public Fact transform(FactEntity factEntity) {
    Activity activity = new Activity(factEntity.getActivity(), factEntity.getCategory());
    // TODO: Correct date conversion!
    Date startTime = new Date();
    Date endTime = new Date();

    return new Fact.Builder()
        .activity(activity)
        .startTime(startTime)
        .endTime(endTime)
        .description(factEntity.getDescription())
        .tags(factEntity.getTags())
        .build();
  }

  public FactEntity transform(Fact fact) {
    return null;
  }
}
