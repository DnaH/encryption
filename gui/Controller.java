package gui;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import RC2.RC2;

public class Controller
{
	@FXML private TextArea inputText;
	@FXML private TextArea outputText;
	@FXML private TextField inputKey;
	@FXML private TextField inputFile;
	@FXML private TextField outputFile;
	@FXML private RadioButton radioFile;
	@FXML private RadioButton radioText;
	private Stage primaryStage;
	
	@FXML
	public void handleEncrypt()
	{
		if(radioText.isSelected())
		{
			encryptText();
		}
		else
		{
			encryptFile();
		}
		
	}
	
	@FXML
	public void handleDecrypt()
	{
		if(radioText.isSelected())
		{
			decryptText();
		}
		else
		{
			decryptFile();
		}
	}
	
	@FXML
	public void handleChooseInFile()
	{
		FileChooser choose = new FileChooser();
		choose.setTitle("Eingabe - Datei auswählen...");
		choose.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
		
		File selectedFile = choose.showOpenDialog(primaryStage);
		if (selectedFile != null)
		{
			inputFile.setText(selectedFile.toString());
		}
	}
	
	@FXML
	public void handleChooseOutFile()
	{
		FileChooser choose = new FileChooser();
		choose.setTitle("Ausgabe - Datei auswählen...");
		choose.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
		
		File selectedFile = choose.showOpenDialog(primaryStage);
		if (selectedFile != null)
		{
			outputFile.setText(selectedFile.toString());
		}
	}
	
	private void encryptText()
	{
		if(inputText.getText().length() == 0)
		{
			inputText.setPromptText("Bitte geben Sie einen Eingabe-Text ein.");
			if(inputKey.getText().length() == 0)
			{
				inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			}
			return;
		}
		if(inputKey.getText().length() == 0)
		{
			inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			return;
		}
			
		outputText.setText(RC2.encrypt(inputText.getText(), inputKey.getText()));
	}
	
	private void decryptText()
	{
		if(inputText.getText().length() == 0)
		{
			inputText.setPromptText("Bitte geben Sie einen Eingabe-Text ein.");
			if(inputKey.getText().length() == 0)
			{
				inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			}
			return;
		}
		if(inputKey.getText().length() == 0)
		{
			inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			return;
		}
			
		outputText.setText(RC2.decrypt(inputText.getText(), inputKey.getText()));
	}

	private void encryptFile()
	{
		if(inputFile.getText().length() == 0)
		{
			inputFile.setPromptText("Bitte geben Sie einen Dateipfad ein.");
			if(inputKey.getText().length() == 0)
			{
				inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			}
			return;
		}
		if(inputKey.getText().length() == 0)
		{
			inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			return;
		}
		if(outputFile.getText().length() == 0)
		{
			outputFile.setText("Bitte geben Sie einen Dateipfad ein.");
			return;
		}
		
		try
		{
			File file1 = new File(inputFile.getText());
			File file2 = new File(outputFile.getText());
			RandomAccessFile infile = new RandomAccessFile(file1, "r");
			RandomAccessFile outfile = new RandomAccessFile(file2, "rw");
			
			long block = infile.readLong();
			
			while(block != -1)
			{				
				outfile.writeLong(RC2.encryptLong(block, inputKey.getText()));
				block = infile.readLong();
			}
			
			infile.close();
			outfile.close();
		}
		catch(NullPointerException | FileNotFoundException e)
		{
			inputFile.setText("Bitte geben Sie einen gültigen Dateipfad an.");
		} catch (IOException e)
		{
			
		}
	}
	
	private void decryptFile()
	{
		if(inputFile.getText().length() == 0)
		{
			inputFile.setPromptText("Bitte geben Sie einen Dateipfad ein.");
			if(inputKey.getText().length() == 0)
			{
				inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			}
			return;
		}
		if(inputKey.getText().length() == 0)
		{
			inputKey.setPromptText("Bitte geben Sie einen Schlüssel ein.");
			return;
		}
		if(outputFile.getText().length() == 0)
		{
			outputFile.setText("Bitte geben Sie einen Dateipfad ein.");
			return;
		}
		
		try
		{
			File file1 = new File(inputFile.getText());
			File file2 = new File(outputFile.getText());
			RandomAccessFile infile = new RandomAccessFile(file1, "r");
			RandomAccessFile outfile = new RandomAccessFile(file2, "rw");
			
			long block = infile.readLong();
			
			while(block != -1)
			{				
				outfile.writeLong(RC2.decryptLong(block, inputKey.getText()));
				block = infile.readLong();
			}
			
			infile.close();
			outfile.close();
		}
		catch(NullPointerException | FileNotFoundException e)
		{
			inputFile.setText("Bitte geben Sie einen gültigen Dateipfad an.");
		} catch (IOException e)
		{
			
		}
	}

}
