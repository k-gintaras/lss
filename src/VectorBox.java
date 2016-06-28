import java.util.Arrays;

public class VectorBox {
    public double[] values;
    public String nominalLabel="unknown";
    public double numericLabel;
    private int closest = 0;
    private int furthest = 0;
    private boolean predicted=false;
    private int predicts=0;

    public int getPredicts() {
        return predicts;
    }

    public boolean isPredicted() {
        return predicted;
    }

    public void setPredicted(boolean predicted) {
	predicts++;
        this.predicted = predicted;
    }
    
    public VectorBox(double[] vector) {
	this.values = new double[vector.length];
	for (int i = 0; i < vector.length; i++) {
	    this.values[i] = vector[i];
	}
    }

    public VectorBox(double[] vector, double label) {
	this.values = new double[vector.length];
	for (int i = 0; i < vector.length; i++) {
	    this.values[i] = vector[i];
	}
	this.numericLabel = label;
    }

    public VectorBox(double[] vector, String label) {
	this.values = new double[vector.length];
	for (int i = 0; i < vector.length; i++) {
	    this.values[i] = vector[i];
	}
	this.nominalLabel = label;
    }

    public int getFurthest() {
	return furthest;
    }

    public void setFurthest() {
	this.furthest++;
    }

    public int getClosest() {
	return closest;
    }

    public void setClosest() {
	this.closest++;
    }

    public void resetClosest() {
	this.closest = 0;
    }

    public void resetFurthest() {
	this.furthest = 0;
    }

    public String getNominalLabel() {
	return nominalLabel;
    }

    public void setNominalLabel(String nominalLabel) {
	this.nominalLabel = nominalLabel;
    }

    public double getNumericLabel() {
	return numericLabel;
    }

    public void setNumericLabel(double numericLabel) {
	this.numericLabel = numericLabel;
    }

    public int hashCode() {
	final int prime = 31;
	int result = 67;
//	long temp;
	result = prime * result + closest;
	result = prime * result + Arrays.hashCode(values);
	return result;
    }

    public String toString() {
	String all = "";
	int len = values.length;
	for (int i = 0; i < len; i++) {
	    all += values[i];
	    if (i < len - 1) {
		all += ",";
	    }
	}
	all += "," + nominalLabel;
	return all;
    }

}
