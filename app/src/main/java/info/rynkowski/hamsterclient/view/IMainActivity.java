package info.rynkowski.hamsterclient.view;

public interface IMainActivity {
    public void startService();
    public void stopService();
    public void sendRequestToService(int what);
}