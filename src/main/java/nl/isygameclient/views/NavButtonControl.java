package nl.isygameclient.views;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavButtonControl extends JFXButton implements Initializable {

    private final String glyphName;
    private final String source;

    @FXML
    private MaterialIconView iconView;

    public NavButtonControl(@NamedArg("text") String text, @NamedArg("glyphName") String glyphName, @NamedArg("source") String source) {
        super(text);
        this.glyphName = glyphName;
        this.source = source;

        URL url = getClass().getResource("/nl/isygameclient/views/nav-button.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iconView.setGlyphName(this.glyphName);
    }

    public void activate() {
        this.getStyleClass().add("active");
    }

    public void deactivate() {
        this.getStyleClass().remove("active");
    }

    public String getSource() {
        return source;
    }
}
