package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	
	static final int SIZE = 500;
	
	static final int NUM_OF_DIV_X = 5;
	static final int NUM_OF_DIV_Y = 5;
	
	//Must be a possible value or else code with crash (Safe value is 2 * (SIZE / NUM_OF_DIV_X))
	static final float MAX_DISTANCE = 140.0f;
	
	static final boolean INVERTED = true;
	static final boolean RED_CHANNEL = true;
	static final boolean GREEN_CHANNEL = false;
	static final boolean BLUE_CHANNEL = false;
	
	static final int DIFFUSE_LEVEL = 3;
	
	static int[][] noiseNodeXList = new int[NUM_OF_DIV_Y][NUM_OF_DIV_X];
	static int[][] noiseNodeYList = new int[NUM_OF_DIV_Y][NUM_OF_DIV_X];
	
	@Override
	public void start(Stage primaryStage) {
		
		Pane root = new Pane();
		Scene scene = new Scene(root, SIZE, SIZE);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		for(int i = 0; i < DIFFUSE_LEVEL; i++) {
		
			int partialSizeX = SIZE / NUM_OF_DIV_X;
			int partialSizeY = SIZE / NUM_OF_DIV_Y;
			
			//Creates the "Peaks" or "nodes"
			for(int divX = 0; divX < NUM_OF_DIV_X; divX++) {
				for(int divY = 0; divY < NUM_OF_DIV_Y; divY++) {
					noiseNodeXList[divY][divX] = (int) (Math.random() * partialSizeX) + (divX * partialSizeX);
					noiseNodeYList[divY][divX] = (int) (Math.random() * partialSizeY) + (divY * partialSizeY);
				}
			}
			
			//Creates each pixel as a coloured rectangle
			for(int x = 0; x < SIZE; x++) {
				for(int y = 0; y < SIZE; y++) {
					Rectangle r = new Rectangle();
					r.setLayoutX(x);
					r.setLayoutY(y);
					r.setWidth(1);
					r.setHeight(1);
					float minDistance = 10000.0f;
					float distance = 0.0f;
					for(int divX = 0; divX < NUM_OF_DIV_X; divX++) {
						for(int divY = 0; divY < NUM_OF_DIV_Y; divY++) {
							distance = (float) Math.sqrt(
									Math.pow(noiseNodeXList[divY][divX] - x, 2) +
									Math.pow(noiseNodeYList[divY][divX] - y, 2)
									);
							if(distance < minDistance)
								minDistance = distance;
						}
					}
					//Colour proportional to distance
					r.setFill(INVERTED ? 
						new Color(RED_CHANNEL ? Math.max(Math.min(1.0 - minDistance/MAX_DISTANCE, 1.0), 0.0) : 0.0, 
								GREEN_CHANNEL ? Math.max(Math.min(1.0 - minDistance/MAX_DISTANCE, 1.0), 0.0) : 0.0, 
								BLUE_CHANNEL ? Math.max(Math.min(1.0 - minDistance/MAX_DISTANCE, 1.0), 0.0) : 0.0, 
								1.0f / ((float) DIFFUSE_LEVEL)
							) :
						new Color(RED_CHANNEL ? Math.max(Math.min(minDistance/MAX_DISTANCE, 1.0), 0.0) : 0.0, 
								GREEN_CHANNEL ? Math.max(Math.min(minDistance/MAX_DISTANCE, 1.0), 0.0) : 0.0, 
								BLUE_CHANNEL ? Math.max(Math.min(minDistance/MAX_DISTANCE, 1.0), 0.0) : 0.0, 
								1.0f / ((float) DIFFUSE_LEVEL)
							)
					);
					root.getChildren().add(r);
				}
			}
		
		}
		
//		WritableImage image = root.snapshot(new SnapshotParameters(), null);
//	    File file = new File("out.png");
//	    try { ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file); } 
//	    catch (IOException e) { System.out.println("Bad code lmao"); }
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
