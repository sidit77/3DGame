package com.base.game;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Game;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.LoggingLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Starter {

    private static JDialog dialog;

    private static int height, width, framerate;
    private static boolean vsync, resizeable;
    private static Game game;

    public static void main(String[] args){
        Configurator.defaultConfig().level(LoggingLevel.DEBUG).formatPattern("{level}: {message}").activate();

        ArrayList<String> modeNames = new ArrayList<String>();
        HashMap<String, org.lwjgl.opengl.DisplayMode> modesMap = new HashMap<String, org.lwjgl.opengl.DisplayMode>();

        org.lwjgl.opengl.DisplayMode[] modes = new org.lwjgl.opengl.DisplayMode[0];
        try {
            modes = Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        for (int i=0;i<modes.length;i++) {
            org.lwjgl.opengl.DisplayMode current = modes[i];
            modeNames.add(current.getWidth() + "x" + current.getHeight() + " " + current.getBitsPerPixel() + "bits " + current.getFrequency() + "Hz");
            modesMap.put(current.getWidth() + "x" + current.getHeight() + " " + current.getBitsPerPixel() + "bits " + current.getFrequency() + "Hz", current);
        }

        int sizeX = 600, sizeY = 400;
        dialog = new JDialog((JFrame)null, "Options", true);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds(dimension.width/2-sizeX/2,dimension.height/2-sizeY/2, sizeX, sizeY);
        dialog.setLayout(null);

        JButton button = new JButton("START");
        button.setBounds(sizeX/2-50, sizeY-80, 100, 30);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dialog.setVisible(false);
            }
        });
        dialog.add(button, null);

        JLabel widthLabel = new JLabel("Width: ");
        widthLabel.setBounds(30,30,50,25);
        dialog.add(widthLabel);

        JTextField widthField = new JTextField("1280");
        widthField.setBounds(80, 30, 100, 25);
        dialog.add(widthField);


        JLabel heightLabel = new JLabel("Height: ");
        heightLabel.setBounds(30,70,50,25);
        dialog.add(heightLabel);

        JTextField heightField = new JTextField("720");
        heightField.setBounds(80, 70, 100, 25);
        dialog.add(heightField);


        JLabel fpsLabel = new JLabel("FPS: ");
        fpsLabel.setBounds(30,110,50,25);
        dialog.add(fpsLabel);

        JTextField fpsField = new JTextField("60");
        fpsField.setBounds(80, 110, 100, 25);
        dialog.add(fpsField);


        JCheckBox vsyncCheckBox = new JCheckBox("VSync");
        vsyncCheckBox.setBounds(30,150,100,25);
        vsyncCheckBox.setSelected(true);
        dialog.add(vsyncCheckBox);

        JCheckBox resizeableCheckBox = new JCheckBox("Resizeable");
        resizeableCheckBox.setBounds(30,180,100,25);
        dialog.add(resizeableCheckBox);


        HashMap<String, Game> games = new HashMap<String, Game>();
        games.put("TestGame", new TestGame());
        games.put("TestGame2", new TestGame2());
        Object[] options = games.keySet().toArray();
        JComboBox gamesSelection = new JComboBox(options);
        gamesSelection.setBounds(30, 220, 100, 25);
        dialog.add(gamesSelection);

        dialog.setVisible(true);

        try {
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
            framerate = Integer.parseInt(fpsField.getText());
            vsync = vsyncCheckBox.isSelected();
            resizeable = resizeableCheckBox.isSelected();
            game = games.get(gamesSelection.getSelectedItem());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "false Input");
            Logger.error(e);
            dialog.setVisible(true);
        }

        CoreEngine engine = new CoreEngine(width, height, framerate, vsync, resizeable, game);
        engine.createWindow("3D Game Engine");
        engine.start();

        System.exit(0);
    }

}
