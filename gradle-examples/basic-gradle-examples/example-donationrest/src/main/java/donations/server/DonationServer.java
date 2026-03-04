package donations.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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