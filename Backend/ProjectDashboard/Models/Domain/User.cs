using Microsoft.AspNetCore.Identity;
using ProjectDashboard.Models.Domain.Interfaces;
using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain {
	public class User: IdentityUser<int>, IPerson {

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

		public int CompanyId { get; set; }

		public Company Company { get; set; }

		public ICollection<TeamMember> Teams { get; set; }
	}
}
