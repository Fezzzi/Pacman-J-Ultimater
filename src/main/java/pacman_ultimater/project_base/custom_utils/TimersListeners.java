package pacman_ultimater.project_base.custom_utils;

import java.awt.event.ActionListener;

/**
 * Class holding together action listeners of game timers.
 */
public class TimersListeners
{
    private final ActionListener pacman_timer;
    private final ActionListener ghost_timer;
    private final ActionListener pacman_smooth_timer;
    private final ActionListener ghost_smooth_timer;

    /**
     * basic class constructor
     * @param pacman pacman's timer action listener
     * @param ghost ghost's timer action listener
     * @param pac_smooth pacman's smooth timer action listener.
     * @param ghost_smooth ghost's smooth timer action listener
     */
    public TimersListeners(ActionListener pacman, ActionListener ghost, ActionListener pac_smooth, ActionListener ghost_smooth)
    {
        pacman_timer = pacman;
        ghost_timer = ghost;
        pacman_smooth_timer = pac_smooth;
        ghost_smooth_timer = ghost_smooth;
    }

    //<editor-fold desc="- LISTENERS GETTERS -">

    public ActionListener getPacman_timer() {
        return pacman_timer;
    }

    public ActionListener getGhost_timer() {
        return ghost_timer;
    }

    public ActionListener getPacman_smooth_timer() {
        return pacman_smooth_timer;
    }

    public ActionListener getGhost_smooth_timer() {
        return ghost_smooth_timer;
    }

    //</editor-fold>
}
