package info.rynkowski.hamsterclient.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * It is partial java implementation of python class Fact from project Hamster.
 * The original can be found at hamster/lib/__init__.py at project hamster time tracker.
 */
public class Fact implements Serializable {
    private String activity;
    private String category;
    private String description;
    private List<String> tags;
    private int startTime;
    private int endTime;

    public Fact() {
    }

    public Fact(AdapterStruct5 struct) {
        this.activity = struct.name();
        this.category = struct.category();
        this.description = struct.description();
        this.tags = struct.tags();
        this.startTime = struct.start_time();
        this.endTime = struct.end_time();
    }

    public Fact(String activity, String category, String description, List<String> tags, int start_time, int end_time) {
        this.activity = activity;
        this.category = category;
        this.description = description;
        this.tags = tags;
        this.startTime = start_time;
        this.endTime = end_time;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String serialized_name() {
        String res = activity;
        if (!category.isEmpty()) {
            res += "@" + category;
        }
        res += ",";
        if (!description.isEmpty()) {
            res += description;
        }
        res += " ";
        if (!tags.isEmpty()) {
            res += "#" + StringUtils.join(tags, '#');
        }
        return res;
    }
}
