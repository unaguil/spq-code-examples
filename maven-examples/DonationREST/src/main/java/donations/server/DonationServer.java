package donations.server;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import donations.serialization.Donation;
import donations.serialization.DonationInfo;

@Path("/collector")
@Produces(MediaType.APPLICATION_JSON)
public class DonationServer {

	private DonationCollector donationCollector;

	public DonationServer() {
		donationCollector = new DonationCollector();
	}

	@POST
	@Path("/donations")
	public Response addDonation(Donation donation) {
		int total = donationCollector.addDonation(donation.getDonation());
		System.out.println("Received donation: " + donation.getDonation() + ". Total donations: " + total);
		return Response.ok().build();
	}

	@GET
	@Path("/donations")
	public Response getDonationInfo() {
		DonationInfo donationInfo = donationCollector.getDonationInfo();
		return Response.ok(donationInfo).build();
	}
}