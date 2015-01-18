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

    public Fact(String activity, String category, String description, List<String> tags, int startTime, int endTime) {
        this.activity = activity;
        this.category = category;
        this.description = description;
        this.tags = tags;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private Fact(Builder b) {
        this.activity = b.activity;
        this.category = b.category;
        this.description = b.description;
        this.tags = b.tags;
        this.startTime = b.startTime;
        this.endTime = b.endTime;
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

    public static class Builder {
        private String activity;
        private String category;
        private String description;
        private List<String> tags;
        private int startTime;
        private int endTime;

        public Builder() {
        }

        public Builder(String activity, String category, String description, List<String> tags,
                       int startTime, int endTime) {
            this.activity = activity;
            this.category = category;
            this.description = description;
            this.tags = tags;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Builder activity(String activity) {
            this.activity = activity;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder startTime(int startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(int endTime) {
            this.endTime = endTime;
            return this;
        }

        public Fact build() {
            return new Fact(this);
        }
    }
}
