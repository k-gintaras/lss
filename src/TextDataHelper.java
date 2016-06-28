import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextDataHelper {
    private List<VectorBox> vectors;
    private List<String[]> data;
    private String[] labels;
    private String[] headers;
    private String file;
    private ListHelper d = new ListHelper();
    MathHelper m = new MathHelper();

    public TextDataHelper(String file) {
	this.file = file;
	vectors = new ArrayList<VectorBox>();
	data = new ArrayList<String[]>();
	loadList();
	loadVectors();
    }

    public TextDataHelper() {
    }

    private void loadVectors() {
	// save the header column and then remove it
	int len = data.size();
	labels = d.getColumn(data, data.get(0).length - 1);
	List<String[]> noLabel = d.removeColumn(data, data.get(0).length - 1, data.get(0).length - 1);
	List<double[]> numbers = d.parseList(noLabel);

	for (int i = 0; i < len; i++) {
	    double[] cur = numbers.get(i);
//	    cur = m.normalize(cur, 1, 100);
	    VectorBox v = new VectorBox(cur, labels[i]);
	    vectors.add(v);
	}
	Collections.shuffle(vectors);
    }

    public void printData() {
	System.out.println(join(headers, ", "));
	for (int i = 0; i < vectors.size(); i++) {
	    VectorBox cur = vectors.get(i);
	    double[] vector = cur.values;
	    for (int j = 0; j < vector.length; j++) {
		System.out.print(vector[j] + ",");
	    }
	    System.out.println("Label: " + cur.getNominalLabel());
	}
    }

    public List<VectorBox> getDataFromString(String str) {
	vectors = new ArrayList<VectorBox>();
	List<String[]> strings = new ArrayList<String[]>();
	String[] split = str.split("\\s+");

	// String[] split=str.split(System.lineSeparator());// does not work
	// when copy paste is used

	for (int i = 0; i < split.length; i++) {
	    strings.add(split[i].split(","));
	}

	int len = strings.size();
	int last = strings.get(0).length - 1;
	labels = d.getColumn(strings, last);
	List<String[]> noLabel = d.removeColumn(strings, last, last);
	List<double[]> numbers = d.parseList(noLabel);

	for (int i = 0; i < len; i++) {
	    double[] cur = numbers.get(i);
	    VectorBox v = new VectorBox(cur, labels[i]);
	    vectors.add(v);
	}
	return vectors;
    }

    private void loadList() {
	List<String> list = fromFile(file);
	headers = list.get(0).split(",");
	list.remove(0);

	for (int i = 0; i < list.size(); i++) {
	    String cur = list.get(i);
	    String[] instance = cur.split(",");
	    data.add(instance);
	}
    }

    public double parse(String in) {
	return Double.parseDouble(in);
    }

    public double[] parseArray(String[] toParse) {
	double[] parsed = new double[toParse.length];

	for (int i = 0; i < parsed.length; i++) {
	    parsed[i] = parse(toParse[i]);
	}
	return parsed;
    }

    /**
     * @param List<String>
     * @param String
     *            path
     */
    public void toFile(List<String> list, String path) {
	try {
	    FileWriter x = new FileWriter(path);
	    PrintWriter y = new PrintWriter(x);
	    for (int i = 0; i < list.size(); i++) {
		y.print(list.get(i));
		// this prevents printing last line as empty
		if (i < list.size() - 1) {
		    y.print("\r");
		}
	    }
	    x.close();
	    y.close();

	} catch (FileNotFoundException e) {
	    new Err("toFile FileNotFoundException : " + e.toString());
	} catch (IOException e) {
	    new Err("toFile IOEException : " + e.toString());
	}
    }

    /**
     * @return List<String>
     */
    public List<String> fromFile(String path) {
	List<String> lines = new ArrayList<String>();
	try {
	    FileReader x = new FileReader(path);
	    BufferedReader y = new BufferedReader(x);
	    lines.add(y.readLine());
	    String str;
	    while ((str = y.readLine()) != null) {
		lines.add(str);
	    }
	    x.close();
	    y.close();

	} catch (EOFException e) {
	    new Err("\n" + "fromFile EOFE : " + e.toString());
	} catch (FileNotFoundException e) {
	    new Err("\n" + "fromFile FileNotFoundException : " + e.toString());
	} catch (IOException e) {
	    new Err("\n" + "fromFile IOException : " + e.toString());
	}
	return lines;
    }

    /**
     * @return String manipulation
     */
    public String join(String[] str, String sym) {
	String data = "";
	for (int i = 0; i < str.length; i++) {
	    data += str[i];
	    if (i < str.length - 1) {
		data += sym;
	    }
	}
	return data;
    }

    public List<VectorBox> getData() {
	return vectors;
    }

    public List<VectorBox> getVectors() {
	return vectors;
    }

    public void setVectors(List<VectorBox> vectors) {
	this.vectors = vectors;
    }

    public String[] getLabels() {
	ListHelper l = new ListHelper();
	labels = l.uniques(labels);
	return labels;
    }

    public void setLabels(String[] labels) {
	this.labels = labels;
    }

    public String[] getHeaders() {
	return headers;
    }

    public void setHeaders(String[] headers) {
	this.headers = headers;
    }

    public String getFile() {
	return file;
    }

    public void setFile(String file) {
	this.file = file;
    }

    public ListHelper getD() {
	return d;
    }

    public void setD(ListHelper d) {
	this.d = d;
    }

    public void setData(List<String[]> data) {
	this.data = data;
    }
}
