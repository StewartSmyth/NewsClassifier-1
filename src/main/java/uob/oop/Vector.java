package uob.oop;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 3.1 - 0.5 marks
        doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        //TODO Task 3.2 - 2 marks
        try {
            return doubElements[_index];
        }catch(Exception e){
            return -1;
        }
    }

    public void setElementatIndex(double _value, int _index) {
        //TODO Task 3.3 - 2 marks
        try {
            doubElements[_index] = _value;
        }catch(Exception e){
            doubElements[doubElements.length-1] = _value;
        }
    }

    public double[] getAllElements() {
        //TODO Task 3.4 - 0.5 marks

        return doubElements; //you need to modify the return value
    }

    public int getVectorSize() {
        //TODO Task 3.5 - 0.5 marks
        return doubElements.length; //you need to modify the return value
    }

    public Vector reSize(int _size) {
        //TODO Task 3.6 - 6 marks

        if(_size <= 0||_size == doubElements.length ){
            return new Vector(doubElements);
        }
        else{
            double[] newElements = new double[_size];
            if(_size < doubElements.length){
                for(int i = 0; i<_size; i++){
                    newElements[i] = doubElements[i];
                }
            }
            else{
                for(int i = 0; i< doubElements.length; i++){
                    newElements[i] = doubElements[i];
                }
                for(int i = doubElements.length; i<_size; i++){
                    newElements[i] = -1;
                }
            }
            return new Vector(newElements);
        }
    }


    public Vector add(Vector _v) {
        //TODO Task 3.7 - 2 marks
        double[] thisVector = new double[_v.doubElements.length];
        double[] addedVector = new double[doubElements.length];

        if(_v.doubElements.length < doubElements.length) {
            addedVector = _v.reSize(doubElements.length).getAllElements();
            thisVector = getAllElements();
        }
        else if(_v.doubElements.length > doubElements.length){
            thisVector = reSize(_v.doubElements.length).getAllElements();
            addedVector = _v.getAllElements();
        }
        else{
            thisVector = getAllElements();
            addedVector = _v.getAllElements();
        }
        double[] finalElements = new double[thisVector.length];
        for(int i = 0; i<thisVector.length; i++){
            finalElements[i] = thisVector[i] + addedVector[i];
        }
        return new Vector(finalElements); //you need to modify the return value
    }

    public Vector subtraction(Vector _v) {
        //TODO Task 3.8 - 2 marks
        double[] thisVector = new double[_v.doubElements.length];
        double[] subtractedVector = new double[doubElements.length];

        if(_v.doubElements.length < doubElements.length) {
            subtractedVector = _v.reSize(doubElements.length).getAllElements();
            thisVector = getAllElements();
        }
        else if(_v.doubElements.length > doubElements.length){
            thisVector = reSize(_v.doubElements.length).getAllElements();
            subtractedVector = _v.getAllElements();
        }
        else{
            thisVector = getAllElements();
            subtractedVector = _v.getAllElements();
        }
        double[] finalElements = new double[thisVector.length];
        for(int i = 0; i<thisVector.length; i++){
            finalElements[i] = thisVector[i] - subtractedVector[i];
        }
        return new Vector(finalElements); //you need to modify the return value
    }

    public double dotProduct(Vector _v) {
        //TODO Task 3.9 - 2 marks

        double[] thisVector = new double[_v.doubElements.length];
        double[] multipliedVector = new double[doubElements.length];

        if(_v.doubElements.length < doubElements.length) {
            multipliedVector = _v.reSize(doubElements.length).getAllElements();
            thisVector = getAllElements();
        }
        else if(_v.doubElements.length > doubElements.length){
            thisVector = reSize(_v.doubElements.length).getAllElements();
            multipliedVector = _v.getAllElements();
        }
        else{
            thisVector = getAllElements();
            multipliedVector = _v.getAllElements();
        }
        double finalProduct = 0;
        for(int i = 0; i<thisVector.length; i++){
            finalProduct += thisVector[i] * multipliedVector[i];
        }
        return finalProduct; //you need to modify the return value
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 3.10 - 6.5 marks
        double dotProduct = dotProduct(_v);
        double uVectorRepresentation = 0;
        double vVectorRepresentation = 0;
        double[] thisVector = new double[_v.doubElements.length];
        double[] vVector = new double[doubElements.length];

        if(_v.doubElements.length < doubElements.length) {
            vVector = _v.reSize(doubElements.length).getAllElements();
            thisVector = getAllElements();
        }
        else if(_v.doubElements.length > doubElements.length){
            thisVector = reSize(_v.doubElements.length).getAllElements();
            vVector = _v.getAllElements();
        }
        else{
            thisVector = getAllElements();
            vVector = _v.getAllElements();
        }
        for(double num : thisVector){
            uVectorRepresentation+=Math.pow(num,2);
        }
        for(double num : vVector){
            vVectorRepresentation+=Math.pow(num,2);
        }
        uVectorRepresentation = Math.sqrt(uVectorRepresentation);
        vVectorRepresentation = Math.sqrt(vVectorRepresentation);



        return (dotProduct)/(uVectorRepresentation*vVectorRepresentation); //you need to modify the return value
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;

        if (this.getVectorSize() != v.getVectorSize())
            return false;

        for (int i = 0; i < this.getVectorSize(); i++) {
            if (this.getElementatIndex(i) != v.getElementatIndex(i)) {
                boolEquals = false;
                break;
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
