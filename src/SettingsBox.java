import java.util.Arrays;

class SettingsBox implements Comparable<SettingsBox> {
    // needed to compare when searching for best settings
    private int one = 0;
    private int other = 0;
    private SimilaritySettingsBox comparableSimilaritySettings = new SimilaritySettingsBox("r",0.42,0.5,0.04,150.0,0.9,0.4);

    private boolean checkOutliers = false;
    private boolean averaging = false;
    private int averagingShift = 1;
    private int averagingStrength = 1;
    private double outlierSearchSensitivity = 0;
    private double predictionSearchSensitivity = 0.5670000000000001;//0.496
    private int leastClosestVectors = 1;
    private int attemptsForBestSettings = 10;
    private String[] labels = { "noLabel", "noLabel" };
    private String[] similarityFormulas = { "p", "h", "e", "r", "l", "g", "s", "c" };
    
    private SimilaritySettingsBox closestSimilaritySettings = new SimilaritySettingsBox("g",0.03,0.67,0.04,0.0,0.39,0.87);
    private SimilaritySettingsBox furthestSimilaritySettings = new SimilaritySettingsBox("r",0.42,0.5,0.04,150.0,0.9,0.4);
    private SimilaritySettingsBox outlierSimilaritySettings = new SimilaritySettingsBox("c", 0, 0, 0.07, 0, 0, 0);
    private SimilaritySettingsBox predictSimilaritySettings = new SimilaritySettingsBox("c",0.19,0.33,0.91,140.0,0.76,0.01);


    public SettingsBox() {
    }

    /**
     * do not use this, this is used to compare different similarity settings
     * when slow learn is enabled
     */
    public SettingsBox(int one, int other, SimilaritySettingsBox ks) {
	this.one = one;
	this.other = other;
	comparableSimilaritySettings = ks;
    }

    public SettingsBox(int one, int other, SimilaritySettingsBox similaritySettings, boolean closest) {
	if (closest) {
	    this.one = one;
	    this.other = other;
	    closestSimilaritySettings = similaritySettings;
	} else {
	    this.one = one;
	    this.other = other;
	    furthestSimilaritySettings = similaritySettings;
	}
    }
    
    public SettingsBox( boolean checkOutliers, boolean averaging, int averagingShift, int averagingStrength, double outlierSearchSensitivity, double predictionSearchSensitivity, int leastClosestVectors,
	    int attemptsForBestSettings, String[] labels) {
	this.checkOutliers = checkOutliers;
	this.averaging = averaging;
	this.averagingShift = averagingShift;
	this.averagingStrength = averagingStrength;
	this.outlierSearchSensitivity = outlierSearchSensitivity;
	this.predictionSearchSensitivity = predictionSearchSensitivity;
	this.leastClosestVectors = leastClosestVectors;
	this.attemptsForBestSettings = attemptsForBestSettings;
	this.labels = labels;
    }

    public SettingsBox(boolean checkOutliers, boolean averaging, int averagingShift, int averagingStrength, double outlierSearchSensitivity, double predictionSearchSensitivity, int leastClosestVectors,
	    int attemptsForBestSettings, String[] labels, SimilaritySettingsBox closestSimilarity, SimilaritySettingsBox furthestSimilarity, SimilaritySettingsBox predictSimilarity) {
	this.checkOutliers = checkOutliers;
	this.averaging = averaging;
	this.averagingShift = averagingShift;
	this.averagingStrength = averagingStrength;
	this.outlierSearchSensitivity = outlierSearchSensitivity;
	this.predictionSearchSensitivity = predictionSearchSensitivity;
	this.leastClosestVectors = leastClosestVectors;
	this.labels = labels;
	this.outlierSimilaritySettings = furthestSimilarity;
	this.predictSimilaritySettings = predictSimilarity;
    }

    public int getOne() {
	return one;
    }

    public void setOne(int one) {
	this.one = one;
    }

    public int getOther() {
	return other;
    }

    public void setOther(int other) {
	this.other = other;
    }

    public boolean checkOutliers() {
	return checkOutliers;
    }

    public void setCheckOutliers(boolean checkOutliers) {
	this.checkOutliers = checkOutliers;
    }

    public double getOutlierSearchSensitivity() {
	return outlierSearchSensitivity;
    }

    public void setOutlierSearchSensitivity(double outlierSearchSensitivity) {
	this.outlierSearchSensitivity = outlierSearchSensitivity;
    }

    public double getPredictionSearchSensitivity() {
	return predictionSearchSensitivity;
    }

    public void setPredictionSearchSensitivity(double predictionSearchSensitivity) {
	this.predictionSearchSensitivity = predictionSearchSensitivity;
    }

    public int getLeastClosestVectors() {
	return leastClosestVectors;
    }

    public void setLeastClosestVectors(int leastClosestVectors) {
	this.leastClosestVectors = leastClosestVectors;
    }

    public int getAttemptsForBestSearching() {
	return attemptsForBestSettings;
    }

    public void setAttemptsForBestSettings(int attemptsForBestSettings) {
	this.attemptsForBestSettings = attemptsForBestSettings;
    }

    public SimilaritySettingsBox getComparableSimilaritySettings() {
	return comparableSimilaritySettings;
    }

    public void setComparableSimilaritySettings(SimilaritySettingsBox comparableSimilaritySettings) {
	this.comparableSimilaritySettings = comparableSimilaritySettings;
    }

    public SimilaritySettingsBox getOutlierSimilaritySettings() {
	return outlierSimilaritySettings;
    }

    public void setOutlierSimilaritySettings(SimilaritySettingsBox outlierSimilaritySettings) {
	this.outlierSimilaritySettings = outlierSimilaritySettings;
    }

    public SimilaritySettingsBox getPredictSimilaritySettings() {
	return predictSimilaritySettings;
    }

    public void setPredictSimilaritySettings(SimilaritySettingsBox predictSimilaritySettings) {
	this.predictSimilaritySettings = predictSimilaritySettings;
    }

    public int compareTo(SettingsBox o) {
	return (one + other) - (o.getOne() + o.getOther());
    }

    public boolean averaging() {
	return averaging;
    }

    public void setAveraging(boolean averaging) {
	this.averaging = averaging;
    }

    public void setLabels(String[] labels) {
	this.labels = labels;
    }

    public int getAveragingShift() {
	return averagingShift;
    }

    public void setAveragingShift(int averagingShift) {
	this.averagingShift = averagingShift;
    }

    public int getAveragingStrength() {
	return averagingStrength;
    }

    public void setAveragingStrength(int averagingStrength) {
	this.averagingStrength = averagingStrength;
    }

    public String printComparableSettings() {
	return "One: " + one + ", other: " + other + ", SimilaritySettings: " + comparableSimilaritySettings.toString();
    }

    public String[] getLabels() {
	return labels;
    }

    public String[] getSimilarityFormulas() {
	return similarityFormulas;
    }

    public void setSimilarityFormulas(String[] similarityFormulas) {
	this.similarityFormulas = similarityFormulas;
    }

    public SimilaritySettingsBox getClosestSimilaritySetting() {
	return closestSimilaritySettings;
    }

    public SimilaritySettingsBox getFurthestSimilaritySetting() {
	return furthestSimilaritySettings;
    }

    public void setClosestSimilaritySettings(SimilaritySettingsBox closestSimilaritySettings) {
	this.closestSimilaritySettings = closestSimilaritySettings;
    }

    public void setFurthestSimilaritySettings(SimilaritySettingsBox furthestSimilaritySettings) {
	this.furthestSimilaritySettings = furthestSimilaritySettings;
    }

    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + attemptsForBestSettings;
	result = prime * result + (averaging ? 1231 : 1237);
	result = prime * result + averagingShift;
	result = prime * result + averagingStrength;
	result = prime * result + (checkOutliers ? 1231 : 1237);
	result = prime * result + ((closestSimilaritySettings == null) ? 0 : closestSimilaritySettings.hashCode());
	result = prime * result + ((comparableSimilaritySettings == null) ? 0 : comparableSimilaritySettings.hashCode());
	result = prime * result + ((furthestSimilaritySettings == null) ? 0 : furthestSimilaritySettings.hashCode());
	result = prime * result + Arrays.hashCode(labels);
	result = prime * result + leastClosestVectors;
	result = prime * result + one;
	result = prime * result + other;
	long temp;
	temp = Double.doubleToLongBits(outlierSearchSensitivity);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + ((outlierSimilaritySettings == null) ? 0 : outlierSimilaritySettings.hashCode());
	result = prime * result + ((predictSimilaritySettings == null) ? 0 : predictSimilaritySettings.hashCode());
	temp = Double.doubleToLongBits(predictionSearchSensitivity);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + Arrays.hashCode(similarityFormulas);
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SettingsBox other = (SettingsBox) obj;
	if (attemptsForBestSettings != other.attemptsForBestSettings)
	    return false;
	if (averaging != other.averaging)
	    return false;
	if (averagingShift != other.averagingShift)
	    return false;
	if (averagingStrength != other.averagingStrength)
	    return false;
	if (checkOutliers != other.checkOutliers)
	    return false;
	if (closestSimilaritySettings == null) {
	    if (other.closestSimilaritySettings != null)
		return false;
	} else if (!closestSimilaritySettings.equals(other.closestSimilaritySettings))
	    return false;
	if (comparableSimilaritySettings == null) {
	    if (other.comparableSimilaritySettings != null)
		return false;
	} else if (!comparableSimilaritySettings.equals(other.comparableSimilaritySettings))
	    return false;
	if (furthestSimilaritySettings == null) {
	    if (other.furthestSimilaritySettings != null)
		return false;
	} else if (!furthestSimilaritySettings.equals(other.furthestSimilaritySettings))
	    return false;
	if (!Arrays.equals(labels, other.labels))
	    return false;
	if (leastClosestVectors != other.leastClosestVectors)
	    return false;
	if (one != other.one)
	    return false;
	if (this.other != other.other)
	    return false;
	if (Double.doubleToLongBits(outlierSearchSensitivity) != Double.doubleToLongBits(other.outlierSearchSensitivity))
	    return false;
	if (outlierSimilaritySettings == null) {
	    if (other.outlierSimilaritySettings != null)
		return false;
	} else if (!outlierSimilaritySettings.equals(other.outlierSimilaritySettings))
	    return false;
	if (predictSimilaritySettings == null) {
	    if (other.predictSimilaritySettings != null)
		return false;
	} else if (!predictSimilaritySettings.equals(other.predictSimilaritySettings))
	    return false;
	if (Double.doubleToLongBits(predictionSearchSensitivity) != Double.doubleToLongBits(other.predictionSearchSensitivity))
	    return false;
	if (!Arrays.equals(similarityFormulas, other.similarityFormulas))
	    return false;
	return true;
    }
}