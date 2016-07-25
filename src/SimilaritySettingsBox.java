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
public class SimilaritySettingsBox {
    private String formula;
    private double p;
    private double q;
    private double ro;
    private double gamma;
    private double delta;
    private double k;
    private double precision;

    public SimilaritySettingsBox() {
    }
    
    public SimilaritySettingsBox(String formula, double p, double q, double ro, double gamma, double delta, double k) {
	this.formula = formula;
	this.p = p;
	this.q = q;
	this.ro = ro;
	this.gamma = gamma;
	this.delta = delta;
	this.k = k;
    }

    public String getFormula() {
	return formula;
    }

    public void setFormula(String formula) {
	this.formula = formula;
    }

    public double getP() {
	return p;
    }

    public void setP(double p) {
	this.p = p;
    }

    public double getQ() {
	return q;
    }

    public void setQ(double q) {
	this.q = q;
    }

    public double getRo() {
	return ro;
    }

    public void setRo(double ro) {
	this.ro = ro;
    }

    public double getGamma() {
	return gamma;
    }

    public void setGamma(double gamma) {
	this.gamma = gamma;
    }

    public double getDelta() {
	return delta;
    }

    public void setDelta(double delta) {
	this.delta = delta;
    }

    public double getK() {
	return k;
    }

    public void setK(double k) {
	this.k = k;
    }
    
    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public String toString() {
	return formula+","+p+","+q+","+ro+","+gamma+","+delta+","+k;
    }

    public int hashCode() {
	final int prime = 31;
	int result = 1;
	long temp;
	temp = Double.doubleToLongBits(delta);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + ((formula == null) ? 0 : formula.hashCode());
	temp = Double.doubleToLongBits(gamma);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(k);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(p);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(q);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(ro);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SimilaritySettingsBox other = (SimilaritySettingsBox) obj;
	if (Double.doubleToLongBits(delta) != Double.doubleToLongBits(other.delta))
	    return false;
	if (formula == null) {
	    if (other.formula != null)
		return false;
	} else if (!formula.equals(other.formula))
	    return false;
	if (Double.doubleToLongBits(gamma) != Double.doubleToLongBits(other.gamma))
	    return false;
	if (Double.doubleToLongBits(k) != Double.doubleToLongBits(other.k))
	    return false;
	if (Double.doubleToLongBits(p) != Double.doubleToLongBits(other.p))
	    return false;
	if (Double.doubleToLongBits(q) != Double.doubleToLongBits(other.q))
	    return false;
	if (Double.doubleToLongBits(ro) != Double.doubleToLongBits(other.ro))
	    return false;
	return true;
    }
}
