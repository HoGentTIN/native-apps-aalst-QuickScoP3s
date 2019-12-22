using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectDashboard.DTO.Identity {
	public class RegisterDTO {

		public string Picture { get; set; }

		[Required]
		public string FirstName { get; set; }

		[Required]
		public string LastName { get; set; }

		[Required]
		[EmailAddress]
		public string Email { get; set; }

		[Required]
		[Phone]
		public string PhoneNumber { get; set; }

		[Required]
		[MinLength(6)]
		public string Password { get; set; }

		[Required]
		[Compare(nameof(Password))]
		public string PasswordConfirm { get; set; }

	}
}
