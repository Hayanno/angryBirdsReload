package angrybirdsreload.settings;

public interface ISettings {

    void load();

    void setStageLevel(int currentStage, int currentLevel);

    String get(String scope, String key);

    void set(String key, String value);

    void save();
}
