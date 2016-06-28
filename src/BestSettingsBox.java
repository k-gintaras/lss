public class BestSettingsBox  implements Comparable<BestSettingsBox>{
    private double precision;
    private SimilaritySettingsBox currentClosestSimilarity;
    private SimilaritySettingsBox currentFurthestSimilarity;
    private SimilaritySettingsBox currentPredictSimilarity;

    public BestSettingsBox(double precision, SimilaritySettingsBox currentClosestSimilarity, SimilaritySettingsBox currentFurthestSimilarity, SimilaritySettingsBox currentLearningSimilarity) {
	this.precision = precision;
	this.currentClosestSimilarity = currentClosestSimilarity;
	this.currentFurthestSimilarity = currentFurthestSimilarity;
	this.currentPredictSimilarity = currentLearningSimilarity;
    }

    public BestSettingsBox(SimilaritySettingsBox currentClosestSimilarity, SimilaritySettingsBox currentFurthestSimilarity, SimilaritySettingsBox currentLearningSimilarity) {
	this.currentClosestSimilarity = currentClosestSimilarity;
	this.currentFurthestSimilarity = currentFurthestSimilarity;
	this.currentPredictSimilarity = currentLearningSimilarity;
    }

    public double getPrecision() {
	return precision;
    }

    public void setPrecision(double precision) {
	this.precision = precision;
    }

    public SimilaritySettingsBox getClosestSimilarity() {
	return currentClosestSimilarity;
    }

    public void setClosestSimilarity(SimilaritySettingsBox currentClosestSimilarity) {
	this.currentClosestSimilarity = currentClosestSimilarity;
    }

    public SimilaritySettingsBox getFurthestSimilarity() {
	return currentFurthestSimilarity;
    }

    public void setFurthestSimilarity(SimilaritySettingsBox currentFurthestSimilarity) {
	this.currentFurthestSimilarity = currentFurthestSimilarity;
    }

    public SimilaritySettingsBox getPredictSimilarity() {
	return currentPredictSimilarity;
    }

    public void setLearningSimilarity(SimilaritySettingsBox currentLearningSimilarity) {
	this.currentPredictSimilarity = currentLearningSimilarity;
    }

    public int compareTo(BestSettingsBox o) {
	double comparison=precision- o.getPrecision();
	int theReturn=(int)(comparison*100000);
	return theReturn;
    }
}
