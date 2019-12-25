using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain {
	public class Team {

		public int Id { get; set; }

		public User Lead { get; set; }

		public ICollection<TeamMember> Members { get; set; }

	}
}
