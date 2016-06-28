public class LabelDataBox implements Comparable<LabelDataBox>{
private int count;
private String label;

    public LabelDataBox() {
    }
    
    public LabelDataBox(int count, String label) {
	this.count = count;
	this.label = label;
    }

    public int compareTo(LabelDataBox l) {
	return count -l.getCount();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
