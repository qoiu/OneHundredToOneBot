package com.github.qoiu.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class JavaFXTrayIconSample extends Application {

    // one icon location is shared between the application tray icon and task bar icon.
    // you could also use multiple icons to allow for clean display of tray icons on hi-dpi devices.
    private static final String iconImageLoc =
            "https://icons.iconarchive.com/icons/thesquid.ink/free-flat-sample/16/owl-icon.png";

    private final TrayBtn[] buttons;

    public JavaFXTrayIconSample(TrayBtn...trayBtns) {
        buttons = trayBtns;
    }

    // sets up the javafx application.
    // a tray icon is setup for the icon, but the main stage remains invisible until the user
    // interacts with the tray icon.
    @Override
    public void start(final Stage stage) {
        // stores a reference to the stage.
        // application stage is stored so that it can be shown and hidden based on system tray icon operations.

        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);

        // sets up the tray icon (using awt code run on the swing thread).
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        // out stage will be translucent, so give it a transparent style.
        stage.initStyle(StageStyle.TRANSPARENT);
    }

    /**
     * Sets up a system tray icon for the application.
     */
    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            URL imageLoc = new URL(
                    iconImageLoc
            );
            java.awt.Image image = ImageIO.read(imageLoc);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

//            // if the user double-clicks on the tray icon, show the main app stage.
//            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));


            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).

            // setup the popup menu for the application.
            java.awt.PopupMenu popup = new java.awt.PopupMenu();
            for (TrayBtn btn:buttons) {
                btn.addBtn(popup);
            }
            trayIcon.setPopupMenu(popup);

            // add the application tray icon to the system tray.
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    static class TrayBtn {
        private final String text;
        private final ActionListener listener;

        public TrayBtn(String text, ActionListener listener) {
            this.text = text;
            this.listener = listener;
        }

        private void addBtn(PopupMenu popupMenu){
            java.awt.MenuItem item = new java.awt.MenuItem(text);
            item.addActionListener(listener);
            popupMenu.add(item);
        }
    }
}