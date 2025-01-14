package ui;

import main.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchBar extends UserInterface {
    public JTextField textField = new JTextField();
    public JButton button = new JButton();

    Font foreFont = new Font("Arial", Font.ITALIC, 15);
    Font userTyped = new Font("Arial", Font.PLAIN, 15);

    Color foreColor = new Color(40, 40, 40);
    Color typedColor = new Color(30, 30, 30);
    Color backgroundColor = Color.gray;

    public final String foreMessage = "Search here!";

    public SearchBar(Panel p, int x, int y, int width, int height) {
        super(p);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setSearchBar();
    }

    void setSearchBar() {
        textField.setBounds(x, y, width, height);
        textField.setFont(foreFont);
        textField.setForeground(foreColor);
        textField.setBackground(backgroundColor);
        textField.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        textField.setText(foreMessage);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(textField.getText().equals(foreMessage)){
                    textField.setFont(userTyped);
                    textField.setForeground(typedColor);
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(textField.getText().isEmpty()){
                    textField.setFont(foreFont);
                    textField.setForeground(foreColor);
                    textField.setText(foreMessage);
                    p.resp.resSelect.searching = false;
                    p.repaint();
                }
            }
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(textField.getText().isEmpty() && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String userText = textField.getText();
                p.resp.resSelect.searching = !userText.isEmpty();
                if(e.getKeyCode() == KeyEvent.VK_ENTER) button.requestFocusInWindow();
                if(p.resp.resSelect.searching) p.resp.resSelect.searchItem(userText);
                p.repaint();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                return;
            }
        });
        p.add(button);
        p.add(textField);
    }

}
