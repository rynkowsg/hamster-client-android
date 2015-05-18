package info.rynkowski.hamsterclient.data.entity.mapper;

import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.gnome.Struct5;
import org.gnome.Struct7;

public class FactEntityMapper {

  public FactEntityMapper() {
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
    Activity activity = new Activity(factEntity.getActivity(), factEntity.getCategory());
    return new Fact.Builder().activity(activity)
        .startTime(new Date(factEntity.getStartTime()))
        .endTime(new Date(factEntity.getEndTime()))
        .description(factEntity.getDescription())
        .tags(factEntity.getTags())
        .build();
  }

  public FactEntity transform(Fact fact) {
    return new FactEntity.Builder().activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory())
        .startTime(fact.getStartTime().getTime())
        .endTime(fact.getEndTime().getTime())
        .description(fact.getDescription())
        .tags(fact.getTags())
        .build();
  }
}