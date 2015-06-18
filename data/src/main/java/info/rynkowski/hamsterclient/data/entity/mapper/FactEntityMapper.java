package info.rynkowski.hamsterclient.data.entity.mapper;

import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Struct5;
import org.gnome.Struct7;

@Singleton
public class FactEntityMapper {

  @Inject public FactEntityMapper() {
    //empty
  }

  public List<Fact> transformFromStruct5(List<Struct5> structList) {
    List<Fact> factList = new ArrayList<>(structList.size());
    Fact fact;
    for (Struct5 struct : structList) {
      fact = transform(new FactEntity(struct));
      factList.add(fact);
    }
    return factList;
  }

  public List<Fact> transformFromStruct7(List<Struct7> structList) {
    List<Fact> factList = new ArrayList<>(structList.size());
    Fact fact;
    for (Struct7 struct : structList) {
      fact = transform(new FactEntity(struct));
      factList.add(fact);
    }
    return factList;
  }

  public Fact transform(FactEntity factEntity) {
    Calendar startTime = Calendar.getInstance();
    startTime.setTime(new Date((long) (factEntity.getStartTime()) * 1000));
    Calendar endTime = Calendar.getInstance();
    endTime.setTime(new Date((long) (factEntity.getEndTime()) * 1000));

    Activity activity = new Activity(factEntity.getActivity(), factEntity.getCategory());
    return new Fact.Builder().activity(activity)
        .startTime(startTime)
        .endTime(endTime)
        .description(factEntity.getDescription())
        .tags(factEntity.getTags())
        .build();
  }

  public FactEntity transform(Fact fact) {
    return new FactEntity.Builder().activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory())
        .startTime((int) (fact.getStartTime().getTimeInMillis() / 1000))
        .endTime((int) (fact.getEndTime().getTimeInMillis() / 1000))
        .description(fact.getDescription())
        .tags(fact.getTags())
        .build();
  }
}
