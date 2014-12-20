package info.rynkowski.hamsterclient.view;

public enum FragmentType {
    TEST;

    static FragmentType get(int navPosition) {
        switch (navPosition) {
            case 0: return TEST;
            default: return TEST;
        }
    }
}