package model;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import controller.Controller;
public class CaptchaPrinter {
    public void print(String captcha){
        int width = 170;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(captcha, 10, 20);
        String gridCharacters = "*#@$";
        int random = Controller.randomNumber(2) , randomChar = Controller.randomNumber(gridCharacters.length());
        char grid = (random == 1) ? ' ' : gridCharacters.charAt(randomChar);
        char printer = (random == 0) ? ' ' : gridCharacters.charAt(randomChar);
        char noise;
        if(random == 0) noise = gridCharacters.charAt((randomChar + 1) % gridCharacters.length());
        else noise = ' ';
        for (int y = 0; y < height; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < width; x++){
                if(image.getRGB(x, y) != -16777216)
                    stringBuilder.append((Controller.randomNumber(10) == 0) ? noise : printer);
                else
                    stringBuilder.append(grid);
            } 
            if (stringBuilder.toString().trim().isEmpty())continue;
            System.out.println(stringBuilder);
        }
    }
}