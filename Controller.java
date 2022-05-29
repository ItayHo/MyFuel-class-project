package mines;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

	   @FXML
	  public Button resetbutton;
	   @FXML
	   public TextField widthbox;
	   @FXML
	   public TextField heightbox;
	   @FXML
	   public TextField minesbox;
	   @FXML

	public Button getResetbutton() { //return reset option by button
		return resetbutton;
	}

	public int getHeight() { //returns the integer value of the text height inside the hbox
		int ret;
		try {
			ret = Integer.parseInt(heightbox.getText());

			return ret > 0 ? ret : 0; 
		}
		catch(Exception e) 
		{
			return 0;
		}
	}
	public int getWidth() { ////returns the integer value of the text width inside the hbox
		int ret;
		try {
			ret = Integer.parseInt(widthbox.getText());
			return ret > 0 ? ret : 0;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	public int getMines() { //returns the mines inside the text box
		int ret;
		try {
			ret = Integer.parseInt(minesbox.getText());
			return ret > 0 ? ret : 0;
		}
		catch(Exception e)
		{
			return 0;
		}
	}


}
