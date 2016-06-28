import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FastMachine {
    protected ListHelper listOfArrays = new ListHelper();
    protected MathHelper extraMath = new MathHelper();
    protected List<OneVsOtherBox> labelPairs = new ArrayList<OneVsOtherBox>();
    protected String[] labels;
    protected SettingsBox currentSettings;
    protected HashMap<Integer, VectorBox> learnMap = new HashMap<Integer, VectorBox>();

    public FastMachine(SettingsBox settings) {
	this.currentSettings = settings;
    }

    public void learn(List<VectorBox> learnData) {
	learnMap = new HashMap<Integer, VectorBox>();
	for (int i = 0; i < learnData.size(); i++) {
	    learnMap.put(new Integer(learnData.get(i).hashCode()), learnData.get(i));
	}

	loadLabelPairs(learnData);
	if (currentSettings.averaging()) {
	    averageClusters();
	}
	findAllClosestAndFurthest(labelPairs, currentSettings.getClosestSimilaritySetting(), currentSettings.getFurthestSimilaritySetting());
	if (currentSettings.checkOutliers()) {
	    checkAllOutliers();
	}
    }

    public void loadLabelPairs(List<VectorBox> learnData) {
	labels = currentSettings.getLabels();
	for (int i = 0; i < labels.length; i++) {
	    labelPairs.add(new OneVsOtherBox(learnData, labels[i]));
	}
    }

    public void findAllClosestAndFurthest(List<OneVsOtherBox> labelPairs, SimilaritySettingsBox closest, SimilaritySettingsBox furthest) {
	for (int i = 0; i < labelPairs.size(); i++) {
	    OneVsOtherBox labelPair = labelPairs.get(i);
	    findClosestAndFurthest(labelPair, closest, furthest);
	}
    }

    public void findClosestAndFurthest(OneVsOtherBox oneVsOther, SimilaritySettingsBox closest, SimilaritySettingsBox furthest) {
	List<VectorBox> one = oneVsOther.getOne();
	List<VectorBox> other = oneVsOther.getOther();
	// closest
	List<VectorBox> closestOne = closestVectors(other, one, closest);
	List<VectorBox> closestOther = closestVectors(one, other, closest);
	// furthest
	List<VectorBox> furthestOne = furthestVectors(other, one, furthest);
	List<VectorBox> furthestOther = furthestVectors(one, other, furthest);

	// load closest
	oneVsOther.setClosestOne(closestOne);
	oneVsOther.setClosestOther(closestOther);
	// load furthest
	oneVsOther.setFurthestOne(furthestOne);
	oneVsOther.setFurthestOther(furthestOther);

	resetClosestOrFurthest(one, other, true);
	resetClosestOrFurthest(one, other, false);
    }

    public String getPredictionInformation(List<VectorBox> predicted, List<VectorBox> existing) {
	String n = "\r\n";
	String info = "Prediction Information: " + n;
	int len = predicted.size();
	double good = len;
	for (int i = 0; i < len; i++) {
//	    String existingLabel = existing.get(i).getNominalLabel();
	    String predictedLabel = predicted.get(i).getNominalLabel();
	    int pH = predicted.get(i).hashCode();
	    info += "Predicted: " + predictedLabel + ", ";
	    info += "Existing: " + learnMap.get(new Integer(pH)).getNominalLabel() + n;

	    if (!learnMap.get(new Integer(pH)).getNominalLabel().equals(predictedLabel)) {
		good -= 1;
	    }
	}
	double pct = (100 * good) / len;
	pct = extraMath.round(pct, 3);
	info += "Total: " + len + n;
	info += "Correct Prediction: " + pct + "%" + " Correct Instances: " + (good) + n;
	info += "Incorrect Prediction: " + extraMath.round(100 - pct, 3) + "%" + " InCorrect Instances: " + (len - good) + n;
	return info;
    }

    public List<VectorBox> predict(List<VectorBox> predictData, SettingsBox settings) {
	currentSettings = settings;
	Collections.shuffle(predictData);
	return predict(predictData);
    }

    public List<VectorBox> predict(List<VectorBox> data) {
	resetDataLabels(data);
	for (int i = 0; i < data.size(); i++) {
	    assignLabels(data.get(i));
	}
	return data;
    }

    public void assignLabels(VectorBox unknownLabel) {
	for (int i = 0; i < labelPairs.size(); i++) {
	    OneVsOtherBox l = labelPairs.get(i);
	    assignLabel(unknownLabel, l, currentSettings.getPredictSimilaritySettings(), l.getOneLabel());
	}
    }

    public void resetDataLabels(List<VectorBox> data) {
	for (int i = 0; i < data.size(); i++) {
	    VectorBox v = data.get(i);
	    v.setNominalLabel("unknown");
	}
    }

    /**
     * assume it is the rest for "better" prediction, current problem for
     * multi-class fails the above
     * unknownLabel.setNominalLabel(getMostCommonLabel()); fair algorithm
     * knowledge test is when default is not assumed, but then we do not follow
     * the rules of statistics to assume the most probable case by default, as a
     * bonus prediction helper, unknownLabel.label=0; if algorithm does not
     * know, it does not assume default label to win "fake" points for
     * prediction, also if predictors guess that item is in multiple labels,
     * good to just add the guesses, then later to simply chose either 1st guess
     * or last guess (did not work and was commented out)
     */
    public void assignLabel(VectorBox unknownLabel, OneVsOtherBox p, SimilaritySettingsBox similaritySettings, String one) {
	// if (!unknownLabel.isPredicted()) {
	if (inSphere2(unknownLabel, p.getClosestOne(), p.getFurthestOne(), p.getClosestOther(), similaritySettings)) {
	    unknownLabel.setPredicted(true);
	    unknownLabel.setNominalLabel(one);
	}
	// }
	// if (inSphere(unknownLabel, p.getClosestOther(), p.getFurthestOther(),
	// p.getClosestOne(), similaritySettings)) {
	// String previous = unknownLabel.getNominalLabel();
	// unknownLabel.setNominalLabel(previous+","+other);
	// }
    }

    public boolean inSphere(VectorBox unknownLabel, List<VectorBox> closestOnes, List<VectorBox> furthestOnes, List<VectorBox> closestOthers, SimilaritySettingsBox similaritySettings) {
	for (int i = 0; i < furthestOnes.size(); i++) {
	    VectorBox furthestOne = furthestOnes.get(i);
	    for (int j = 0; j < closestOnes.size(); j++) {
		VectorBox closestOne = closestOnes.get(j);
		double innerDistance = similarity(closestOne, furthestOne, similaritySettings);
		for (int k = 0; k < closestOthers.size(); k++) {
		    VectorBox closestOther = closestOthers.get(k);
		    double outerDistance = similarity(closestOne, closestOther, similaritySettings);
		    double halfOfOuterDistance = outerDistance / 2;
		    double radiusToCheck = innerDistance + halfOfOuterDistance;
		    radiusToCheck *= currentSettings.getPredictionSearchSensitivity();
		    double distance = similarity(unknownLabel, furthestOne, similaritySettings);
		    if (distance <= radiusToCheck) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public boolean inSphere2(VectorBox unknownLabel, List<VectorBox> closestOnes, List<VectorBox> furthestOnes, List<VectorBox> closestOthers, SimilaritySettingsBox similaritySettings) {
	for (int i = 0; i < furthestOnes.size(); i++) {
	    // furthestOne--->closestOne---><---closestOther<---furthestOther
	    VectorBox furthestOne = furthestOnes.get(i);
	    // furthestOne-----------------><---closestOtherToFurthestOne------------
	    VectorBox closestOtherToFurthest = findClosest(furthestOne, closestOthers, similaritySettings);
	    // --------->closestOneToClosestOther---><---closestOtherToFurthestOne---
	    VectorBox closestOneToClosestOther = findClosest(closestOtherToFurthest, closestOnes, similaritySettings);
	    VectorBox midPoint = extraMath.middle(closestOtherToFurthest, closestOneToClosestOther);
	    double midDist = similarity(midPoint, closestOtherToFurthest, similaritySettings);
	    double safeDist = similarity(furthestOne, closestOtherToFurthest, similaritySettings) - midDist;
	    safeDist *= currentSettings.getPredictionSearchSensitivity();
	    double distance = similarity(unknownLabel, furthestOne, similaritySettings);
	    if (distance <= safeDist) {
		return true;
	    }
	}
	for (int j = 0; j < closestOnes.size(); j++) {
	    VectorBox closestOne = closestOnes.get(j);
	    VectorBox closestOther = findClosest(closestOne, closestOthers, similaritySettings);
	    double dist1 = similarity(unknownLabel, closestOne, similaritySettings);
	    VectorBox midP = extraMath.middle(closestOne, closestOther);
	    double midDist = similarity(midP, closestOne, similaritySettings);
	    if (dist1 <= midDist) {
		return true;
	    }
	}
	return false;
    }

    private VectorBox findClosest(VectorBox furthestOne, List<VectorBox> closestOthers, SimilaritySettingsBox similaritySettings) {
	// int size = closestOthers.size();
	// double distance = similarity(furthestOne, closestOthers.get(0),
	// similaritySettings);
	// VectorBox closest = closestOthers.get(0);
	// for (int i = 0; i < size; i++) {
	// VectorBox closestOther = closestOthers.get(i);
	// double d = similarity(furthestOne, closestOther, similaritySettings);
	// if (d < distance) {
	// distance = d;
	// closest = closestOther;
	// }
	// }
	// return closest;
	return closestOrFurthest(closestOthers, furthestOne, similaritySettings, true);
    }

    public void checkAllOutliers() {
	double sensitivity = currentSettings.getOutlierSearchSensitivity();
	SimilaritySettingsBox similaritySettings = currentSettings.getOutlierSimilaritySettings();
	for (int i = 0; i < labelPairs.size(); i++) {
	    List<VectorBox> one = labelPairs.get(i).getOne();
	    List<VectorBox> other = labelPairs.get(i).getOther();
	    List<VectorBox> furthestOne = labelPairs.get(i).getFurthestOne();
	    List<VectorBox> furthestOther = labelPairs.get(i).getFurthestOther();
	    List<VectorBox> closestOne = labelPairs.get(i).getClosestOne();
	    List<VectorBox> closestOther = labelPairs.get(i).getClosestOther();
	    one = checkOutliers(furthestOne, closestOne, other, similaritySettings, sensitivity);
	    other = checkOutliers(furthestOther, closestOther, closestOne, similaritySettings, sensitivity);
	    labelPairs.get(i).setOne(one);
	    labelPairs.get(i).setOther(other);
	}
    }

    public List<VectorBox> checkOutliers(List<VectorBox> potentialOutliers, List<VectorBox> closestVectors, List<VectorBox> vectorsToAvoid, SimilaritySettingsBox similaritySettings, double sensitivity) {
	for (int i = 0; i < potentialOutliers.size(); i++) {
	    VectorBox potentialOutlier = potentialOutliers.get(i);
	    // each potential outlier is furthest vector from closest vectors
	    for (int j = 0; j < closestVectors.size(); j++) {
		VectorBox closestVector = closestVectors.get(j);
		// furthestVector(i)<---->closestVector(j)
		double innerDistance = similarity(potentialOutlier, closestVector, similaritySettings);
		for (int k = 0; k < vectorsToAvoid.size(); k++) {
		    VectorBox avoid = vectorsToAvoid.get(k);
		    // closestVector(j)<--|-->oppositeClassClosestVector(k)
		    double halfOfOuterDistance = similarity(closestVector, avoid, similaritySettings) / 2;
		    // furthestVector(i)<---->+<--halfWayToOppositeClassClosestVector(k)
		    // halfOfOuterDistance *= sensitivity;
		    double safetyRadius = innerDistance + halfOfOuterDistance;
		    safetyRadius *= sensitivity;
		    // furthestVector(i)<---->+closestVector(j)+<--half way to
		    // OppositeClassClosestVector(k)
		    // (Af)|<---->(Ac)<--|-->(Bc)
		    boolean inside = inRange(safetyRadius, potentialOutlier, vectorsToAvoid, similaritySettings);
		    if (!inside) {
			if (i >= 0) {
			    potentialOutliers.remove(i);
			    i--;
			    j = closestVectors.size();
			    k = vectorsToAvoid.size();
			}
		    }
		}
	    }
	}
	return potentialOutliers;
    }

    public boolean inRange(double radius, VectorBox a, List<VectorBox> bs, SimilaritySettingsBox similaritySettings) {
	for (int i = 0; i < bs.size(); i++) {
	    VectorBox b = bs.get(i);
	    double distance = similarity(a, b, similaritySettings);
	    if (distance <= radius) {
		return true;
	    }
	}
	return false;
    }

    public void averageClusters() {
	int averagingStrength = currentSettings.getAveragingStrength();
	int shift = currentSettings.getAveragingShift();
	for (int i = 0; i < averagingStrength; i++) {
	    for (int j = 0; j < labelPairs.size(); j++) {
		if (j > i) {
		    List<VectorBox> one = labelPairs.get(j).getOne();
		    List<VectorBox> other = labelPairs.get(j).getOther();
		    one = averaging(one, shift);
		    other = averaging(other, shift);
		    labelPairs.get(j).setOne(one);
		    labelPairs.get(j).setOther(other);
		}
	    }
	}
    }

    public List<VectorBox> find(List<VectorBox> forEveryThis, List<VectorBox> findClosestOrFurthestThis, SimilaritySettingsBox s, boolean closest) {
	List<VectorBox> closestOrFurthestVectors = new ArrayList<VectorBox>();
	List<VectorBox> data = new ArrayList<VectorBox>();
	int leastAmount = currentSettings.getLeastClosestVectors();
	if (isGoodList(forEveryThis) && isGoodList(findClosestOrFurthestThis)) {
	    for (int i = 0; i < forEveryThis.size(); i++) {
		VectorBox target = forEveryThis.get(i);
		VectorBox closestVector = closestOrFurthest(findClosestOrFurthestThis, target, s, closest);
		if (closestVector != null) {
		    if (closest) {
			// if it is not already used
			if (closestVector.getClosest() == 0) {
			    closestOrFurthestVectors.add(closestVector);
			}
			closestVector.setClosest();
		    } else {
			// not used as the closest one already
			if (closestVector.getClosest() == 0) {
			    if (closestVector.getFurthest() == 0) {
				closestOrFurthestVectors.add(closestVector);
			    }
			    closestVector.setFurthest();
			}
		    }
		}
	    }
	    for (int i = 0; i < closestOrFurthestVectors.size(); i++) {
		VectorBox target = closestOrFurthestVectors.get(i);
		if (closest) {
		    if (target.getClosest() >= leastAmount) {
			data.add(target);
		    }
		} else {
		    if (target.getFurthest() >= leastAmount) {
			data.add(target);
		    }
		}
	    }
	}
	return data;
    }

    public void resetClosestOrFurthest(List<VectorBox> one, List<VectorBox> other, boolean closest) {
	if (closest) {
	    resetClosest(one, other);
	} else {
	    resetFurthest(one, other);
	}
    }

    public void resetClosest(List<VectorBox> one, List<VectorBox> other) {
	for (int i = 0; i < one.size(); i++) {
	    one.get(i).resetClosest();
	}
	for (int i = 0; i < other.size(); i++) {
	    other.get(i).resetClosest();
	}
    }

    public void resetFurthest(List<VectorBox> one, List<VectorBox> other) {
	for (int i = 0; i < one.size(); i++) {
	    one.get(i).resetFurthest();
	}
	for (int i = 0; i < other.size(); i++) {
	    other.get(i).resetFurthest();
	}
    }

    public boolean isGoodList(List<VectorBox> forEveryThis) {
	if (forEveryThis != null) {
	    if (!(forEveryThis.isEmpty())) {
		return true;
	    }
	}
	return false;
    }

    public VectorBox closestOrFurthest(List<VectorBox> bs, VectorBox target, SimilaritySettingsBox s, boolean closest) {
	VectorBox oldClosest = null;
	int itself = target.hashCode();
	if (!bs.isEmpty()) {
	    double oldDistance = similarity(target, bs.get(0), s);
	    oldClosest = bs.get(0);
	    for (int j = 0; j < bs.size(); j++) {
		VectorBox newClosest = bs.get(j);
		int id = newClosest.hashCode();
		if (id != itself) {
		    double newDistance = similarity(target, newClosest, s);
		    if (closest) {
			if (newDistance < oldDistance) {
			    oldDistance = newDistance;
			    oldClosest = newClosest;
			}
		    } else {
			// find furthest
			if (newDistance > oldDistance) {
			    oldDistance = newDistance;
			    oldClosest = newClosest;
			}
		    }
		}
	    }
	}
	return oldClosest;
    }

    public double similarity(VectorBox a, VectorBox b, SimilaritySettingsBox s) {
	SimilarityHelper sf = new SimilarityHelper();
	String formula = s.getFormula();
	double p = s.getP();
	double q = s.getQ();
	double k = s.getK();
	double ro = s.getRo();
	double delta = s.getDelta();
	double gamma = s.getGamma();
	// pherlgs
	switch (formula) {
	case "p":
	    return sf.polynomial(a, b, p, q);
	case "h":
	    return sf.hybrid(a, b, gamma, p, q);
	case "e":
	    return sf.exponential(a, b, gamma);
	case "r":
	    return sf.RBF(a, b, ro);
	case "l":
	    return sf.linear(a, b);
	case "g":
	    return sf.gaussian(a, b, gamma);
	case "s":
	    return sf.sigmoidal(a, b, k, delta);
	case "c":
	    return sf.euclidean(a, b);
	default:
	    return sf.linear(a, b);
	}
    }

    public List<VectorBox> furthestVectors(List<VectorBox> as, List<VectorBox> bs, SimilaritySettingsBox s) {
	return find(as, bs, s, false);
    }

    public List<VectorBox> closestVectors(List<VectorBox> as, List<VectorBox> bs, SimilaritySettingsBox s) {
	return find(as, bs, s, true);
    }

    public List<VectorBox> averaging(List<VectorBox> toAbstract, int from) {
	List<Object> toRotate = castVectorsToObjects(toAbstract);
	toRotate = listOfArrays.rotateList(toRotate, from);
	List<VectorBox> toAverage = castObjectsToVectors(toRotate);
	return averageVectors(toAverage);
    }

    public List<VectorBox> averageVectors(List<VectorBox> toAverage) {
	List<VectorBox> averaged = new ArrayList<VectorBox>();
	for (int i = 0; i < toAverage.size(); i++) {
	    VectorBox a = toAverage.get(i);
	    for (int j = 0; j < toAverage.size(); j++) {
		if (j > i) {
		    VectorBox b = toAverage.get(j);
		    VectorBox mid = extraMath.middle(a, b);
		    averaged.add(mid);
		}
	    }
	}
	return averaged;
    }

    public List<VectorBox> castObjectsToVectors(List<Object> toCast) {
	int len = toCast.size();
	List<VectorBox> casted = new ArrayList<VectorBox>();
	for (int i = 0; i < len; i++) {
	    casted.add((VectorBox) toCast.get(i));
	}
	return casted;
    }

    public List<Object> castVectorsToObjects(List<VectorBox> toCast) {
	int len = toCast.size();
	List<Object> casted = new ArrayList<Object>();
	for (int i = 0; i < len; i++) {
	    casted.add((Object) toCast.get(i));
	}
	return casted;
    }

    public List<OneVsOtherBox> getLabelPairs() {
	return labelPairs;
    }

    public void setLabelPairs(List<OneVsOtherBox> labelPairs) {
	this.labelPairs = labelPairs;
    }

    public SettingsBox getCurrentSettings() {
	return currentSettings;
    }
}