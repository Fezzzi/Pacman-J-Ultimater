package pacman_ultimater.project_base.gui_swing;

import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;
import pacman_ultimater.project_base.gui_swing.ui.controller.MainFrameController;
import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import java.io.IOException;

/**
 * Program's entry point.
 * Instantiates MainFrameController with instances of model and view and passes control over program to controller.
 */
class Runner {

    public static void main(String[] args) {
        try {
            MainFrame view = new MainFrame();
            MainFrameModel model = new MainFrameModel(view);
            MainFrameController controller = new MainFrameController(model, view);
            controller.OpenMenu();
        }
        catch (IOException ignore) {}
    }
}
