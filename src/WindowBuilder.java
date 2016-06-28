import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class WindowBuilder extends JFrame {
    /**
     * @author Ubaby
     */
    private static final long serialVersionUID = 5152052669876967438L;
    private List<JButton> buttonList = new ArrayList<JButton>();
    private List<JPanel> contentPaneList = new ArrayList<JPanel>();
    private List<JTextArea> textAreaList = new ArrayList<JTextArea>();
    private List<JPasswordField> passwordList = new ArrayList<JPasswordField>();
    private JPanel container = new JPanel();
    private JPanel scrollContainer = new JPanel();

    private int whichInputPane = 0;
    private int whichPane = 0;
    private int whichButton = 0;
    private int whichTextArea = 0;
    private int outputTextAreaHeight = 500;

    /**
     * @param buttonList
     * @param contentPaneList
     * @throws HeadlessException
     */
    public WindowBuilder() {

    }

    public void initializeDisplay(String title, int width, int height) {
	setSize(width, height);
	// setLayout(new BorderLayout(1, 1));
	setTitle(title);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocation(0, 0);
	scrollContainer.setBackground(new Color(100, 100, 100));
	scrollContainer.setLayout(new BoxLayout(scrollContainer, BoxLayout.Y_AXIS));
    }

    public void setPanes(int amount) {
	contentPaneList = new ArrayList<JPanel>();
	for (int i = 0; i < amount; i++) {
	    contentPaneList.add(new JPanel());
	}
    }

    public void setButtons(int amount) {
	for (int i = 0; i < amount; i++) {
	    buttonList.add(new JButton());
	}
    }

    public void setButtonNames(String[] names) {
	for (int i = 0; i < buttonList.size(); i++) {
	    buttonList.get(i).setText(names[i]);
	}
    }

    public void setTextAreas(int amount) {
	for (int i = 0; i < amount; i++) {
	    textAreaList.add(new JTextArea());
	}
    }

    public void setPasswordAreas(int amount) {
	for (int i = 0; i < amount; i++) {
	    passwordList.add(new JPasswordField());
	}
    }

    public List<JPasswordField> getPasswordList() {
	return passwordList;
    }

    public void setTextAreaNames(String[] names) {
	for (int i = 0; i < textAreaList.size(); i++) {
	    textAreaList.get(i).setName(names[i]);
	}
    }

    public void setButtonsToPanes(int whichPane, int whichButton) {
	contentPaneList.get(whichPane).add(buttonList.get(whichButton));
    }

    public void setPasswordsToPanes(int whichPane, int whichPassword) {
	contentPaneList.get(whichPane).add(passwordList.get(whichPassword));
    }

    public void setTextToPanes(int whichPane, int whichText) {
	contentPaneList.get(whichPane).add(textAreaList.get(whichText));
    }

    public void setPanesToContent(int whichPane) {
	// container.add(contentPaneList.get(whichPane));
	scrollContainer.add(contentPaneList.get(whichPane));
    }

    public void setVisible() {
	// add(container);
	// scrollContainer.setLayout(new BorderLayout(2, 2));
	// scrollContainer.setLayout(new BorderLayout(10, 10));
	JScrollPane scrollOfScrollContainer = new JScrollPane(scrollContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	add(scrollOfScrollContainer);
	setVisible(true);
    }

    public JPanel getMainPanel() {
	return container;
    }

    public List<JTextArea> getTextAreas() {
	return textAreaList;
    }

    public List<JButton> getButtons() {
	return buttonList;
    }

    public List<JPanel> getPanes() {
	return contentPaneList;
    }

    public void setInputOutputArea() {
	contentPaneList.add(new JPanel());
	setPanesToContent(whichInputPane);
	getPanes().get(whichInputPane).setBackground(new Color(100, 100, 100));
	whichPane++;
	whichInputPane++;
    }

    public void setInputTextArea(String name, int where) {
	contentPaneList.add(new JPanel());

	getPanes().get(where).add(getPanes().get(whichPane));
	getPanes().get(whichPane).setBackground(new Color(151, 215, 45));

	setTextAreas(2);
	getTextAreas().get(whichTextArea).setText(name);
	getTextAreas().get(whichTextArea).setEditable(false);
	getTextAreas().get(whichTextArea).setBackground(new Color(250, 250, 250));
	setTextToPanes(whichPane, whichTextArea);
	whichTextArea++;
	getTextAreas().get(whichTextArea).setPreferredSize(new Dimension(150, 20));
	setTextToPanes(whichPane, whichTextArea);

	whichPane++;
	whichTextArea++;
    }

    public void setOutputTextArea(String name, int where) {
	contentPaneList.add(new JPanel());

	getPanes().get(where).add(getPanes().get(whichPane));
	getPanes().get(whichPane).setBackground(new Color(151, 215, 45));

	setTextAreas(2);
	getTextAreas().get(whichTextArea).setText(name);
	getTextAreas().get(whichTextArea).setEditable(false);
	getTextAreas().get(whichTextArea).setBackground(new Color(250, 250, 250));
	setTextToPanes(whichPane, whichTextArea);
	whichTextArea++;
	// getTextAreas().get(whichTextArea).setPreferredSize(new Dimension(700,
	// 500));

	JScrollPane scroll = new JScrollPane(getTextAreas().get(whichTextArea), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scroll.setPreferredSize(new Dimension(700, outputTextAreaHeight));

	contentPaneList.get(whichPane).add(scroll);

	whichPane++;
	whichTextArea++;
    }

    public void setButtonArea(String name, int where) {
	contentPaneList.add(new JPanel());
	getPanes().get(where).add(getPanes().get(whichPane));
	getPanes().get(whichPane).setBackground(new Color(151, 215, 45));

	setButtons(1);
	getButtons().get(whichButton).setText(name);
	setButtonsToPanes(whichPane, whichButton);

	whichPane++;
	whichButton++;
    }

    public void setOutputTextAreaHeights(int i) {
	outputTextAreaHeight = i;
    }

    public JTextArea getTextAreaByName(String name) {
	for (int i = 0; i < textAreaList.size(); i++) {
	    JTextArea t = textAreaList.get(i);
	    String cur = t.getText();
	    if (name.equals(cur)) {
		try {
		    JTextArea t2 = textAreaList.get(i + 1);
		    return t2;
		} catch (Exception e) {
		    return null;
		}
	    }
	}
	return null;
    }

    public void getButtonByName() {

    }
}
