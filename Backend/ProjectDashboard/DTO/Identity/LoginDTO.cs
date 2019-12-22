﻿using System.ComponentModel.DataAnnotations;

namespace ProjectDashboard.DTO.Identity {
	public class LoginDTO {

		[Required]
		[EmailAddress]
		public string Email { get; set; }

		[Required]
		public string Password { get; set; }

	}
}
