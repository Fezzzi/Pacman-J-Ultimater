package pacman_ultimater.project_base.core;

import pacman_ultimater.project_base.gui_swing.ui.controller.MenuController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyBindings {
    private JPanel panel;
    private MenuController mc;

    public KeyBindings(JPanel panel, MenuController menuController){
        this.panel = panel;
        mc = menuController;
    }

    public InputMap getIMap(){
        InputMap iMap = panel.getInputMap();

        // ALPHABET SYMBOLS -------------------------------------------------------------------------------
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "p_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), "o_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "i_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0), "u_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0), "y_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "t_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "r_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "e_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "w_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "q_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "l_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "k_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0), "j_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "h_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "g_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "f_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "d_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "m_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "n_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "b_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "v_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "c_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "x_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "z_key");

        // NUMBER SYMBOLS ----------------------------------------------------------------------------------
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), "0_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "1_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "2_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "3_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "4_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), "5_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0), "6_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), "7_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0), "8_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_9, 0), "9_key");

        // SPECIAL SYMBOLS ---------------------------------------------------------------------------------
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "+_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "-_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "=_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "/_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, 0), "\\_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ASTERISK, 0), "*_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LESS, 0), "<_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, 0), ">_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, 0), ",_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMBER_SIGN, 0), "#_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_AT, 0), "@_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EXCLAMATION_MARK, 0), "!_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_AMPERSAND, 0), "&_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), "._key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_COLON, 0), ":_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SEMICOLON, 0), ";_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UNDERSCORE, 0), "__key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT_PARENTHESIS, 0), "(_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT_PARENTHESIS, 0), ")_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space_key");

        // SPECIAL KEYS -----------------------------------------------------------------------------------
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "back_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter_key");

        return iMap;
    }

    public ActionMap getAMap(){
        ActionMap aMap = panel.getActionMap();

        // ALPHABET SYMBOLS -------------------------------------------------------------------------------
        aMap.put("p_key", key_pressed_P);
        aMap.put("o_key", key_pressed_O);
        aMap.put("i_key", key_pressed_I);
        aMap.put("u_key", key_pressed_U);
        aMap.put("y_key", key_pressed_Y);
        aMap.put("t_key", key_pressed_T);
        aMap.put("r_key", key_pressed_R);
        aMap.put("e_key", key_pressed_E);
        aMap.put("w_key", key_pressed_W);
        aMap.put("q_key", key_pressed_Q);
        aMap.put("l_key", key_pressed_L);
        aMap.put("k_key", key_pressed_K);
        aMap.put("j_key", key_pressed_J);
        aMap.put("h_key", key_pressed_H);
        aMap.put("g_key", key_pressed_G);
        aMap.put("f_key", key_pressed_F);
        aMap.put("d_key", key_pressed_D);
        aMap.put("s_key", key_pressed_S);
        aMap.put("a_key", key_pressed_A);
        aMap.put("m_key", key_pressed_M);
        aMap.put("n_key", key_pressed_N);
        aMap.put("b_key", key_pressed_B);
        aMap.put("v_key", key_pressed_V);
        aMap.put("c_key", key_pressed_C);
        aMap.put("x_key", key_pressed_X);
        aMap.put("z_key", key_pressed_Z);

        // NUMBER SYMBOLS ----------------------------------------------------------------------------------
        aMap.put("0_key", key_pressed_0);
        aMap.put("1_key", key_pressed_1);
        aMap.put("2_key", key_pressed_2);
        aMap.put("3_key", key_pressed_3);
        aMap.put("4_key", key_pressed_4);
        aMap.put("5_key", key_pressed_5);
        aMap.put("6_key", key_pressed_6);
        aMap.put("7_key", key_pressed_7);
        aMap.put("8_key", key_pressed_8);
        aMap.put("9_key", key_pressed_9);

        // SPECIAL SYMBOLS ---------------------------------------------------------------------------------
        aMap.put("+_key", key_pressed_PLUS);
        aMap.put("-_key", key_pressed_MINUS);
        aMap.put("=_key", key_pressed_EQUALS);
        aMap.put("/_key", key_pressed_SLASH);
        aMap.put("\\_key", key_pressed_BACK_SLASH);
        aMap.put("*_key", key_pressed_ASTERISK);
        aMap.put("<_key", key_pressed_LESS);
        aMap.put(">_key", key_pressed_GREATER);
        aMap.put(",_key", key_pressed_COMMA);
        aMap.put("#_key", key_pressed_HASH);
        aMap.put("@_key", key_pressed_AT);
        aMap.put("!_key", key_pressed_EXCLAMATION_MARK);
        aMap.put("&_key", key_pressed_AMPERSAND);
        aMap.put("._key", key_pressed_PERIOD);
        aMap.put(":_key", key_pressed_COLON);
        aMap.put(";_key", key_pressed_SEMICOLON);
        aMap.put("__key", key_pressed_UNDERSCORE);
        aMap.put("(_key", key_pressed_LEFT_PARENTHESIS);
        aMap.put(")_key", key_pressed_RIGHT_PARENTHESIS);
        aMap.put("space_key", key_pressed_SPACE);

        // SPECIAL KEYS ------------------------------------------------------------------------------------
        aMap.put("up_key", key_pressed_UP);
        aMap.put("right_key", key_pressed_RIGHT);
        aMap.put("down_key", key_pressed_DOWN);
        aMap.put("left_key", key_pressed_LEFT);
        aMap.put("esc_key", key_pressed_ESC);
        aMap.put("back_key", key_pressed_BACK);
        aMap.put("enter_key", key_pressed_ENTER);

        return aMap;
    }

    // ALPHABET SYMBOLS -------------------------------------------------------------------------------
    private Action key_pressed_P = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_P, "P"); }
    };
    private Action key_pressed_O = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_O, "O"); }
    };
    private Action key_pressed_I = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_I, "I"); }
    };
    private Action key_pressed_U = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_U, "U"); }
    };
    private Action key_pressed_Y = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Y, "Y"); }
    };
    private Action key_pressed_T = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_T, "T"); }
    };
    private Action key_pressed_R = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_R, "R"); }
    };
    private Action key_pressed_E = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_E, "E"); }
    };
    private Action key_pressed_W = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_W, "W"); }
    };
    private Action key_pressed_Q = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q, "Q"); }
    };
    private Action key_pressed_L = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q, "L"); }
    };
    private Action key_pressed_K = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q, "K"); }
    };
    private Action key_pressed_J = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q, "J"); }
    };
    private Action key_pressed_H = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q, "H"); }
    };
    private Action key_pressed_G = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q, "G"); }
    };
    private Action key_pressed_F = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q, "F"); }
    };
    private Action key_pressed_D = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_D, "D"); }
    };
    private Action key_pressed_S = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_S, "S"); }
    };
    private Action key_pressed_A = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "A"); }
    };
    private Action key_pressed_M = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "M"); }
    };
    private Action key_pressed_N = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "N"); }
    };
    private Action key_pressed_B = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "B"); }
    };
    private Action key_pressed_V = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "V"); }
    };
    private Action key_pressed_C = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "C"); }
    };
    private Action key_pressed_X = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "X"); }
    };
    private Action key_pressed_Z = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "Z"); }
    };

    // NUMBER SYMBOLS ----------------------------------------------------------------------------------
    private Action key_pressed_0 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_0, "0"); }
    };
    private Action key_pressed_1 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_1, "1"); }
    };
    private Action key_pressed_2 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_2, "2"); }
    };
    private Action key_pressed_3 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_3, "3"); }
    };
    private Action key_pressed_4 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_4, "4"); }
    };
    private Action key_pressed_5 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_5, "5"); }
    };
    private Action key_pressed_6 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_6, "6"); }
    };
    private Action key_pressed_7 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_7, "7"); }
    };
    private Action key_pressed_8 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_8, "8"); }
    };
    private Action key_pressed_9 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_9, "9"); }
    };

    // SPECIAL SYMBOLS ---------------------------------------------------------------------------------
    private Action key_pressed_PLUS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_PLUS, "+"); }
    };
    private Action key_pressed_MINUS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_MINUS, "-"); }
    };
    private Action key_pressed_EQUALS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_EQUALS, "="); }
    };
    private Action key_pressed_SLASH = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_SLASH, "/"); }
    };
    private Action key_pressed_BACK_SLASH = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_BACK_SLASH, "\\"); }
    };
    private Action key_pressed_ASTERISK = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ASTERISK, "*"); }
    };
    private Action key_pressed_LESS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_LESS, "<"); }
    };
    private Action key_pressed_GREATER = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_GREATER, ">"); }
    };
    private Action key_pressed_COMMA = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_COMMA, ","); }
    };
    private Action key_pressed_HASH = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_NUMBER_SIGN, "#"); }
    };
    private Action key_pressed_AT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_AT, "@"); }
    };
    private Action key_pressed_EXCLAMATION_MARK = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_EXCLAMATION_MARK, "!"); }
    };
    private Action key_pressed_AMPERSAND = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_AMPERSAND, "&"); }
    };
    private Action key_pressed_PERIOD = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_PERIOD, "."); }
    };
    private Action key_pressed_COLON = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_COLON, ":"); }
    };
    private Action key_pressed_SEMICOLON = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_SEMICOLON, ";"); }
    };
    private Action key_pressed_UNDERSCORE = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_UNDERSCORE, "_"); }
    };
    private Action key_pressed_LEFT_PARENTHESIS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_LEFT_PARENTHESIS, "("); }
    };
    private Action key_pressed_RIGHT_PARENTHESIS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_RIGHT_PARENTHESIS, ")"); }
    };
    private Action key_pressed_SPACE = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_SPACE, " "); }
    };

    // SPECIAL KEYS ------------------------------------------------------------------------------------
    private Action key_pressed_UP = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_UP, "up"); }
    };
    private Action key_pressed_RIGHT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_RIGHT, "right"); }
    };
    private Action key_pressed_DOWN = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_DOWN, "down"); }
    };
    private Action key_pressed_LEFT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_LEFT, "left"); }
    };
    private Action key_pressed_ENTER = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ENTER, "enter"); }
    };
    private Action key_pressed_BACK = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_BACK_SPACE, "back"); }
    };
    private Action key_pressed_ESC = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ESCAPE, "esc"); }
    };

    private void keyDownHandler(int key, String str){
        mc.keyDownHandler(key, str);
    }
}
