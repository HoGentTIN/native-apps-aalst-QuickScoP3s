namespace ProjectDashboard.Models.Domain {
	public class TeamMember {

		public int TeamId { get; set; }
		public int MemberId { get; set; }

		public Team Team { get; set; }
		public User Member { get; set; }

	}
}
