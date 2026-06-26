package org.targol.resoplan;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.targol.resoplan.ui.utils.ThemesManager.Theme;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
public class ResoPlanJavaFxApplication extends Application {

	private ConfigurableApplicationContext context;
	private Stage mainWindow;

	@Override
	public void init() {
		this.context = new SpringApplicationBuilder(SpringApplication.class).headless(false).run();
	}

	@Override
	public void start(final Stage stage) throws Exception {
		this.mainWindow = stage;
		final Locale locale = Locale.getDefault();
		final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", locale); //$NON-NLS-1$
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"), bundle); //$NON-NLS-1$
		loader.setControllerFactory(this.context::getBean);
		final Parent root = loader.load();
		stage.setTitle("ResoPlan"); //$NON-NLS-1$
		stage.setScene(new Scene(root));
		final Theme theme = PreferencesManager.getInstance().getCurrentTheme();
		stage.getScene().getStylesheets().clear();
		stage.getScene().getStylesheets().add(getClass().getResource(theme.getCssfilePath()).toExternalForm());
		stage.setMaximized(true);
		stage.show();
	}

	@Override
	public void stop() {
		this.context.close();
		Platform.exit();
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
