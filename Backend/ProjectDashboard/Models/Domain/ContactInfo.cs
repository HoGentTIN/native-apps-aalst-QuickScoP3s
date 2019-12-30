namespace ProjectDashboard.Models.Domain {
	public class ContactInfo {

		public string FirstName { get; set; }

		public string LastName { get; set; }

		public string Email { get; set; }

		public string PhoneNumber { get; set; }

		public ContactInfo() { }

		public ContactInfo(string firstName, string lastName, string email, string phoneNr) {
			this.FirstName = firstName;
			this.LastName = lastName;
			this.Email = email;
			this.PhoneNumber = phoneNr;
		}

	}
}
