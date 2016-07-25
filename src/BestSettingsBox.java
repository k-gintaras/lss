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
