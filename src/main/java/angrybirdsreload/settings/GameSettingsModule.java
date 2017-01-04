package angrybirdsreload.settings;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class GameSettingsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ISettings.class).to(GameSettings.class).in(Scopes.SINGLETON); // this is lazy singleton
        //bind(ISettings.class).to(ISettings.class).asEagerSingleton(); // this is eager one
    }
}
