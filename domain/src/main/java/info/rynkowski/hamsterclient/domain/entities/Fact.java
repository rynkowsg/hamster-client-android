package info.rynkowski.hamsterclient.domain.entities;

import java.util.Date;
import java.util.List;

public class Fact {
  Activity activity;
  Date startTime;
  Date endTime;
  List<Tag> tags;
  String description;
}
