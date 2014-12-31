package info.rynkowski.hamsterclient.ui;

public interface IMainActivity {
    public void startService();
    public void stopService();
    public void sendRequestToService(int what);
}