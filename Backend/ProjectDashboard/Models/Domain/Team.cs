using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain {
	public class Team {

		public int Id { get; set; }

		public string Name { get; set; }

		public int LeadId { get; set; }
		public User Lead { get; set; }

		public ICollection<TeamMember> Members { get; set; } = new List<TeamMember>();

		public Team() { }

		public Team(string name, User lead) {
			this.Name = name;
			this.Lead = lead;
		}
	}
}
