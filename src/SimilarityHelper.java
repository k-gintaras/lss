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
public class SimilarityHelper {
    private static MathHelper v = new MathHelper();

    public SimilarityHelper() {
    }
    
    public double euclidean(VectorBox a, VectorBox b) {
	return v.euclideanDistance(a, b);
    }
    public double linear(VectorBox a, VectorBox b) {
	return v.dotProduct(a, b);
    }

    public double RBF(VectorBox a, VectorBox b, double ro) {
	double gamma = 1 / (2 * v.square(ro));
	return gaussian(a, b, gamma);
    }

    public double gaussian(VectorBox a, VectorBox b, double gamma) {
	// assuming that ||a-b|| is euclidean distance
	double result = gamma * v.square(v.euclideanDistance(a, b));
	return Math.exp(result);
    }

    public double exponential(VectorBox a, VectorBox b, double gamma) {
	double result = gamma * v.euclideanDistance(a, b);
	return Math.exp(result);
    }

    public double polynomial(VectorBox a, VectorBox b, double p, double q) {
	double result = p + v.dotProduct(a, b);
	return Math.pow(result, q);
    }

    public double hybrid(VectorBox a, VectorBox b, double gamma, double p, double q) {
	return polynomial(a, b, p, q) * gaussian(a, b, gamma);
    }

    public double sigmoidal(VectorBox a, VectorBox b, double k, double delta) {
	VectorBox scaled = v.scale(k, a);
	double result = v.dotProduct(b, scaled) - delta;
	return Math.tanh(result);
    }
}
