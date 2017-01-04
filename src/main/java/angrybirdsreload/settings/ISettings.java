package angrybirdsreload.settings;

public interface ISettings {

    void load();

    String get(String key);

    void set(String key, String value);

    void save();
}
