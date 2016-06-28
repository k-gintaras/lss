import java.util.Arrays;
import java.util.List;

public class MathHelper {

    public MathHelper() {
    }

    /**
     * direction vector
     **
     * w(u1/||u1||,u2/||u||...)
     **
     * w(cosAlpha,cosBeta...)
     * 
     * @return Vector, unit vector or direction vector
     */
    public VectorBox unit(VectorBox u) {
	int dimension = u.values.length;
	double[] vector = new double[dimension];
	double norm = norm(u);
	for (int i = 0; i < dimension; i++) {
	    double value = u.values[i];
	    vector[i] = value / norm;
	}
	return new VectorBox(vector);
    }

    /**
     * length or magnitude ||A||
     * 
     * @return double norm
     */
    public double norm(VectorBox u) {
	double sum = 0;
	for (int i = 0; i < u.values.length; i++) {
	    sum += square(u.values[i]);
	}
	return Math.sqrt(sum);
    }

    public VectorBox sumAll(List<VectorBox> vectors) {
	int dimension = vectors.get(0).values.length;
	double[] vector = new double[dimension];
	for (int i = 0; i < vectors.size(); i++) {
	    VectorBox v = vectors.get(i);
	    for (int j = 0; j < dimension; j++) {
		vector[i] += v.values[i];
	    }
	}
	return new VectorBox(vector);
    }

    public VectorBox sum(VectorBox u, VectorBox v) {
	int dimension = u.values.length;
	double[] vector = new double[dimension];
	for (int j = 0; j < dimension; j++) {
	    vector[j] = u.values[j] + v.values[j];
	}
	return new VectorBox(vector);
    }

    public VectorBox difference(VectorBox u, VectorBox v) {
	int dimension = u.values.length;
	double[] vector = new double[dimension];
	for (int j = 0; j < dimension; j++) {
	    vector[j] = u.values[j] - v.values[j];
	}
	return new VectorBox(vector);
    }

    public VectorBox differenceAll(List<VectorBox> vectors) {
	int dimension = vectors.get(0).values.length;
	double[] vector = new double[dimension];
	for (int i = 0; i < vectors.size(); i++) {
	    VectorBox v = vectors.get(i);
	    for (int j = 0; j < dimension; j++) {
		vector[i] -= v.values[i];
	    }
	}
	return new VectorBox(vector);
    }

    /**
     * only if vector is (a,b) not (a,b,c...)
     **
     * <x,y> inner product
     **
     * x . y dot product
     **
     * scalar product
     * 
     */
    public double dotProduct(VectorBox _u, VectorBox _v) {
	double[] u, v;
	double product = 0;
	u = _u.values;
	v = _v.values;
	for (int i = 0; i < u.length; i++) {
	    product += u[i] * v[i];
	}
	return product;
    }

    /**
     * multiply by scalar
     */
    public VectorBox scale(double scalar, VectorBox _v) {
	double[] v, s;
	v = _v.values;
	int dimension = v.length;
	s = new double[dimension];
	for (int i = 0; i < dimension; i++) {
	    s[i] = v[i] * scalar;
	}
	return new VectorBox(s);
    }

    public VectorBox projection(VectorBox u, VectorBox v) {
	double c = dotProduct(u, v) / dotProduct(v, v);
	return scale(c, v);
    }

    public double euclideanDistance(VectorBox a, VectorBox b) {
	double[] A = a.values, B = b.values;
	double total = 0;

	for (int i = 0; i < B.length; i++) {
	    total += square(A[i] - B[i]);
	}
	return Math.sqrt(total);
    }

    public double angle(VectorBox a, VectorBox b) {
	return degree(Math.acos(dotProduct(a, b) / (norm(a) * norm(b))));
    }

    public double radian(VectorBox a, VectorBox b) {
	return Math.acos(dotProduct(a, b) / (norm(a) * norm(b)));
    }

    public double degree(double radians) {
	return radians * 180 / Math.PI;
    }

    public double square(double a) {
	return a * a;
    }

    public VectorBox middle(VectorBox a, VectorBox b) {
	double[] avg = new double[a.values.length];
	double[] sum = sum(a, b).values;
	for (int i = 0; i < avg.length; i++) {
	    avg[i] = sum[i] / 2;
	}
	return new VectorBox(avg);
    }

    public double round(double num, int how) {
	double power = Math.pow(10, how);
	return Math.round(num * power) / power;
    }

    public double normalize(double value, double itsMin, double itsMax, double targetMin, double targetMax) {
	return (((value - itsMin) / (itsMax - itsMin)) * (targetMax - targetMin) + targetMin);
    }

    public double[] normalize(double[] toNormalize, double targetMin, double targetMax) {
	int size = toNormalize.length;
	double[] normalized = new double[size];
	double[] minMax = new double[size];
	for (int i = 0; i < minMax.length; i++) {
	    minMax[i] = toNormalize[i];
	}
	Arrays.sort(minMax);
	double min = minMax[0];
	double max = minMax[size - 1];

	for (int i = 0; i < toNormalize.length; i++) {
	    normalized[i] = round(normalize(toNormalize[i], min, max, targetMin, targetMax),3);
	}
	return normalized;
    }
}
