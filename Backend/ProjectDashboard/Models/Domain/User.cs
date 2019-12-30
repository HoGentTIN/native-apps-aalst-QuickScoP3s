using Microsoft.AspNetCore.Identity;
using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain {
	public class User: IdentityUser<int> {

		/// <summary>
		/// Base64 encoded image string
		/// </summary>
		public string Picture { get; set; }

		public string FirstName { get; set; }

		public string LastName { get; set; }

		public override string Email { 
			get => base.Email; 
			set {
				base.Email = value;
				base.UserName = value; // Required for Auth
			}
		}

		public int? CompanyId { get; set; }

		public Company Company { get; set; }

		public ICollection<TeamMember> Teams { get; set; }

		public ICollection<ProjectTask> Tasks { get; set; }

		public User() { }

		public User(string firstName, string lastName, string email, string phoneNumber, Company company = null) {
			this.FirstName = firstName;
			this.LastName = lastName;
			this.Email = email;
			this.PhoneNumber = phoneNumber;
			this.Company = company;
		}
	}
}
