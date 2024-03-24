package com.example.buttondrug;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;

import java.io.IOException;

public class ButtonDrug extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group group = new Group();
        Group btngroup = new Group();
        group.getChildren().add(btngroup);
        Scene scene = new Scene(group, 800, 600);

        Group rectsGroup = new Group();
        group.getChildren().add(rectsGroup);
        RectangleHandler rects = new RectangleHandler(rectsGroup);


        ButtonWrapper button1 = new ButtonWrapper("First button", btngroup, rects);
        button1.setPrefSize(200, 200);
        button1.setLayoutX(120);
        button1.setLayoutY(120);
        btngroup.getChildren().add(button1);

        ButtonWrapper button2 = new ButtonWrapper("Second button", btngroup, rects);
        button2.setPrefSize(200, 200);
        button2.setLayoutX(140);
        button2.setLayoutY(140);
        btngroup.getChildren().add(button2);

        ButtonWrapper button3 = new ButtonWrapper("Third button", btngroup, rects);
        button3.setPrefSize(200, 200);
        button3.setLayoutX(160);
        button3.setLayoutY(160);
        btngroup.getChildren().add(button3);

        rects.addButton(button1);
        rects.addButton(button2);
        rects.addButton(button3);
        
        stage.setTitle("Nikulin Lev: first GUI app");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

class ButtonClickHandler implements EventHandler<MouseEvent>{
    ButtonWrapper button;
    
    ButtonClickHandler(ButtonWrapper b){
        this.button = b;
    }

    @Override
    public void handle(MouseEvent event) {
        button.setText("Button pressed");
        button.clickStart[0] = event.getX();
        button.clickStart[1] = event.getY();
        button.group.getChildren().remove(button);
        button.group.getChildren().add(button);
    }
}

class ButtonDragHandler implements EventHandler<MouseEvent>{
    ButtonWrapper button;
    RectangleHandler r;
    
    ButtonDragHandler(ButtonWrapper b, RectangleHandler r){
        this.button = b;
        this.r = r;
    }

    @Override
    public void handle(MouseEvent event) {
        double newx = event.getSceneX();
        double newy = event.getSceneY();
        if (newx >=0 && newx <= 800) button.setLayoutX(newx - button.clickStart[0]);
        if (newy >=0 && newy <= 600) button.setLayoutY(newy - button.clickStart[1]);
        r.recomputeButton(button);
    }
}

class ButtonReleaseHandler implements EventHandler<MouseEvent>{
    ButtonWrapper button;
    
    ButtonReleaseHandler(ButtonWrapper b){
        this.button = b;
    }

    @Override
    public void handle(MouseEvent event) {
        button.setText(button.text);
    }
}

class ButtonWrapper extends Button {
    double[] clickStart = new double[2];
    String text;
    Group group;
    EventHandler<MouseEvent> clickHandler, dragHandler, releaseHandler;

    ButtonWrapper(String text, Group group, RectangleHandler r){
        super(text);
        this.text = text;
        this.group = group;
        clickHandler = new ButtonClickHandler(this);
        dragHandler = new ButtonDragHandler(this, r);
        releaseHandler = new ButtonReleaseHandler(this);
        this.setOnMousePressed(clickHandler);
        this.setOnMouseDragged(dragHandler);
        this.setOnMouseReleased(releaseHandler);
    }
}

class RectangleHandler{
    Group group = new Group();
    ArrayList<ButtonWrapper> buttons = new ArrayList<ButtonWrapper>();
    ArrayList<ArrayList<Rectangle>> rectangles = new ArrayList<ArrayList<Rectangle>>();

    RectangleHandler(Group group){
        this.group = group;
    }

    void addButton(ButtonWrapper button){
        buttons.add(button);
        if(buttons.size() > 1){
            rectangles.add(new ArrayList<Rectangle>());
            for(int i = 0; i < buttons.size() - 1; i++){
                int j = buttons.size() - 1;
                //find intersection
                double x1, y1, w1, h1, x2, y2, w2, h2;
                x1 = buttons.get(i).getLayoutX();
                y1 = buttons.get(i).getLayoutY();
                h1 = buttons.get(i).getPrefHeight();
                w1 = buttons.get(i).getPrefWidth();
                x2 = buttons.get(j).getLayoutX();
                y2 = buttons.get(j).getLayoutY();
                h2 = buttons.get(j).getPrefHeight();
                w2 = buttons.get(j).getPrefWidth();
                if(Math.abs(x2-x1) < (w2+w1)/2 && Math.abs(y2-y1) < (h2+h1)/2){
                    double ix, iy, iw, ih;
                    iw = (w2+w1)/2 - Math.abs(x2-x1);
                    ih = (h2+h1)/2 - Math.abs(y2-y1);
                    ix = (x1 + x2 + w1/2 + w2/2)/2;
                    iy = (y1 + y2 + h1/2 + h2/2)/2;
                    Rectangle rect = (ih > 0 && iw > 0) ? new Rectangle(ix-iw/2, iy-ih/2, iw, ih) : new Rectangle(ix-iw/2, iy-ih/2, 0, 0);
                    group.getChildren().add(rect);
                    rectangles.get(j-1).add(rect);
                }
            }
        }
    }

    public void recomputeButton(ButtonWrapper btn){

        for(int i = 0; i < buttons.size(); i++){
            int j = buttons.indexOf(btn);
            if(i == j) continue;
            //find intersection
            double x1, y1, w1, h1, x2, y2, w2, h2;
            x1 = buttons.get(i).getLayoutX();
            y1 = buttons.get(i).getLayoutY();
            h1 = buttons.get(i).getPrefHeight();
            w1 = buttons.get(i).getPrefWidth();
            x2 = buttons.get(j).getLayoutX();
            y2 = buttons.get(j).getLayoutY();
            h2 = buttons.get(j).getPrefHeight();
            w2 = buttons.get(j).getPrefWidth();
            double ix, iy, iw, ih;
            ix = (x1 + x2 + w1/2 + w2/2) / 2;
            iy = (y1 + y2 + h1/2 + h2/2) / 2;
            if(Math.abs(x2-x1) < (w2+w1)/2 && Math.abs(y2-y1) < (h2+h1)/2){
                iw = (w2+w1)/2 - Math.abs(x2-x1);
                ih = (h2+h1)/2 - Math.abs(y2-y1);
            }
            else{
                iw = ih = 0;
            }
            
            Rectangle rect;
            if(i < j) rect = rectangles.get(j-1).get(i);
            else rect =  rectangles.get(i-1).get(j);

            rect.relocate(ix - iw / 2, iy - ih / 2);
            rect.setWidth(iw);
            rect.setHeight(ih);
        }
    }
}