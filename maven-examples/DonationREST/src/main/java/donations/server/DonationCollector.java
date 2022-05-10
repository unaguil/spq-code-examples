package donations.server;

import donations.serialization.DonationInfo;

public class DonationCollector {

    private static int lastDonation = 0;
    private static int totalDonations = 0;
    
    public int addDonation(int donation) {
        synchronized(this) {
            lastDonation = donation;
            totalDonations += donation;
            return totalDonations;
        }
    }

    public DonationInfo getDonationInfo() {
        DonationInfo donationInfo = new DonationInfo();
		synchronized(this) {
			donationInfo.setLast(lastDonation);
			donationInfo.setTotal(totalDonations);
        }
        return donationInfo;
    }
}