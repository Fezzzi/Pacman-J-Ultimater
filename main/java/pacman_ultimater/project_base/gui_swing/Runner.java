package pacman_ultimater.project_base.gui_swing;

import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;
import pacman_ultimater.project_base.gui_swing.ui.controller.MainFrameController;
import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

class Runner {

    public static void main(String[] args) {
        MainFrame view = new MainFrame();
        MainFrameModel model = new MainFrameModel(view);
        MainFrameController controller = new MainFrameController(model, view);
        controller.OpenMenu();
    }
}
