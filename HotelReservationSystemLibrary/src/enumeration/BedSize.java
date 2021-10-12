/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumeration;

/**
 *
 * @author Winter
 */
public enum BedSize {
    KING(193d, 203.5),
    QUEEN(152d, 203.5),
    FULL(134.5, 190.5),
    TWIN(96.5, 188.0);
    
    private final Double width;
    private final Double length;

    private BedSize(Double width, Double length) {
        this.width = width;
        this.length = length;
    }
    
    public String dimension() {
        return width + " X " + length;
    }

    public Double getWidth() {
        return width;
    }

    public Double getLength() {
        return length;
    }
}
