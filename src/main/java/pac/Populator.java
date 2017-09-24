package pac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dmytro Tymoshenko on 15.05.17.
 */
@Component
public class Populator {

    @Autowired
    private GoogleApi googleApi;

    @Autowired ModuleFile moduleFile;

    public void populate(){

        for (int i = 0; i < 3; i++) {



        }

    }

}
