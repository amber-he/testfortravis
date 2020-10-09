import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.*;

import org.apache.commons.io.FilenameUtils;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.*;
import org.odftoolkit.odfdom.doc.OdfTextDocument;


public class notepad extends Application {

    private TextArea text;
    public String path;
    public int index =0;


    private void Textadd(BorderPane borderPane) {
        text = new TextArea();
        borderPane.setCenter(text);
    }
    public  void  newfunction(Stage primaryStage,Scene scene){
        Stage stage =primaryStage;
        text.setText("");
        primaryStage.close();
        stage.setScene(scene);
        stage.show();
    }
    public void openfunction(Stage primaryStage) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File openFile = fileChooser.showOpenDialog(primaryStage);


        if (openFile != null && openFile.exists()) {

            try {
                String fileName=openFile.getName();
//                    String suffix=fileName.substring(fileName.lastIndexOf("."));
                String extension = FilenameUtils.getExtension(fileName);
                if("odt".equalsIgnoreCase(extension)){
                    OdfTextDocument textDocument = OdfTextDocument.loadDocument(openFile);
                    String texts = textDocument.getContentRoot().getTextContent();
                    text.setText(texts);
                }else {
                    FileInputStream in = new FileInputStream(openFile);
                    byte[] b = new byte[(int) openFile.length()];
                    in.read(b);
                    text.setText(new String(b));
                    in.close();
                }
                //-------------------------------------------------
                path = openFile.getPath();
                int lastIndex = path.lastIndexOf("\\");
                String title = path.substring(lastIndex + 1);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void savefounction(Stage primaryStage){
        if (path == null) {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT","*.txt"));
            File f = fileChooser.showSaveDialog(primaryStage);
            if (f != null && f.exists()) {
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    out.write(text.getText().getBytes());
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
                    out.write(text.getText().getBytes());
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
    public void btn1function(TextField tf1){
        String get = tf1.getText();
        int k=0;
        String wholecontent= text.getText();
        if(wholecontent.length()==0){
            JOptionPane.showMessageDialog(null,"can not found what you are looking for","search result",JOptionPane.INFORMATION_MESSAGE);
        }
        if(wholecontent.contains(get)){
            k=wholecontent.indexOf(get);
            text.positionCaret(k);
            text.selectRange(k,k+get.length());
            index=k+get.length();
        }

        else{
            JOptionPane.showMessageDialog(null,"can not found what you are looking for","search result",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void btn2function(TextField tf1){
        if (text.getText().length() == 0) {
            JOptionPane.showMessageDialog(null,"can not found what you are looking for","search result",JOptionPane.INFORMATION_MESSAGE);

        } else {
            String get = tf1.getText();
            int k = text.getText().indexOf(get, index);
            if (k > -1) {
                text.positionCaret(k);
                text.selectRange(k, k + get.length());
                index = k + get.length();
            } else {
                JOptionPane.showMessageDialog(null, "can not found what you are looking for", "search result", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        primaryStage.setTitle("Test Editor");
        BorderPane borderPane = new BorderPane();
        Textadd(borderPane);

        MenuBar menuBar = new MenuBar();
        borderPane.setTop(menuBar);
        Menu file = new Menu("File");
//        file.setMnemonicParsing(true);
        Menu search = new Menu("Search");
//        search.setMnemonicParsing(true);
        Menu view = new Menu("View");
        Menu manage = new Menu("Manage");
        Menu help = new Menu("Help");

//菜单子菜单
        MenuItem New = new MenuItem("New");
        MenuItem Open = new MenuItem("Open");
        MenuItem Save = new MenuItem("Save");
        MenuItem saveaspdf = new MenuItem("Save as pdf");
        MenuItem Exit = new MenuItem("Exit");
        MenuItem Print = new MenuItem("Print");
        file.getItems().addAll(New, Open, Save,saveaspdf,Exit,Print);

        MenuItem Search =new MenuItem("Search");
        search.getItems().addAll(Search);

        MenuItem paste = new MenuItem("Paste");
        MenuItem timedate =new MenuItem("time&date");
        MenuItem cut = new MenuItem("cut");
        MenuItem Copy = new MenuItem("Copy");
        MenuItem SelectAll = new MenuItem("Select ALL");

        manage.getItems().addAll(timedate,paste,cut,Copy,SelectAll);


        MenuItem about = new MenuItem("About");
        help.getItems().addAll(about);



        menuBar.getMenus().addAll(file, search, view, manage, help);


        Scene scene = new Scene(borderPane, 500, 500);

        EventHandler<ActionEvent> NewHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newfunction(primaryStage,scene);
            }
        };

        EventHandler<ActionEvent> OpenHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    openfunction(primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        EventHandler<ActionEvent> SaveHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                savefounction(primaryStage);
            }
        };
        EventHandler<ActionEvent> SearchHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HBox h1 = new HBox();
                h1.setPadding(new Insets(20,5,20,5));
                h1.setSpacing(5);
                Label label1 = new Label("Search item");
                TextField tf1 = new TextField();
                h1.getChildren().addAll(label1,tf1);

                VBox v1 = new VBox();
                v1.setPadding(new Insets(20,5,20,5));
                Button btn1 = new Button("find");
                Button btn2 = new Button("find next one");
                v1.getChildren().addAll(btn1,btn2);

                HBox root = new HBox();
                root.getChildren().addAll(h1, v1);

                Stage findstage = new Stage();
                Scene findscnen = new Scene(root, 600, 100);
                findstage.setTitle("Search item");
                findstage.setScene(findscnen);
                findstage.setResizable(false);
                findstage.show();

                EventHandler btn1handler = new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        btn1function(tf1);
                    }
                };
                EventHandler btn2handler = new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        btn2function(tf1);
                    }
                };

                btn1.setOnAction(btn1handler);
                btn2.setOnAction(btn2handler);
            }
        };

        EventHandler tdhandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                text.insertText(0,simpleDateFormat.format(date)+"\n");
            }
        };

        EventHandler abouthandler =new EventHandler() {
            @Override
            public void handle(Event event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("About us");
                alert.setHeaderText("About project&us");
                alert.setContentText("team member:\n   YiTong HE(student number:19029757)\n   MeiChan Guo(student number:19029840)\nclass:\n   IOT NZ181\nother information:\n   this project we work together,We developed different functional modules and be responsible for different test.");
                alert.showAndWait();
            }
        };

        EventHandler pastehandler =new EventHandler() {
            @Override
            public void handle(Event event) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                Clipboard c = clipboard.getSystemClipboard();
                if (c.hasContent(DataFormat.PLAIN_TEXT)) {
                    String s = c.getContent(DataFormat.PLAIN_TEXT).toString();
                    if (text.getSelectedText() != null) {
                        text.replaceSelection(s);
                    } else {
                        int mouse = text.getCaretPosition();
                        text.insertText(mouse, s);
                    }
                }
            }
        };


        EventHandler cuthandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                String t = text.getSelectedText();
                content.putString(t);
                clipboard.setContent(content);
                text.replaceSelection("");
            }
        };

        EventHandler saveaspdfHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                Document document =new Document();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save as pdf");
                File f = fileChooser.showSaveDialog(primaryStage);
                try {
                    PdfWriter.getInstance(document,new FileOutputStream(f.getAbsoluteFile()+".pdf"));
                    document.open();
                    String a = text.getText();
                    document.add(new Paragraph(a));
                    document.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        };

        EventHandler<ActionEvent> SelectAllHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                text.selectAll();
            }
        };

        EventHandler<ActionEvent> CopyHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                String temp = text.getSelectedText();
                content.putString(temp);
                clipboard.setContent(content);
            }
        };

        EventHandler<ActionEvent> PrintHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File f =new File("printer.txt");
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (f.exists()) {
                    try {
                        FileOutputStream out = new FileOutputStream(f);
                        out.write(text.getText().getBytes());
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                DocFlavor flavour = DocFlavor.INPUT_STREAM.AUTOSENSE;
                PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavour, pras);
                PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
                PrintService service = ServiceUI.printDialog(null, 200, 200, printService,defaultService, flavour, pras);
                if(service != null){
                    try {
                        DocPrintJob job = service.createPrintJob(); //创建打印作业
                        FileInputStream fis = new FileInputStream(f); //构造待打印的文件流
                        DocAttributeSet das = new HashDocAttributeSet();
                        Doc doc = new SimpleDoc(fis, flavour, das);
                        job.print(doc, pras);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                f.delete();
            }
        };



        New.setOnAction(NewHandler);
        Open.setOnAction(OpenHandler);
        Save.setOnAction(SaveHandler);
        Search.setOnAction(SearchHandler);
        timedate.setOnAction(tdhandler);
        about.setOnAction(abouthandler);
        paste.setOnAction(pastehandler);
        Copy.setOnAction(CopyHandler);
        cut.setOnAction(cuthandler);
        Print.setOnAction(PrintHandler);
        saveaspdf.setOnAction(saveaspdfHandler);
        SelectAll.setOnAction(SelectAllHandler);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}