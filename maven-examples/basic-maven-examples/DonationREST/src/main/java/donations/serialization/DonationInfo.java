package donations.serialization;

public class DonationInfo {

    private int total;
    private int last;

    public DonationInfo() {
    }

    public DonationInfo(int total, int last) {
        this.total =  total;
        this.last = last;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLast() {
        return this.last;
    }

    public void setLast(int last) {
        this.last = last;
    }
}