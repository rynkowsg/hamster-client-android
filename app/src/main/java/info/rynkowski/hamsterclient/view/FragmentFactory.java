package info.rynkowski.hamsterclient.view;

import android.app.Fragment;

public class FragmentFactory {
    static Type getType(int navPosition) {
        switch (navPosition) {
            case 0:
                return Type.TEST;
            case 1:
                return Type.HOME;
            case 2:
                return Type.HISTORY;
            case 3:
                return Type.STATS;
            case 4:
                return Type.EDIT;
            case 5:
                return Type.ABOUT;
            default:
                return Type.UNKNOWN;
        }
    }

    static Fragment get(Type type) {
        switch (type) {
            case TEST:
                return new TestFragment();
            case HOME:
            case HISTORY:
            case STATS:
            case EDIT:
            case ABOUT:
            case UNKNOWN:
            default:
                return null;
        }
    }

    public enum Type {
        TEST,
        HOME,
        HISTORY,
        STATS,
        EDIT,
        ABOUT,
        UNKNOWN
    }
}