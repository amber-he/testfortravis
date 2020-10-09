import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

import static org.junit.Assert.*;

public class notepadTest extends notepad{

    @Test
    public void testa() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        new notepad().start(new Stage());
                    }
                });
            }
        });
        thread.start();
    }


}