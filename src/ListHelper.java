import java.util.ArrayList;
import java.util.List;

public class ListHelper {
    public ListHelper() {
    }

    public String[] getColumn(List<String[]> list, int a) {
	List<String[]> moved = transposeList(list);
	return moved.get(a);
    }

    public List<String[]> moveColumn(List<String[]> list, int a, int b) {
	List<String[]> moved = transposeList(list);
	String[] temp = moved.get(a);
	moved.remove(a);
	moved.add(b, temp);
	moved = transposeList(moved);
	return moved;
    }

    public List<String[]> removeColumn(List<String[]> list, int a, int b) {
	List<String[]> removed = transposeList(list);
	for (int i = a; i <= b; i++) {
	    removed.remove(i);
	    i--;
	    b--;
	}
	return transposeList(removed);
    }

    public double parse(String in) {
	try {
	    return Double.parseDouble(in);
	} catch (NumberFormatException e) {
	    return -1;
	}
    }

    public double[] parseArray(String[] toParse) {
	double[] parsed = new double[toParse.length];
	for (int i = 0; i < parsed.length; i++) {
	    parsed[i] = parse(toParse[i]);
	}
	return parsed;
    }

    public List<double[]> parseList(List<String[]> list) {
	List<double[]> parsed = new ArrayList<double[]>();
	for (int i = 0; i < list.size(); i++) {
	    parsed.add(parseArray(list.get(i)));
	}
	return parsed;
    }

    public String[][] rotateMatrix(String[][] matrix, boolean clockwise) {
	int rows = matrix.length;
	int cols = matrix[0].length;
	String[][] rotated = new Matrix(cols, rows).getStringMatrix();
	int last;
	if (!clockwise) {
	    last = cols - 1;
	    for (int i = 0; i < cols; i++) {
		for (int j = 0; j < rows; j++) {
		    rotated[i][j] = matrix[j][(last) - i];
		}
	    }

	} else {
	    last = rows - 1;
	    for (int i = 0; i < rows; i++) {
		for (int j = 0; j < cols; j++) {
		    rotated[j][i] = matrix[(last) - i][j];
		}
	    }
	}
	return rotated;
    }

    public String[][] transpose(String[][] matrix) {
	int rows = matrix.length;
	int cols = matrix[0].length;
	String[][] transposed = new Matrix(cols, rows).getStringMatrix();
	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < cols; j++) {
		transposed[j][i] = matrix[i][j];
	    }
	}
	return transposed;
    }

    public List<String[]> transposeList(List<String[]> list) {
	String[][] matrix = stringListToArray(list);
	String[][] transposed = transpose(matrix);
	return stringMatrixToList(transposed);
    }

    public List<String[]> stringMatrixToList(String[][] matrix) {
	int rows = matrix.length, cols = matrix[0].length;
	List<String[]> list = new ArrayList<String[]>(rows);
	for (int i = 0; i < rows; i++) {
	    String[] temp = new String[cols];
	    for (int j = 0; j < cols; j++) {
		temp[j] = matrix[i][j];
	    }
	    list.add(temp);
	}
	return list;
    }

    public List<double[]> numberMatrixToList(double[][] matrix) {
	int rows = matrix.length, cols = matrix[0].length;
	List<double[]> list = new ArrayList<double[]>(rows);
	for (int i = 0; i < rows; i++) {
	    double[] temp = new double[cols];
	    for (int j = 0; j < cols; j++) {
		temp[j] = list.get(i)[j];
	    }
	    list.add(temp);
	}
	return list;
    }

    public String[][] stringListToArray(List<String[]> list) {
	String[][] matrix = new String[list.size()][list.get(0).length];
	int row = matrix.length, col = matrix[0].length;
	for (int i = 0; i < row; i++) {
	    for (int j = 0; j < col; j++) {
		matrix[i][j] = list.get(i)[j];
	    }
	}
	return matrix;
    }

    public double[][] doubleListToArray(List<double[]> list) {
	double[][] matrix = new double[list.size()][list.get(0).length];
	int row = matrix.length, col = matrix[0].length;
	for (int i = 0; i < row; i++) {
	    for (int j = 0; j < col; j++) {
		matrix[i][j] = list.get(i)[j];
	    }
	}
	return matrix;
    }

    /**
     * @param List<Object>
     * @param int
     *            value to rotate from
     */
    public List<Object> rotateList(List<Object> listToRotate, int rotateFrom) {
	int length = listToRotate.size();
	List<Object> rotated = new ArrayList<Object>();
	for (int i = 0; i < length; i++) {
	    rotated.add(null);
	}

	// first put all values forwards from rotation point
	for (int i = rotateFrom; i < length; i++) {
	    rotated.set(i - rotateFrom, listToRotate.get(i));
	}

	// then put values backwards from last item up to rotation point
	for (int i = length - 1; i >= length - rotateFrom; i--) {

	    rotated.set(i, listToRotate.get(length - (i + 1)));
	}
	return rotated;
    }

    public String[] uniques(String[] mixedValues) {
	int len = mixedValues.length;
	List<String> uniques = new ArrayList<String>();

	for (int i = 0; i < len; i++) {
	    String temp = mixedValues[i];
	    for (int j = 0; j < len; j++) {
		// no need to check previous nor itself or null values which
		// have been iterated already
		// if (mixedValues[j] != null && j != i) {
		if (mixedValues[j] != null && j > i) {
		    if (mixedValues[j].equals(temp)) {
			mixedValues[j] = null;
		    }
		}
	    }
	}
	for (int j = 0; j < len; j++) {
	    if (mixedValues[j] != null) {
		uniques.add(mixedValues[j]);
	    }
	}
	String[] uniqueItems = new String[uniques.size()];
	for (int i = 0; i < uniqueItems.length; i++) {
	    uniqueItems[i] = uniques.get(i);
	}
	uniques = null;
	return uniqueItems;
    }

    public List<List<VectorBox>> discretize(List<VectorBox> toDiscretizeIn, int binAmount) {
	List<List<VectorBox>> discretized = new ArrayList<List<VectorBox>>();
	int amountOfItems = toDiscretizeIn.size();
	int remainder = amountOfItems % binAmount;
	int otherChunks = (amountOfItems - remainder) / binAmount;
	int[] allChunks = new int[binAmount];
	int position1 = 0;
	int position2 = otherChunks;
	for (int i = 0; i < allChunks.length; i++) {
	    // last chunk must include remainder length
	    if (i != allChunks.length - 1) {
		discretized.add(toDiscretizeIn.subList(position1, position2));
		position1 += otherChunks;
		position2 += otherChunks;
	    } else {
		discretized.add(toDiscretizeIn.subList(position1, position2));
		discretized.add(toDiscretizeIn.subList(position2, position2 + remainder));
	    }
	}
	return discretized;
    }
}

class Matrix {
    int row, col;

    public Matrix(int row, int col) {
	this.row = row;
	this.col = col;
    }

    public double[][] getNumberMatrix() {
	return new double[row][col];
    }

    public String[][] getStringMatrix() {
	return new String[row][col];
    }

    public List<double[]> getDoubleList() {
	List<double[]> l = new ArrayList<double[]>(row);
	double[] d;
	for (int i = 0; i < row; i++) {
	    d = new double[col];
	    l.set(i, d);
	}
	return l;
    }

    public List<String[]> getStringList() {
	List<String[]> l = new ArrayList<String[]>(row);
	String[] d;
	for (int i = 0; i < row; i++) {
	    d = new String[col];
	    l.set(i, d);
	}
	return l;
    }
}
