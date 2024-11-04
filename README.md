# Least Similar Spheres (LSS) - Alternative Approach to Support Vector Machines
**Author**: Gintaras Koncevicius  
**Published on**: [ResearchGate](https://www.researchgate.net/publication/304777963_least_similar_spheres) | [Blogspot](https://leastsimilarspheres.blogspot.com/)

---

## Project Overview
**Least Similar Spheres (LSS)** reimagines traditional Support Vector Machines (SVMs) by using **geometric spheres** instead of linear hyperplanes. This approach allows for more intuitive data classification in certain datasets, particularly bioassay datasets where traditional SVMs may struggle.

> *Awarded "Project of the Year" at ACE Showcase and published on ResearchGate.*

## Key Features
- **Spherical Classification Boundaries**: Utilizes sphere radii for classification rather than support vectors, simplifying the visual interpretation of classification.
- **Custom Distance Measurement**: Multiple distance formulas implemented, allowing fine-tuning based on dataset requirements.
- **Enhanced Data Handling**: Incorporates slack variables, outlier removal, and cluster averaging to improve classification accuracy.

![LSS Diagram](https://github.com/k-gintaras/lss/blob/master/ReadingMaterials/diagrams/capture_009_16042016_185357.jpg "Example Sphere-Based Classification")

## Notable Results
- Comparable or superior performance to traditional SVMs in specific datasets.
- Exact match with R-SVM predictions in certain bioassay dataset instances.
- Efficiently handles overlapping data points using customizable slack variables.

## How It Works
LSS replaces the traditional SVM approach with spheres, calculating classification boundaries based on the radius of each sphere relative to the dataset's features.

1. **Data Preprocessing**: Outliers are removed, and data points are clustered for smoother classification.
2. **Sphere Generation**: Spheres are generated using various distance measurement formulas.
3. **Classification**: Based on sphere overlap and slack variable adjustments, data is classified with enhanced flexibility compared to traditional SVMs.

### Example Setup
1. **Download**: Clone or download the repository.
2. **Run**: Execute `LSS.jar` to launch the GUI and load sample data in CSV format.
3. **Parameters**: Set classification parameters (distance formula, slack variables) based on your dataset.

---

## Screenshots & Diagrams
Here are some examples illustrating how the LSS algorithm visualizes classification boundaries:

![Sphere-Based Visualization 1](https://github.com/k-gintaras/lss/blob/master/ReadingMaterials/diagrams/capture_003_16042016_142244.jpg)
*Figure: Sphere-based boundaries for initial classification.*

![Outlier Removal Visualization](https://github.com/k-gintaras/lss/blob/master/ReadingMaterials/diagrams/capture_010_16042016_190214.jpg)
*Figure: Outliers removed for cleaner data.*

---

## Further Reading & Publications
For a more in-depth look at the methodology and testing results:
- **[ResearchGate Publication](https://www.researchgate.net/publication/304777963_least_similar_spheres)**: Complete paper with mathematical explanations and experimental outcomes.
- **[Blogspot Post](https://leastsimilarspheres.blogspot.com/)**: Non-technical overview and visual guide to the LSS approach.

## Future Enhancements
- [ ] Interactive real-time visualization of classification boundaries.
- [ ] Additional distance measurement formulas for broader use cases.
- [ ] Command-line functionality for batch processing large datasets.

## Contributing
Interested in expanding the LSS project? Feel free to fork the repository or reach out for collaboration.

---

Thank you for exploring the Least Similar Spheres (LSS) project! For questions or collaboration opportunities, please contact me directly.

