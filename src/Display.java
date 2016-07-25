import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
/**
 * @author Ubaby The MIT License (MIT) Copyright (c)
 * 
 *         <2016><Gintaras Koncevicius>
 * 
 *         Permission is hereby granted, free of charge, to any person obtaining
 *         a copy of this software and associated documentation files (the
 *         "Software"), to deal in the Software without restriction, including
 *         without limitation the rights to use, copy, modify, merge, publish,
 *         distribute, sublicense, and/or sell copies of the Software, and to
 *         permit persons to whom the Software is furnished to do so, subject to
 *         the following conditions:
 * 
 *         The above copyright notice and this permission notice shall be
 *         included in all copies or substantial portions of the Software.
 * 
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *         EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *         MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *         NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *         BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *         ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *         CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *         SOFTWARE.
 */
public class Display extends JFrame implements ActionListener {
    private static final long serialVersionUID = 4193823759488399578L;
    private WindowBuilder w = new WindowBuilder();
    final JFileChooser fileChooser = new JFileChooser();

    // buttons
    private String buttonFindLearn = "1. Find Learn CSV File";
    private String buttonFindPredict = "2. Find Predict CSV File";
    private String buttonLoadFiles = "3. Load Files";
    private String buttonLearnFast = "4. Learn";
    private String buttonPredictFast = "5. Predict";
    private String buttonFindBestSettings = "Find Best Settings";
    private String buttonFindBestLearningSubset = "Find Best Learning Subset";
    private String buttonFindBestPredictSensitivity = "Find Best Prediction Sensitivity";
    private String buttonSaveSettings = "Save Settings";
    private String buttonLoadPreviousFiles = "Load PreviousFiles";

    // inputs
    private String inputLearnDir = "Learn CSV dir: ";
    private String inputPredictDir = "Predict CSV dir: ";

    private String inputLearnClosestSettings = "Learn Closest Settings: ";
    private String inputLearnFurthestSettings = "Learn Furthest Settings: ";
    private String inputPredictSettings = "Predict Settings: ";
//    private String inputOutlierSettings = "Outlier Settings: ";

    // other inputs
    private String inputPredictSensitivity = "Prediction Sensitivity: ";
    private String inputLeastClosestVectors = "Least Closest Vectors: ";
    private String inputAttemptsForBestSettings = "Attempts For Best Settings: ";
//    private String inputCheckOutliers = "Check Outliers: ";
//    private String inputOutlierSearchSensitivity = "Outlier Sensitivity: ";
//    private String inputAveraging = "Do Averaging: ";
//    private String inputAveragingShift = "Averaging Shift: ";
//    private String inputAveragingStrength = "Averaging Strength: ";

    // outputs
    protected String quickInfo = "Quick Info: ";
    protected String outputInfo = "Info: ";
    private String outputLearn = "To Learn Data CSV: ";
    private String outputPredict = "To Predict Data CSV: ";
    private String outputBestSubset = "Best Learning Subset: ";
//    private String predicted = "Predicted: ";
//    private String predictionComparison = "To Compare Prediction With: ";

    private FastMachine f = null;
    private SlowMachine s = null;
    private SettingsBox settings = null;
    private TextDataHelper learnData = null;
    private TextDataHelper predictData = null;
    private JTextArea infoArea;

    private File previousDirectoryFile;
    private String directoryOfPreviousDirectory = "dir.txt";
    private String directoryOfPreviousFiles = "previous.txt";

    private String settingsDirectory = "settings.txt";

    public Display() {
	w.initializeDisplay("Least Similar Spheres Machine", 1400, 800);
	w.getMainPanel().setBackground(new Color(100, 100, 100));

	// panels
	for (int i = 0; i < 18; i++) {
	    w.setInputOutputArea();
	}

	// text inputs
	w.setInputTextArea(quickInfo, 0);
	w.setInputTextArea(inputLearnDir, 2);
	w.setInputTextArea(inputPredictDir, 3);

	w.setInputTextArea(inputPredictSensitivity, 4);
	w.setInputTextArea(inputLeastClosestVectors, 4);
	w.setInputTextArea(inputAttemptsForBestSettings, 4);

//	w.setInputTextArea(inputCheckOutliers, 5);
//	w.setInputTextArea(inputOutlierSearchSensitivity, 5);
//	w.setInputTextArea(inputAveraging, 5);
//	w.setInputTextArea(inputAveragingShift, 5);
//	w.setInputTextArea(inputAveragingStrength, 5);

	w.setInputTextArea(inputLearnClosestSettings, 6);
	w.setInputTextArea(inputLearnFurthestSettings, 7);
	w.setInputTextArea(inputPredictSettings, 8);
//	w.setInputTextArea(inputOutlierSettings, 9);

	// buttons
	w.setButtonArea(buttonFindLearn, 10);
	w.setButtonArea(buttonFindPredict, 10);
	w.setButtonArea(buttonLoadFiles, 10);
	w.setButtonArea(buttonLearnFast, 11);
	w.setButtonArea(buttonPredictFast, 11);
	w.setButtonArea(buttonFindBestSettings, 12);
	w.setButtonArea(buttonFindBestLearningSubset, 12);
	w.setButtonArea(buttonFindBestPredictSensitivity, 12);
	w.setButtonArea(buttonSaveSettings, 12);
	w.setButtonArea(buttonLoadPreviousFiles, 12);

	// outputs
	w.setOutputTextAreaHeights(200);
	w.setOutputTextArea(outputInfo, 1);
//	w.setOutputTextArea(outputLearn, 13);
//	w.setOutputTextArea(outputPredict, 14);
	w.setOutputTextArea(outputBestSubset, 15);
//	w.setOutputTextArea(predicted, 16);
//	w.setOutputTextArea(predictionComparison, 17);

	setListeners();
	infoArea = w.getTextAreaByName(quickInfo);
	w.setVisible();
	try {
	    setPreviousDirectory();
	    setPreviousSettings();
	} catch (Exception e) {
	    addTextToTextArea(e.getStackTrace().toString(), outputInfo, false);
	    e.printStackTrace();
	}
    }

    private void setListeners() {
	for (int i = 0; i < w.getButtons().size(); i++) {
	    w.getButtons().get(i).addActionListener(this);
	}
    }

    public void actionPerformed(ActionEvent e) {
	try {
	    if (previouslyLoadedDirectory() != null) {
		fileChooser.setCurrentDirectory(previouslyLoadedDirectory());
	    }
	    // find learn
	    if (e.getSource() == w.getButtons().get(0)) {
		int returnVal = fileChooser.showDialog(Display.this, "Load Learn");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fileChooser.getSelectedFile();
		    String dir = file.getParent();
		    setPreviousDirectory(dir);
		    String path = file.getAbsolutePath();
		    addTextToTextArea(path, outputInfo, false);
		    textToTextArea(path, inputLearnDir, true);
		} else {
		    addTextToTextArea("File choice was not approved: ",outputInfo,false);
		}
	    }
	    // find predict
	    if (e.getSource() == w.getButtons().get(1)) {
		int returnVal = fileChooser.showDialog(Display.this, "Load Predict");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fileChooser.getSelectedFile();
		    String dir = file.getParent();
		    setPreviousDirectory(dir);
		    String path = file.getAbsolutePath();
		    addTextToTextArea(path, outputInfo, false);
		    textToTextArea(path, inputPredictDir, true);
		} else {
		    addTextToTextArea("File choice was not approved: ",outputInfo, false);
		}
	    }
	    // load files
	    if (e.getSource() == w.getButtons().get(2)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    String dir1 = w.getTextAreaByName(inputLearnDir).getText();
			    String dir2 = w.getTextAreaByName(inputPredictDir).getText();
			    learnData = getLearnData(dir1);
			    predictData = getPredictData(dir2);
			    //listToTextArea(learnData.getData(), outputLearn);
			    //listToTextArea(predictData.getData(), outputPredict);
			    setPreviousFiles(dir1, dir2);
			    addTextToTextArea("Loaded: ", outputInfo, false);
			} catch (Exception e2) {
			    addTextToTextArea("Loading files had a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	    // learn
	    if (e.getSource() == w.getButtons().get(3)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    //learnData.setVectors(getDataFromInput(true));
			    settings = getSettings();
			    settings.setLabels(learnData.getLabels());
			    f = new FastMachine(settings);
			    f.learn(learnData.getData());
			    addTextToTextArea("Learned: ", outputInfo, false);
			} catch (Exception e2) {
			    addTextToTextArea("Learning had a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	    // predict
	    if (e.getSource() == w.getButtons().get(4)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    if (f != null) {
				//predictData.setVectors(getDataFromInput(false));
				settings = getSettings();
				List<VectorBox> predicted = f.predict(predictData.getData(), settings);
				//listToTextArea(predicted, outputPredict);
				String predictionInfo = "If learning data is the same, then prediction results are: \r\n";
				predictionInfo += f.getPredictionInformation(predicted, learnData.getData());
				addTextToTextArea(predictionInfo, outputInfo, false);
			    }
			    addTextToTextArea("Predicted: ", outputInfo, false);
			} catch (Exception e2) {
			    addTextToTextArea("Predicting had a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	    // find best settings
	    if (e.getSource() == w.getButtons().get(5)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    settings = getSettings();
			    settings.setLabels(learnData.getLabels());
			    s = new SlowMachine(settings);
			    s.setInfoArea(infoArea);
			    BestSettingsBox best = s.findBestSettings(learnData.getData(), predictData.getData());
			    addTextToTextArea("Current settings prediction precision: " + best.getPrecision(), outputInfo, false);
			    addTextToTextArea("Closest settings: "+best.getClosestSimilarity().toString(), outputInfo, false);
			    addTextToTextArea("Furthest settings: "+best.getFurthestSimilarity().toString(), outputInfo, false);
			    addTextToTextArea("Predict settings: "+best.getPredictSimilarity().toString(), outputInfo, false);
			    textToTextArea(best.getClosestSimilarity().toString(), inputLearnClosestSettings, true);
			    textToTextArea(best.getFurthestSimilarity().toString(), inputLearnFurthestSettings, true);
			    textToTextArea(best.getPredictSimilarity().toString(), inputPredictSettings, true);
			} catch (Exception e2) {
			    e2.printStackTrace();
			    addTextToTextArea("Finding best settings had a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	    // find best subset
	    if (e.getSource() == w.getButtons().get(6)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    settings = getSettings();
			    settings.setLabels(learnData.getLabels());
			    s = new SlowMachine(settings);
			    s.setInfoArea(infoArea);
			    List<VectorBox> bestSet = s.tryFindBestTrainSet(learnData.getData(), predictData.getData(), settings, settings.getAttemptsForBestSearching(), learnData.getData().size() - 10);
			    listToTextArea(bestSet, outputBestSubset);
			    s = new SlowMachine(settings);
			    s.learn(bestSet);
			    List<VectorBox> predicted = s.predict(predictData.getData());
			    addTextToTextArea("Best Subset Prediction: \r\n" + s.predictonPrecision(predicted, learnData.getData()), outputInfo, false);
			} catch (Exception e2) {
			    addTextToTextArea("Finding best subset had problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	    // find best predict sensitivity
	    if (e.getSource() == w.getButtons().get(7)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    settings = getSettings();
			    settings.setLabels(learnData.getLabels());
			    s = new SlowMachine(settings);
			    s.learn(learnData.getData());
			    s.setInfoArea(infoArea);
			    double sensitivity = s.findBestPredictionSensitivity(predictData.getData(), learnData.getData(), settings, 100);
			    textToTextArea("" + sensitivity, inputPredictSensitivity, true);
			} catch (Exception e2) {
			    addTextToTextArea("Finding best prediction sensitivity had a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	    if (e.getSource() == w.getButtons().get(8)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    saveSettings();
			} catch (Exception e2) {
			    addTextToTextArea("Saving settings had a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	    if (e.getSource() == w.getButtons().get(9)) {
		Thread t = new Thread(new Runnable() {
		    public void run() {
			try {
			    loadPreviousFile();
			} catch (Exception e2) {
			    addTextToTextArea("Loading previous files had a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
			}
		    }
		});
		t.start();
	    }
	} catch (Exception e2) {
	   e2.printStackTrace();
	    addTextToTextArea("There was a problem: " + System.lineSeparator() + exceptionStackToString(e2), outputInfo, false);
	}
    }

    //http://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string
    public String exceptionStackToString(Exception t){
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	t.printStackTrace(pw);
	return sw.toString();
    }
    
    protected List<VectorBox> getDataFromInput(boolean learnData) {
	if (learnData) {
	    String str = w.getTextAreaByName(outputLearn).getText().toString();
	    TextDataHelper t = new TextDataHelper();
	    return t.getDataFromString(str);
	} else {
	    String str = w.getTextAreaByName(outputPredict).getText().toString();
	    TextDataHelper t = new TextDataHelper();
	    return t.getDataFromString(str);
	}
    }

    protected void loadPreviousFile() {
	File f = new File(directoryOfPreviousFiles);
	if (f.exists()) {
	    TextDataHelper t = new TextDataHelper();
	    List<String> list = t.fromFile(directoryOfPreviousFiles);
	    File train = new File(list.get(0));
	    File predict = new File(list.get(1));
	    learnData = getLearnData(train.getAbsolutePath());
	    predictData = getPredictData(predict.getAbsolutePath());
//	    listToTextArea(learnData.getData(), outputLearn);
//	    listToTextArea(predictData.getData(), outputPredict);
	} else {
	    addTextToTextArea("Previous files file doesn't exist: ",outputInfo,false);
	}
    }

    private void setPreviousFiles(String dir1, String dir2) {
	if (!directoryDataFileExists()) {
	    createDirectoryFile();
	}
	TextDataHelper t = new TextDataHelper();
	List<String> list = new ArrayList<String>();
	list.add(dir1);
	list.add(dir2);
	t.toFile(list, directoryOfPreviousFiles);
    }

    private void setPreviousDirectory(String dir) {
	if (!directoryDataFileExists()) {
	    createDirectoryFile();
	}
	TextDataHelper t = new TextDataHelper();
	List<String> list = new ArrayList<String>();
	list.add(dir);
	t.toFile(list, directoryOfPreviousDirectory);
    }

    private void setPreviousSettings() {
	if (!settingsDataFileExists()) {
	    addTextToTextArea("Settings file did not exist, but it was created: ",outputInfo,false);
	    loadDefaultSettings();
	    saveSettings();
	} else {
	    loadSettingsFromFile();
	}
    }

    private void saveSettings() {
	TextDataHelper t = new TextDataHelper();
	List<String> list = new ArrayList<String>();
	list.add(getOtherSettings());
	list.add(getClosestSettings());
	list.add(getFurthestSettings());
	list.add(getPredictSettings());
//	list.add(getOutlierSettings());
	t.toFile(list, settingsDirectory);
    }

//    private String getOutlierSettings() {
//	return w.getTextAreaByName(inputOutlierSettings).getText().toString();
//    }

    private String getPredictSettings() {
	return w.getTextAreaByName(inputPredictSettings).getText().toString();
    }

    private String getFurthestSettings() {
	return w.getTextAreaByName(inputLearnFurthestSettings).getText().toString();
    }

    private String getClosestSettings() {
	return w.getTextAreaByName(inputLearnClosestSettings).getText().toString();
    }

    private String getOtherSettings() {
	String other = "";
	other += getPredictionSearchSensitivity() + ",";
	other += getLeastClosestVectors() + ",";
	other += getAttemptsForBestSettings();
//	other += getCheckOutliers() + ",";
//	other += getOutlierSearchSensitivity() + ",";
//	other += getAveraging() + ",";
//	other += getAveragingShift() + ",";
//	other += getAveragingStrength();
	return other;
    }

//    private String getAveragingStrength() {
//	return w.getTextAreaByName(inputAveragingStrength).getText().toString();
//    }
//
//    private String getAveragingShift() {
//	return w.getTextAreaByName(inputAveragingShift).getText().toString();
//    }
//
//    private String getAveraging() {
//	return w.getTextAreaByName(inputAveraging).getText().toString();
//    }
//
//    private String getOutlierSearchSensitivity() {
//	return w.getTextAreaByName(inputOutlierSearchSensitivity).getText().toString();
//    }
//
//    private String getCheckOutliers() {
//	return w.getTextAreaByName(inputCheckOutliers).getText().toString();
//    }

    private String getAttemptsForBestSettings() {
	return w.getTextAreaByName(inputAttemptsForBestSettings).getText().toString();
    }

    private String getLeastClosestVectors() {
	return w.getTextAreaByName(inputLeastClosestVectors).getText().toString();
    }

    private String getPredictionSearchSensitivity() {
	return w.getTextAreaByName(inputPredictSensitivity).getText().toString();
    }

    private SettingsBox loadSettingsFromFile() {
	TextDataHelper t = new TextDataHelper();
	List<String> list = t.fromFile(settingsDirectory);
	String[] otherSettings = list.get(0).split(",");
	String[] closest = list.get(1).split(",");
	String[] furthest = list.get(2).split(",");
	String[] predict = list.get(3).split(",");
//	String[] outlier = list.get(4).split(",");
	SettingsBox s = null;

	try {
	    s = new SettingsBox();
	    // other
	    s.setPredictionSearchSensitivity(Double.parseDouble(otherSettings[0]));
	    s.setLeastClosestVectors(Integer.parseInt(otherSettings[1]));
	    s.setAttemptsForBestSettings(Integer.parseInt(otherSettings[2]));

	    // outliers
//	    s.setCheckOutliers(Boolean.parseBoolean(otherSettings[3]));
//	    s.setOutlierSearchSensitivity(Double.parseDouble(otherSettings[4]));
//	    s.setOutlierSimilaritySettings(stringToSimilaritySetting(outlier));

	    // averaging
//	    s.setAveraging(Boolean.parseBoolean(otherSettings[5]));
//	    s.setAveragingShift(Integer.parseInt(otherSettings[6]));
//	    s.setAveragingStrength(Integer.parseInt(otherSettings[7]));

	    // similarities
	    s.setClosestSimilaritySettings(stringToSimilaritySetting(closest));
	    s.setFurthestSimilaritySettings(stringToSimilaritySetting(furthest));
	    s.setPredictSimilaritySettings(stringToSimilaritySetting(predict));

	    textToTextArea(list.get(1), inputLearnClosestSettings, true);
	    textToTextArea(list.get(2), inputLearnFurthestSettings, true);
	    textToTextArea(list.get(3), inputPredictSettings, true);
//	    textToTextArea(list.get(4), inputOutlierSettings, true);

	    textToTextArea(otherSettings[0], inputPredictSensitivity, true);
	    textToTextArea(otherSettings[1], inputLeastClosestVectors, true);
	    textToTextArea(otherSettings[2], inputAttemptsForBestSettings, true);

//	    textToTextArea(otherSettings[3], inputCheckOutliers, true);
//	    textToTextArea(otherSettings[4], inputOutlierSearchSensitivity, true);
//	    textToTextArea(otherSettings[5], inputAveraging, true);
//	    textToTextArea(otherSettings[6], inputAveragingShift, true);
//	    textToTextArea(otherSettings[7], inputAveragingStrength, true);

	} catch (Exception e) {
	    addTextToTextArea("Problem Loading Settings: " + e.toString(),outputInfo,false);
	    e.printStackTrace();
	}
	if (s != null) {
	    addTextToTextArea("Successfully loaded previous settings from file: ", outputInfo,false);
	}

	return s;
    }

    private SimilaritySettingsBox stringToSimilaritySetting(String[] s) {
	ListHelper list = new ListHelper();
	try {
	    String formula = s[0];
	    double p = list.parse(s[1]);
	    double q = list.parse(s[2]);
	    double ro = list.parse(s[3]);
	    double gamma = list.parse(s[4]);
	    double delta = list.parse(s[5]);
	    double k = list.parse(s[6]);
	    return new SimilaritySettingsBox(formula, p, q, ro, gamma, delta, k);
	} catch (Exception e) {
	    addTextToTextArea("Problem Parsing Similarity: " + e.toString(),outputInfo,false);
	    e.printStackTrace();
	}
	return null;
    }

    private void setPreviousDirectory() {
	if (!directoryDataFileExists()) {
	    previousDirectoryFile = null;
	} else {
	    TextDataHelper t = new TextDataHelper();
	    List<String> list = t.fromFile(directoryOfPreviousDirectory);
	    previousDirectoryFile = new File(list.get(0));
	}
    }

    private boolean settingsDataFileExists() {
	return new File(settingsDirectory).exists();
    }

    private boolean directoryDataFileExists() {
	return new File(directoryOfPreviousDirectory).exists();
    }

    private void createDirectoryFile() {
	File file = new File(directoryOfPreviousDirectory);
	try {
	    file.setWritable(true);
	    file.createNewFile();
	} catch (IOException e) {
	    addTextToTextArea("Can't create file: " + e.toString(),outputInfo,false);
	    e.printStackTrace();
	}
    }

    private File previouslyLoadedDirectory() {
	return previousDirectoryFile;
    }

    private void listToTextArea(List<VectorBox> list, String name) {
	JTextArea t = w.getTextAreaByName(name);
	if (t != null) {
	    String all = "", n = System.lineSeparator();
	    for (int i = 0; i < list.size(); i++) {
		all += list.get(i);
		if (i < list.size() - 1) {
		    all += n;
		}
	    }
	    t.setText(all);
	} else {
	    addTextToTextArea("Can't Find Text Area: " + name,outputInfo,false);
	}
    }

    private void textToTextArea(String txt, String name, boolean resize) {
	JTextArea t = w.getTextAreaByName(name);
	if (t != null) {
	    if (resize) {
		int w = txt.length();
		int s = 7;
		int width = w * s;
		t.setPreferredSize(new Dimension(width, (int) t.getSize().getHeight()));
	    }
	    t.setText(txt);
	} else {
	    addTextToTextArea("Can't Find Text Area: " + name,outputInfo,false);
	}
    }

    public void addTextToTextArea(String txt, String name, boolean resize) {
	JTextArea t = w.getTextAreaByName(name);
	if (t != null) {
	    String p = t.getText();
	    String prev = "";
	    if (!p.equals("")) {
		prev = p + System.lineSeparator();
	    }
	    textToTextArea(prev + txt, name, resize);
	} else {
	    addTextToTextArea("Can't Find Text Area: " + name,outputInfo,false);
	}
	t.setCaretPosition(t.getDocument().getLength());
    }

    private SettingsBox getSettings() {
	return loadSettings();
    }

    private SettingsBox loadSettings() {
	SettingsBox s = new SettingsBox();
	try {
	    // other
	    s.setPredictionSearchSensitivity(Double.parseDouble(getPredictionSearchSensitivity()));
	    s.setLeastClosestVectors(Integer.parseInt(getLeastClosestVectors()));
	    s.setAttemptsForBestSettings(Integer.parseInt(getAttemptsForBestSettings()));

	    // outliers
//	    s.setCheckOutliers(Boolean.parseBoolean(getCheckOutliers()));
//	    s.setOutlierSearchSensitivity(Double.parseDouble(getOutlierSearchSensitivity()));
//
//	    // averaging
//	    s.setAveraging(Boolean.parseBoolean(getAveraging()));
//	    s.setAveragingShift(Integer.parseInt(getAveragingShift()));
//	    s.setAveragingStrength(Integer.parseInt(getAveragingStrength()));

	    // similarities
	    s.setClosestSimilaritySettings(stringToSimilaritySetting(getClosestSettings().split(",")));
	    s.setFurthestSimilaritySettings(stringToSimilaritySetting(getFurthestSettings().split(",")));
	    s.setPredictSimilaritySettings(stringToSimilaritySetting(getPredictSettings().split(",")));
//	    s.setOutlierSimilaritySettings(stringToSimilaritySetting(getOutlierSettings().split(",")));
	} catch (Exception e) {
	    addTextToTextArea("Problem Loading Settings: ",outputInfo,false);
	    e.printStackTrace();
	}
	return s;
    }

    private void loadDefaultSettings() {
	textToTextArea("c,0.42,0.5,0.04,150.0,0.9,0.4", inputLearnClosestSettings, true);
	textToTextArea("c,0.42,0.5,0.04,150.0,0.9,0.4", inputLearnFurthestSettings, true);
	textToTextArea("c,0.42,0.5,0.04,150.0,0.9,0.4", inputPredictSettings, true);
//	textToTextArea("c,0.42,0.5,0.04,150.0,0.9,0.4", inputOutlierSettings, true);

	textToTextArea("" + 1, inputPredictSensitivity, true);
	textToTextArea("" + 1, inputLeastClosestVectors, true);
	textToTextArea("" + 10, inputAttemptsForBestSettings, true);

//	textToTextArea("" + false, inputCheckOutliers, true);
//	textToTextArea("" + 1, inputOutlierSearchSensitivity, true);
//	textToTextArea("" + false, inputAveraging, true);
//	textToTextArea("" + 1, inputAveragingShift, true);
//	textToTextArea("" + 1, inputAveragingStrength, true);
    }

    private TextDataHelper getLearnData(String dir) {
	TextDataHelper data = new TextDataHelper(dir);
	//listToTextArea(data.getData(), outputLearn);
	return data;
    }

    private TextDataHelper getPredictData(String dir) {
	TextDataHelper data = new TextDataHelper(dir);
	//listToTextArea(data.getData(), outputPredict);
	return data;
    }
}
