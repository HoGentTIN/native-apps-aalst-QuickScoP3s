using Microsoft.AspNetCore.Identity;

namespace ProjectDashboard.Models.Domain {
	public class User: IdentityUser<int> {

		/// <summary>
		/// Base64 encoded image string
		/// </summary>
		public string Picture { get; set; }

		public string FirstName { get; set; }

		public string LastName { get; set; }

	}
}
