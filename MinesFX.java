package mines;

import java.io.IOException;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MinesFX extends Application {
	
	private Button butt[][]; //button matrix that will hold the button grid
	private Mines mine;
	private int minesNum;
	private boolean GameOv;
	private Stage stage;
	private Controller control;
	private int height;
	private int width;
	private GridPane grid;
	private Random r = new Random(); 
	private boolean gridEmpty;
	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox root;
		try { 
			FXMLLoader load = new FXMLLoader();
			load.setLocation(getClass().getResource("MineSweeper.fxml"));
			root = load.load();
			control = load.getController(); //holds those controllers classes
			this.stage=stage; 
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene s = new Scene(root);
		stage.setScene(s);
		stage.setTitle("The New MineSweeper");
		stage.show();
		gridEmpty=true;
		Button reset = control.getResetbutton(); 
		reset.setOnAction(new Reset(control, root)); //setting handler for the reset button
	}

	public GridPane minegrid(int height,int width) //creates buttons gridpane for mine board
	{
		GridPane gp = new GridPane();
		for (int i = 0 ;i < height;i++)
			for (int j=0;j<width;j++)
			{ butt[i][j] = new Button(mine.get(i, j));
				Button b=butt[i][j];
				b.setPrefSize(50, 50); 
				b.setMaxSize(50, 50); //buttons sizes
				b.setMinSize(45, 45);
				b.setOnMouseClicked(new ButtonClick(i,j)); //setting event handler for each button (mouse event)
				b.setFont(new Font("Arial",20));
				gp.add(b, i, j); //adding button to grid
			}
		gp.setPadding(new Insets(60));
		//adding mines to the grid in random places
		for (int i = 0 ; i < minesNum ; i++) mine.addMine(r.nextInt(height), r.nextInt(width));
		return gp;

	}

	 class Reset implements EventHandler<ActionEvent> //in case reset button has pressed
	 {
		 private Controller c;
		 private HBox root;
		 public Reset(Controller c,HBox root)
		 {
			 this.c=c;
			 this.root=root;
		 }

		@Override
		public void handle(ActionEvent event) { //will create new board with correct parameters from the positions
			if (!gridEmpty)
				root.getChildren().remove(grid);
			gridEmpty=false;
			GameOv=false;
			height=c.getHeight(); //Receiving height& width from textfield
			width=c.getWidth(); 
			minesNum=c.getMines(); //Receiving mines number
			if (height==0||width==0) return; //in case there is not any number there
			mine = new Mines(height,width,minesNum); 
			butt = new Button[height][width]; 
			grid=minegrid(height,width); //creating the buttons to grid
			root.getChildren().add(grid); 
			stage.setMaxHeight(1600);
			stage.setMaxWidth(1600);
			stage.setHeight(width*60+150); //setting window height&width
			stage.setWidth(height*65+150); 

		}

	 }

	 class ButtonClick implements EventHandler<MouseEvent> //mouse event handler for clicking on game buttons
	 {
		 private int x,y; // useful colors to paint the flags and mines buttons
		 private BackgroundFill bgFill = new BackgroundFill(Color.BLACK, new CornerRadii(10), null);    
	     private Background bg = new Background(bgFill);
	     private BackgroundFill bgFill2 = new BackgroundFill(Color.LIGHTPINK, new CornerRadii(10), null);    
	     private Background fg = new Background(bgFill2);
	     private BackgroundFill bgFill3 = new BackgroundFill(Color.gray(0.9), new CornerRadii(10), null);    
	     private Background gg = new Background(bgFill3);
		 public ButtonClick(int x,int y) //sets click position
		 {
			 this.x=x; this.y=y;
			 
			 
		 }

		@Override
		public void handle(MouseEvent click) {
			MouseButton mb = click.getButton(); //mb will hold which button of the mouse was pressed
			if (mb==MouseButton.PRIMARY && mine.get(x, y)!="F") //in case leftclick of the mouse that played and its not a flag
			{
				if (!mine.isDone())
				{
					if(!mine.open(x, y)) //in case a mine was opened
					{
						mine.setShowAll(true); //hitting mine will put the show on true
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Lose");
						alert.setHeaderText(null);
						alert.setContentText("ouchh! it was a mine!");
				        butt[x][y].setBackground(bg);
				        for (int i = 0 ; i <control.getHeight();i++) //coloring the all rest mines in black
							for (int j=0;j<control.getWidth();j++) 
									if(!mine.open(i, j))
										butt[i][j].setBackground(bg);
				        
						if (!GameOv) 
							alert.show();
						GameOv=true;
					}
				}

				 if (mine.isDone()) //if the user won, means there are no mines open and closed places
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Win");
					alert.setHeaderText(null);
					alert.setContentText("yeyy you won!!");
					if (!GameOv)
						alert.show();
					GameOv=true;
				}
				for (int i = 0 ; i <control.getHeight() ;i++)// make the upcoming board
					for (int j=0;j<control.getWidth();j++) 
						butt[i][j].setText(mine.get(i, j));
						
					
				
			}
			else if (mb==MouseButton.SECONDARY) //right click on the mouse will put flag in the position
			{
				mine.toggleFlag(x, y);
				butt[x][y].setText(mine.get(x, y));
				if(butt[x][y].toString().contains("F")) { 
					if(butt[x][y].getBackground().equals(fg)) // will paint the Flags buttons
						butt[x][y].setBackground(gg);
					else 
						butt[x][y].setBackground(fg);

				} //paint back the controller
				else 
					if(butt[x][y].getBackground().equals(fg))
						butt[x][y].setBackground(gg);

			}
		}

	 }

}



