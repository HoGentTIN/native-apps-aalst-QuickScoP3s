using ProjectDashboard.Models.Domain.Interfaces;
using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain {
	public class Team {

		public int Id { get; set; }

		public IPerson Lead { get; set; }

		public ICollection<TeamMember> Members { get; set; }

	}
}
