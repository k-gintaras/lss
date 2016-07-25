import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
public class SlowMachine extends FastMachine {
    private JTextArea infoArea = null;

    public SlowMachine(SettingsBox settings) {
	super(settings);
    }

    public BestSettingsBox findBestSettings(List<VectorBox> learnData, List<VectorBox> predictData) {
	learnMap = new HashMap<Integer, VectorBox>();
	for (int l = 0; l < learnData.size(); l++) {
	    learnMap.put(new Integer(learnData.get(l).hashCode()), learnData.get(l));
	}

	loadLabelPairs(learnData);
	List<OneVsOtherBox> labelPairs = getLabelPairs();
	SettingsBox currentSettings = getCurrentSettings();

	List<BestSettingsBox> bestSettings = new ArrayList<BestSettingsBox>();
	loadBestSettings(labelPairs, currentSettings);
	for (int i = 0; i < labelPairs.size(); i++) {
	    OneVsOtherBox labelPair = labelPairs.get(i);
	    List<SettingsBox> closests = labelPair.getBestClosestSettings();
	    for (int j = 0; j < closests.size(); j++) {
		SettingsBox closest = closests.get(j);
		List<SettingsBox> furthests = labelPair.getBestFurthestSettings();
		for (int k = 0; k < furthests.size(); k++) {
		    SettingsBox furthest = furthests.get(k);
		    SimilaritySettingsBox closestSetting, furthestSetting, predictionSetting;
		    closestSetting = closest.getClosestSimilaritySetting();
		    furthestSetting = furthest.getFurthestSimilaritySetting();
		    loadClosestFurthest(labelPairs, closestSetting, furthestSetting);
		    resetDataLabels(predictData);
		    predictionSetting = tryPredict(labelPairs, predictData, learnData, currentSettings);
		    bestSettings.add(new BestSettingsBox(predictionSetting.getPrecision(), closestSetting, furthestSetting, predictionSetting));
		    addDisplayInfo("Trying to find best setting: " + "One Vs. Other Label Pair: " + (i + 1) + " Tries: " + (i + 1) * (j + 1) + " Settings: " + (i + 1) * (j + 1) * (k + 1));
		}
	    }
	}
	Collections.sort(bestSettings);
	return bestSettings.get(bestSettings.size() - 1);
    }

    public void addDisplayInfo(String info) {
	if (infoArea != null) {
	    int w = info.length();
	    int s = 6;
	    int width = w * s;
	    infoArea.setPreferredSize(new Dimension(width, (int) infoArea.getSize().getHeight()));
	    infoArea.setText(info);
	    infoArea.setText(info);
	}
    }

    public void loadBestSettings(List<OneVsOtherBox> labelPairs, SettingsBox currentSettings) {
	for (int i = 0; i < labelPairs.size(); i++) {
	    OneVsOtherBox labelPair = labelPairs.get(i);
	    List<VectorBox> one = labelPair.getOne();
	    List<VectorBox> other = labelPair.getOther();
	    List<SettingsBox> bestClosestSettings = tryFindSettings(one, other, currentSettings, true);
	    resetClosestOrFurthest(one, other, true);
	    resetClosestOrFurthest(one, other, false);
	    List<SettingsBox> bestFurthestSettings = tryFindFurthestSettings(one, other, bestClosestSettings, currentSettings);
	    labelPair.setBestClosestSettings(bestClosestSettings);
	    labelPair.setBestFurthestSettings(bestFurthestSettings);
	}
    }

    public void loadClosestFurthest(List<OneVsOtherBox> labelPairs, SimilaritySettingsBox closest, SimilaritySettingsBox furthest) {
	for (int i = 0; i < labelPairs.size(); i++) {
	    OneVsOtherBox labelPair = labelPairs.get(i);
	    findClosestAndFurthest(labelPair, closest, furthest);
	}
    }

    public SimilaritySettingsBox tryPredict(List<OneVsOtherBox> labelPairs, List<VectorBox> data, List<VectorBox> existing, SettingsBox currentSettings) {
	List<SimilaritySettingsBox> randomSettings = getRandomSimilaritySettings(currentSettings);
	int size = randomSettings.size();
	double[] precisions = new double[size];

	SimilaritySettingsBox bestSetting = getRandomSimilaritySetting("g");
	double bestPrecision = 0;
	for (int i = 0; i < size; i++) {
	    SimilaritySettingsBox randomSetting = randomSettings.get(i);
	    for (int j = 0; j < labelPairs.size(); j++) {
		OneVsOtherBox labelPair = labelPairs.get(j);
		for (int k = 0; k < data.size(); k++) {
		    VectorBox unknownLabel = data.get(k);
		    assignLabel(unknownLabel, labelPair, randomSetting, labelPair.getOneLabel());
		}
	    }
	    precisions[i] = predictonPrecision(data, existing);
	    resetDataLabels(data);
	    if (bestPrecision < precisions[i]) {
		bestPrecision = precisions[i];
		bestSetting = randomSetting;
		bestSetting.setPrecision(bestPrecision);
	    }
	}
	return bestSetting;
    }

    public String getMostCommonLabel(List<VectorBox> data, String[] labels) {
	List<LabelDataBox> labelData = new ArrayList<LabelDataBox>();
	for (int i = 0; i < labels.length; i++) {
	    int count = 0;
	    for (int j = 0; j < data.size(); j++) {
		if (data.get(j).equals(labels[i])) {
		    count++;
		}
	    }
	    labelData.add(new LabelDataBox(count, labels[i]));
	}
	Collections.sort(labelData);
	return labelData.get(labelData.size() - 1).getLabel();
    }

    public void findAllClosestAndFurthest(List<OneVsOtherBox> labelPairs, SimilaritySettingsBox closest, SimilaritySettingsBox furthest) {
	for (int i = 0; i < labelPairs.size(); i++) {
	    OneVsOtherBox labelPair = labelPairs.get(i);
	    findClosestAndFurthest(labelPair, closest, furthest);
	}
    }

    public double findBestPredictionSensitivity(List<VectorBox> predictData, List<VectorBox> withLabelsForComparison, SettingsBox bestSetting, int attempts) {
	List<OneVsOtherBox> labelPairs = getLabelPairs();
	SettingsBox currentSettings = getCurrentSettings();

	loadClosestFurthest(labelPairs, bestSetting.getClosestSimilaritySetting(), bestSetting.getFurthestSimilaritySetting());
	double bestSensitivity = 1;
	double bestPrecision = 0;
	int count = attempts * 100;
	double limit = (double) count;
	for (int i = 1; i < count; i++) {
	    double predictionSearchSensitivity = 1 / (limit / (double) i);
	    currentSettings.setPredictionSearchSensitivity(predictionSearchSensitivity);
	    resetDataLabels(predictData);
	    List<VectorBox> predicted = predict(predictData, currentSettings);
	    double precision = predictonPrecision(predicted, withLabelsForComparison);

	    if (precision > bestPrecision) {
		addDisplayInfo("Trying to find best closest sensitivity: " + "Try: " + (i + 1) + ", Sensitivity: " + predictionSearchSensitivity + ", Prediction: " + precision);
		bestPrecision = precision;
		bestSensitivity = predictionSearchSensitivity;
	    }
	}
	return bestSensitivity;
    }

    public void resetDataLabels(List<VectorBox> data) {
	for (int i = 0; i < data.size(); i++) {
	    VectorBox v = data.get(i);
	    v.setNominalLabel("unknown");
	}
    }

    public double predictonPrecision(List<VectorBox> predicted, List<VectorBox> existing) {
	int len = predicted.size();
	double good = len;
	for (int i = 0; i < len; i++) {
//	    String existingLabel = existing.get(i).getNominalLabel();
	    String predictedLabel = predicted.get(i).getNominalLabel();

	    int pH = predicted.get(i).hashCode();
	    // int eH=existing.get(i).hashCode();

	    if (!learnMap.get(new Integer(pH)).getNominalLabel().equals(predictedLabel)) {
		good -= 1;
	    }

	    // if (!existingLabel.equals(predictedLabel)) {
	    // good -= 1;
	    // }
	}

	double pct = (100 * good) / len;
	pct = extraMath.round(pct, 3);
	return pct;
    }

    public List<SettingsBox> tryFindFurthestSettings(List<VectorBox> one, List<VectorBox> other, List<SettingsBox> settingsList, SettingsBox settings) {
	List<List<SettingsBox>> furthestSettingsMatrix = new ArrayList<List<SettingsBox>>();
	for (int i = 0; i < settingsList.size(); i++) {
	    SimilaritySettingsBox currentClosestSimilarity = settingsList.get(i).getClosestSimilaritySetting();
	    List<VectorBox> closestOne = find(other, one, currentClosestSimilarity, true);
	    List<VectorBox> closestOther = find(one, other, currentClosestSimilarity, true);
	    List<SettingsBox> current = tryFindSettings(closestOne, closestOther, settings, false);
	    furthestSettingsMatrix.add(current);
	}
	return furthestSettingsList(furthestSettingsMatrix);
    }

    public List<SettingsBox> furthestSettingsList(List<List<SettingsBox>> furthestSettingsMatrix) {
	Collections.sort(furthestSettingsMatrix, new Comparator<List<SettingsBox>>() {
	    public int compare(List<SettingsBox> o1, List<SettingsBox> o2) {
		int totala = 0;
		int totalb = 0;
		for (int i = 0; i < o1.size(); i++) {
		    SettingsBox s = o1.get(i);
		    totala += s.getOne() + s.getOther();
		}
		for (int i = 0; i < o2.size(); i++) {
		    SettingsBox s = o2.get(i);
		    totalb += s.getOne() + s.getOther();
		}
		return totalb - totala;
	    }
	});
	return furthestSettingsMatrix.get(0);
    }

    public List<SettingsBox> tryFindSettings(List<VectorBox> one, List<VectorBox> other, SettingsBox settings, boolean closest) {
	String[] similarityFormulas = settings.getSimilarityFormulas();
	int attempts = settings.getAttemptsForBestSearching();
	int maxAmountOfVectors = settings.getLeastClosestVectors();
	List<SettingsBox> listOfSettings = new ArrayList<SettingsBox>();
	List<VectorBox> closestOrFurthestOne;
	List<VectorBox> closestOrFurthestOther;
	for (int i = 0; i < similarityFormulas.length; i++) {
	    for (int j = 0; j < attempts; j++) {
		SimilaritySettingsBox similaritySettings = getRandomSimilaritySetting(similarityFormulas[i]);
		if (closest) {
		    resetClosestOrFurthest(one, other, true);
		    closestOrFurthestOne = find(other, one, similaritySettings, true);
		    closestOrFurthestOther = find(one, other, similaritySettings, true);
		    listOfSettings.add(new SettingsBox(closestOrFurthestOne.size(), closestOrFurthestOther.size(), similaritySettings, true));
		} else {
		    resetClosestOrFurthest(one, other, true);
		    resetClosestOrFurthest(one, other, false);
		    closestOrFurthestOne = find(other, one, similaritySettings, false);
		    closestOrFurthestOther = find(one, other, similaritySettings, false);
		    listOfSettings.add(new SettingsBox(closestOrFurthestOne.size(), closestOrFurthestOther.size(), similaritySettings, false));
		}
	    }
	}
	List<SettingsBox> filtered = filterSimilaritySettings(listOfSettings, maxAmountOfVectors);
	return filtered;
    }

    public List<SettingsBox> filterSimilaritySettings(List<SettingsBox> listOfSettings, int tolerance) {
	Collections.sort(listOfSettings);
	List<SettingsBox> lowest = new ArrayList<SettingsBox>();
	int ones = 0;
	int others = 0;
	boolean set = false;
	for (int i = 0; i < listOfSettings.size(); i++) {
	    SettingsBox cur = listOfSettings.get(i);
	    if (cur.getOne() > 0 && cur.getOther() > 0) {
		if (!set) {
		    ones = cur.getOne();
		    others = cur.getOther();
		    set = true;
		}
		if (cur.getOne() <= ones + (tolerance - 1) && cur.getOther() <= others + (tolerance - 1)) {
		    lowest.add(cur);
		}
	    }
	}
	return lowest;
    }

    public SimilaritySettingsBox getRandomSimilaritySetting(String similarityFormula) {
	MathHelper v = extraMath;
	double p = v.normalize(new Random().nextInt(100), 0, 100, 0, 1);
	double q = v.normalize(new Random().nextInt(100), 0, 100, 0, 1);
	double ro = v.normalize(new Random().nextInt(100), 0, 100, 0, 1);
	double gamma = v.normalize(new Random().nextInt(100), 0, 100, 0, 200);
	double delta = v.normalize(new Random().nextInt(100), 0, 100, 0, 1);
	double k = v.normalize(new Random().nextInt(100), 0, 100, 0, 1);
	return new SimilaritySettingsBox(similarityFormula, p, q, ro, gamma, delta, k);
    }

    public List<SimilaritySettingsBox> getRandomSimilaritySettings(SettingsBox s) {
	String[] sf = s.getSimilarityFormulas();
	List<SimilaritySettingsBox> similaritySettings = new ArrayList<SimilaritySettingsBox>();
	for (int i = 0; i < sf.length; i++) {
	    similaritySettings.add(getRandomSimilaritySetting(sf[i]));
	}
	return similaritySettings;
    }

    public JTextArea getInfoArea() {
	return infoArea;
    }

    public void setInfoArea(JTextArea infoArea) {
	this.infoArea = infoArea;
    }

    public List<VectorBox> tryFindBestTrainSet(List<VectorBox> learn, List<VectorBox> predict, SettingsBox s, int times, int subsetSize) {
	List<List<VectorBox>> randomSubsets = getRandomSubsets(learn, times, subsetSize);
	double[] results = new double[times];
	double high = 0;
	for (int i = 0; i < times; i++) {
	    List<VectorBox> randomSubset = randomSubsets.get(i);
	    FastMachine lss = new FastMachine(s);
	    lss.learn(randomSubset);
	    resetDataLabels(predict);
	    List<VectorBox> predicted = lss.predict(predict);
	    results[i] = predictonPrecision(predicted, learn);
	    if (results[i] > high) {
		high = results[i];
		addDisplayInfo("Trying to find best learning subset: " + "Try: " + (i + 1) + " Prediction: " + high + "%");
	    }
	}

	double highest = results[0];
	List<VectorBox> bestTrainingSubset = randomSubsets.get(0);
	for (int i = 0; i < results.length; i++) {
	    if (highest < results[i]) {
		highest = results[i];
		bestTrainingSubset = randomSubsets.get(i);
	    }
	}
	return bestTrainingSubset;
    }

    public List<List<VectorBox>> getRandomSubsets(List<VectorBox> list, int howMany, int howBig) {
	List<List<VectorBox>> randomSubsets = new ArrayList<List<VectorBox>>();
	for (int i = 0; i < howMany; i++) {
	    randomSubsets.add(randomSubset(list, howBig));
	}
	return randomSubsets;
    }

    private List<VectorBox> randomSubset(List<VectorBox> list, int howBig) {
	List<VectorBox> randomized = new ArrayList<VectorBox>();
	for (int i = 0; i < howBig; i++) {
	    int r = new Random().nextInt(list.size() - 1);
	    randomized.add(list.get(r));
	}
	return randomized;
    }
}