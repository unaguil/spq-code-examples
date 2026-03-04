package donations.serialization;

public class Donation {

    private int donation;

    public Donation() {
        this.donation = 0;
    }

    public Donation(int donation) {
        this.donation = donation;
    }

    public int getDonation() {
        return this.donation;
    }

    public void setDonation(int donation) {
        this.donation = donation;
    }
}