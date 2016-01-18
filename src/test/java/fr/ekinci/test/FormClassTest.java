package fr.ekinci.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import fr.ekinci.htmlform.annotation.DateFormat;

/**
 * Must be "public" class
 * 
 * @author Gokan
 */
public class FormClassTest {
    private String  field1;
    @DateFormat("dd/MM/yyyy")
    private Date    field2;
    private short   field3;
    private Short   field4;
    private int     field5;
    private Integer field6;
    private long    field7;
    private Long    field8;
    private float   field9;
    private Float   field10;
    private double  field11;
    private Double  field12;
    private BigInteger    field13;
    private BigDecimal    field14;
    private List<String>  field15;
    @DateFormat("dd/MM/yyyy")
    private List<Date>    field16;
    private List<Short>   field17;
    private List<Integer> field18;
    private List<Long>    field19;
    private List<Float>   field20;
    private List<Double>  field21;
    private List<BigInteger>   field22;
    private List<BigDecimal>   field23;
    private Set<String>  field24;
    @DateFormat("dd/MM/yyyy")
    private Set<Date>    field25;
    private Set<Short>   field26;
    private Set<Integer> field27;
    private Set<Long>    field28;
    private Set<Float>   field29;
    private Set<Double>  field30;
    private Set<BigInteger>   field31;
    private Set<BigDecimal>   field32;
    
    @Override
    public String toString() {
        return "FormClassTest [field1=" + field1 + ", field2=" + field2
                + ", field3=" + field3 + ", field4=" + field4 + ", field5="
                + field5 + ", field6=" + field6 + ", field7=" + field7
                + ", field8=" + field8 + ", field9=" + field9 + ", field10="
                + field10 + ", field11=" + field11 + ", field12=" + field12
                + ", field13=" + field13 + ", field14=" + field14
                + ", field15=" + field15 + ", field16=" + field16
                + ", field17=" + field17 + ", field18=" + field18
                + ", field19=" + field19 + ", field20=" + field20
                + ", field21=" + field21 + ", field22=" + field22
                + ", field23=" + field23 + ", field24=" + field24
                + ", field25=" + field25 + ", field26=" + field26
                + ", field27=" + field27 + ", field28=" + field28
                + ", field29=" + field29 + ", field30=" + field30
                + ", field31=" + field31 + ", field32=" + field32 + "]";
    }

    public String getField1() {
        return field1;
    }

    public Date getField2() {
        return field2;
    }

    public short getField3() {
        return field3;
    }

    public Short getField4() {
        return field4;
    }

    public int getField5() {
        return field5;
    }

    public Integer getField6() {
        return field6;
    }

    public long getField7() {
        return field7;
    }

    public Long getField8() {
        return field8;
    }

    public float getField9() {
        return field9;
    }

    public Float getField10() {
        return field10;
    }

    public double getField11() {
        return field11;
    }

    public Double getField12() {
        return field12;
    }

    public BigInteger getField13() {
        return field13;
    }

    public BigDecimal getField14() {
        return field14;
    }

    public List<String> getField15() {
        return field15;
    }

    public List<Date> getField16() {
        return field16;
    }

    public List<Short> getField17() {
        return field17;
    }

    public List<Integer> getField18() {
        return field18;
    }

    public List<Long> getField19() {
        return field19;
    }

    public List<Float> getField20() {
        return field20;
    }

    public List<Double> getField21() {
        return field21;
    }

    public List<BigInteger> getField22() {
        return field22;
    }

    public List<BigDecimal> getField23() {
        return field23;
    }

    public Set<String> getField24() {
        return field24;
    }

    public Set<Date> getField25() {
        return field25;
    }

    public Set<Short> getField26() {
        return field26;
    }

    public Set<Integer> getField27() {
        return field27;
    }

    public Set<Long> getField28() {
        return field28;
    }

    public Set<Float> getField29() {
        return field29;
    }

    public Set<Double> getField30() {
        return field30;
    }

    public Set<BigInteger> getField31() {
        return field31;
    }

    public Set<BigDecimal> getField32() {
        return field32;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public void setField2(Date field2) {
        this.field2 = field2;
    }

    public void setField3(short field3) {
        this.field3 = field3;
    }

    public void setField4(Short field4) {
        this.field4 = field4;
    }

    public void setField5(int field5) {
        this.field5 = field5;
    }

    public void setField6(Integer field6) {
        this.field6 = field6;
    }

    public void setField7(long field7) {
        this.field7 = field7;
    }

    public void setField8(Long field8) {
        this.field8 = field8;
    }

    public void setField9(float field9) {
        this.field9 = field9;
    }

    public void setField10(Float field10) {
        this.field10 = field10;
    }

    public void setField11(double field11) {
        this.field11 = field11;
    }

    public void setField12(Double field12) {
        this.field12 = field12;
    }

    public void setField13(BigInteger field13) {
        this.field13 = field13;
    }

    public void setField14(BigDecimal field14) {
        this.field14 = field14;
    }

    public void setField15(List<String> field15) {
        this.field15 = field15;
    }

    public void setField16(List<Date> field16) {
        this.field16 = field16;
    }

    public void setField17(List<Short> field17) {
        this.field17 = field17;
    }

    public void setField18(List<Integer> field18) {
        this.field18 = field18;
    }

    public void setField19(List<Long> field19) {
        this.field19 = field19;
    }

    public void setField20(List<Float> field20) {
        this.field20 = field20;
    }

    public void setField21(List<Double> field21) {
        this.field21 = field21;
    }

    public void setField22(List<BigInteger> field22) {
        this.field22 = field22;
    }

    public void setField23(List<BigDecimal> field23) {
        this.field23 = field23;
    }

    public void setField24(Set<String> field24) {
        this.field24 = field24;
    }

    public void setField25(Set<Date> field25) {
        this.field25 = field25;
    }

    public void setField26(Set<Short> field26) {
        this.field26 = field26;
    }

    public void setField27(Set<Integer> field27) {
        this.field27 = field27;
    }

    public void setField28(Set<Long> field28) {
        this.field28 = field28;
    }

    public void setField29(Set<Float> field29) {
        this.field29 = field29;
    }

    public void setField30(Set<Double> field30) {
        this.field30 = field30;
    }

    public void setField31(Set<BigInteger> field31) {
        this.field31 = field31;
    }

    public void setField32(Set<BigDecimal> field32) {
        this.field32 = field32;
    }
}
