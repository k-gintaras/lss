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
