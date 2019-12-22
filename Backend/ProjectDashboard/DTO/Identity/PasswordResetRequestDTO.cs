using System.ComponentModel.DataAnnotations;

namespace ProjectDashboard.DTO.Identity {
	public class PasswordResetRequestDTO {

		[Required]
		[EmailAddress]
		public string Email { get; set; }

		public string Token { get; set; }

		public string NewPassword { get; set; }
	}
}
