package pacman_ultimater.project_base.core;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Class handling bindings of all the usefull keys.
 * Creates keyMap and inputMap for given panel using given keyDown handler.
 */
public class KeyBindings {

    private final JPanel panel;
    private IKeyDownHandler handler;

    public KeyBindings(JPanel panel, IKeyDownHandler handler)
    {
        this.panel = panel;
        this.handler = handler;
    }

    public void init(){
        setIMap();
        setAMap();
    }

    public void changeHandler(IKeyDownHandler handler){
        this.handler = handler;
    }

    private void setIMap()
    {
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
    }

    private void setAMap()
    {
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
    }

    // ALPHABET SYMBOLS -------------------------------------------------------------------------------
    private final Action key_pressed_P = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_P); }
    };
    private final Action key_pressed_O = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_O); }
    };
    private final Action key_pressed_I = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_I); }
    };
    private final Action key_pressed_U = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_U); }
    };
    private final Action key_pressed_Y = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Y); }
    };
    private final Action key_pressed_T = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_T); }
    };
    private final Action key_pressed_R = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_R); }
    };
    private final Action key_pressed_E = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_E); }
    };
    private final Action key_pressed_W = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_W); }
    };
    private final Action key_pressed_Q = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Q); }
    };
    private final Action key_pressed_L = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_L); }
    };
    private final Action key_pressed_K = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_K); }
    };
    private final Action key_pressed_J = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_J); }
    };
    private final Action key_pressed_H = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_H); }
    };
    private final Action key_pressed_G = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_G); }
    };
    private final Action key_pressed_F = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_F); }
    };
    private final Action key_pressed_D = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_D); }
    };
    private final Action key_pressed_S = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_S); }
    };
    private final Action key_pressed_A = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A); }
    };
    private final Action key_pressed_M = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_M); }
    };
    private final Action key_pressed_N = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_N); }
    };
    private final Action key_pressed_B = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_B); }
    };
    private final Action key_pressed_V = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_V); }
    };
    private final Action key_pressed_C = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_C); }
    };
    private final Action key_pressed_X = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_X); }
    };
    private final Action key_pressed_Z = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_Z); }
    };

    // NUMBER SYMBOLS ----------------------------------------------------------------------------------
    private final Action key_pressed_0 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_0); }
    };
    private final Action key_pressed_1 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_1); }
    };
    private final Action key_pressed_2 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_2); }
    };
    private final Action key_pressed_3 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_3); }
    };
    private final Action key_pressed_4 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_4); }
    };
    private final Action key_pressed_5 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_5); }
    };
    private final Action key_pressed_6 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_6); }
    };
    private final Action key_pressed_7 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_7); }
    };
    private final Action key_pressed_8 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_8); }
    };
    private final Action key_pressed_9 = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_9); }
    };

    // SPECIAL SYMBOLS ---------------------------------------------------------------------------------
    private final Action key_pressed_PLUS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_PLUS); }
    };
    private final Action key_pressed_MINUS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_MINUS); }
    };
    private final Action key_pressed_EQUALS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_EQUALS); }
    };
    private final Action key_pressed_SLASH = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_SLASH); }
    };
    private final Action key_pressed_BACK_SLASH = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_BACK_SLASH); }
    };
    private final Action key_pressed_ASTERISK = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ASTERISK); }
    };
    private final Action key_pressed_LESS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_LESS); }
    };
    private final Action key_pressed_GREATER = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_GREATER); }
    };
    private final Action key_pressed_COMMA = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_COMMA); }
    };
    private final Action key_pressed_HASH = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_NUMBER_SIGN); }
    };
    private final Action key_pressed_AT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_AT); }
    };
    private final Action key_pressed_EXCLAMATION_MARK = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_EXCLAMATION_MARK); }
    };
    private final Action key_pressed_AMPERSAND = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_AMPERSAND); }
    };
    private final Action key_pressed_PERIOD = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_PERIOD); }
    };
    private final Action key_pressed_COLON = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_COLON); }
    };
    private final Action key_pressed_SEMICOLON = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_SEMICOLON); }
    };
    private final Action key_pressed_UNDERSCORE = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_UNDERSCORE); }
    };
    private final Action key_pressed_LEFT_PARENTHESIS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_LEFT_PARENTHESIS); }
    };
    private final Action key_pressed_RIGHT_PARENTHESIS = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_RIGHT_PARENTHESIS); }
    };
    private final Action key_pressed_SPACE = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_SPACE); }
    };

    // SPECIAL KEYS ------------------------------------------------------------------------------------
    private final Action key_pressed_UP = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_UP); }
    };
    private final Action key_pressed_RIGHT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_RIGHT); }
    };
    private final Action key_pressed_DOWN = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_DOWN); }
    };
    private final Action key_pressed_LEFT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_LEFT); }
    };
    private final Action key_pressed_ENTER = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ENTER); }
    };
    private final Action key_pressed_BACK = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_BACK_SPACE); }
    };
    private final Action key_pressed_ESC = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ESCAPE); }
    };

    private void keyDownHandler(int key)
    {
        handler.handleKey(key);
    }
}
